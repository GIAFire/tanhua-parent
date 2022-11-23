package com.itheima.app.controller;

import com.itheima.app.interceptor.UserHolder;
import com.itheima.app.manager.VideoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class VideoController {

    @Autowired
    private VideoManager videoManager;

    // 查询推荐视频
    @GetMapping("/smallVideos")
    public ResponseEntity findRecommendVideoVoByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 获取登录用户id
        Long userId = UserHolder.get().getId();
        // 调用manager
        return videoManager.findRecommendVideoVoByPage(userId, pageNum, pageSize);
    }

    // 发布视频
    @PostMapping("/smallVideos")
    public void publishVideo(MultipartFile videoThumbnail, MultipartFile videoFile) throws IOException {
        // 获取登录用户id
        Long userId = UserHolder.get().getId();
        // 调用manager
        videoManager.publishVideo(userId,videoThumbnail, videoFile);
        System.out.println("视频上传成功");
    }

    // 关注用户
    @PostMapping("/smallVideos/{focusUserId}/userFocus")
    public void saveFocusUser(@PathVariable Long focusUserId) {
        // 获取登录用户id
        Long userId = UserHolder.get().getId();
        // 调用manager
        videoManager.saveFocusUser(userId, focusUserId);
    }

    // 取消关注
    @PostMapping("/smallVideos/{focusUserId}/userUnFocus")
    public void removeFocusUser(@PathVariable Long focusUserId) {
        // 获取登录用户id
        Long userId = UserHolder.get().getId();
        // 调用manager
        videoManager.removeFocusUser(userId, focusUserId);
    }
}

