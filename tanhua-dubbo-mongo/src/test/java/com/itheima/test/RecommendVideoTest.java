package com.itheima.test;

import cn.hutool.core.util.RandomUtil;
import com.itheima.domain.mongo.Movement;
import com.itheima.domain.mongo.RecommendMovement;
import com.itheima.domain.mongo.RecommendVideo;
import com.itheima.domain.mongo.Video;
import com.itheima.util.ConstantUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendVideoTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    // 添加推荐动态测试数据
    @Test
    public void test01() throws Exception {
        Query query = new Query().skip(0).limit(10);
        List<Video> videoList = mongoTemplate.find(query, Video.class);

        for (int i = 0; i < 10; i++) {
            RecommendVideo recommendVideo = new RecommendVideo();
            recommendVideo.setDate(System.currentTimeMillis());
            recommendVideo.setUserId(99L);//推荐用户id
            recommendVideo.setVid(videoList.get(i).getVid());
            recommendVideo.setScore(RandomUtil.randomDouble(70, 99));
            recommendVideo.setVideoId(videoList.get(i).getId());
            mongoTemplate.save(recommendVideo);
        }
    }
}