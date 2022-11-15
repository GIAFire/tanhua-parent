package com.itheima.service.mongo;

import com.itheima.domain.mongo.FocusUser;

public interface FocusUserService {

    // 实现关注
    void saveFocusUser(FocusUser focusUser);

    // 取消关注
    void removeFocusUser(Long userId, Long focusUserId);
}
