package com.itheima.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

// 好友动态表
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendMovement implements Serializable {
    private ObjectId id;
    private Long userId; // 好友id
    private ObjectId publishId; //发布id
    private Long created; //发布的时间

}