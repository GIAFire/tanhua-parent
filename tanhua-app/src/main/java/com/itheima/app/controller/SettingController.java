package com.itheima.app.controller;

import com.itheima.app.manager.SettingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SettingController {

    @Autowired
    private SettingManager settingManager;

    // 查询通用设置
    @GetMapping("/users/settings")
    public ResponseEntity findSettingVo() {
        // 调用manager
        return settingManager.findSettingVo();
    }

    // 黑名单查询
    @GetMapping("/users/blacklist")
    public ResponseEntity findBalckListByPageBeanVo(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 调用manager
        return settingManager.findBalckListByPageBeanVo(pageNum, pageSize);
    }

    // 移除黑名单
    @DeleteMapping("/users/blacklist/{blackUserId}")
    public void deleteBalckList(@PathVariable Long blackUserId){
        // 调用manager
        settingManager.deleteBalckList(blackUserId);
    }
}
