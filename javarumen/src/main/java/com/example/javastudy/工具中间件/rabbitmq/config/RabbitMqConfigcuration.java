package com.example.javastudy.工具中间件.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 * 但是如果rabbitmq server重启后，则队列和消息就会丢失
 * 1、队列持久化
 * durable=True
 * 2、消息持久化
 * delivery_mode = 2
 *
 *
 * 如果申明一个queue为持久化，那么就需要在服务端和客户端都需要设置
 *
 * 如果申明一个消息的持久化，加一个
 * delivery_mode = 2
 */
@Configuration
public class RabbitMqConfigcuration {


//    1声明注册交换机
    @Bean
    public FanoutExchange fanoutExchange(){
        return  new FanoutExchange("fanout_order_exchange",true,false);
    }
    //    2申明队列
    @Bean
    public Queue smsQueue(){
        return new Queue("smsque",true) ;
    }
    @Bean
    public Queue eamilQueue(){
        return new Queue("邮箱",true) ;
    }
//      完成队列和交换机的绑定
          @Bean
    public Binding smsBing(){
        return BindingBuilder.bind(smsQueue()).to(fanoutExchange());
          }
    @Bean
    public Binding emailBing(){
        return BindingBuilder.bind(eamilQueue()).to(fanoutExchange());
    }
}

