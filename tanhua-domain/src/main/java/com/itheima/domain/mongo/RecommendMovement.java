package com.itheima.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "recommend_movement")
//推荐给我的动态
public class RecommendMovement implements Serializable {

    private ObjectId id; //主键id
    private Long created; //日期
    @Indexed
    private Long userId; //推荐的用户id
    private Long pid;
    private ObjectId publishId; //主键id
    @Indexed
    private Double score = 0d; //推荐得分
}