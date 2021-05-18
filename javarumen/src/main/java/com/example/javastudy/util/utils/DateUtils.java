package com.example.javastudy.util.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYYMMDDHHMMSSsss = "yyyyMMddHHmmssSSSS";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String YYYY_MM_DD_HH = "yyyy-MM-dd HH:00:00";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    /**
     * 获取当前日期, 默认格式为yyyyMMdd
     *
     * @return String
     */
    public static String getDate2()
    {
        return dateTimeNow(YYYYMMDD);
    }

    public static String getYearBegin() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_YEAR, 1);

        return parseDateToStr(YYYY_MM_DD, c.getTime());
    }


    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }


    public static final String getOffsetHourTime(int hour, String pattern) {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
        return parseDateToStr(pattern, calendar.getTime());
    }

    public static final String getOffsetDayTime(int days) {
        return parseDateToStr(YYYY_MM_DD_HH_MM_SS, addDays(new Date(), days));
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeMile()
    {
        return dateTimeNow(YYYYMMDDHHMMSSsss);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(Date time){
        String stamp = "";
        if (!"".equals(time)) {//时间不为空
            try {
                stamp = String.valueOf(time.getTime()/1000);
            } catch (Exception e) {
                System.out.println("参数为空！");
            }
        }else {    //时间为空
            long current_time = System.currentTimeMillis();  //获取当前时间
            stamp = String.valueOf(current_time/1000);
        }
        return stamp;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String time){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        String stamp = "";
        if (!"".equals(time)) {//时间不为空
            try {
                stamp = String.valueOf(sdf.parse(time).getTime()/1000);
            } catch (Exception e) {
                System.out.println("参数为空！");
            }
        }else {    //时间为空
            long current_time = System.currentTimeMillis();  //获取当前时间
            stamp = String.valueOf(current_time/1000);
        }
        return stamp;
    }
    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(int time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_Date = sdf.format(new Date(time * 1000L));
        return time_Date;

    }

    public static long getOffsetMinute(String beginTime, String endTime){
        try {
            Date begin = parseDate(beginTime, YYYY_MM_DD_HH_MM_SS);
            Date end = parseDate(endTime, YYYY_MM_DD_HH_MM_SS);
            long l = end.getTime() - begin.getTime();

            return TimeUnit.MILLISECONDS.toMinutes(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getOffsetHour(String beginTime, String endTime){
        try {
            Date begin = parseDate(beginTime, YYYY_MM_DD_HH_MM_SS);
            Date end = parseDate(endTime, YYYY_MM_DD_HH_MM_SS);
            long l = end.getTime() - begin.getTime();

            return TimeUnit.MILLISECONDS.toHours(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getOffsetDay(String beginTime, String endTime){
        try {
            Date begin = parseDate(beginTime, YYYY_MM_DD);
            Date end = parseDate(endTime, YYYY_MM_DD);
            long l = end.getTime() - begin.getTime();

            return TimeUnit.MILLISECONDS.toDays(l)+1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static String parseDateTime(String str, String fromParsePatterns, String toParsePatterns)
    {
        Date date = null;
        try {
            date = new SimpleDateFormat(fromParsePatterns).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(toParsePatterns).format(date);
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static int getHours(String str, String parsePatterns)
    {
        Date date = null;
        try {
            date = new SimpleDateFormat(parsePatterns).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.HOUR_OF_DAY);
    }

    public static void main(String[] args) {
        String b = "2021-11-12";
        String e = "2021-02-02";
        long offsetDay = getOffsetDay(b, e);
        System.out.println(offsetDay);
        System.out.println(DateUtils.parseDateTime(b, "yyyy-MM-dd", "yyyy年M月d日"));
        System.out.println(getYearBegin());
    }
}
