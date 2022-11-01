package com.itheima.domain.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

//用户类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Long id;//用户id
    private String mobile; //手机号
    private String password; //密码

    @TableField(fill = FieldFill.INSERT) //插入时自动填充
    private Date created;//创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时自动更新
    private Date updated;//更新时间

    //环信用户信息（预留字段）
    private String hxUser;
    private String hxPassword;
}