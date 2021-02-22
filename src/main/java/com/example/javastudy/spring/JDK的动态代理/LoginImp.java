package com.example.javastudy.spring.JDK的动态代理;

public class LoginImp implements loginDao {
    @Override
    public int login(int a, int b) {
        return a+b;
    }

    @Override
    public void update(String a) {

    }
}
