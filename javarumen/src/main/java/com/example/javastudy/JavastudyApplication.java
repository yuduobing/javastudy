package com.example.javastudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavastudyApplication {

    public static void main(String[] args) {


//       Class[] interfaces={loginDao.class};


//    pringApplication.run(JavastudyApplication.class, args);

     //3个参数分别是类加载器，被增强的类，增强类

//        Proxy.newProxyInstance(JDKProxy.class.getClassLoader(),interfaces,new LoginProxy ())

        SpringApplication.run(JavastudyApplication.class,args);

    }

}
//创建代理对象代码
//class    LoginProxy implements InvocationHandler{
//
////      类创建就会被创建写增强逻辑
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        return null;
//    }
//}