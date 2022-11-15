package com.itheima.service.mongo.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.itheima.domain.mongo.UserLocation;
import com.itheima.service.mongo.UserLocationService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;


@DubboService
public class UserLocationServiceImpl implements UserLocationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveLocation(Double longitude, Double latitude, String addrStr, Long userId) {
        // 1.根据用户id查询地理位置
        Query query = new Query(
                Criteria.where("userId").is(userId)
        );
        UserLocation userLocation = mongoTemplate.findOne(query, UserLocation.class);
        // 2.判断
        if (userLocation == null) { // 之前没有地址记录，新增
            userLocation = new UserLocation();
            userLocation.setUserId(userId);
            userLocation.setLocation(new GeoJsonPoint(longitude, latitude));
            userLocation.setAddress(addrStr);
            userLocation.setCreated(System.currentTimeMillis());
            userLocation.setUpdated(System.currentTimeMillis());
            // 保存
            mongoTemplate.save(userLocation);
        } else { // 更新
            userLocation.setLocation(new GeoJsonPoint(longitude, latitude)); // 新坐标
            userLocation.setAddress(addrStr); // 新位置描述
            userLocation.setLastUpdated(userLocation.getUpdated());// 上次更新时间
            userLocation.setUpdated(System.currentTimeMillis());// 本次更新时间
            // 更新
            mongoTemplate.save(userLocation);
        }
    }

    // 搜附近
    @Override
    public List<Long> searchNearUserId(Long userId, Long distance) {
        // 1.找圆心
        Query queryUser = new Query(Criteria.where("userId").is(userId));
        GeoJsonPoint location = mongoTemplate.findOne(queryUser, UserLocation.class).getLocation();
        // 2.指定半径
        Distance dis = new Distance(distance / 1000, Metrics.KILOMETERS);// 公里
        // 3.画圆
        Circle circle = new Circle(location, dis);
        // 4.条件搜索
        Query query = new Query(Criteria.where("location").withinSphere(circle));
        List<UserLocation> userLocationList = mongoTemplate.find(query, UserLocation.class);
        // 5.返回用户id
        // 5-1 声明id集合
        List<Long> nearUserIdList = new ArrayList<>();
        // 5-2遍历userLocationList
        if (CollectionUtil.isNotEmpty(userLocationList)) {
            for (UserLocation userLocation : userLocationList) {
                // 取出id添加到id集合
                nearUserIdList.add(userLocation.getUserId());
            }
        }
        // 5-3返回结果
        return nearUserIdList;
    }

}