package com.itheima.service.db.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.domain.db.Notification;
import com.itheima.mapper.NotificationMapper;
import com.itheima.service.db.NotificationService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public Notification findByUserId(Long userId) {
        return notificationMapper.selectOne(new LambdaQueryWrapper<Notification>()
                .eq(Notification::getUserId,userId));
    }
}
