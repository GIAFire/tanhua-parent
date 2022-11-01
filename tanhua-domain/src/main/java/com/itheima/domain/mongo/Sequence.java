package com.itheima.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sequence")
public class Sequence { // pid字段自增表

    private ObjectId id;
    private long seqId; // 自增id
    private String collName; // 自增id所属表
}