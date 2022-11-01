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
@Document(collection = "recommend_user")//推荐过来的用户
public class RecommendUser implements Serializable {
    private ObjectId id; //主键id
    private Long created; //日期
    @Indexed
    private Long userId; //大数据推荐的用户id
    private Long toUserId; //登录用户id
    @Indexed
    private Double score =0d; //推荐得分
}