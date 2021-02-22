package com.example.javastudy.接口规范.登录验证;

import com.example.javastudy.接口规范.person;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

//上下问对象
public class RequestContext {
    public static HttpServletRequest getCurrentRequest() {
        // 通过`RequestContextHolder`获取当前request请求对象
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    public static person getCurrentUser() {
        // 通过request对象获取session对象，再获取当前用户对象
        return (person)getCurrentRequest().getSession().getAttribute("user");
    }

//    用法
//
//    User user = RequestContext.getCurrentUser();
//    System.out.println("service层---当前登录用户对象：" + user);
}

