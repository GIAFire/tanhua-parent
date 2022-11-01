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
@Document(collection = "focus_user")
public class FocusUser implements Serializable {

    private ObjectId id; //主键id
    private Long created; //关注时间
    
    private Long userId; //用户id
    private Long focusUserId; //小视频用户id

}