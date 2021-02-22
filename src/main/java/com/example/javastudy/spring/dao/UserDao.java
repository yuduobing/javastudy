package com.example.javastudy.spring.dao;


import org.springframework.stereotype.Repository;

//名称可以不写默认是首字母小写也可以指定名称
//有多个接口实现类@autowried写抽象类，没法找
@Repository(value = "userDapImpll1")
public class UserDao {


    public void say(){

        System.out.println("进来了");
    };
}
