package com.itheima.util;

//常量定义
public class ConstantUtil {
    //===================================系统使用=====================================//
    //初始化密码
    public static final String INIT_PASSWORD = "123456";
    //jwt加密盐
    public static final String JWT_SECRET = "tanhua";


    //===================================redis中使用===================================//
    //短信验证码
    public static final String SMS_CODE = "sms:";
    //token
    public static final String USER_TOKEN = "token:";
    //动态点赞
    public static final String MOVEMENT_LIKE = "movement_like:{}_{}";
    //动态喜欢
    public static final String MOVEMENT_LOVE = "movement_love:{}_{}";
    //视频点赞
    public static final String VIDEO_LIKE = "video_like:{}_{}";
    //关注用户
    public static final String FOCUS_USER = "focus_user:{}_{}";
    //用户最后一次访问时间
    public static final String LAST_ACCESS_TIME = "last_access_time:";

    //===================================redis中使用======================================//
    //管理员登录验证码
    public static final String ADMIN_CODE = "admin_code_";
    //管理员令牌
    public static final String ADMIN_TOKEN = "admin_token_";


    //===================================mongodb中使用===================================//
    //视频大数据id标识
    public static final String VIDEO_ID = "video";
    //动态大数据id标识
    public static final String MOVEMENT_ID = "movement";
    //用户大数据id标识
    public static final String USER_ID = "user";


    //我的动态表名前缀
    public static final String MOVEMENT_MINE = "movement_mine_";
    //好友动态表名前缀
    public static final String MOVEMENT_FRIEND = "movement_friend_";

}
