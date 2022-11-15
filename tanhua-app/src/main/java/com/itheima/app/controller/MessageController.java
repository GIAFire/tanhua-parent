package com.itheima.app.controller;

import com.itheima.app.interceptor.UserHolder;
import com.itheima.app.manager.MessageManager;
import com.itheima.app.manager.UserManager;
import com.itheima.vo.HuanXinVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MessageController {

    @Autowired
    private MessageManager messageManager;

    //  获取环信账号
    @GetMapping("/huanxin/user")
    public ResponseEntity findHuanxinUser() {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.封装vo
        HuanXinVo vo = new HuanXinVo();
        vo.setUsername("HX" + userId);
        vo.setPassword("123456");
        // 3.返回
        return ResponseEntity.ok(vo);
    }

    // 打招呼
    @PostMapping("/tanhua/strangerQuestions")
    public void strangerQuestions(@RequestBody Map<String, String> param) {
        // 接收参数
        Long jiarenId = Long.parseLong(param.get("userId"));
        String reply = param.get("reply");
        // 调用manager
        messageManager.strangerQuestions(jiarenId,reply);
    }

    // 添加好友
    @PostMapping("/messages/contacts")
    public void  addContacts(@RequestBody Map<String,Long> param){
        // 1.接收参数
        Long friendId = param.get("userId");
        // 2.调用manager
        messageManager.addContacts(friendId);
    }

    // 联系人列表
    @GetMapping("/messages/contacts")
    public ResponseEntity findContactVoByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 调用manager
        return messageManager.findContactVoByPage(pageNum, pageSize);
    }

    @Autowired
    private UserManager userManager;

    // 根据huanxinId查询用户基本信息
    @GetMapping("/messages/userinfo")
    public ResponseEntity huanxinUserInfo(String huanxinId){ // HX99、HX12
        // 对环信id进行字符串截取
        String userId = huanxinId.replace("HX", "");
        // 根据id查询用户信息
        return userManager.findUserInfoVo(Long.valueOf(userId));
    }

    // 查看点赞列表
    @GetMapping("/messages/likes")
    public ResponseEntity findLikes(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 指定点赞类型
        Integer commentType = 1;
        // 调用manager
        return messageManager.findUserCommentVoByPage(commentType,pageNum,pageSize);
    }

    // 查看评论列表
    @GetMapping("/messages/comments")
    public ResponseEntity findComments(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 指定点赞类型
        Integer commentType = 2;
        // 调用manager
        return messageManager.findUserCommentVoByPage(commentType,pageNum,pageSize);
    }

    // 查看喜欢列表
    @GetMapping("/messages/loves")
    public ResponseEntity findLoves(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize) {
        // 指定点赞类型
        Integer commentType = 3;
        // 调用manager
        return messageManager.findUserCommentVoByPage(commentType,pageNum,pageSize);
    }

//    public ResponseEntity announcements(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
//                                        @RequestParam(value = "pagesize", defaultValue = "10") Integer pageSize){
//        messageManager.announcements(pageNum,pageSize);
//    }
}
