package com.itheima.domain.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log implements Serializable {

    private Long id;//id
    private Long userId;//用户id
    private String logTime;//操作时间
    private String place;//登陆地点
    private String equipment;//登陆设备
    /**
     * 操作类型
     * 0101为登录，0102为注册，0201为发动态，0202为浏览动态，0203为动态点赞，0204为动态喜欢，
     * 0205为评论，0206为动态取消点赞，0207为动态取消喜欢，0301为发小视频，0302为小视频点赞，
     * 0303为小视频取消点赞，0304为小视频评论
     */
    private String type;

    @TableField(fill = FieldFill.INSERT)
    private Date created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;

}