package com.itheima.service.mongo;

import com.itheima.domain.mongo.Visitor;
import com.itheima.vo.VisitorVo;

import java.util.List;

public interface VisitorService {

    // 查询最近访客
    List<Visitor> findVisitors(Long userId, Long lastAccessTime);
}
