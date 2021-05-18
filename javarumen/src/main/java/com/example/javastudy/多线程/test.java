package com.example.javastudy.多线程;

import java.util.*;

//三步实例化一个类。start方法会启动run方法
//放入线程类
//启动

public class test {
//    static 的作用
//    静态类可直接未实例化调用
     public static  void  main(){

//        System.out.println(sales.b);
//         把不安全的类转换成线程安全的类
//         List<Object> objects = Collections.synchronizedList(new ArrayList<>());


         sales a=new sales();
        Thread   win1= new Thread(a);
        Thread   win2= new Thread(a);
        win1.setName("窗口1");
        win1.setName("窗口2");
        win1.start();
        win2.start();


    }



}
