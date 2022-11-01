package com.itheima.app.exception;

import com.itheima.vo.ErrorResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionAdvice {


    // 捕获全局非预期异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity handlerExceptionMethod(Exception ex){
        // 1.将异常信息输出到控制台
        ex.printStackTrace();
        // 2.友情提示
        return ResponseEntity.status(500).body(ErrorResult.error());
    }
}
