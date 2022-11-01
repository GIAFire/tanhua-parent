package com.itheima.service.db.impl;

import com.itheima.domain.db.UserInfo;
import com.itheima.mapper.UserInfoMapper;
import com.itheima.service.db.UserInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    // 更新用户信息
    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    // 查询用户信息
    @Override
    public UserInfo findById(Long userId) {
        return userInfoMapper.selectById(userId);
    }
}
