package com.itheima.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuanXinVo {

    // 用户ID
    private String username;

    // 用户密码
    private String password;
}
