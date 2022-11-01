package com.itheima.service.db.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.domain.db.BlackList;
import com.itheima.mapper.BlackListMapper;
import com.itheima.service.db.BlackListService;
import com.itheima.vo.PageBeanVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class BlackListServiceImpl implements BlackListService {

    @Autowired
    private BlackListMapper blackListMapper;

    @Override
    public PageBeanVo findByPage(Long userId, Integer pageNum, Integer pageSize) {
        // 1.开启分页
        Page<BlackList> page = new Page<>(pageNum, pageSize);
        // 2.查询分页
        LambdaQueryWrapper<BlackList> qw = new LambdaQueryWrapper<>();
        qw.eq(BlackList::getUserId, userId);
        page = blackListMapper.selectPage(page, qw);
        // 3.封装pageBeanVo
        return new PageBeanVo(pageNum, pageSize, page.getTotal(), page.getRecords());
    }

    // 移除黑名单
    @Override
    public void delete(Long userId, Long blackUserId) {
        // 构建条件
        LambdaQueryWrapper<BlackList> qw = new LambdaQueryWrapper<>();
        qw.eq(BlackList::getUserId, userId);
        qw.eq(BlackList::getBlackUserId, blackUserId);
        // mapper删除
        blackListMapper.delete(qw);
    }
}