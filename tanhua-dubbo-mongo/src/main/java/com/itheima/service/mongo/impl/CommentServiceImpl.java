package com.itheima.service.mongo.impl;

import com.itheima.domain.mongo.Comment;
import com.itheima.domain.mongo.Movement;
import com.itheima.service.mongo.CommentService;
import com.itheima.vo.PageBeanVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@DubboService
public class CommentServiceImpl implements CommentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // 保存
    @Override
    public Integer saveComment(Comment comment) {
        // 1.直接保存
        mongoTemplate.save(comment);
        // 2.更新动态详情数量，根据commentType判断
        switch (comment.getCommentType()) {
            case 1: { // 动态点赞
                // i 先查动态
                Movement movement = mongoTemplate.findById(comment.getPublishId(), Movement.class);
                // ii 数量+并更新
                movement.setLikeCount(movement.getLikeCount() + 1);
                mongoTemplate.save(movement);
                // 3.返回数量
                return movement.getLikeCount();
            }
            case 2: {// 动态评论
                // i 先查动态
                Movement movement = mongoTemplate.findById(comment.getPublishId(), Movement.class);
                // ii 数量+并更新
                movement.setCommentCount(movement.getCommentCount() + 1);
                mongoTemplate.save(movement);
                // 3.返回数量
                return movement.getCommentCount();
            }
            case 3: {// 动态喜欢
                // i 先查动态
                Movement movement = mongoTemplate.findById(comment.getPublishId(), Movement.class);
                // ii 数量+并更新
                movement.setLoveCount(movement.getLoveCount() + 1);
                mongoTemplate.save(movement);
                // 3.返回数量
                return movement.getLoveCount();
            }
            case 4: {
            }
            case 5: {
            }
            case 6: {
            }
        }

        return 0;
    }

    // 删除
    @Override
    public Integer removeComment(ObjectId publishId, Long userId, Integer commentType) {
        // 1.根据三个条件删除
        Query query = new Query(
                Criteria.where("publishId").is(publishId)
                        .and("userId").is(userId)
                        .and("commentType").is(commentType)
        );
        mongoTemplate.remove(query, Comment.class);

        // 2.更新动态详情数量
        switch (commentType) {
            case 1: { // 动态点赞
                // i 先查动态详情
                Movement movement = mongoTemplate.findById(publishId, Movement.class);
                // ii 数量-并更新
                movement.setLikeCount(movement.getLikeCount() - 1);
                mongoTemplate.save(movement);
                // 3.返回数量
                return movement.getLikeCount();
            }
            case 2: { // 动态评论
                // i 先查动态详情
                Movement movement = mongoTemplate.findById(publishId, Movement.class);
                // ii 数量-并更新
                movement.setCommentCount(movement.getCommentCount() - 1);
                mongoTemplate.save(movement);
                // 3.返回数量
                return movement.getCommentCount();
            }
            case 3: {// 动态喜欢
                // i 先查动态详情
                Movement movement = mongoTemplate.findById(publishId, Movement.class);
                // ii 数量-并更新
                movement.setLoveCount(movement.getLoveCount() - 1);
                mongoTemplate.save(movement);
                // 3.返回数量
                return movement.getLoveCount();
            }
            case 4: {
            }
            case 5: {
            }
            case 6: {
            }
        }
        return 0;
    }

    // 评论分页查询
    @Override
    public PageBeanVo findCommentByPage(ObjectId publishId, Integer commentType, Integer pageNum, Integer pageSize) {
        // 1.构建条件
        Query query = new Query(
                Criteria.where("publishId").is(publishId) // 业务条件
                        .and("commentType").is(commentType)
        ).with(Sort.by(Sort.Order.desc("created"))) // 时间降序
                .skip((pageNum - 1) * pageSize).limit(pageSize); // 分页查询
        // 2.查询list
        List<Comment> commentList = mongoTemplate.find(query, Comment.class);
        // 3.查询总记录数
        long counts = mongoTemplate.count(new Query(Criteria.where("publishId").is(publishId).and("commentType").is(commentType)), Comment.class);
        // 4.返回并封装分页对象
        return new PageBeanVo(pageNum, pageSize, counts, commentList);
    }
}
