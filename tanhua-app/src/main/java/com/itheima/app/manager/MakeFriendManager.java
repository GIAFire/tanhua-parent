package com.itheima.app.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.itheima.app.interceptor.UserHolder;
import com.itheima.domain.db.Question;
import com.itheima.domain.db.UserInfo;
import com.itheima.domain.mongo.RecommendUser;
import com.itheima.domain.mongo.Visitor;
import com.itheima.service.db.QuestionService;
import com.itheima.service.db.UserInfoService;
import com.itheima.service.mongo.RecommendUserService;
import com.itheima.service.mongo.UserLocationService;
import com.itheima.service.mongo.VisitorService;
import com.itheima.util.ConstantUtil;
import com.itheima.vo.NearUserVo;
import com.itheima.vo.PageBeanVo;
import com.itheima.vo.RecommendUserVo;
import com.itheima.vo.VisitorVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MakeFriendManager {


    @DubboReference
    private RecommendUserService recommendUserService;

    @DubboReference
    private UserInfoService userInfoService;

    // 今日佳人
    public ResponseEntity findTodayBest() {
        // 1.获取线程内toUserId
        Long toUserId = UserHolder.get().getId();
        // 2.调用rpc查询今日佳人
        RecommendUser recommendUser = recommendUserService.findTodayBest(toUserId);
        if (recommendUser==null) {
            // 新用户模拟数据
            recommendUser = new RecommendUser();
            recommendUser.setScore(RandomUtil.randomDouble(90, 99));
            recommendUser.setUserId(RandomUtil.randomLong(1,20));
        }

        // 3.封装vo
        // 3-1 查询userInfo
        UserInfo userInfo = userInfoService.findById(recommendUser.getUserId());
        // 3-2 开始封装
        RecommendUserVo vo = new RecommendUserVo();
        vo.setUserInfo(userInfo); // 用户信息
        vo.setFateValue(recommendUser.getScore().longValue());// 缘分值

        // 4.返回vo
        return ResponseEntity.ok(vo);

    }

    // 推荐用户分页
    public ResponseEntity findRecommendUserVoByPage(Integer pageNum, Integer pageSize) {
        // 1.获取线程内toUserId
        Long toUserId = UserHolder.get().getId();
        // 2.调用rpc查询推荐用户分页
        PageBeanVo pageBeanVo = recommendUserService.findRecommendUserByPage(toUserId, pageNum, pageSize);
        // 3.封装vo
        // 3-1 声明voList
        List<RecommendUserVo> voList = new ArrayList<>();
        // 3-2 获取recommendUserList
        List<RecommendUser> recommendUserList = (List<RecommendUser>) pageBeanVo.getItems();
        // 3-3 遍历recommendUserList
        if (CollectionUtil.isNotEmpty(recommendUserList)) {
            for (RecommendUser recommendUser : recommendUserList) {
                // 查询userInfo
                UserInfo userInfo = userInfoService.findById(recommendUser.getUserId());
                // 封装vo
                RecommendUserVo vo = new RecommendUserVo();
                vo.setUserInfo(userInfo);
                vo.setFateValue(recommendUser.getScore().longValue());
                // 添加到集合
                voList.add(vo);
            }
        }
        // 4.将voList设置到分页对象
        pageBeanVo.setItems(voList);
        // 5.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }

    // 查询用户封面信息
    public ResponseEntity personalInfo(Long jiarenId) {
        // 1.获取线程内toUserId
        Long toUserId = UserHolder.get().getId();
        RecommendUser recommendUser;
        if (jiarenId == toUserId) { // 个人动态封面
            recommendUser = new RecommendUser();
            recommendUser.setUserId(jiarenId);
            recommendUser.setScore(99D);
        } else { // 推荐用户封面
            // 2.调用rpc查询缘分值
            recommendUser = recommendUserService.personalInfo(jiarenId, toUserId);
        }
        // 3.封装vo
        RecommendUserVo vo = new RecommendUserVo();
        // 设置userInfo
        UserInfo userInfo = userInfoService.findById(recommendUser.getUserId());
        vo.setUserInfo(userInfo);
        // 设置缘分值
        vo.setFateValue(recommendUser.getScore().longValue());
        // 4.返回vo
        return ResponseEntity.ok(vo);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @DubboReference
    private VisitorService visitorService;

    // 最近访客
    public ResponseEntity visitors() {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.获取上次查询时间 （redis）
        String lastAccessTime = stringRedisTemplate.opsForValue().get(ConstantUtil.LAST_ACCESS_TIME + userId);
        // 3.判断
        List<Visitor> visitorList;
        if (StrUtil.isNotEmpty(lastAccessTime)) { // 根据 redis时间查询
            visitorList = visitorService.findVisitors(userId, Long.valueOf(lastAccessTime));
        } else { // 根据系统时间查询
            visitorList = visitorService.findVisitors(userId, System.currentTimeMillis());
        }
        // 4.封装vo
        // 4-1创建voList
        List<VisitorVo> voList = new ArrayList<>();
        // 4-2遍历visitorList
        if (CollectionUtil.isNotEmpty(visitorList)) {
            for (Visitor visitor : visitorList) {
                //  创建vo
                VisitorVo vo = new VisitorVo();
                // 设置 userInfo
                UserInfo userInfo = userInfoService.findById(visitor.getVisitorUserId());
                vo.setUserInfo(userInfo);
                // 设置缘分值
                vo.setFateValue(visitor.getScore().longValue());
                // 添加到集合
                voList.add(vo);
            }
        }
        // 5.记录本次查询时间（redis）
        stringRedisTemplate.opsForValue().set(ConstantUtil.LAST_ACCESS_TIME + userId, String.valueOf(System.currentTimeMillis()));

        // 6.返回voList
        return ResponseEntity.ok(voList);
    }

    @DubboReference
    private UserLocationService userLocationService;

    // 上报地理位置
    public void location(Double longitude, Double latitude, String addrStr) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.调用rpc保存
        userLocationService.saveLocation(longitude, latitude, addrStr, userId);

    }

    // 搜附近
    public ResponseEntity searchNearUserVo(String gender, Long distance) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.调用rpc查询附近的人
        List<Long> nearUserIdList = userLocationService.searchNearUserId(userId, distance);
        // 3.遍历
        List<NearUserVo> voList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(nearUserIdList)) {
            for (Long nearUserId : nearUserIdList) {
                // 排除自己
                if (userId == nearUserId) {
                    continue;
                }
                // 查询userInfo
                UserInfo userInfo = userInfoService.findById(nearUserId);
                // 排除异性
                if (!StrUtil.equals(gender, userInfo.getGender())) {
                    continue;
                }
                // 封装vo
                NearUserVo vo = new NearUserVo();
                vo.setUserId(userInfo.getId());
                vo.setNickname(userInfo.getNickname());
                vo.setAvatar(userInfo.getAvatar());
                // 添加集合
                voList.add(vo);
            }
        }
        // 4.返回voList
        return ResponseEntity.ok(voList);
    }

    @DubboReference
    private QuestionService questionService;

    // 查看陌生人问题
    public ResponseEntity strangerQuestions(Long jiarenId) {
        // 1.查询
        Question question = questionService.findByUserId(jiarenId);
        // 2.空值判断
        if (question==null) {
            question = new Question(); // 默认值
        }
        // 3.返回结果
        return ResponseEntity.ok(question.getStrangerQuestion());
    }
}
