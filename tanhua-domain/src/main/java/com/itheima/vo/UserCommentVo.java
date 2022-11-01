package com.itheima.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentVo {
    private String id;
    private String avatar;
    private String nickname;
    private String createDate;
}