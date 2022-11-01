package com.itheima.app.controller;

import com.itheima.app.manager.UserManager;
import com.itheima.domain.db.User;
import com.itheima.domain.db.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserManager userManager;

    // 1.保存用户
    @PostMapping("/user/save")
    public ResponseEntity save(@RequestBody User user) {
        return userManager.save(user);
    }

    // 2.手机号查询
    @GetMapping("/user/findByPhone")
    public ResponseEntity findByPhone(String phone) {
        return userManager.findByPhone(phone);
    }

    // 获取验证码
    @PostMapping("/user/login")
    public String sendSms(@RequestBody Map<String, String> param){
        return userManager.smsSend(param.get("phone"));
    }

    // 校验验证码
    @PostMapping("/user/loginVerification")
    public ResponseEntity loginVerification(@RequestBody Map<String, String> param){
        String phone = param.get("phone");
        String verificationCode = param.get("verificationCode");
        return userManager.loginVerification(phone,verificationCode);
    }

    // 保存用户基本信息
    @PostMapping("/user/loginReginfo")
    public void loginReginfo(@RequestBody UserInfo userInfo, @RequestHeader("Authorization") String token) {
        // 调用manager
        userManager.loginReginfo(userInfo, token);
    }

    // 保存用户头像信息
    @PostMapping({"/users/header","/user/loginReginfo/head"})
    public ResponseEntity loginReginfoHead(MultipartFile headPhoto, @RequestHeader("Authorization") String token) throws IOException { // 接收参数
        // 调用manager
        return userManager.loginReginfoHead(headPhoto,token);
    }

    // 查询用户信息
    @GetMapping("/users")
    public ResponseEntity findUserInfoVo(@RequestParam(value = "userID",required = false)Long userId,@RequestHeader("Authorization") String token){
        // 判断
        if (userId !=null) {
            // 调用manager
            return userManager.findUserInfoVo(userId);
        }else{
            // 解析token
            User user = userManager.findUserByToken(token);
            // 调用manager
            return userManager.findUserInfoVo(user.getId());
        }
    }

    // 更新用户基本信息
    @PutMapping("/users")
    public void putUserInfo(@RequestBody UserInfo userInfo) {
        // 调用manager
        userManager.putUserInfo(userInfo);
    }
}
