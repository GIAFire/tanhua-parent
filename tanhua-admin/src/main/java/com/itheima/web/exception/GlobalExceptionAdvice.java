package com.itheima.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

//全局异常处理器
@ControllerAdvice
public class GlobalExceptionAdvice {

    //处理预期异常
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handlerBusinessException(Exception exception) {
        //打印异常
        exception.printStackTrace();

        //返回错误
        Map<String, String> errorResult = new HashMap<>();
        errorResult.put("message", exception.getMessage());//获取自定义异常信息
        return ResponseEntity.status(500).body(errorResult);
    }

    //处理非预期异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity handlerException(Exception exception) {
        //打印异常
        exception.printStackTrace();

        //返回错误
        Map<String, String> errorResult = new HashMap<>();
        errorResult.put("message", "网络繁忙~~~");
        return ResponseEntity.status(500).body(errorResult);
    }
}
