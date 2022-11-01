package com.itheima.service.db;

import com.itheima.domain.db.User;

public interface UserService {
    // 保存用户
    Long save(User user);

    // 手机查询用户
    User findByPhone(String phone);
}
