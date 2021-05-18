package com.example.javastudy.接口规范;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public String addUser(person user) {
        // 直接编写业务逻辑
        return "success";
    }
}
