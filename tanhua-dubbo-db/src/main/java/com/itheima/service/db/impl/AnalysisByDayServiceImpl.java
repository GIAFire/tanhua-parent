package com.itheima.service.db.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.domain.db.AnalysisByDay;
import com.itheima.domain.db.Log;
import com.itheima.mapper.AnalysisByDayMapper;
import com.itheima.mapper.LogMapper;
import com.itheima.service.db.AnalysisByDayService;
import com.itheima.util.ComputeUtil;
import com.itheima.vo.AnalysisSummaryVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

@DubboService
public class AnalysisByDayServiceImpl implements AnalysisByDayService {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private AnalysisByDayMapper analysisByDayMapper;

    @Override
    public void logToAnalysisByDay() {
        // 1.准备基础数据
        // 1-1 今天 2022-02-24
        String today = DateUtil.formatDate(new Date());
        // 1-2 昨天 2022-2-23
        String yesterday = DateUtil.offsetDay(new Date(), -1).toDateStr();

        // 2.查询log
        // 2-1 今日注册
        Integer regCount = logMapper.findRegAngLogCount("0102", today);
        // 2-2 今日活跃
        Integer activeCount = logMapper.findActiveCount(today);
        // 2-3 今日登录
        Integer logCount = logMapper.findRegAngLogCount("0101", today);
        // 2-4 次日留存
        Integer retentionCount = logMapper.findRetentionCount(today, yesterday);

        // 3.保存analysisByDay
        // 3-1 先查询是否有数据
        LambdaQueryWrapper<AnalysisByDay> qw = new LambdaQueryWrapper<>();
        qw.eq(AnalysisByDay::getRecordDate, today);
        AnalysisByDay analysisByDay = analysisByDayMapper.selectOne(qw);
        if (analysisByDay == null) { // 3-2 新增
            analysisByDay = new AnalysisByDay();
            analysisByDay.setRecordDate(new Date()); // 记录时间
            analysisByDay.setNumRegistered(regCount); // 注册人数
            analysisByDay.setNumActive(activeCount); // 活跃人数
            analysisByDay.setNumLogin(logCount); // 登录人数
            analysisByDay.setNumRetention1d(retentionCount); // 次日留存
            analysisByDayMapper.insert(analysisByDay); // 使用insert方法
        } else { // 3-3 更新
            analysisByDay.setNumRegistered(regCount); // 注册人数
            analysisByDay.setNumActive(activeCount); // 活跃人数
            analysisByDay.setNumLogin(logCount); // 登录人数
            analysisByDay.setNumRetention1d(retentionCount); // 次日留存
            analysisByDayMapper.updateById(analysisByDay); // 使用updateById方法
        }

    }

    // 查询统计数据
    @Override
    public AnalysisSummaryVo findAnalysisSummaryVo() {
        // 1.准备基础数据
        // 1-1 今天
        String today = DateUtil.offsetDay(new Date(), 0).toDateStr();
        // 1-2 昨天
        String yesterDay = DateUtil.offsetDay(new Date(), -1).toDateStr();
        // 1-3 7天前
        String weekDay = DateUtil.offsetDay(new Date(), -6).toDateStr();
        // 1-4 30天前
        String monthDay = DateUtil.offsetDay(new Date(), -29).toDateStr();
        // 1-5 今天基础数据
        AnalysisByDay todayAnay = analysisByDayMapper.findBaseByRecordDate(today);
        // 1-6 昨天的基础数据
        AnalysisByDay yesterDayAnay = analysisByDayMapper.findBaseByRecordDate(yesterDay);


        // 2.凑齐9项数据
        // 2-1 累计用户数
        Long cumulativeUsers = analysisByDayMapper.findCumulativeUsers();
        // 2-2 过去30天活跃用户数
        Long activePassMonth = analysisByDayMapper.findActivePass(monthDay, today);
        // 2-3 过去7天活跃用户
        Long activePassWeek = analysisByDayMapper.findActivePass(weekDay, today);
        // 2-4 今日新增用户数量
        Integer newUsersToday = todayAnay.getNumRegistered();
        // 2-5 今日新增用户涨跌率，单位百分数，正数为涨，负数为跌   BigDecimal : 商业数字格式
        BigDecimal newUsersTodayRate = ComputeUtil.computeRate(newUsersToday, yesterDayAnay.getNumRegistered());
        // 2-6 今日登录次数
        Integer loginTimesToday = todayAnay.getNumLogin();
        // 2-7 今日登录次数涨跌率，单位百分数，正数为涨，负数为跌
        BigDecimal loginTimesTodayRate = ComputeUtil.computeRate(loginTimesToday, yesterDayAnay.getNumLogin());
        // 2-8 今日活跃用户数量
        Integer activeUsersToday = todayAnay.getNumActive();
        // 2-9 今日活跃用户涨跌率，单位百分数，正数为涨，负数为跌
        BigDecimal activeUsersTodayRate = ComputeUtil.computeRate(activeUsersToday, yesterDayAnay.getNumActive());

        // 3.封装返回vo
        AnalysisSummaryVo vo = new AnalysisSummaryVo();
        vo.setCumulativeUsers(cumulativeUsers);
        vo.setActivePassWeek(activePassWeek);
        vo.setActivePassMonth(activePassMonth);
        vo.setNewUsersToday(newUsersToday.longValue());
        vo.setNewUsersTodayRate(newUsersTodayRate);
        vo.setLoginTimesToday(loginTimesToday.longValue());
        vo.setLoginTimesTodayRate(loginTimesTodayRate);
        vo.setActiveUsersToday(activeUsersToday.longValue());
        vo.setActiveUsersTodayRate(activeUsersTodayRate);
        return vo;
    }
}
