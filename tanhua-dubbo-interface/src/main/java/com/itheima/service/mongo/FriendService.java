package com.itheima.service.mongo;

import com.itheima.vo.PageBeanVo;

public interface FriendService {

    // 添加好友
    void addContacts(Long userId, Long friendId);

    // 查看联系人
    PageBeanVo findContactByPage(Long userId, Integer pageNum, Integer pageSize);
}
