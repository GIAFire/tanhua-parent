package com.itheima.vo;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.domain.db.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//访问用户
public class VisitorVo {
    private Long id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String[] tags;
    private Long fateValue;

    // 设置用户信息
    public void setUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            BeanUtil.copyProperties(userInfo, this);
            if (userInfo.getTags() != null) {
                this.setTags(userInfo.getTags().split(","));
            }
        }
    }
}