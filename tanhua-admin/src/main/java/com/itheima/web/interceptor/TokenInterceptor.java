package com.itheima.web.interceptor;

import cn.hutool.core.util.StrUtil;
import com.itheima.domain.db.Admin;
import com.itheima.web.manager.AdminManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminManager adminManager;

    // 获取token,拦截非登录用户
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        // 获取请求头的token
        String token = request.getHeader("Authorization");

        // 2.没取到就返回401
        if (StrUtil.isEmpty(token)) {
            response.setStatus(401);
            return false;
        }

        // 3.查询redis中是否还有登录状态,没取到就返回401
        Admin admin = adminManager.findAdminByToken(token);
        if (admin == null) {
            response.setStatus(401);
            return false;
        }

        // 4.将admin绑定当前线程
        AdminHolder.set(admin);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AdminHolder.remove();
    }
}
