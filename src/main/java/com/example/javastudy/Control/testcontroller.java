package com.example.javastudy.Control;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@RequestMapping("/test")
public class testcontroller {

    @RequestMapping("/hello")
    public  String  hello(){
         return "helloworld余多彬";
    }
}
