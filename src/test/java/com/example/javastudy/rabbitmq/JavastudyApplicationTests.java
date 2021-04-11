package com.example.javastudy.rabbitmq;

import com.example.javastudy.工具中间件.rabbitmq.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavastudyApplicationTests2 {

 @Autowired
 private OrderService orderService;
    @Test
    void  testspring(){

       orderService.mark("1","2",3);
    }





}
