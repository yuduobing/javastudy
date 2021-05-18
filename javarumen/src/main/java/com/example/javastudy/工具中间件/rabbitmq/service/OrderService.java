package com.example.javastudy.工具中间件.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@Service
public class OrderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void mark(String userid, String productid, int num) {
        String orderid = UUID.randomUUID().toString();
        System.out.println(" 生产的订单"+orderid );
        String exchangeName = "fanout_order_exchange";
        //fanout没有路由
        String routingKey=" ";
        rabbitTemplate.convertAndSend(exchangeName,routingKey,orderid);
    }
}
