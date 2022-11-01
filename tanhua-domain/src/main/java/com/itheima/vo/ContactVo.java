package com.itheima.vo;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.domain.db.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactVo implements Serializable { // 好友（联系人）

    private Long id;
    private String userId;  // 好友id
    private String avatar; // 好友头像
    private String nickname; // 好友昵称
    private String gender; // 好友性别
    private Integer age; // 好友年龄
    private String city; // 好友所在城市

    // 设置用户信息
    public void setUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            BeanUtil.copyProperties(userInfo, this);
        }
        this.setUserId(userInfo.getId().toString());
    }


}