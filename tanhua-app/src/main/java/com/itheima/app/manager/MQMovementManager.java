package com.itheima.app.manager;

import com.itheima.domain.mongo.Movement;
import com.itheima.domain.mongo.MovementScore;
import com.itheima.service.mongo.MovementService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//负责向mq发送消息
@Component
public class MQMovementManager {

    @DubboReference
    private MovementService movementService;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //针对动态的操作
    public static final Integer MOVEMENT_PUBLISH = 1;// 发动态
    public static final Integer MOVEMENT_BROWSE = 2;// 浏览动态
    public static final Integer MOVEMENT_LIKE = 3;// 点赞
    public static final Integer MOVEMENT_LOVE = 4;// 喜欢
    public static final Integer MOVEMENT_COMMENT = 5;// 评论
    public static final Integer MOVEMENT_DISLIKE = 6;// 取消点赞
    public static final Integer MOVEMENT_DISLOVE = 7;// 取消喜欢

    public void sendMovement(Long userId, ObjectId movementId, Integer type) {
        MovementScore movementScore = new MovementScore();
        movementScore.setUserId(userId); // 操作人id
        // 获取动态pid
        Long pid = movementService.findById(movementId).getPid();
        movementScore.setMovementId(pid); // 动态pid
        movementScore.setDate(System.currentTimeMillis()); // 日志时间

        switch (type) {
            case 1: {
                movementScore.setScore(20d);
                break;
            }
            case 2: {
                movementScore.setScore(1d);
                break;
            }
            case 3: {
                movementScore.setScore(5d);
                break;
            }
            case 4: {
                movementScore.setScore(8d);
                break;
            }
            case 5: {
                movementScore.setScore(10d);
                break;
            }
            case 6: {
                movementScore.setScore(-5d);
                break;
            }
            case 7: {
                movementScore.setScore(-8d);
                break;
            }
        }
        
        // 发送mq消息
        rabbitTemplate.convertAndSend("tanhua.recommend.movement",movementScore );
    }
}