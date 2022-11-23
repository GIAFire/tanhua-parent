package com.itheima.job;

import com.itheima.service.db.AnalysisByDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnalysisJob {

    @Autowired
    private AnalysisByDayService analysisByDayService;


    // @Scheduled(cron = "0 0 0/1 * * ?") 间隔一小时
    @Scheduled(cron = "0/30 * * * * ?")
    public void logToAnalysisJob(){
        System.out.println("日志同步每日分析开始....");
        analysisByDayService.logToAnalysisByDay();
        System.out.println("日志同步每日分析结束....");
    }
}
