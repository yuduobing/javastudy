package com.example.javastudy.接口规范.登录验证;

import com.example.javastudy.接口规范.person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class SessionController {
    @PostMapping("login")
    public  String  login(@RequestBody @Valid person user, HttpSession session){
        if (user.getAccount().equals("admin")&& "admin".equals(user.getPassword())) {
            // 正确的话就将用户信息存到session中
            session.setAttribute("user", user);
            return "登录成功";
        }

        return "账号或密码错误";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 退出登录就是将用户信息删除
        session.removeAttribute("user");
        return "退出成功";
    }
    @GetMapping("api")
    public String api(HttpSession session) {
        // 模拟各种api，访问之前都要检查有没有登录，没有登录就提示用户登录

        // 如果有登录就调用业务层执行业务逻辑，然后返回数据
        return "成功返回数据";
    }

    @GetMapping("api2")
    public String api2(HttpSession session) {
        // 模拟各种api，访问之前都要检查有没有登录，没有登录就提示用户登录

        // 如果有登录就调用业务层执行业务逻辑，然后返回数据
        return "成功返回数据";
    }

    }

