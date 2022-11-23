package com.itheima.web.controller;

import com.itheima.web.manager.AnalysisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalysisController {

    @Autowired
    private AnalysisManager analysisManager;

    // 统计展示
    @GetMapping("/dashboard/summary")
    public ResponseEntity findSummaryVo(){
        return analysisManager.findSummaryVo();
    }
}
