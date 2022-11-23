package com.itheima.listener;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.mongo.MovementScore;
import com.itheima.domain.mongo.Video;
import com.itheima.domain.mongo.VideoScore;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RecommendVideoListener {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RabbitListener(queuesToDeclare = @Queue("tanhua.recommend.video"))
    public void listenMovement(VideoScore videoScore){
        System.out.println("接收视频行为日志："+videoScore);
        mongoTemplate.save(videoScore);
    }
}