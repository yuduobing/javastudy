package com.example.javastudy.websocket;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Configuration
 * @bean
 * 表示给spring容器注入bean  他们两个一般一起使用
 * 他们和@component的区别是他们使用了动态代理cglib 所以每次调用都会返回同一个实例
 */
//@Configuration
public class WebSocketConfig {

    /**
     * 给spring容器注入这个ServerEndpointExporter对象
     * 相当于xml：
     * <beans>
     *     <bean id="serverEndpointExporter" class="org.springframework.web.socket.server.standard.ServerEndpointExporter"/>
     * </beans>
     *
     * 检测所有带有@serverEndpoint注解的bean并注册他们。
     * @return
     */
    @Bean
    public  ServerEndpointExporter serverEndpointExporter(){
        System.out.println("我被注入了");
        return  new ServerEndpointExporter();
    }
}