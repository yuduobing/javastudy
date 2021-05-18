package com.example.javastudy.多线程;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class sales implements Runnable {

    private static int a = 100;


    private static int b = 100;

    public void tosale() {
        System.out.println("进来了");

            while (a >0) {

//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//先赋值后减1
                a--;
                String name = Thread.currentThread().getName();
                log.println(name);
                System.out.println(name+"买了一张票" + a );
            }

    }


    @Override
    public void run() {
        tosale();
    }


//    int ticketSum = 100;
//    Object a=new Object();
//    @Override
//    public void run() {
// synchronized (a ){
//
//
////        try {
////            System.out.println("开始卖票"+Thread.currentThread().getName());
////            Thread.sleep(100);
////
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        // 有余票，就卖
//        while (ticketSum > 0) {
//        System.out.println(Thread.currentThread().getName() + "售出第" + (100 - ticketSum + 1) + "张票");
//            ticketSum--;
//        }
//        System.out.println(Thread.currentThread().getName() + "表示没有票了");
//    }}
//}
}