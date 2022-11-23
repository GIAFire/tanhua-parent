package com.itheima.app.manager;

import com.itheima.domain.mongo.MovementScore;
import com.itheima.domain.mongo.VideoScore;
import com.itheima.service.mongo.MovementService;
import com.itheima.service.mongo.VideoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//负责向mq发送消息
@Component
public class MQVideoManager {

    @DubboReference
    private VideoService videoService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //针对动态的操作
    public static final Integer VIDEO_PUBLISH = 1;// 发视频
    public static final Integer VIDEO_BROWSE = 2;// 浏览视频
    public static final Integer VIDEO_LIKE = 3;// 点赞
    public static final Integer VIDEO_LOVE = 4;// 喜欢
    public static final Integer VIDEO_COMMENT = 5;// 评论
    public static final Integer VIDEO_DISLIKE = 6;// 取消点赞
    public static final Integer VIDEO_DISLOVE = 7;// 取消喜欢

    public void sendMovement(Long userId, ObjectId videoId, Integer type) {
        VideoScore videoScore = new VideoScore();
        videoScore.setUserId(userId); // 操作人id
        // 获取动态pid
        Long vid = videoService.findById(videoId).getVid();
        videoScore.setVideoId(vid); // 动态pid
        videoScore.setDate(System.currentTimeMillis()); // 日志时间

        switch (type) {
            case 1: {
                videoScore.setScore(20d);
                break;
            }
            case 2: {
                videoScore.setScore(1d);
                break;
            }
            case 3: {
                videoScore.setScore(5d);
                break;
            }
            case 4: {
                videoScore.setScore(8d);
                break;
            }
            case 5: {
                videoScore.setScore(10d);
                break;
            }
            case 6: {
                videoScore.setScore(-5d);
                break;
            }
            case 7: {
                videoScore.setScore(-8d);
                break;
            }
        }

        // 发送mq消息
        rabbitTemplate.convertAndSend("tanhua.recommend.video",videoScore );
    }
}