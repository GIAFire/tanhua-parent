package com.itheima.service.db.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.domain.db.UserInfo;
import com.itheima.mapper.UserInfoMapper;
import com.itheima.service.db.UserInfoService;
import com.itheima.vo.PageBeanVo;
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

    // 分页查询
    @Override
    public PageBeanVo findByPage(Integer pageNum, Integer pageSize) {
        // 构建分页对象
        Page<UserInfo> page = new Page<>(pageNum, pageSize);
        // 分页查询
        page = userInfoMapper.selectPage(page, null);
        // 封装vo并返回
        return new PageBeanVo(pageNum, pageSize, page.getTotal(), page.getRecords());
    }
}
