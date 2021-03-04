package com.example.javastudy.leetcode;

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

}
