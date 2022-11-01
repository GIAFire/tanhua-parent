package com.itheima.service.mongo;

import com.itheima.domain.mongo.Movement;
import com.itheima.vo.PageBeanVo;
import org.bson.types.ObjectId;

public interface MovementService {

    // 发布动态
    void publishMovement(Movement movement);

    // 查询我的动态
    PageBeanVo findMyMovementByPage(Long userId,Integer pageNum, Integer pageSize);

    // 查询好友动态
    PageBeanVo findFriendMovementByPage(Long userId, Integer pageNum, Integer pageSize);

    // 查询推荐动态
    PageBeanVo findRecommendMovementByPage(Long userId, Integer pageNum, Integer pageSize);

    // 根据id查询
    Movement findById(ObjectId id);
}
