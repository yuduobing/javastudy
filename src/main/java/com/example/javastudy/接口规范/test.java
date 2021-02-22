package com.example.javastudy.接口规范;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
//@RequestMapping("/jkgf")
public class test {

    @Autowired
     UserService person1mapper;

    @GetMapping("/test")
    public  String  test(){
        return  "helloworld";
    }
    @PostMapping("/addUser")
    public String addUser(@RequestBody @Valid person user) {
        // 如果有参数校验失败，会将错误信息封装成对象组装在BindingResult里
//        for (ObjectError error : bindingResult.getAllErrors()) {
//            return error.getDefaultMessage();
//        }
        return person1mapper.addUser(user);
    }
//自己捕捉异常
//    @PostMapping("/addUser")
//    public ResultVO<String> addUser(@RequestBody @Valid User user, BindingResult bindingResult) {
//        for (ObjectError error : bindingResult.getAllErrors()) {
//            // 拿到校验错误的参数字段
//            String field = bindingResult.getFieldError().getField();
//            // 判断是哪个字段发生了错误，然后返回数据响应体
//            switch (field) {
//                case "account":
//                    return new ResultVO<>(100001, "账号验证错误", error.getDefaultMessage());
//                case "password":
//                    return new ResultVO<>(100002, "密码验证错误", error.getDefaultMessage());
//                case "email":
//                    return new ResultVO<>(100003, "邮箱验证错误", error.getDefaultMessage());
//            }
//        }
//        // 没有错误则返回则直接返回正确的信息
//        return new ResultVO<>(userService.addUser(user));
//    }

    @GetMapping("/getUser")
    public person getUser() {
        person user = new person();
        user.setId(1L);
        user.setAccount("12345678");
        user.setPassword("12345678");
        user.setEmail("123@qq.com");
        return     user;
//        return new AjaxResult(user);
    }



}
