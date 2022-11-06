package com.itheima.service.mongo.impl;

import com.itheima.domain.mongo.RecommendUser;
import com.itheima.service.mongo.RecommendUserService;
import com.itheima.vo.PageBeanVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


@DubboService
public class RecommendUserServiceImpl implements RecommendUserService {

    @Autowired
    private MongoTemplate mongoTemplate;


    // 查询今日佳人
    @Override
    public RecommendUser findTodayBest(Long toUserId) {
        // 1.构建条件
        Query query = new Query(
                Criteria.where("toUserId").is(toUserId)
        ).with(Sort.by(Sort.Order.desc("score")))
                .skip(0).limit(1);
        // 2.查询推荐用户
        RecommendUser recommendUser = mongoTemplate.findOne(query, RecommendUser.class);
        // 3.返回
        return recommendUser;
    }

    // 分页查询
    @Override
    public PageBeanVo findRecommendUserByPage(Long toUserId, Integer pageNum, Integer pageSize) {
        // 1.构建条件
        Integer index = (pageNum - 1) * pageSize;
        Query query = new Query(
                Criteria.where("toUserId").is(toUserId)  // 条件
        ).with(Sort.by(Sort.Order.desc("score"))) // 排序
                .skip(index + 1).limit(pageSize); // 分页
        // 2.查询list
        List<RecommendUser> recommendUserList = mongoTemplate.find(query, RecommendUser.class);

        // 3 .返回并封装分页对象
        return new PageBeanVo(pageNum, pageSize, 0L, recommendUserList);
    }

    // 查询二人的缘分值
    @Override
    public RecommendUser personalInfo(Long jiarenId, Long toUserId) {
        // 1.构建条件
        Query query = new Query(
                Criteria.where("userId").is(jiarenId)
                        .and("toUserId").is(toUserId)
        );
        // 2 .查询并返回缘分值
        return mongoTemplate.findOne(query, RecommendUser.class);
    }
}