package com.itheima.web.manager;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.itheima.domain.db.Admin;
import com.itheima.service.db.AdminService;
import com.itheima.util.ConstantUtil;
import com.itheima.util.JwtUtil;
import com.itheima.web.exception.BusinessException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminManager {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 生成验证码
    public LineCaptcha verification(String uuid) {
        // 1.使用hutool生成验证码
        LineCaptcha lineCaptcha = new LineCaptcha(200, 100);
        // 2.存入redis中
        stringRedisTemplate.opsForValue().set(ConstantUtil.ADMIN_CODE + uuid, lineCaptcha.getCode(), Duration.ofMinutes(5));
        // 3.返回验证码对象
        return lineCaptcha;
    }

    @DubboReference
    private AdminService adminService;

    // 管理员登录
    public ResponseEntity login(String username, String password, String verificationCode, String uuid) {
        // 1.根据uuid查询redis
        String codeFromRedis = stringRedisTemplate.opsForValue().get(ConstantUtil.ADMIN_CODE + uuid);
        // 2.比对验证码
        if (!StrUtil.equals(verificationCode, codeFromRedis)) {
            // 不一致
            throw new BusinessException("验证码不匹配");
        }
        // 3.调用rpc查询
        // md5加密
        String pwd = SecureUtil.md5(password);
        Admin admin = adminService.findByUsernameAndPassword(username, pwd);
        if (admin == null) {
            // 登录失败
            throw new BusinessException("用户名密码不正确~~~");
        }
        // 4.登录成功
        // 4-1制作token
        admin.setPassword(null);
        Map<String, Object> claims = BeanUtil.beanToMap(admin);
        String token = JwtUtil.createToken(claims);
        // 4-2 token存入redis
        String json = JSON.toJSONString(admin);
        stringRedisTemplate.opsForValue().set(ConstantUtil.ADMIN_TOKEN + token, json, Duration.ofHours(1));
        // 5.返回token
        Map resultMap = new HashMap();
        resultMap.put("token", token);
        return ResponseEntity.ok(resultMap);
    }

    // 获取管理员信息
    public Admin findAdminByToken(String token) {
        // 1.根据token查询redis
        // 处理token  Bearer sdfasdf234234gdf
        token = token.replace("Bearer ", "");// 注意后面有一个空格
        String json = stringRedisTemplate.opsForValue().get(ConstantUtil.ADMIN_TOKEN + token);
        // 判断处理
        if (StrUtil.isEmpty(json)) {
            return null;
        }
        // 2.将json转为admin对象
        Admin admin = JSON.parseObject(json, Admin.class);
        // 3.续期
        stringRedisTemplate.opsForValue().set(ConstantUtil.ADMIN_TOKEN + token, json, Duration.ofHours(1));
        // 4.返回admin
        return admin;
    }

    // 用户退出
    public void logout(String token) {
        // 处理token  Bearer sdfasdf234234gdf
        token = token.replace("Bearer ", "");
        // 删除redis中token
        stringRedisTemplate.delete(ConstantUtil.ADMIN_TOKEN + token);
    }
}
