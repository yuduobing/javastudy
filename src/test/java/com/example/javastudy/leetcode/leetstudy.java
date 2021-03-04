package com.example.javastudy.leetcode;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */

//单元测试类的原注解是这样的：
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//经查阅资料，得知SpringBootTest在启动的时候不会启动服务器，所以WebSocket自然会报错，这个时候需要添加选项webEnvironment，以便提供一个测试的web环境。如下：
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class leetstudy {
    @Test
    public  void test(){
        System.out.println("你好");
        int[] a={0,0,1,1,1,2,2,3,3,4};
       int nums= 数组.duplicateDelete(a);
        System.out.println(nums);
        System.out.println(a.length);
        for (int i=0;i<a.length;i++){
            System.out.println(a[i]);
        }
    }
    @Test
    public  void maProfit(){


        int[] a={7,1,5,3,6,4};
        int nums= 数组.maxProfit(a);
        System.out.println(nums);
        System.out.println(a.length);
        for (int i=0;i<a.length;i++){
            System.out.println(a[i]);
        }
    }
    //测试是否有重复元素
    @Test
    public  void maProfit2(){
        /**/
        int[] a={7,1,5,1,6,4};
        boolean nums= 数组.containsDuplicate(a);
        System.out.println(nums);

    }



}
