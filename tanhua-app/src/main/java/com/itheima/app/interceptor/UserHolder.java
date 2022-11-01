package com.itheima.app.interceptor;

import com.itheima.domain.db.User;

// 线程内存储 用户信息
public class UserHolder {

    private static final ThreadLocal<User> TLUSER = new ThreadLocal<>();

    // 设置
    public static void set(User user) {
        TLUSER.set(user);
    }

    // 获取
    public static User get() {
        return TLUSER.get();
    }

    // 删除
    public static void remove() {
        TLUSER.remove();
    }
}
