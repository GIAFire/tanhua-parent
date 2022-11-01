package com.itheima.app.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.itheima.app.interceptor.UserHolder;
import com.itheima.domain.db.BlackList;
import com.itheima.domain.db.Notification;
import com.itheima.domain.db.Question;
import com.itheima.domain.db.UserInfo;
import com.itheima.service.db.BlackListService;
import com.itheima.service.db.NotificationService;
import com.itheima.service.db.QuestionService;
import com.itheima.service.db.UserInfoService;
import com.itheima.vo.PageBeanVo;
import com.itheima.vo.SettingVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SettingManager {

    @DubboReference
    private QuestionService questionService;

    @DubboReference
    private NotificationService notificationService;

    // 查询通用设置
    public ResponseEntity findSettingVo() {
        // 1.获取线程内user对象
        Long userId = UserHolder.get().getId();
        // 2.查询陌生人问题
        Question question = questionService.findByUserId(userId);
        // 3.查询推送通知
        Notification notification = notificationService.findByUserId(userId);
        // 4.封装settingVo
        SettingVo vo = new SettingVo();
        vo.setId(userId); // 用户id
        if (question != null) {
            vo.setStrangerQuestion(question.getStrangerQuestion()); // 陌生人问题
        }
        vo.setPhone(UserHolder.get().getMobile());// 用户手机号
        if (notification != null) { // 推送通知
            vo.setLikeNotification(notification.getLikeNotification());
            vo.setPinglunNotification(notification.getPinglunNotification());
            vo.setGonggaoNotification(notification.getGonggaoNotification());
        }
        // 5.返回vo
        return ResponseEntity.ok(vo);
    }

    @DubboReference
    private BlackListService blackListService;

    @DubboReference
    private UserInfoService userInfoService;

    // 黑名单查询
    public ResponseEntity findBalckListByPageBeanVo(Integer pageNum, Integer pageSize) {
        // 1.获取线程内的userId
        Long userId = UserHolder.get().getId();
        // 2.根据三个条件查询
        PageBeanVo pageBeanVo = blackListService.findByPage(userId, pageNum, pageSize);
        // 3.根据黑名单用户id查询userInfo
        // 3-1 获取黑名单list
        List<BlackList> blackListList = (List<BlackList>) pageBeanVo.getItems();
        // 3-2 声明userInfoList
        List<UserInfo> userInfoList = new ArrayList<>();
        // 3-3 完成封装设置
        if (CollectionUtil.isNotEmpty(blackListList)) {
            for (BlackList blackList : blackListList) {
                // 获取黑名单用户id
                Long blackUserId = blackList.getBlackUserId();
                // 查询userInfo
                UserInfo userInfo = userInfoService.findById(blackUserId);
                // 设置到list集合
                userInfoList.add(userInfo);
            }
        }
        // 4.替换 将userInfoList设置到pageBeanVo
        pageBeanVo.setItems(userInfoList);
        // 5.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }

    // 移除黑名单
    public void deleteBalckList(Long blackUserId) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.rpc删除
        blackListService.delete(userId, blackUserId);
    }
}
