package com.itheima.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

//JWT的生成和解析
public class JwtUtil {
    // 创建token
    public static String createToken(Map claims) {
        return Jwts.builder()
                .setClaims(claims) //设置响应数据体
                .signWith(SignatureAlgorithm.HS256, ConstantUtil.JWT_SECRET) //设置加密方法和加密盐
                .compact();
    }

    // 解析token
    public static Map parseToken(String token) {
        return Jwts.parser().setSigningKey(ConstantUtil.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
