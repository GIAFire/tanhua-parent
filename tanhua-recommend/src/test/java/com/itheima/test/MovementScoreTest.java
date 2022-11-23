package com.itheima.test;

import cn.hutool.core.util.RandomUtil;
import com.itheima.domain.mongo.Movement;
import com.itheima.domain.mongo.MovementScore;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovementScoreTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    //模拟1-4号用户对动态的各种操作
    @Test
    public void testPublishScoreData() {

        //查询所有动态
        List<Movement> list = mongoTemplate.find(new Query(), Movement.class);

        //每次模拟是1-4随机一个用户对随机一条动态的随机得分
        for (int i = 0; i < 100; i++) {
            MovementScore score = new MovementScore();
            score.setId(ObjectId.get());//主键
            score.setDate(System.currentTimeMillis());//时间
            Movement movement = list.get(RandomUtil.randomInt(0, list.size()));//随机找到一条动态
            score.setMovementId(movement.getPid());//获取动态id
            score.setScore(Double.valueOf(new Random().nextInt(10)));
            score.setUserId(RandomUtil.randomLong(1, 5));//随机选择4个用户
            mongoTemplate.save(score);
        }
    }
}