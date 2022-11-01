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
@Document(collection = "recommend_video")//推荐过来的视频
public class RecommendVideo implements Serializable {
    private ObjectId id; //主键id
    private Long date; //时间
    private Long vid;//大数据推荐使用
    @Indexed
    private Long userId; //推荐给的用户id
    private ObjectId videoId;//视频id
    @Indexed
    private Double score = 0d; //推荐得分
}