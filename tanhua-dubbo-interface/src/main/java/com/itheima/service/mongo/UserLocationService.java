package com.itheima.service.mongo;

public interface UserLocationService {

    // 上报地理位置
    void saveLocation(Double longitude, Double latitude, String addrStr, Long userId);
}
