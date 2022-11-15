package com.itheima.app.controller;

import com.itheima.app.manager.MakeFriendManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MakeFriendController {

    @Autowired
    private MakeFriendManager makeFriendManager;

    // 今日佳人
    @GetMapping("/tanhua/todayBest")
    public ResponseEntity findTodayBest() {
        // 调用manager
        return makeFriendManager.findTodayBest();
    }

    // 推荐用户分页
    @GetMapping("/tanhua/recommendation")
    public ResponseEntity findRecommendUserVoByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 调用manager
        return makeFriendManager.findRecommendUserVoByPage(pageNum, pageSize);
    }

    // 查询用户封面信息
    @GetMapping("/tanhua/{jiarenId}/personalInfo")
    public ResponseEntity personalInfo(@PathVariable Long jiarenId) {
        // 调用manager
        return makeFriendManager.personalInfo(jiarenId);
    }

    // 是否喜欢
    @GetMapping("users/{likeUserId}/alreadyLove")
    public ResponseEntity alreadyLove(@PathVariable Long likeUserId){
        return ResponseEntity.ok(true); // TODO 左滑不喜欢右滑喜欢  待完成
    }

    // 最近访客
    @GetMapping("/movements/visitors")
    public ResponseEntity visitors(){
        // 调用manager
        return makeFriendManager.visitors();
    }

    // 上报地理位置
    @PostMapping("/baidu/location")
    public void location(@RequestBody Map<String, String> param) {
        System.out.println("上报地理位置："+param);
        // 1.获取参数
        Double longitude = Double.parseDouble(param.get("longitude"));
        Double latitude = Double.parseDouble(param.get("latitude"));
        String addrStr = param.get("addrStr");
        // 2.调用manager
        makeFriendManager.location(longitude,latitude,addrStr);
    }

    // 搜附近
    @GetMapping("/tanhua/search")
    public ResponseEntity searchNearUserVo(String gender,Long distance){
        // 调用manager
        return makeFriendManager.searchNearUserVo(gender,distance);
    }

    // 查看陌生人问题
    @GetMapping("/tanhua/strangerQuestions")
    public ResponseEntity strangerQuestions(@RequestParam(value = "userId")Long jiarenId){
        // 调用manager
        return makeFriendManager.strangerQuestions(jiarenId);
    }
}
