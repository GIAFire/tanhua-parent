package com.itheima.service.db;

import com.itheima.vo.AnalysisSummaryVo;

public interface AnalysisByDayService {

    // 日志同步每日分析
    void logToAnalysisByDay();

    // 查询统计数据
    AnalysisSummaryVo findAnalysisSummaryVo();
}
