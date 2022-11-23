package com.itheima.web.controller;

import cn.hutool.captcha.LineCaptcha;
import com.itheima.domain.db.Admin;
import com.itheima.web.manager.AdminManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    private AdminManager adminManager;

    // 生成验证码
    @GetMapping("/system/users/verification")
    public void verification(String uuid, HttpServletResponse response) throws Exception {
        // 调用manager
        LineCaptcha lineCaptcha = adminManager.verification(uuid);
        // response响应
        lineCaptcha.write(response.getOutputStream());
    }

    // 管理员登录
    @PostMapping("/system/users/login")
    public ResponseEntity login(@RequestBody Map<String, String> param) {
        // 接收参数
        String username = param.get("username");
        String password = param.get("password");
        String verificationCode = param.get("verificationCode");
        String uuid = param.get("uuid");
        // 调用manager
        return adminManager.login(username, password, verificationCode, uuid);
    }

    // 获取管理员信息
    @PostMapping("/system/users/profile")
    public ResponseEntity profile(@RequestHeader("Authorization") String token) {
        // 调用manager
        Admin admin = adminManager.findAdminByToken(token);
        return ResponseEntity.ok(admin);
    }

    // 用户退出
    @PostMapping("/system/users/logout")
    public void logout(@RequestHeader("Authorization") String token){
        // 调用manager
        adminManager.logout(token);
    }
}
