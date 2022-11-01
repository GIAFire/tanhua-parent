package com.itheima.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

// 动态发布表
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movement")
public class Movement implements Serializable {
    private ObjectId id; //主键id
    private Long userId; // 用户id
    private Long pid; //Long类型，用于推荐系统的模型
    private String textContent; //文字
    private List<String> medias; //媒体数据，图片或小视频 url
    private String longitude; //经度
    private String latitude; //纬度
    private String location; //位置名称
    private Integer state = 0; // 状态： 0 未审核、 1 已审核 2 已驳回
    private Long created; //发布时间

    private Integer seeType; // 谁可以看，1-公开，2-私密，3-部分可见，4-不给谁看
    private Integer likeCount = 0; //点赞数
    private Integer commentCount = 0; //评论数
    private Integer loveCount = 0; //喜欢数
}