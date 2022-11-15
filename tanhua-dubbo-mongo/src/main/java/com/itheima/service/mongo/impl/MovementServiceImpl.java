package com.itheima.service.mongo.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.itheima.domain.mongo.*;
import com.itheima.service.mongo.MovementService;
import com.itheima.util.ConstantUtil;
import com.itheima.vo.PageBeanVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@DubboService
public class MovementServiceImpl implements MovementService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisIdService redisIdService;
    @Autowired
    private MovementAsyncService movementAsyncService;

    @Override
    public void publishMovement(Movement movement) {
        // 0.redis主键自增
        Long pid = redisIdService.getNextId(ConstantUtil.MOVEMENT_ID);
        movement.setPid(pid);
        // 1.保存动态详情
        mongoTemplate.save(movement);
        // 2.保存个人动态
        // 2-1 封装实体
        MyMovement myMovement = new MyMovement();
        myMovement.setPublishId(movement.getId()); // 动态id
        myMovement.setCreated(movement.getCreated());// 发布时间
        // 2-2 mongo保存
        mongoTemplate.save(myMovement, ConstantUtil.MOVEMENT_MINE + movement.getUserId()); // movement_mine_1

        // 3.调用多线程技术（异步）
        movementAsyncService.saveMovementFriend(movement);

    }

    // 查询我的动态
    @Override
    public PageBeanVo findMyMovementByPage(Long userId, Integer pageNum, Integer pageSize) {
        // 1.构建查询条件
        Integer index = (pageNum-1)*pageSize;
        Query query = new Query()
                .with(Sort.by(Sort.Order.desc("created")))
                .skip(index).limit(pageSize);
        // 2.查询个人动态 movement_mine_1
        List<MyMovement> myMovementList = mongoTemplate.find(query, MyMovement.class, ConstantUtil.MOVEMENT_MINE + userId);
        // 3.查询动态详情
        List<Movement> movementList = new ArrayList<>();
        for (MyMovement myMovement : myMovementList) {
            // 根据动态id查询
            Movement movement = mongoTemplate.findById(myMovement.getPublishId(), Movement.class);
            // 审核通过
            if (movement.getState()==1) {
                // 添加list集合
                movementList.add(movement);
            }
        }
        // 4.查询总记录数 （不能使用上面的query，因为加了分页）
        long counts = mongoTemplate.count(new Query(), ConstantUtil.MOVEMENT_MINE + userId);
        // 5.封装并返回分页对象
        return new PageBeanVo(pageNum, pageSize, counts, movementList);
    }

    // 查询好友动态
    @Override
    public PageBeanVo findFriendMovementByPage(Long userId, Integer pageNum, Integer pageSize) {
        // 1.构建查询条件
        Integer index = (pageNum - 1) * pageSize;
        Query query = new Query()
                .with(Sort.by(Sort.Order.desc("created"))) // 排序
                .skip(index).limit(pageSize);// 分页
        // 2.查询好友动态  movement_friend_1
        List<FriendMovement> friendMovementList = mongoTemplate.find(query, FriendMovement.class, ConstantUtil.MOVEMENT_FRIEND + userId);
        // 3.封装动态详情
        // 3-1声明movementList
        List<Movement> movementList =  new ArrayList<>();
        // 3-2遍历好友动态
        if (CollectionUtil.isNotEmpty(friendMovementList)) {
            for (FriendMovement friendMovement : friendMovementList) {
                // 查询动态详情
                Movement movement = mongoTemplate.findById(friendMovement.getPublishId(), Movement.class);
                // 判断状态
                if (movement.getState()==1) {
                    // 添加到集合
                    movementList.add(movement);
                }
            }
        }
        // 4.查询总记录数（略）
        // 5.封装并返回分页对象
        return new PageBeanVo(pageNum, pageSize, 0L, movementList);
    }

    // 查询推荐动态
    @Override
    public PageBeanVo findRecommendMovementByPage(Long userId, Integer pageNum, Integer pageSize) {
        // 1.构建查询条件
        Integer index = (pageNum - 1) * pageSize;
        Query query = new Query(
                Criteria.where("userId").is(userId)
        ).with(Sort.by(Sort.Order.desc("score"))).with(Sort.by(Sort.Order.desc("created")))
                .skip(index).limit(pageSize);
        // 2.查询推荐动态
        List<RecommendMovement> recommendMovementList = mongoTemplate.find(query, RecommendMovement.class);
        // 3.遍历查询动态详情
        List<Movement> movementList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(recommendMovementList)) {
            for (RecommendMovement recommendMovement : recommendMovementList) {
                // 查询动态详情
                Movement movement = mongoTemplate.findById(recommendMovement.getPublishId(), Movement.class);
                // 判断状态
                if (movement.getState() == 1) {
                    movementList.add(movement);
                }
            }
        }
        // 4.封装并返回分页对象
        return new PageBeanVo(pageNum, pageSize, 0L, movementList);
    }

    @Override
    public Movement findById(ObjectId id) {
        return mongoTemplate.findById(id, Movement.class);
    }
}
