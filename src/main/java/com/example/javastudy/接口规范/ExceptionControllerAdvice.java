package com.example.javastudy.接口规范;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常处理，抛出接口错误拦截参数
@RestControllerAdvice
public class ExceptionControllerAdvice {

    //校验失败自动抛出

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回
        System.out.println("参数错误"+objectError.getDefaultMessage());
        return objectError.getDefaultMessage();
    }

    @ExceptionHandler(APIException.class)
    public AjaxResult APIExceptionHandler(APIException e) {
        System.out.println("参数错误"+e.getMsg());
        return new AjaxResult(e.getCode(), "响应失败", e.getMsg());
    }

}
