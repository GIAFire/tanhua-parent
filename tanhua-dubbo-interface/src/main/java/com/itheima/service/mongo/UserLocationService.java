package com.itheima.service.mongo;

import java.util.List;

public interface UserLocationService {

    // 上报地理位置
    void saveLocation(Double longitude, Double latitude, String addrStr, Long userId);

    // 搜附近
    List<Long> searchNearUserId(Long userId, Long distance);
}
