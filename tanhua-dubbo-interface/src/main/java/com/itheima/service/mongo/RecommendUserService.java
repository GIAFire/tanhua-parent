package com.itheima.service.mongo;

import com.itheima.domain.mongo.RecommendUser;
import com.itheima.vo.PageBeanVo;

public interface RecommendUserService {

    // 查询今日佳人
    RecommendUser findTodayBest(Long userId);

    // 分页查询
    PageBeanVo findRecommendUserByPage(Long toUserId, Integer pageNum, Integer pageSize);

    // 查询二人的缘分值
    RecommendUser personalInfo(Long jiarenId,Long toUserId);
}
