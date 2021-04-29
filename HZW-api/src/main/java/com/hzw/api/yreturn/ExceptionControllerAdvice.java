package com.hzw.api.yreturn;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultCode MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        System.out.println(e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        // 然后提取错误提示信息进行返回
        return ResultCode.error(10010, message);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResultCode RuntimeExceptionhandle(RuntimeException e) {
        //这个是当spring跑出异常时出现的页面
        String message = e.getMessage();
        return ResultCode.error(10010, message);
    }
}