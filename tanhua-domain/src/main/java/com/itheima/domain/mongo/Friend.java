package com.itheima.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

// 好友表
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "friend")
public class Friend implements Serializable {
    private ObjectId id;
    private Long created; //时间
    private Long userId; //用户id
    private Long friendId; //好友id
}