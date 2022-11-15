package com.itheima.app.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.itheima.app.interceptor.UserHolder;
import com.itheima.autoconfig.huanxin.HuanXinTemplate;
import com.itheima.domain.db.Question;
import com.itheima.domain.db.UserInfo;
import com.itheima.domain.mongo.Comment;
import com.itheima.domain.mongo.Friend;
import com.itheima.service.db.QuestionService;
import com.itheima.service.db.UserInfoService;
import com.itheima.service.mongo.CommentService;
import com.itheima.service.mongo.FriendService;
import com.itheima.vo.ContactVo;
import com.itheima.vo.PageBeanVo;
import com.itheima.vo.UserCommentVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Component
public class MessageManager {

    @DubboReference
    private UserInfoService userInfoService;

    @DubboReference
    private QuestionService questionService;

    @Autowired
    private HuanXinTemplate huanXinTemplate;

    // 打招呼
    public void strangerQuestions(Long jiarenId, String reply) {
        // 1.获取线程内id
        Long userId = UserHolder.get().getId();
        // 2.获取昵称
        String nickname = userInfoService.findById(userId).getNickname();
        // 3.获取佳人陌生人问题
        Question question = questionService.findByUserId(jiarenId);
        if (question == null) {
            question = new Question();
        }
        // 4.组装json
        Map requestBody = new HashMap();
        requestBody.put("userId", userId);
        requestBody.put("huanXinId", "HX" + userId);
        requestBody.put("nickname", nickname);
        requestBody.put("strangerQuestion", question.getStrangerQuestion());
        requestBody.put("reply", reply);
        String json = JSON.toJSONString(requestBody);
        // 5.发送给佳人
        huanXinTemplate.sendMsg("HX" + jiarenId, json);
    }

    @DubboReference
    private FriendService friendService;

    // 添加好友
    public void addContacts(Long friendId) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.mongo好友添加
        friendService.addContacts(userId, friendId);
        // 3.环信好友添加
        huanXinTemplate.addContacts("HX" + userId, "HX" + friendId);
    }

    // 联系人列表
    public ResponseEntity findContactVoByPage(Integer pageNum, Integer pageSize) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.调用rpc查询
        PageBeanVo pageBeanVo = friendService.findContactByPage(userId, pageNum, pageSize);
        // 3.封装vo
        // 3-1 声明voList
        List<ContactVo> voList = new ArrayList<>();
        // 3-2 获取friendList
        List<Friend> friendList = (List<Friend>) pageBeanVo.getItems();
        // 3-3 遍历friendList
        if (CollectionUtil.isNotEmpty(friendList)) {
            for (Friend friend : friendList) {
                // 先好友查询userInfo
                UserInfo userInfo = userInfoService.findById(friend.getFriendId());
                // 创建vo
                ContactVo vo = new ContactVo();
                vo.setUserInfo(userInfo); // 好友信息
                vo.setUserId("HX" + friend.getFriendId()); // 好友的环信id

                // 添加到集合
                voList.add(vo);
            }
        }
        // 4.将voList设置到分页对象
        pageBeanVo.setItems(voList);
        // 5.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }

    @DubboReference
    private CommentService commentService;

    public ResponseEntity findUserCommentVoByPage(Integer commentType, Integer pageNum, Integer pageSize) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.调用rpc查询
        PageBeanVo pageBeanVo = commentService.findUserCommentByPage(userId, commentType, pageNum, pageSize);
        // 3.封装vo
        // 3-1 声明volist
        List<UserCommentVo> voList = new ArrayList<>();
        // 3-2 获取commentList
        List<Comment> commentList = (List<Comment>) pageBeanVo.getItems();
        // 3-3 遍历commentList
        if (CollectionUtil.isNotEmpty(commentList)) {
            for (Comment comment : commentList) {
                // 先查询userInfo
                UserInfo userInfo = userInfoService.findById(comment.getUserId());// 查评论人的信息
                // 创建vo
                UserCommentVo vo = new UserCommentVo();
                vo.setId(comment.getId().toHexString()); // 评论id
                vo.setNickname(userInfo.getNickname());
                vo.setAvatar(userInfo.getAvatar());
                vo.setCreateDate(DateUtil.formatDateTime(new Date(comment.getCreated())));
                // 添加到集合
                voList.add(vo);
            }
        }
        // 4.将voList设置到分页对象
        pageBeanVo.setItems(voList);
        // 5.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }

    // TODO 公告,待做
//    public ResponseEntity announcements(Integer pageNum,Integer pageSize){
//
//    }
}
