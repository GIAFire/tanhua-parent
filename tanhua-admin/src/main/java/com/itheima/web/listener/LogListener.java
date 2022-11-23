package com.itheima.web.listener;

import com.itheima.domain.db.Log;
import com.itheima.service.db.LogService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class LogListener {
    @DubboReference
    private LogService logService;

    @RabbitListener(queuesToDeclare = @Queue("tanhua.log"))
    public void listenLog(Log log){
        System.out.println("接受日志消息:"+log);
        logService.save(log);
    }
}
