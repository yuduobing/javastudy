package com.example.javastudy.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
public class AyUtils {
//    得到几天前数据
    public static String getCurrentBeforeNum(String pattern, int num){
        //默认是当前日期

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, num);
        System.out.println(cal.getTime());
        return dateToString(cal.getTime(), pattern);
    }

//    转换时间为string
    public static String dateToString(Date date, String pattern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(pattern).format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateString;
    }
    /**
     * 获取SimpleDateFormat
     *
     * @param pattern 日期格式
     * @return
     */
    public static SimpleDateFormat getDateFormat(String pattern) {
        if (pattern != null && pattern.equals("")) {
            throw new RuntimeException("无效日期格式");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat;
    }






}
