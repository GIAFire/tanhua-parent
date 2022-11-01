package com.itheima.vo;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.domain.db.UserInfo;
import com.itheima.domain.mongo.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVo implements Serializable {

    private String id;
    private Long userId; // 小视频用户id
    private String avatar; //小视频用户头像
    private String nickname; //小视频用户昵称
    private String cover; //封面
    private String videoUrl; //视频URL
    private String signature; //签名
    private Integer likeCount; //点赞数量
    private Integer hasLiked=0; //是否已赞（1是，0否）
    private Integer hasFocus=0; //是是否关注 （1是，0否）
    private Integer commentCount; //评论数量


    // 设置用户信息
    public void setUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            BeanUtil.copyProperties(userInfo, this);
        }
        this.setUserId(userInfo.getId());
    }

    // 设置视频信息
    public void setVideo(Video video){
        if (video!=null) {
            BeanUtil.copyProperties(video, this);// 视频信息主要字段
            // 封装其他补充字段
            this.setCover(video.getPicUrl()); // 视频封面
            this.setSignature(video.getText()); // 视频文字
        }
    }

}