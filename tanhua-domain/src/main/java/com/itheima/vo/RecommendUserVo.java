package com.itheima.vo;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.domain.db.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendUserVo {

    private Long id;
    private String avatar; // 头像
    private String nickname; // 昵称
    private String gender; //性别 man woman
    private Integer age; // 年龄
    private String[] tags; // 标签
    private Long fateValue; //缘分值

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