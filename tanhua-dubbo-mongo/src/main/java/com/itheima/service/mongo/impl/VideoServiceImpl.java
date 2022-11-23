package com.itheima.service.mongo.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.itheima.domain.mongo.Movement;
import com.itheima.domain.mongo.RecommendVideo;
import com.itheima.domain.mongo.Video;
import com.itheima.service.mongo.VideoService;
import com.itheima.util.ConstantUtil;
import com.itheima.vo.PageBeanVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.List;

@DubboService
public class VideoServiceImpl implements VideoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PageBeanVo findRecommendVideoByPage(Long userId, Integer pageNum, Integer pageSize) {
        // 1.构建条件
        Query query = new Query(
                Criteria.where("userId").is(userId)
        ).with(Sort.by(Sort.Order.desc("date")))
                .skip((pageNum - 1) * pageSize).limit(pageSize);
        // 2.查询视频推荐列表
        List<RecommendVideo> recommendVideoList = mongoTemplate.find(query, RecommendVideo.class);
        // 3.根据推荐查询视频详情
        List<Video> videoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(recommendVideoList)) {
            for (RecommendVideo recommendVideo : recommendVideoList) {
                // 根据视频id查询视频详情
                Video video = mongoTemplate.findById(recommendVideo.getVideoId(), Video.class);
                // 添加到集合
                videoList.add(video);
            }
        }
        // 4.返回并封装分页对象
        return new PageBeanVo(pageNum, pageSize, 0L, videoList);
    }

    @Autowired
    private RedisIdService redisIdService;

    @Override
    public void publishVideo(Video video) {
        // 1.设置vid
        Long vid = redisIdService.getNextId(ConstantUtil.VIDEO_ID);
        video.setVid(vid);
        // 2.保存
        mongoTemplate.save(video);
        // 3.将视频推荐给自己
        RecommendVideo recommendVideo = new RecommendVideo();
        recommendVideo.setDate(video.getCreated()); // 推荐视频
        recommendVideo.setVid(video.getVid());// 大数据视频推荐id
        recommendVideo.setUserId(video.getUserId());// 自己id
        recommendVideo.setVideoId(video.getId());// 视频id
        recommendVideo.setScore(RandomUtil.randomDouble(80, 99));// 推荐得分
        mongoTemplate.save(recommendVideo);

    }

    @Override
    public Video findById(ObjectId id) {
        return mongoTemplate.findById(id, Video.class);
    }
}
