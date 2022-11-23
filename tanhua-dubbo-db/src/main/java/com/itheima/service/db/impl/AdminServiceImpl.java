package com.itheima.service.db.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.domain.db.Admin;
import com.itheima.mapper.AdminMapper;
import com.itheima.service.db.AdminService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findByUsernameAndPassword(String username, String password) {
        // 1.构建条件
        LambdaQueryWrapper<Admin> qw = new LambdaQueryWrapper<>();
        qw.eq(Admin::getUsername, username);
        qw.eq(Admin::getPassword, password);
        // 2.执行查询
        return adminMapper.selectOne(qw);
    }
}
