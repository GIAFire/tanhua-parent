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
@Document(collection = "video")
public class Video implements Serializable {

    private ObjectId id;  //主键id
    private Long vid;     //大数据使用的id
    private Long created; //创建时间


    private Long userId; // 小视频用户id
    private String text; //文字
    private String picUrl; //视频封面文件，URL
    private String videoUrl; //视频文件，URL


    private Integer likeCount = 0; //点赞数
    private Integer commentCount = 0; //评论数
    private Integer loveCount = 0; //喜欢数
}