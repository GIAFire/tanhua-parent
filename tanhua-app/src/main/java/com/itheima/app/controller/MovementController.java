package com.itheima.app.controller;

import com.itheima.app.manager.MovementManager;
import com.itheima.domain.mongo.Movement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class MovementController {

    @Autowired
    private MovementManager movementManager;

    // 发布动态  Content-Type	multipart/form-data  不能使用@RequestBody注解
    @PostMapping("/movements")
    public void publishMovement(Movement movement, MultipartFile[] imageContent) throws IOException {
        // 调用manager
        movementManager.publishMovement(movement,imageContent);
    }

    // 查看个人动态
    @GetMapping("/movements/all")
    public ResponseEntity findMyMovementByPage(
            Long userId,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 调用manager
        return movementManager.findMyMovementByPage(userId, pageNum, pageSize);
    }

    // 查询好友动态
    @GetMapping("/movements")
    public ResponseEntity findFriendMovementByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 调用manager
        return movementManager.findFriendMovementByPage(pageNum,pageSize);
    }

    // 查询推荐动态
    @GetMapping("/movements/recommend")
    public ResponseEntity findRecommendMovementByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 调用manager
        return movementManager.findRecommendMovementByPage(pageNum,pageSize);
    }

    // 动态点赞
    @GetMapping("/movements/{publishId}/like")
    public ResponseEntity movementLike(@PathVariable String publishId) {
        // 调用manager
        return movementManager.movementLike(publishId);
    }

    // 动态取消点赞
    @GetMapping("/movements/{publishId}/dislike")
    public ResponseEntity movementDisLike(@PathVariable String publishId) {
        // 调用manager
        return movementManager.movementDisLike(publishId);
    }

    // 动态喜欢
    @GetMapping("/movements/{publishId}/love")
    public ResponseEntity movementLove(@PathVariable String publishId) {
        // 调用manager
        return movementManager.movementLove(publishId);
    }

    // 动态取消喜欢
    @GetMapping("/movements/{publishId}/unlove")
    public ResponseEntity movementUnLove(@PathVariable String publishId) {
        // 调用manager
        return movementManager.movementUnLove(publishId);
    }

    // 查询单条动态
    @GetMapping("/movements/{publishId}")
    public ResponseEntity findMovementVoById(@PathVariable String publishId) {
        // 调用manager
        return movementManager.findMovementVoById(publishId);
    }

    // 动态评论分页查询
    @GetMapping("/comments")
    public ResponseEntity findMovementCommentVo(
            @RequestParam(value = "movementId") String publishId,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 调用manager
        return movementManager.findMovementCommentVo(publishId,pageNum,pageSize);
    }

    // 动态评论
    @PostMapping("/comments")
    public ResponseEntity movementComment(@RequestBody Map<String,String> param){
        // 1.接收参数
        String publishId = param.get("movementId");
        String content = param.get("comment");
        // 2.调用manager
        return movementManager.movementComment(publishId,content);
    }
}

