package com.itheima.service.mongo;

import com.itheima.domain.mongo.Video;
import com.itheima.vo.PageBeanVo;

public interface VideoService {

    // 推荐视频查询
    PageBeanVo findRecommendVideoByPage(Long userId,Integer pageNum, Integer pageSize);

    // 发布视频
    void publishVideo(Video video);
}
