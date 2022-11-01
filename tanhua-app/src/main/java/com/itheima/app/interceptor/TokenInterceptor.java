package com.itheima.app.interceptor;

import com.itheima.app.manager.UserManager;
import com.itheima.domain.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserManager userManager;

    // 预处理拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.取出请求头的token
        String token = request.getHeader("Authorization");
        // 2.解析token获取user
        User user = userManager.findUserByToken(token);
        // 3.判断
        if (user == null) {
            response.setStatus(401); // 权限不足
            return false;
        }
        // 4.将user存储到线程内
        UserHolder.set(user);

        // 5.放行
        return true;
    }

    // 处理响应
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.remove();
    }
}
