package com.itheima.web.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.itheima.domain.db.UserInfo;
import com.itheima.domain.mongo.Comment;
import com.itheima.domain.mongo.Movement;
import com.itheima.service.db.UserInfoService;
import com.itheima.service.mongo.CommentService;
import com.itheima.service.mongo.MovementService;
import com.itheima.vo.CommentVo;
import com.itheima.vo.MovementVo;
import com.itheima.vo.PageBeanVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserManager {

    @DubboReference
    private UserInfoService userInfoService;

    // 分页查询
    public ResponseEntity findUserInfoByPage(Integer pageNum, Integer pageSize) {
        // 调用rpc查询
        PageBeanVo pageBeanVo = userInfoService.findByPage(pageNum, pageSize);
        // 返回
        return ResponseEntity.ok(pageBeanVo);
    }

    // 用户详情
    public ResponseEntity findUserInfoById(Long userId) {
        // 调用rpc查询
        UserInfo userInfo = userInfoService.findById(userId);
        // 返回userInfo
        return ResponseEntity.ok(userInfo);
    }

    @DubboReference
    private MovementService movementService;

    // 动态列表
    public ResponseEntity findMovementVoByPage(Long userId, Integer state, Integer pageNum, Integer pageSize) {
        // 1.调用rpc查询
        PageBeanVo pageBeanVo = movementService.findMovementByPage(userId, state, pageNum, pageSize);
        // 2.封装vo
        // 2-1 声明voList
        List<MovementVo> voList = new ArrayList<>();
        // 2-2 获取movementList
        List<Movement> movementList = (List<Movement>) pageBeanVo.getItems();
        // 2-3 遍历movementList
        if (CollectionUtil.isNotEmpty(movementList)) {
            for (Movement movement : movementList) {
                // 查询userInfo
                UserInfo userInfo = userInfoService.findById(movement.getUserId());
                // 创建vo
                MovementVo vo = new MovementVo();
                vo.setUserInfo(userInfo);
                vo.setMovement(movement);
                // 自定义时间
                vo.setCreateDate(DateUtil.formatDateTime(new Date(movement.getCreated())));
                // 添加到集合
                voList.add(vo);
            }
        }
        // 3.将voList设置到分页对象
        pageBeanVo.setItems(voList);
        // 4.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }


    // 动态详情
    public ResponseEntity findMovementVoById(String publishId) {
        // 1.调用rpc查询动态详情
        Movement movement = movementService.findById(new ObjectId(publishId));
        // 2.封装vo
        MovementVo vo = new MovementVo();
        vo.setUserInfo(userInfoService.findById(movement.getUserId())); // 用户信息
        vo.setMovement(movement); // 动态信息
        vo.setCreateDate(DateUtil.formatDateTime(new Date(movement.getCreated()))); // 指定动态时间
        // 3.返回vo
        return ResponseEntity.ok(vo);
    }

    @DubboReference
    private CommentService commentService;

    // 动态评论分页
    public ResponseEntity findCommentVoByPage(Integer pageNum, Integer pageSize, String publishId) {
        // 1.调用rpc查询
        PageBeanVo pageBeanVo = commentService.findCommentByPage(new ObjectId(publishId), 2, pageNum, pageSize);
        // 2.封装vo
        // 2-1 声明
        List<CommentVo> voList = new ArrayList<>();
        // 2-2 获取
        List<Comment> commentList = (List<Comment>) pageBeanVo.getItems();
        // 2-3 遍历
        if (CollectionUtil.isNotEmpty(commentList)) {
            for (Comment comment : commentList) {
                // 查询userInfo
                UserInfo userInfo = userInfoService.findById(comment.getUserId());
                // 创建vo
                CommentVo vo = new CommentVo();
                vo.setId(comment.getId().toHexString());
                vo.setAvatar(userInfo.getAvatar());
                vo.setNickname(userInfo.getNickname());
                vo.setContent(comment.getContent());
                vo.setCreateDate(DateUtil.formatDateTime(new Date(comment.getCreated())));
                // 添加到集合
                voList.add(vo);
            }
        }
        // 3.将voList添加到集合
        pageBeanVo.setItems(voList);

        // 4.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }
}
