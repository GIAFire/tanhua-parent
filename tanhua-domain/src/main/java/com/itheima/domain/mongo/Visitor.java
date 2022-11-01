package com.itheima.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "visitor")
//历史访问用户
public class Visitor implements Serializable {

    private ObjectId id;
    private Long date; //来访时间

    private Long userId; //我的id
    private Long visitorUserId; //来访用户id
    private Double score = 0D; //来访用户的推荐得分
    private String from; //来源，如首页、圈子等

}