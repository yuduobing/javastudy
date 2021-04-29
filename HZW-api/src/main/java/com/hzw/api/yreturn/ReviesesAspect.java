package com.hzw.api.yreturn;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */

@Aspect
@Component
public class ReviesesAspect {

    private final static Logger logger = LoggerFactory.getLogger(ReviesesAspect.class);

    //@Before的注解在方法执行之前执行
    //拦截该路径下studentList()方法，两个点表示任何参数
//    @Before("execution(public * com.example.controller.StudentController.studentList(..))")
//    public void log(){
//        System.out.println("Test before");
//    }
//
//    @After("execution(public * com.example.controller.StudentController.studentList(..))")
//    public void doAfter(){
//        System.out.println("Test after");
//    }

    //定义一个公用方法
    @Pointcut("execution(* com.hzw.api.controller.v2.RevieseController.*(..))")

    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String a = request.getRequestURI();

        //url
        logger.info("url={}", request.getRequestURI());

        //method
        logger.info("method={}", request.getMethod());

//        if (request.getRequestURI().equals("/api/revise/generateUpload"))
//        {
//
//
//           Object[] baleR= joinPoint.getArgs();
//            for (int i = 0; i < Object.length; i++) {
//                String  tracesn= Object[i];
//            }
//
//            //取出溯源码时间
//           //判断时间
//
//        }

        //ip
        logger.info("ip={}", request.getRemoteAddr());

        //method
        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        //param
        logger.info("args={}", joinPoint.getArgs());
    }

    @After("log()")
    public void doAfter() {
    }

    @AfterReturning(returning = "obj", pointcut = "log()")
    public void doAfterReturnig(Object obj) {
        logger.info("reponse={}", obj);
    }
}
