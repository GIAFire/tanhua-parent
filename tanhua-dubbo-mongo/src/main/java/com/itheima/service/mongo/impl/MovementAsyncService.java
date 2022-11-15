package com.itheima.service.mongo.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.itheima.domain.mongo.Friend;
import com.itheima.domain.mongo.FriendMovement;
import com.itheima.domain.mongo.Movement;
import com.itheima.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovementAsyncService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // 查询好友，保存好友动态
    @Async
    public void saveMovementFriend(Movement movement) {
        // 3.查询好友
        // 3-1 构建条件
        Query query = new Query(Criteria.where("userId").is(movement.getUserId()));
        // 3-2 查询
        List<Friend> friendList = mongoTemplate.find(query, Friend.class);
        // 3-3 遍历好友
        if (CollectionUtil.isNotEmpty(friendList)) {
            for (Friend friend : friendList) {
                // 3-4 获取好友id
                Long friendId = friend.getFriendId();
                // 4. 保存到好友动态表
                // 4-1 封装实体
                FriendMovement friendMovement = new FriendMovement();
                friendMovement.setUserId(movement.getUserId()); // 动态发布人id
                friendMovement.setPublishId(movement.getId()); // 动态id
                friendMovement.setCreated(movement.getCreated());// 发布时间
                // 4-2 mongo保存
                mongoTemplate.save(friendMovement, ConstantUtil.MOVEMENT_FRIEND + friendId); // movement_friend_2
            }
        }
    }
}
