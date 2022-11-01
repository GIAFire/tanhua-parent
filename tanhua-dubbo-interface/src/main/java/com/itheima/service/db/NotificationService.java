package com.itheima.service.db;

import com.itheima.domain.db.Notification;

public interface NotificationService {

    Notification findByUserId(Long userId);
}
