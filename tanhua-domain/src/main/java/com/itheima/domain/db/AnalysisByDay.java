package com.itheima.domain.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisByDay implements Serializable {

    private Long id;
    private Date recordDate;//统计日期
    private Integer numRegistered = 0;//新注册用户数
    private Integer numActive = 0;//活跃用户数
    private Integer numLogin = 0;//登录次数
    private Integer numRetention1d = 0;//次日留存用户数

    @TableField(fill = FieldFill.INSERT)
    private Date created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;

}