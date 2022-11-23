package com.itheima.test;

import cn.hutool.core.util.RandomUtil;
import com.itheima.domain.mongo.Movement;
import com.itheima.domain.mongo.MovementScore;
import com.itheima.domain.mongo.Video;
import com.itheima.domain.mongo.VideoScore;
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
public class VideoScoreTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    //模拟1-4号用户对动态的各种操作
    @Test
    public void testPublishScoreData() {

        //查询所有动态
        List<Video> list = mongoTemplate.find(new Query(), Video.class);

        //每次模拟是1-4随机一个用户对随机一条动态的随机得分
        for (int i = 0; i < 100; i++) {
            VideoScore videoScore = new VideoScore();
            videoScore.setId(ObjectId.get());//主键
            videoScore.setDate(System.currentTimeMillis());//时间
            Video video = list.get(RandomUtil.randomInt(0, list.size()));//随机找到一条动态
            videoScore.setVideoId(video.getVid());//获取动态id
            videoScore.setScore(Double.valueOf(new Random().nextInt(10)));
            videoScore.setUserId(RandomUtil.randomLong(1, 5));//随机选择4个用户
            mongoTemplate.save(videoScore);
        }
    }
}