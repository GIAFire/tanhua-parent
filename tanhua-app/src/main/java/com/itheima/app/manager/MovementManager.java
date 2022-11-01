package com.itheima.app.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itheima.app.interceptor.UserHolder;
import com.itheima.autoconfig.oss.OssTemplate;
import com.itheima.domain.db.UserInfo;
import com.itheima.domain.mongo.Comment;
import com.itheima.domain.mongo.Movement;
import com.itheima.service.db.UserInfoService;
import com.itheima.service.mongo.CommentService;
import com.itheima.service.mongo.MovementService;
import com.itheima.util.ConstantUtil;
import com.itheima.vo.CommentVo;
import com.itheima.vo.MovementVo;
import com.itheima.vo.PageBeanVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MovementManager {

    @DubboReference
    private MovementService movementService;

    @Autowired
    private OssTemplate ossTemplate;

    // 发布动态
    public void publishMovement(Movement movement, MultipartFile[] imageContent) throws IOException {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.上传图片到 oss]
        // 2-1 声明图片地址集合
        List<String> medias = new ArrayList<>();
        // 2-2 遍历图片
        String url = "https://";
        for (MultipartFile multipartFile : imageContent) {
            // 2-3上传
            url += ossTemplate.upload(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
            // 2-4 保存图片地址集合
            medias.add(url);
        }
        // 3.完善动态详情
        movement.setId(ObjectId.get()); // java工具生成动态id（后面要用）
        movement.setUserId(userId); // 发布人id
        movement.setMedias(medias);// 动态图片地址
        movement.setState(1); // TODO 暂时写1，后期会用阿里云内容审核完善
        movement.setCreated(System.currentTimeMillis());// 发布时间
        movement.setSeeType(1); // 目前探花1.0 公开所有
        // 4.调用rpc发布
        movementService.publishMovement(movement);

    }

    @DubboReference
    private UserInfoService userInfoService;

    // 查看个人动态
    public ResponseEntity findMyMovementByPage(Long userId, Integer pageNum, Integer pageSize) {
        // 1.调用rpc查询动态详情
        PageBeanVo pageBeanVo = movementService.findMyMovementByPage(userId, pageNum, pageSize);
        // 2.封装movementVo
        // 2-1 声明voList
        List<MovementVo> voList = new ArrayList<>();
        // 2-2 获取movementList
        List<Movement> movementList = (List<Movement>) pageBeanVo.getItems();
        // 2-3 遍历movementList
        if (CollectionUtil.isNotEmpty(movementList)) {
            for (Movement movement : movementList) {
                // 查询userInfo
                UserInfo userInfo = userInfoService.findById(movement.getUserId());
                // 封装   注意：先设置userInfo  在设置 movement
                MovementVo vo = new MovementVo();
                vo.setUserInfo(userInfo);
                vo.setMovement(movement);
                // 是否点赞
                if (stringRedisTemplate.hasKey(StrUtil.format(ConstantUtil.MOVEMENT_LIKE,userId,movement.getId().toHexString()))) {
                    vo.setHasLiked(1);
                }
                // 设置到voList中
                voList.add(vo);
            }
        }
        // 3.将voList设置到分页对象
        pageBeanVo.setItems(voList);
        // 4.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }

    // 查询好友动态
    public ResponseEntity findFriendMovementByPage(Integer pageNum, Integer pageSize) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.调用rpc查询动态详情
        PageBeanVo pageBeanVo = movementService.findFriendMovementByPage(userId, pageNum, pageSize);
        // 3.封装movementVo
        // 3-1 声明voList
        List<MovementVo> voList = new ArrayList<>();
        // 3-2 获取movementList
        List<Movement> movementList = (List<Movement>) pageBeanVo.getItems();
        // 3-3 遍历movementList
        if (CollectionUtil.isNotEmpty(movementList)) {
            for (Movement movement : movementList) {
                // 查询userInfo
                UserInfo userInfo = userInfoService.findById(movement.getUserId());
                // 创建vo
                MovementVo vo = new MovementVo();
                vo.setUserInfo(userInfo); // 先
                vo.setMovement(movement); // 后
                // 是否点赞
                if (stringRedisTemplate.hasKey(StrUtil.format(ConstantUtil.MOVEMENT_LIKE,userId,movement.getId().toHexString()))) {
                    vo.setHasLiked(1);
                }
                // 添加到集合
                voList.add(vo);
            }
        }
        // 4.将voList设置分页对象
        pageBeanVo.setItems(voList);
        // 5.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }

    // 查询推荐动态
    public ResponseEntity findRecommendMovementByPage(Integer pageNum, Integer pageSize) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.查询推荐动态详情
        PageBeanVo pageBeanVo = movementService.findRecommendMovementByPage(userId, pageNum, pageSize);
        // 3.封装movementVo
        // 3-1 声明voList
        List<MovementVo> voList = new ArrayList<>();
        // 3-2 获取movementList
        List<Movement> movementList = (List<Movement>) pageBeanVo.getItems();
        // 3-3 遍历movementList
        if (CollectionUtil.isNotEmpty(movementList)) {
            for (Movement movement : movementList) {
                // 查询userInfo
                UserInfo userInfo = userInfoService.findById(movement.getUserId());
                // 创建vo
                MovementVo vo = new MovementVo();
                vo.setUserInfo(userInfo); // 先
                vo.setMovement(movement); // 后端
                // 是否点赞
                if (stringRedisTemplate.hasKey(StrUtil.format(ConstantUtil.MOVEMENT_LIKE,userId,movement.getId().toHexString()))) {
                    vo.setHasLiked(1);
                }
                if (stringRedisTemplate.hasKey(StrUtil.format(ConstantUtil.MOVEMENT_LOVE,userId,movement.getId().toHexString()))) {
                    vo.setHasLiked(1);
                }
                //  添加集合
                voList.add(vo);
            }
        }
        // 4.voList设置到分页对象
        pageBeanVo.setItems(voList);

        // 5.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }

    @DubboReference
    private CommentService commentService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 动态点赞
    public ResponseEntity movementLike(String publishId) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.查询动态详情
        Movement movement = movementService.findById(new ObjectId(publishId));
        // 3.封装comment
        Comment comment = new Comment();
        comment.setCreated(System.currentTimeMillis()); // 点赞时间
        comment.setPublishId(movement.getId());// 动态id
        comment.setCommentType(1); // 点赞类型
        comment.setUserId(userId); // 登录人id
        comment.setPublishUserId(movement.getUserId()); // 动态发布人id

        // 4.调用rpd保存
        Integer likeCount = commentService.saveComment(comment);

        // 5. 向redis中存储标记  movement_like:{userId}_{publishId} = 1
        stringRedisTemplate.opsForValue().set(StrUtil.format(ConstantUtil.MOVEMENT_LIKE, userId, publishId), "1");

        // 6.返回点赞数量
        return ResponseEntity.ok(likeCount);
    }

    // 动态取消点赞
    public ResponseEntity movementDisLike(String publishId) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.调用rpc删除（三个条件）
        Integer likeCount = commentService.removeComment(new ObjectId(publishId), userId, 1);
        // 3.从reids中删除点赞标记
        stringRedisTemplate.delete(StrUtil.format(ConstantUtil.MOVEMENT_LIKE, userId, publishId));
        // 4.返回点赞数量
        return ResponseEntity.ok(likeCount);
    }

    // 动态喜欢
    public ResponseEntity movementLove(String publishId) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.查询动态详情
        Movement movement = movementService.findById(new ObjectId(publishId));
        // 3.封装comment对象
        Comment comment = new Comment();
        comment.setCreated(System.currentTimeMillis()); // 喜欢时间
        comment.setPublishId(movement.getId());// 动态id
        comment.setCommentType(3);// 动态喜欢类型
        comment.setUserId(userId); // 登录人id
        comment.setPublishUserId(movement.getUserId());// 动态发布人id
        // 4.调用rpc保存
        Integer loveCount = commentService.saveComment(comment);
        // 5.向redis中存储喜欢标记
        stringRedisTemplate.opsForValue().set(StrUtil.format(ConstantUtil.MOVEMENT_LOVE, userId, publishId), "1");
        // 6.返回喜欢数量
        return ResponseEntity.ok(loveCount);
    }

    // 动态取消喜欢
    public ResponseEntity movementUnLove(String publishId) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.调用rpc删除（三个条件）
        Integer loveCount = commentService.removeComment(new ObjectId(publishId), userId, 3);
        // 3.从redis中删除喜欢标记
        stringRedisTemplate.delete(StrUtil.format(ConstantUtil.MOVEMENT_LOVE, userId, publishId));
        // 4.返回喜欢数量
        return ResponseEntity.ok(loveCount);
    }

    // 查询单条动态
    public ResponseEntity findMovementVoById(String publishId) {
        // 1.调用rpc查询动态详情
        Movement movement = movementService.findById(new ObjectId(publishId));
        // 2.封装vo
        // 2-1 先查userInfo
        UserInfo userInfo = userInfoService.findById(movement.getUserId());
        // 2-2 开始封装
        MovementVo vo = new MovementVo();
        vo.setUserInfo(userInfo); //  先
        vo.setMovement(movement); // 后
        // 3.返回vo
        return ResponseEntity.ok(vo);
    }

    // 动态评论分页查询
    public ResponseEntity findMovementCommentVo(String publishId, Integer pageNum, Integer pageSize) {
        // 1.调用rpc分页查询
        PageBeanVo pageBeanVo = commentService.findCommentByPage(new ObjectId(publishId), 2, pageNum, pageSize);
        // 2.封装commentVo
        // 3-1 声明voList
        List<CommentVo> voList = new ArrayList<>();
        // 3-2 获取commentList
        List<Comment> commentList = (List<Comment>) pageBeanVo.getItems();
        // 3-3 遍历commentList
        if (CollectionUtil.isNotEmpty(commentList)) {
            for (Comment comment : commentList) {
                // 先查userInfo
                UserInfo userInfo = userInfoService.findById(comment.getUserId());
                // 封装vo
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
        // 3.将voList设置分页对象
        pageBeanVo.setItems(voList);
        // 4.返回分页对象
        return ResponseEntity.ok(pageBeanVo);
    }

    // 发表评论
    public ResponseEntity movementComment(String publishId, String content) {
        // 1.获取线程内userId
        Long userId = UserHolder.get().getId();
        // 2.查询动态详情
        Movement movement = movementService.findById(new ObjectId(publishId));
        // 3.封装comment
        Comment comment = new Comment();
        comment.setCreated(System.currentTimeMillis()); // 评论时间
        comment.setPublishId(movement.getId());// 动态id
        comment.setCommentType(2);// 动态评论类型
        comment.setUserId(userId); // 登录人id
        comment.setPublishUserId(movement.getUserId());// 动态发布人id
        comment.setContent(content); // 评论 内容
        // 4.调用 rpc保存
        Integer commentCount = commentService.saveComment(comment);
        // 5.返回评论数量
        return ResponseEntity.ok(commentCount);
    }
}
