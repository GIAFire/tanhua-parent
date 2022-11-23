package com.itheima.service.mongo;

import com.itheima.domain.mongo.Movement;
import com.itheima.domain.mongo.Video;
import com.itheima.vo.PageBeanVo;
import org.bson.types.ObjectId;

public interface VideoService {

    // 推荐视频查询
    PageBeanVo findRecommendVideoByPage(Long userId,Integer pageNum, Integer pageSize);

    // 发布视频
    void publishVideo(Video video);

    // 根据id查询
    Video findById(ObjectId id);
}
