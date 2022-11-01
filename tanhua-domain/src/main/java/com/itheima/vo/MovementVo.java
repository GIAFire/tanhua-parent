package com.itheima.vo;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.domain.db.UserInfo;
import com.itheima.domain.mongo.Movement;
import com.itheima.util.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovementVo implements Serializable {

    private String id; //动态id

    private Long userId; //用户id
    private String avatar; //头像
    private String nickname; //昵称
    private String gender; //性别 man woman
    private Integer age; //年龄
    private String[] tags; //标签

    private String textContent; //文字动态
    private String[] imageContent; //图片动态
    private String distance; //距离
    private String createDate; //发布时间 如: 10分钟前
    private Integer likeCount; //点赞数
    private Integer commentCount; //评论数
    private Integer loveCount; //喜欢数

    private Integer hasLiked = 0; //是否点赞（1是，0否）
    private Integer hasLoved = 0; //是否喜欢（1是，0否）

    // 设置用户信息
    public void setUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            BeanUtil.copyProperties(userInfo, this);
            if (userInfo.getTags() != null) {
                this.setTags(userInfo.getTags().split(","));
            }
            this.setUserId(userInfo.getId());
        }
    }

    // 设置动态信息
    public void setMovement(Movement movement) {
        // 设置动态信息
        BeanUtil.copyProperties(movement, this);
        // 设置动态图片
        this.setImageContent(movement.getMedias().toArray(new String[]{}));
        // 设置发布时间
        this.setCreateDate(DateFormatUtil.format(new Date(movement.getCreated())));
        // 根据经纬度坐标计算距离
        this.setDistance("距离1.2公里");
    }
}