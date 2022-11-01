package com.itheima.domain.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

//陌生人问题
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
    private Long id;
    private Long userId;//用户id
    private String strangerQuestion="朋友对你来说意味着什么？"; //问题内容

    @TableField(fill = FieldFill.INSERT)
    private Date created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;
}