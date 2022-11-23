package com.itheima.listener;

import com.itheima.domain.mongo.MovementScore;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MovementScoreListener {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RabbitListener(queuesToDeclare = @Queue("tanhua.recommend.movement"))
    public void listenMovement(MovementScore movementScore){
        System.out.println("接收动态行为日志："+movementScore);
        mongoTemplate.save(movementScore);
    }
}
