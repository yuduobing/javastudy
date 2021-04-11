package com.example.javastudy.leetcode;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import jdk.nashorn.internal.runtime.FindProperty;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
public class 数组 {
    /*
    * 1数组有序重复去重法
    * */



    public  static  int duplicateDelete(int[] nums){
        if (nums.length==0){
            return 0;
        }
        else {
            int i=0;
            for (int j=1;j<nums.length;j++){
                if (nums[i]!=nums[j]){
                    i++;
                    nums[i]=nums[j];
                }


            }

             return  i+1;

        }
        /*
        重复元素
         */

    }

    /*
           2贪心算法
           买卖股票的最佳时机 II
给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。

设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。

注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。

示例 1:

输入: [7,1,5,3,6,4]
输出: 7
解释: 在第 2 天（股票价格 = 1）的时候买入，在第 3 天（股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。
     随后，在第 4 天（股票价格 = 3）的时候买入，在第 5 天（股票价格 = 6）的时候卖出, 这笔交易所能获得利润 = 6-3 = 3 。

    * */
    public  static int  maxProfit(int[] prices) {
        if (prices.length==0){
            return 0;
        }
        int lirun=0;

        for(int i=0;i<prices.length;i++){
            for(int j=i+1;j<prices.length;j++){

                /*
                * 只需要找到最近的低和高出进行买
                * */
                if (prices[i]>prices[j]){

                    break;
                }

                if (prices[i]<prices[j]){
                    if (j<prices.length-1){

                        if (prices[j]>prices[j+1]){
                            if(prices[j]-prices[i]>lirun){
                                lirun=prices[j]-prices[i];
                            }
                        }
                    }

                }


            }

        }

        return lirun;

    }

    /*
    * 判断数组是否有重复元素
    * */
    public  static  boolean containsDuplicate(int[]  nums){
//           时间长
//        for (int i = 0; i < nums.length; i++) {
//            for (int j = 0; j < i; j++) {
//               if (nums[i]==nums[j]){
//                   return  true;
//               }
//            }
//        }
//
//        return  false;
        ///使用set集合  优势时间短
        HashSet<Object> objects = new HashSet<>();
        for (int num : nums) {
            objects.add(num);

        }
        return   objects.size()!=nums.length;

    }

/**
 * 反转字符串
 */
    public static  void revisechar(char[] nums){
        System.out.println(nums);
        for (int i = 0; i < nums.length/2; i++) {
            char  temp=nums[i];

            nums[i]=nums[nums.length-1-i];

            nums[nums.length-1-i]=temp;
        }

        System.out.println(nums);
    }
    /*
    反转正数
    给你一个 32 位的有符号整数 x ，返回 x 中每位上的数字反转后的结果。

如果反转后整数超过 32 位的有符号整数的范围 [−231,  231 − 1] ，就返回 0。

假设环境不允许存储 64 位整数（有符号或无符号）。


     */
    public  static  int  revised(int x) {

//         我的解法是错误的ge不会一直*100
//        int huander=x/100;
//        int bai=( x-huander*100)/10;
//        int ge=x-huander*100-bai*10;
//        System.out.println(ge*100+bai*10+huander);
//辗转除10
        long   result=0;
        while (Math.abs(x)>0){
           int b=x%10;
           result=result *10 + b;;
           x=x/10;
        }
        if (result>Integer.MAX_VALUE||result<Integer.MIN_VALUE) {
            return 0;
        }
        return (int)result;
    }


    public  void sasa (){


        List<JSONObject> objects1 = new ArrayList<>();
        //字段名 和list中的数据保持一致
        String[] sname = {"SID", "PID"};

        try {
            for (int i = 0; i < sname.length; i++) {
                String s = sname[i];
                HashSet<String> set = new HashSet<>();

                objects1.forEach(val -> {
                    String stringname = null;


                        stringname = val.getStr(s);


                    set.add(stringname);
                });
                //取出set数据放入制定好的格式
                set.forEach(val -> {
                   final   HashMap<String, Object> objectObjectHashMap = new HashMap<>();
                    objects1.forEach(valobject -> {


                        if (valobject.getStr(s).equalsIgnoreCase(val)) {
                            //数据相等放入
                            objectObjectHashMap.put("values",valobject);
                        }

                    });

                });

            }



        } catch (Exception  e) {
            e.printStackTrace();
        }

    }
}
