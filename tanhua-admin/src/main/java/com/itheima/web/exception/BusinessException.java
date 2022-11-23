package com.itheima.web.exception;

//自定义业务异常
public class BusinessException extends RuntimeException {
    public BusinessException(String msg) {
        super(msg);
    }
}
