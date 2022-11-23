package com.itheima.app.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.itheima.autoconfig.face.AipFaceTemplate;
import com.itheima.autoconfig.huanxin.HuanXinTemplate;
import com.itheima.autoconfig.oss.OssTemplate;
import com.itheima.autoconfig.sms.SmsTemplate;
import com.itheima.domain.db.Log;
import com.itheima.domain.db.User;
import com.itheima.domain.db.UserInfo;
import com.itheima.service.db.UserInfoService;
import com.itheima.service.db.UserService;
import com.itheima.util.ConstantUtil;
import com.itheima.util.JwtUtil;
import com.itheima.vo.ErrorResult;
import com.itheima.vo.UserInfoVo;
import io.netty.util.internal.StringUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserManager {

    @DubboReference
    private UserService userService;
    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @DubboReference
    private UserInfoService userInfoService;
    @Autowired
    private AipFaceTemplate aipFaceTemplate;
    @Autowired
    private OssTemplate ossTemplate;
    @Autowired
    private HuanXinTemplate huanXinTemplate;

    // 保存用户
    public ResponseEntity save(User user) {
        Long userId = userService.save(user);
        return ResponseEntity.ok(userId);   // 状态200，将数据设置到响应体返回
    }

    // 手机号查询
    public ResponseEntity findByPhone(String phone) {
        User user = userService.findByPhone(phone);
        return ResponseEntity.ok(user); // 状态200，将数据转json设置到响应体返回
    }

    public String smsSend(String phone) {
        //String code = RandomUtil.randomNumbers(6);
        String code = "123456"; //TODO 开发使用123456
        stringRedisTemplate.opsForValue()
                .set(ConstantUtil.SMS_CODE + phone, code, Duration.ofMinutes(5));
        return code;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 注册、登录
    public ResponseEntity loginVerification(String phone, String verificationCode) {
        // 1.取出redis验证码
        String codeFromRedis = stringRedisTemplate.opsForValue().get(ConstantUtil.SMS_CODE + phone);
        // 2.比对验证码
        if (!StrUtil.equals(verificationCode, codeFromRedis)) {
            return ResponseEntity.status(500).body(ErrorResult.loginError());
        }
        // 3.根据手机号查询
        User user = userService.findByPhone(phone);
        Boolean isNew;
        String type;
        if (user != null) { // 老用户
            isNew = false;
            type = "0101";
        } else { // 新用户
            type = "0102";
            isNew = true;
            // 保存新用户
            user = new User();
            user.setMobile(phone);
            user.setPassword(ConstantUtil.INIT_PASSWORD);
            Long userId = userService.save(user);
            user.setId(userId);
            // 向环信注册此用户
            huanXinTemplate.register("HX" + userId);
        }
        // 4.jwt制作令牌
        user.setPassword(null);
        Map<String, Object> claims = BeanUtil.beanToMap(user);
        String token = JwtUtil.createToken(claims);

        // 5.向redis存令牌
        String json = JSON.toJSONString(user);
        stringRedisTemplate.opsForValue().set(ConstantUtil.USER_TOKEN + token, json, Duration.ofDays(7));

        // 将redis中验证码
        stringRedisTemplate.delete(ConstantUtil.SMS_CODE + phone);

        //-------------------------
        // 封装log对象
        Log log = new Log();
        log.setUserId(user.getId());// 用户id
        log.setLogTime(DateUtil.formatDate(new Date())); // 操作时间  2022-2-24
        log.setPlace("北京顺义黑马程序员"); // 地点
        log.setEquipment("华为Mate40Pro"); // 设备
        log.setType(type); //  类型
        // 发送mq消息
        rabbitTemplate.convertAndSend("tanhua.log", log);
        //-------------------------

        // 6.封装返回结果
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("isNew", isNew);
        return ResponseEntity.ok(map);
    }

    public void loginReginfo(UserInfo userInfo, String token) {
        // 1.解析token获取user
        User user = findUserByToken(token);
        // 2.设置用户id到userInfo中
        userInfo.setId(user.getId());
        // 3.调用rpc保存
        userInfoService.save(userInfo);
    }

    // 解析token获取user
    public User findUserByToken(String token) {
        // 查询redis中是否有token令牌
        Boolean isLogin = stringRedisTemplate.hasKey(ConstantUtil.USER_TOKEN + token);
        if (isLogin) { // 登录了
            // 获取用户信息
            String json = stringRedisTemplate.opsForValue().get(ConstantUtil.USER_TOKEN + token);
            // 转为user对象
            User user = JSON.parseObject(json, User.class);
            // 续期
            stringRedisTemplate.opsForValue().set(ConstantUtil.USER_TOKEN + token, json, Duration.ofDays(7));
            // 返回
            return user;
        } else { // 失效了
            return null;
        }
    }

    // 保存用户头像信息
    public ResponseEntity loginReginfoHead(MultipartFile headPhoto, String token) throws IOException {
        // 1.人脸检测
        boolean detect = aipFaceTemplate.detect(headPhoto.getBytes());
        if (detect == false) { // 非人脸
            return ResponseEntity.status(500).body(ErrorResult.faceError());
        }
        // 2.头像上传阿里云oss
        String picUrl = "https://";
        picUrl += ossTemplate.upload(headPhoto.getOriginalFilename(), headPhoto.getInputStream());
        // 3.解析token获取user
        User user = findUserByToken(token);
        // 4.封装userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setCoverPic(picUrl);
        userInfo.setAvatar(picUrl);
        // 5.调用rpc更新
        userInfoService.update(userInfo);
        return ResponseEntity.ok(null);
    }

    // 查询用户信息
    public ResponseEntity findUserInfoVo(Long userId) {
        // 1.先根据用户id查询
        UserInfo userInfo = userInfoService.findById(userId);
        // 2.封装vo
        UserInfoVo vo = new UserInfoVo();
        BeanUtil.copyProperties(userInfo, vo);
        // 3.返回vo
        return ResponseEntity.ok(vo);
    }

    // 更新用户基本信息
    public void putUserInfo(UserInfo userInfo){
        userInfoService.update(userInfo);
    }
}
