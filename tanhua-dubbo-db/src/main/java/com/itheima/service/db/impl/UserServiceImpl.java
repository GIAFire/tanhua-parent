package com.itheima.service.db.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.domain.db.User;
import com.itheima.mapper.UserMapper;
import com.itheima.service.db.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long save(User user) {
        // 密码加密
        String md5Pwd = SecureUtil.md5(user.getPassword());
        user.setPassword(md5Pwd);
        // 调用mapper保存
        userMapper.insert(user);
        // 返回主键
        return user.getId();
    }

    @Override
    public User findByPhone(String phone) {
        // 构建条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMobile, phone);
        // 调用mapper查询
        return userMapper.selectOne(queryWrapper);
    }
}
