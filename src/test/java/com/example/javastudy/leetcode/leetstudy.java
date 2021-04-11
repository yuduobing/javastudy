package com.example.javastudy.leetcode;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
    public void test() {
        System.out.println("你好");
        int[] a = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int nums = 数组.duplicateDelete(a);
        System.out.println(nums);
        System.out.println(a.length);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    @Test
    public void maProfit() {

        int[] a = {7, 1, 5, 3, 6, 4};
        int nums = 数组.maxProfit(a);
        System.out.println(nums);
        System.out.println(a.length);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    //测试是否有重复元素
    @Test
    public void maProfit2() {
        /**/
        int[] a = {7, 1, 5, 1, 6, 4};
        boolean nums = 数组.containsDuplicate(a);
        System.out.println(nums);

    }

    //数组反转
    @Test
    public void charReverse() {
        /**/
//        char[] a={"h","e","l","l","o"};
        char[] cs2 = {'I', 'L', 'o', 'v', 'e', 'C', 'o', 'd', 'e'};
        数组.revisechar(cs2);

    }

    @Test
    public void 反转整数() {
        /**/
//        char[] a={"h","e","l","l","o"};
        int a = 120;
        数组.revised(a);

    }

    @Test
    public void 挑出第一个不重复的数字() {
        String s = "loveliove";
        int a = 字符串.noduplicate(s);
        System.out.println(a);
    }

    @Test
//    /*循环遍历树根算法
    public void 自定义算法() throws JSONException {
        //传入的的数组
        List<JSONObject> objects1 = new ArrayList<>();
        //字段名 和list中的数据保持一致
        String[] sname = {"SID", "PID"};

        try {
            for (int i = 0; i < sname.length; i++) {
                String s = sname[i];
                HashSet<String> set = new HashSet<>();

                objects1.forEach(val -> {
                    String stringname = null;

                    try {
                        stringname = val.getString(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    set.add(stringname);
                });
                //取出set数据放入制定好的格式
                set.forEach(val -> {
                    HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
                    objects1.forEach(valobject -> {

                        try {
                            if (valobject.getString(s).equalsIgnoreCase(val)) {
                                //数据相等放入
//                                objectObjectHashMap.put();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    });

                });

            }



        } catch (Exception  e) {
            e.printStackTrace();
        }

    }

}