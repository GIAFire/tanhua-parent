package com.itheima.service.db;

import com.itheima.domain.db.Admin;

public interface AdminService {

    // 根据用户名密码查询用户
    Admin findByUsernameAndPassword(String username,String password);
}
