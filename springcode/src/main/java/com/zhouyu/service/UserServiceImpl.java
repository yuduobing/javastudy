package com.zhouyu.service;

import com.spring.Autowired;
import com.spring.Component;

@Component("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private OrderService orderService;

    private String name;

    public void setName(String name) {
        this.name = name;
    }


    public void test() {
        System.out.println(orderService);
        System.out.println(name);
    }


}
