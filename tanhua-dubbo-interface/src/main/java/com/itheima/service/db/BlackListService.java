package com.itheima.service.db;

import com.itheima.vo.PageBeanVo;

public interface BlackListService {

    PageBeanVo findByPage(Long userId,Integer pageNum,Integer pageSize);

    // 移除黑名单
    void delete(Long userId, Long blackUserId);
}
