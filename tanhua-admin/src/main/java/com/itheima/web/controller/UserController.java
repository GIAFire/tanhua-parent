package com.itheima.web.controller;

import com.itheima.web.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserManager userManager;

    // 分页查询
    @GetMapping("/manage/users")
    public ResponseEntity findUserInfoByPage(@RequestParam(value = "page",defaultValue = "1")Integer pageNum,
    @RequestParam(value = "pagesize",defaultValue = "10") Integer pageSize){
        return userManager.findUserInfoByPage(pageNum,pageSize);
    }

    // 用户详情
    @GetMapping("/manage/users/{userId}")
    public ResponseEntity findUserInfoById(@PathVariable Long  userId){
        return userManager.findUserInfoById(userId);
    }

    // 动态列表
    @GetMapping("/manage/messages")
    public ResponseEntity findMovementVoByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "uid", required = false) Long userId,
            Integer state) {
        return userManager.findMovementVoByPage(userId, state, pageNum, pageSize);
    }

    // 动态详情
    @GetMapping("/manage/messages/{publishId}")
    public ResponseEntity findMovementVoById(@PathVariable String publishId){
        return userManager.findMovementVoById(publishId);
    }

    // 动态评论分页
    @GetMapping("/manage/messages/comments")
    public ResponseEntity findCommentVoByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pagesize",defaultValue = "10") Integer pageSize,
            @RequestParam("messageID") String publishId){
        return userManager.findCommentVoByPage(pageNum,pageSize,publishId);
    }
}
