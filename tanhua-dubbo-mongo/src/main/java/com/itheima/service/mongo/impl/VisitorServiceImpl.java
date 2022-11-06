package com.itheima.service.mongo.impl;
import com.itheima.domain.mongo.Visitor;
import com.itheima.service.mongo.VisitorService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.net.CacheRequest;
import java.util.List;

@DubboService
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Visitor> findVisitors(Long userId, Long lastAccessTime) {
        // 1.构建条件
        Query query = new Query(
                Criteria.where("userId").is(userId) // 条件
                        .and("date").gt(lastAccessTime)
        ).with(Sort.by(Sort.Order.desc("date"))) // 排序
                .skip(0).limit(5); // 前五条

        // 2.查询并返回
        return mongoTemplate.find(query, Visitor.class);
    }
}
