package com.itheima.service.mongo.impl;

import com.itheima.domain.mongo.FocusUser;
import com.itheima.service.mongo.FocusUserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.net.CacheRequest;

@DubboService
public class FocusUserServiceImpl implements FocusUserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveFocusUser(FocusUser focusUser) {
        mongoTemplate.save(focusUser);
    }

    @Override
    public void removeFocusUser(Long userId, Long focusUserId) {
        // 1.构建条件
        Query query = new Query(
                Criteria.where("userId").is(userId)
                        .and("focusUserId").is(focusUserId)
        );
        // 2.删除记录
        mongoTemplate.remove(query, FocusUser.class);
    }
}
