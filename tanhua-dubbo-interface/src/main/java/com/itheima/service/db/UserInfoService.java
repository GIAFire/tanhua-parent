package com.itheima.service.db;

import com.itheima.domain.db.UserInfo;
import com.itheima.vo.PageBeanVo;

public interface UserInfoService {

    void save(UserInfo userInfo);

    // 更新用户信息
    void update(UserInfo userInfo);

    // 查询用户信息
    UserInfo findById(Long userId);

    // 分页查询
    PageBeanVo findByPage(Integer pageNum, Integer pageSize);
}
