package com.zhouyu;

import com.spring.ZhouyuApplicationContext;
import com.zhouyu.service.UserService;

import java.net.MalformedURLException;

public class Test {

    public static void main(String[] args) throws MalformedURLException {
//       1 加载配置
        ZhouyuApplicationContext applicationContext = new ZhouyuApplicationContext(AppConfig.class);
//2从容器中拿到
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();  // 1. 代理对象   2. 业务test
    }
}
