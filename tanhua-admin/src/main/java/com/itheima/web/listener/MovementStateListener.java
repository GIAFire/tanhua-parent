package com.itheima.web.listener;

import com.itheima.autoconfig.lvwang.AliyunGreenTemplate;
import com.itheima.domain.mongo.Movement;
import com.itheima.service.mongo.MovementService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MovementStateListener {

    @DubboReference
    private MovementService movementService;

    @Autowired
    private AliyunGreenTemplate  aliyunGreenTemplate;

    @RabbitListener(queuesToDeclare = @Queue("tanhua.movement.state"))
    public void listenMovementState(String publishId){
        System.out.println("动态审核开始。。。。");
        // 查询动态详情
        Movement movement = movementService.findById(new ObjectId(publishId));

        // 审核文本&文本
        Boolean checkText = aliyunGreenTemplate.checkText(movement.getTextContent());
        Boolean checkImage = aliyunGreenTemplate.checkImage(movement.getMedias());
        if (checkImage && checkText) {
            movement.setState(1); // 审核通过
        }else{
            movement.setState(2);// 审核驳回，待人工审核
        }

        // 更新mongo
        movementService.updateMovement(movement);
        System.out.println("动态审核结束。。。。");
    }
}
