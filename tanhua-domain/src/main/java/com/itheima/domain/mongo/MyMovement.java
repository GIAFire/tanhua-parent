package com.itheima.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

//个人动态表，用于存储自己发布的数据
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyMovement implements java.io.Serializable {

    private ObjectId id; //主键id
    private ObjectId publishId; //发布id
    private Long created; //发布时间

}