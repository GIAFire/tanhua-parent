package com.itheima.web.manager;

import com.itheima.service.db.AnalysisByDayService;
import com.itheima.vo.AnalysisSummaryVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AnalysisManager {

    @DubboReference
    private AnalysisByDayService analysisByDayService;

    // 统计展示
    public ResponseEntity findSummaryVo() {
        AnalysisSummaryVo analysisSummaryVo = analysisByDayService.findAnalysisSummaryVo();

        return ResponseEntity.ok(analysisSummaryVo);
    }
}
