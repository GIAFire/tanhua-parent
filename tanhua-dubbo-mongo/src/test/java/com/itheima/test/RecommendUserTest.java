package com.itheima.test;

import cn.hutool.core.util.RandomUtil;
import com.itheima.domain.mongo.RecommendUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendUserTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test01() throws Exception {
        for (int i = 1; i <= 15; i++) {
            RecommendUser recommendUser = new RecommendUser();
            recommendUser.setScore(RandomUtil.randomDouble(60, 99));
            recommendUser.setCreated(System.currentTimeMillis());
            recommendUser.setUserId(Long.valueOf(i + ""));
            recommendUser.setToUserId(99L);
            mongoTemplate.save(recommendUser);
        }
    }
}