package com.itheima.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import com.itheima.domain.mongo.Movement;
import com.itheima.domain.mongo.RecommendMovement;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RecommendVideoJob {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 0/1 * * * ?") // 帅哥用1分钟
    public void redisToMongoJob() {
        System.out.println("推荐视频开始....");
        // 1.查redis
        // 1-1 取出所有跟推荐动态相关的key
        Set<String> keySet = stringRedisTemplate.keys("MOVEMENTS_RECOMMEND_*");
        // 1-2 遍历key集合
        if (CollectionUtil.isNotEmpty(keySet)) {
            for (String key : keySet) {
                // 1-3 获取用户id
                String userIdStr = key.replace("MOVEMENTS_RECOMMEND_", "");
                // 1-4 获取推荐给该用户pid
                String pidStr = stringRedisTemplate.opsForValue().get(key);

                // 删除这个key
                stringRedisTemplate.delete(key);
                // 删除此用户之前推荐数据
                mongoTemplate.remove(new Query(Criteria.where("userId").is(Long.valueOf(userIdStr))), RecommendMovement.class);
                // 1-5 遍历pid
                String[] pidArray = pidStr.split(",");
                if (ArrayUtil.isNotEmpty(pidArray)) {
                    for (String pid : pidArray) {
                        // 2.保存mongo
                        // 2-1 根据pid查询动态详情获取动态id
                        ObjectId publishId = mongoTemplate.findOne(new Query(Criteria.where("pid").is(Long.valueOf(pid))), Movement.class).getId();
                        // 2-2 创建推荐实体
                        RecommendMovement recommendMovement = new RecommendMovement();
                        recommendMovement.setCreated(System.currentTimeMillis()); // 时间
                        recommendMovement.setPid(Long.valueOf(pid)); // 动态pid
                        recommendMovement.setPublishId(publishId); // 动态id
                        recommendMovement.setScore(RandomUtil.randomDouble(70, 99)); // 评分
                        recommendMovement.setUserId(Long.valueOf(userIdStr)); // 推荐给谁？
                        // 2-3 保存
                        mongoTemplate.save(recommendMovement);

                    }
                }
            }
        }
        System.out.println("推荐动态结束....");
    }
}