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
@Document(collection = "comment")
//（动态、视频、评论）的点赞、评论、喜欢表
public class Comment implements Serializable {

    private ObjectId id; //主键id
    private Long created; //发表时间

    private ObjectId publishId;    //动态id
    private Integer commentType;   //操作类型： 1-对动态点赞，2-对动态评论，3-对动态喜欢  4-对视频点赞   5-对视频评论    6-对评论点赞
    private Long userId;           //操作人
    private Long publishUserId;    //被操作对象所属人
    private String content;        //评论内容


    private Boolean isParent = false; //是否为父节点，默认是否
    private ObjectId parentId;     // 父节点id
    private Integer likeCount = 0; //评论点赞数

	//动态选择更新的字段
    public String getCol() {
        return this.commentType == 1 ? "likeCount" : commentType==2? "commentCount"
                : "loveCount";
    }
}