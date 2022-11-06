package com.itheima.test;

import cn.hutool.core.util.RandomUtil;
import com.itheima.domain.mongo.Visitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VisitorsTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test01() throws Exception {
        for (int i = 0; i < 5; i++) {
            Visitor visitor = new Visitor();
            visitor.setFrom("首页"); // 访问来源
            visitor.setUserId(99L);//用户id
            visitor.setVisitorUserId(RandomUtil.randomLong(1, 20)); // 访客
            visitor.setScore(RandomUtil.randomDouble(60, 99)); // 缘分值
            visitor.setDate(System.currentTimeMillis()); // 访问时间
            mongoTemplate.save(visitor);
        }
        System.out.println("ok");
    }
}
