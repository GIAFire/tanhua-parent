package com.itheima.service.mongo;

import com.itheima.domain.mongo.Comment;
import com.itheima.vo.PageBeanVo;
import org.bson.types.ObjectId;

public interface CommentService {
    // 保存
    Integer saveComment(Comment comment);

    // 删除comment
    Integer removeComment(ObjectId publishId, Long userId, Integer commentType);

    // 评论分页查询
    PageBeanVo findCommentByPage(ObjectId publishId, Integer commentType, Integer pageNum, Integer pageSize);

    // 查看对你评论人的信息
    PageBeanVo findUserCommentByPage(Long publishUserId,Integer commentType,Integer pageNum,Integer pageSize);
}
