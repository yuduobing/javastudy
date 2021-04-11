package com.example.javastudy.leetcode;

import java.util.HashSet;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
public class 字符串 {

/*
筛选出第一个不重复字符串的下标
 */
    public  static  int  noduplicate(String s){
        //错误原因只比较了之前的，没比较之后的
//        String[] split = s.split("");
//        System.out.println("拆封后的字符串");
//        System.out.println(split);
//        HashSet<Object> objects = new HashSet<>();
//        for (int i = 0; i < split.length; i++) {
//            int length = objects.size();
//            objects.add(split[i]);
//            if (objects.size()==length){
//                return i;
//            }
//        }
//
//        return  -1;
        String[] split = s.split("");
        HashSet<Object> objects = new HashSet<>();
        for (int i = 0; i < split.length; i++) {
//            int length = objects.size();

            for (int j = 1; j < split.length-1; j++) {

                if (  split[i]==split[j])  {

continue;
                }

                return i;
            }
        }

        return  -1;
    }
}
