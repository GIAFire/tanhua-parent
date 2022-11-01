package com.itheima.domain.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

//通知设置
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {
    private Long id;
    private Long userId; //用户id
    private Boolean likeNotification;  //推送喜欢通知
    private Boolean pinglunNotification; //推送评论通知
    private Boolean gonggaoNotification; //推送公告通知

    @TableField(fill = FieldFill.INSERT)
    private Date created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;
}