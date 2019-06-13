package com.whstone.utils.date;


import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间&日期服务
 */

public class DateService {

    /**
     * 默认时间
     */
    private static Date defaultDate = null;


    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(DateService.class);


    /**
     * 获得当前时间，返回java.util.Date
     *
     * @return
     */
    public static Date getCurrentUtilDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 获得前几天的时间（负数则为后几天）
     *
     * @param days
     * @return
     */
    public static Date getBeforeDayDate(int days) {
        return new Date(getCurrentUtilDate().getTime() - days * 24 * 3600000);
    }

    /**
     * 获得默认时间(1970-01-01 00:00:00)
     *
     * @return
     */
    public static Date getDefaultDate() {
        if (defaultDate == null) {
            Calendar c = Calendar.getInstance();
            c.set(1970, 01, 01, 00, 00, 00);
            defaultDate = c.getTime();
        }
        return defaultDate;
    }

    /**
     * 获得当前时间，返回java.sql.Date
     *
     * @return
     */
    public static java.sql.Date getCurrentSqlDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * 获得当前时间，返回字符串(yyyy-MM-dd HH:mm:ss)
     *
     * @return
     */
    public static String getCurrentDateAsString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(getCurrentUtilDate());
    }

    /**
     * 获得当前时间，返回用户自定义格式字符串
     *
     * @param formatStr
     * @return
     */
    public static String getCurrentDateAsStringCustom(String formatStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        return formatter.format(getCurrentUtilDate());
    }

    /**
     * 将日期转换为字符串默认格式:yyyy-MM-dd HH:mm:ss)
     *
     * @param date
     * @return
     */
    public static String parseDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }


    /**
     * 将java.util.Date转化为HH:00格式的字符串
     *
     * @param date
     * @return
     */
    public static String parseDateToHourMinuteString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:00");
        return formatter.format(date);
    }


    /**
     * 将字符串转换为日期java.sql.Date)
     *
     * @param dateStr
     * @return
     */
    public static java.sql.Date parseStringToSqlDate(String dateStr) {
        return java.sql.Date.valueOf(dateStr);
    }

    /**
     * 获得指定格式的日期对象
     *
     * @return
     */


    public static Date changeDateFormat(String str) {
        Date resultDate = null;
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {
            if (str.length() == 10) {
                resultDate = sDateFormat.parse(str + " 00:00:00");
            } else {
                resultDate = sDateFormat.parse(str);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    /**
     * 比如两个Date是否处于同一天
     *
     * @param date1
     * @param date2
     * @return true:处于同一天；false：不处于同一天
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * description:把String日期date按format的格式轉換成Date類型
     *
     * @param date
     * @param format
     * @return
     */
    public static Date convertString2Date(String date, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        Date rtnDate = null;
        try {
            rtnDate = fmt.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnDate;
    }

    /**
     * description:把Date日期按format的格式轉換成String類型
     *
     * @param date
     * @param format
     * @return
     */
    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        String rtnDate = null;
        try {
            rtnDate = fmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnDate;
    }

    /**
     * 当前时间增加24小时
     */
    public static String addHours() {
        long curren = System.currentTimeMillis();
        curren += 24 * 60 * 60 * 1000;
        Date date = new Date(curren);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }


    public static Date addOrMinusYear(Date creatTime, int i) throws Exception {
        Date rtn = null;
        GregorianCalendar cal = new GregorianCalendar();
        //       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //       sdf.parse(creatTime).getTime();
        //       Date date = new Date(creatTime);
        cal.setTime(creatTime);
        //  cal.add(1, i);
        //   如果是月份加减
        cal.add(Calendar.MONDAY, i);
        //   如果是星期加减
        //cal.add(cal.TUESDAY, i);
        //   如果是每日加减
        //cal.add(Calendar.DAY_OF_MONTH, i);
        //   如果是小时加减cal.add(10, i);
        //  如果是分钟加减  cal.add(12, i);
        //   如果是秒的加减cal.add(13, i);
        rtn = cal.getTime();
        return rtn;
    }

    public static String getDurationStringByTime(LocalDateTime beginTime, LocalDateTime endTime) {
        Duration duration = Duration.between(beginTime, endTime);
        Long sec = duration.getSeconds();
        int days = (int) (sec / (24 * 3600));
        int hours = (int) (sec / 3600 % 24);
        int minute = (int) (sec / 60 % 60);
        int second = (int) (sec % 60);
        StringBuilder timeStringBuilder = new StringBuilder();
        timeStringBuilder.append(days == 0 ? "" : days + "天");
        timeStringBuilder.append(hours == 0 ? "" : hours + "小时");
        timeStringBuilder.append(minute == 0 ? "" : minute + "分");
        timeStringBuilder.append(second == 0 ? "" : second + "秒");
        return timeStringBuilder.toString() + "钟";
    }

    /**
     * 获取两个时间的秒间隔
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String intervalSecond(Date startDate, Date endDate) {
        long interval = (endDate.getTime() - startDate.getTime()) / 1000;
        return String.valueOf(interval);
    }

    public static Date offset(Date date, DateField dateField, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(dateField.getValue(), offset);
        return cal.getTime();
    }

    public static Date parse(String dateStr, DateFormat dateFormat) {
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception var4) {
            String pattern;
            if (dateFormat instanceof SimpleDateFormat) {
                pattern = ((SimpleDateFormat) dateFormat).toPattern();
            } else {
                pattern = dateFormat.toString();
            }
            throw new DateTimeException(String.format("Parse [%s] with format [%s] error!", new Object[]{dateStr, pattern}), var4);
        }
    }

    public static void main(String[] args) throws Exception {
        Date date = new Date();

        System.out.println(getCurrentDateAsString());
        System.out.println(date);
        // format对象是用来以指定的时间格式格式化时间的
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 这里的格式可以自己设置
        Date d = sdf.parse("2016-04-12 17:00:00");
        System.out.println(sdf.format(DateService.addOrMinusYear(date, -1)));
        d.setDate(d.getDate() + 7);
        System.out.println(d.getDate());
        d.setMinutes(d.getMinutes() + 15);//给当前时间加50分钟后的时间
        System.out.println(sdf.format(d));
        int i = date.compareTo(d);
        System.out.println(i);


    }
}
