package com.hzw.api.yreturn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */

//拦截器，判断此请求是否需要包装，作用获取此请求，设置属性值

public class ResponseResultInterception implements HandlerInterceptor {
    //标记名称
    public static final String RESPONSE_RESULT_NAME = "1213";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //instanceof 类或者子类
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            System.out.println("方法" + method);
            //包装往下面传递打上标记
            if (clazz.isAnnotationPresent(ResponseResult.class)) {

                request.setAttribute(RESPONSE_RESULT_NAME, clazz.getAnnotation(ResponseResult.class));

            } else if (method.isAnnotationPresent(ResponseResult.class)) {

                request.setAttribute(RESPONSE_RESULT_NAME, method.getAnnotation(ResponseResult.class));
            }

        }
        return true;
    }

}
