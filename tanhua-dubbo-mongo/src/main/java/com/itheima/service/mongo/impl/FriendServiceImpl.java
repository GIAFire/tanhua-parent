package com.itheima.service.mongo.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.itheima.domain.mongo.Friend;
import com.itheima.domain.mongo.FriendMovement;
import com.itheima.domain.mongo.MyMovement;
import com.itheima.service.mongo.FriendService;
import com.itheima.util.ConstantUtil;
import com.itheima.vo.PageBeanVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@DubboService
public class FriendServiceImpl implements FriendService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addContacts(Long userId, Long friendId) {
        // 1.你加他
        // 1-1 判断你是否曾经有他的好友
        boolean exists1 = mongoTemplate.exists(new Query(Criteria.where("userId").is(userId).and("friendId").is(friendId)), Friend.class);
        if (exists1 == false) {
            // 1-2 完成好友添加
            Friend friend1 = new Friend();
            friend1.setCreated(System.currentTimeMillis());
            friend1.setUserId(userId); // 你的id
            friend1.setFriendId(friendId); // 好友id
            mongoTemplate.save(friend1);

            // 1-3 查询他（好友）的个人动态  movement_mine_12
            List<MyMovement> friendMyMovementList = mongoTemplate.findAll(MyMovement.class, ConstantUtil.MOVEMENT_MINE + friendId);
            // 1-4 将他的动态信息保存到你的好友动态中  movement_friend_99
            if (CollectionUtil.isNotEmpty(friendMyMovementList)) {
                for (MyMovement friendMyMovement : friendMyMovementList) {
                    // 封装我的好友动态
                    FriendMovement myFriendMovement = new FriendMovement();
                    myFriendMovement.setUserId(friendId);// 好友id
                    myFriendMovement.setPublishId(friendMyMovement.getPublishId());// 好友动态 id
                    myFriendMovement.setCreated(friendMyMovement.getCreated());// 好友的动态发布时间
                    // 保存我的好友动态
                    mongoTemplate.save(myFriendMovement,ConstantUtil.MOVEMENT_FRIEND+userId);
                }
            }
        }

        // 2.他加你
        // 2-1 判断你是否曾经有他的好友
        boolean exists2 = mongoTemplate.exists(new Query(Criteria.where("userId").is(friendId).and("friendId").is(userId)), Friend.class);
        if (exists2 == false) {
            // 2-2 完成好友添加
            Friend friend2 = new Friend();
            friend2.setCreated(System.currentTimeMillis());
            friend2.setUserId(friendId); // 好友id
            friend2.setFriendId(userId); // 你的id
            mongoTemplate.save(friend2);
        }
        // 2-3 查询你的的个人动态  movement_mine_99
        List<MyMovement> myMovementList = mongoTemplate.findAll(MyMovement.class, ConstantUtil.MOVEMENT_MINE + userId);
        // 2-4 将你的动态信息保存到他的好友动态中  movement_friend_12
        if (CollectionUtil.isNotEmpty(myMovementList)) {
            for (MyMovement myMovement : myMovementList) {
                // 封装他的好友动态表
                FriendMovement friendMovement = new FriendMovement();
                friendMovement.setUserId(userId); // 他的好友id就是你 （梁朝伟）
                friendMovement.setPublishId(myMovement.getPublishId()); // 你的动态id
                friendMovement.setCreated(myMovement.getCreated()); // 你得动态发布时间
                // 保存他的好友动态
                mongoTemplate.save(friendMovement,ConstantUtil.MOVEMENT_FRIEND+friendId);

            }
        }
    }

    // 查看联系人
    @Override
    public PageBeanVo findContactByPage(Long userId, Integer pageNum, Integer pageSize) {
        // 1.构建条件
        Query query = new Query(
                Criteria.where("userId").is(userId)
        ).skip((pageNum - 1) * pageSize).limit(pageSize);
        // 2.查询list
        List<Friend> friendList = mongoTemplate.find(query, Friend.class);
        // 3.查询count
        long counts = mongoTemplate.count(new Query(Criteria.where("userId").is(userId)), Friend.class);
        // 4.封装并返回结果
        return new PageBeanVo(pageNum, pageSize, counts, friendList);
    }
}
