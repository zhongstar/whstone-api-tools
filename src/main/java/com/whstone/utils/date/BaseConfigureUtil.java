package com.whstone.utils.date;

import com.whstone.utils.eunm.BackupTimeStrategyTypeEnum;
import com.whstone.utils.eunm.RestoreTimeStrategyTypeEnum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: powerchen
 * @Date: 2019/4/15 9:20
 */
public class BaseConfigureUtil {
    private static DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
    private static DateFormat dateInstance = DateFormat.getDateInstance();
    private static DateFormat timeInstance = DateFormat.getTimeInstance();
    private static SimpleDateFormat parseDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * startDate 除了单次、按分钟和按小时外，只作为从指定时间开始的参考
     */
    public static Date accordBackupBaseConfigureInferDate(Date assignDate, DateConfigure dateConfigure) throws ParseException {
        if (assignDate == null) assignDate = DateService.getCurrentUtilDate();
        Date result = null;
        Date startTime = dateTimeInstance.parse(dateConfigure.getStartTime());
        switch (BackupTimeStrategyTypeEnum.getByCode(dateConfigure.getTimeStrategy())) {
            case ONCE_BACKUP:
                if (new Date().before(startTime))
                    return startTime;
                else break;
                //现根据分钟和小时来判断
            case MINUTE_INTERVAL_BACKUP:
                result = getDateGreaterAssignInMinutes(assignDate, startTime, dateConfigure.getTimeIntervelOfInt());
                break;
            case HOURS_INTERVAL_BACKUP:
                result = getDateGreaterAssignInMinutes(assignDate, startTime, dateConfigure.getTimeIntervelOfInt());
                break;
            //根据天来推断
            case DAY_BACKUP:
                result = getDateGreaterAssignInDays(assignDate, startTime, dateConfigure.getDayTimes());
                break;
            //根据周来推断
            case WEEK_BACKUP:
                result = getDateGreaterAssignInWeekdays(assignDate, startTime, dateConfigure.getWeekdays(), dateConfigure.getDayTimes());
                break;
            //根据月来推断
            case MONTH_BACKUP:
                result = getDateGreaterAssignInMonthdays(assignDate, startTime, dateConfigure.getMonthDays(), dateConfigure.getDayTimes());
                break;
        }
        return result;
    }

    /**
     * startDate 除了单次、按分钟和按小时外，只作为从指定时间开始的参考
     */
    public static Date accordRestoreBaseConfigureInferDate(Date assignDate, DateConfigure dateConfigure) throws ParseException {
        if (assignDate == null) assignDate = DateService.getCurrentUtilDate();
        Date result = null;
        Date startTime = dateTimeInstance.parse(dateConfigure.getStartTime());
        switch (RestoreTimeStrategyTypeEnum.getByCode(dateConfigure.getTimeStrategy())) {
            case ONCE_BACKUP:
                if (new Date().before(startTime))
                    return startTime;
                else break;
                //根据周来推断
            case WEEK_BACKUP:
                result = getDateGreaterAssignInWeekdays(assignDate, startTime, dateConfigure.getWeekdays(), dateConfigure.getDayTimes());
                break;
            //根据月来推断
            case MONTH_BACKUP:
                result = getDateGreaterAssignInMonthdays(assignDate, startTime, dateConfigure.getMonthDays(), dateConfigure.getDayTimes());
                break;
        }
        return result;
    }

    //给定一个assignDate和一个startDate，另外的参数是时间间隔,目前按分钟
    //先把assignDate的yyyy-MM-dd嫁接到startDate上，再进行递归加时间间隔，直到时间大于assignDate结束返回处理后的时间
    private static Date getDateGreaterAssignInMinutes(Date assignDate, Date startDate, int minuteInterval) throws ParseException {
        String assignDateHead = dateInstance.format(assignDate);
        while (assignDate.after(startDate)) {
            String startDateTail = timeInstance.format(startDate);
            Date parse = dateTimeInstance.parse(assignDateHead + " " + startDateTail);
            startDate = DateService.offset(parse, DateField.MINUTE, minuteInterval);
        }
        return startDate;
    }

    //   if(start在assign之后)
//         if(timePoints.size==1)
//           if(startTime在timePoints[0]之前)
//               startTime+timePoints[0]
//           else
//               startDate+1天+timePoints[0]
//       else
//           if(startTime在timePoints之中)
//               取第一个大于startTime的时间点
//           else if(startTime在timePoints之前)
//               取第一个
//           else 之后
//                startDate+1天+timePoints[0]
    private static Date getDateGreaterAssignInDays(Date assign, Date start, List<String> timePoints) throws ParseException {
        if (assign.before(start)) {
            return parseTimePoints(start, timePoints);
        } else {
            return parseTimePoints(assign, timePoints);
        }
    }

    private static Date parseTimePoints(Date specify, List<String> timePoints) throws ParseException {
        String date = dateInstance.format(specify);
        String time = timeInstance.format(specify);
        if (timePoints.size() == 1) {
            String timePoint = timePoints.get(0);
            if (timeInstance.parse(time).before(timeInstance.parse(timePoint)) || timeInstance.parse(time).equals(timeInstance.parse(timePoint)))
                return dateTimeInstance.parse(date + " " + timePoint);
            else {
                String nextDate = dateInstance.format(DateService.offset(specify, DateField.DAY_OF_MONTH, 1));
                return dateTimeInstance.parse(nextDate + " " + timePoint);
            }
        } else {
            for (String timePoint : timePoints) {
                if (timeInstance.parse(time).before(timeInstance.parse(timePoint)) || timeInstance.parse(time).equals(timeInstance.parse(timePoint)))
                    return dateTimeInstance.parse(date + " " + timePoint);
            }
            String nextDate = dateInstance.format(DateService.offset(specify, DateField.DAY_OF_MONTH, 1));
            return dateTimeInstance.parse(nextDate + " " + timePoints.get(0));
        }
    }

    private static Date getDateGreaterAssignInWeekdays(Date assign, Date start, List<String> weekdays, List<String> timePoints) throws ParseException {
        if (assign.before(start)) {
            return parseWeekPoints(start, weekdays, timePoints);
        } else {
            return parseWeekPoints(assign, weekdays, timePoints);
        }
    }

    private static Date parseWeekPoints(Date specify, List<String> weekdays, List<String> timePoints) throws ParseException {
        //处理weekdays，基于assignDate和startDate得到对应的时间的数组
        List<String> weekDateByWeekdays = calculateWeekDateByWeekPoints(specify, weekdays);
        String date = dateInstance.format(specify);
        String time = timeInstance.format(specify);
        if (weekDateByWeekdays.size() == 1) {
            Date weekDateByWeekday = parseDate.parse(weekDateByWeekdays.get(0));
            if (dateInstance.parse(date).before(weekDateByWeekday))
                return dateTimeInstance.parse(weekDateByWeekdays.get(0) + " " + timePoints.get(0));
            else if (dateInstance.parse(date).equals(weekDateByWeekday))
                return parseTimePoints(specify, timePoints);
            else
                return dateTimeInstance.parse(parseDate.format(DateService.offset(weekDateByWeekday, DateField.DAY_OF_WEEK, 7)) + " " + timePoints.get(0));
        } else {
            Date firstWeekDate = parseDate.parse(weekDateByWeekdays.get(0));
            for (String weekDateByWeekday : weekDateByWeekdays) {
                Date weekDate = parseDate.parse(weekDateByWeekday);
                if (date.equals(weekDate)) {
                    for (String timePoint : timePoints) {
                        Date parse = timeInstance.parse(timePoint);
                        if (timeInstance.parse(time).before(parse) || timeInstance.parse(time).equals(parse))
                            return dateTimeInstance.parse(date + " " + timePoint);
                    }
                    return dateTimeInstance.parse(dateInstance.parse(weekDateByWeekdays.get(weekDateByWeekdays.indexOf(date) + 1)) + " " + timePoints.get(0));
                } else if (dateInstance.parse(date).before(weekDate))
                    return dateTimeInstance.parse(weekDateByWeekday + " " + timePoints.get(0));
            }
            return dateTimeInstance.parse(parseDate.format(DateService.offset(firstWeekDate, DateField.DAY_OF_WEEK, 7)) + " " + timePoints.get(0));
        }
    }

    private static Date parseMonthPoints(Date specify, List<String> monthdays, List<String> timePoints) throws ParseException {
        //处理monthdays，基于assignDate和startDate得到对应的时间的数组
        List<String> monthDateByMonthdays = calculateMonthDateByMonthdays(specify, monthdays);
        String date = dateInstance.format(specify);
        String time = timeInstance.format(specify);
        if (monthDateByMonthdays.size() == 1) {
            Date monthDateByMonthday = parseDate.parse(monthDateByMonthdays.get(0));
            if (dateInstance.parse(date).before(monthDateByMonthday))
                return dateTimeInstance.parse(monthDateByMonthday + " " + timePoints.get(0));
            else if (dateInstance.parse(date).equals(monthDateByMonthday))
                return parseTimePoints(specify, timePoints);
            else
                return dateTimeInstance.parse(parseDate.format(DateService.offset(monthDateByMonthday, DateField.MONTH, 1)) + " " + timePoints.get(0));
        } else {
            Date firstWeekDate = parseDate.parse(monthDateByMonthdays.get(0));
            for (String monthDateByMonthday : monthDateByMonthdays) {
                if (date.equals(monthDateByMonthday)) {
                    for (String timePoint : timePoints) {
                        Date parse = timeInstance.parse(timePoint);
                        if (timeInstance.parse(time).before(parse) || timeInstance.parse(time).equals(parse))
                            return dateTimeInstance.parse(date + " " + timePoint);
                    }
                    return dateTimeInstance.parse(monthDateByMonthdays.get(monthDateByMonthdays.indexOf(date) + 1) + " " + timePoints.get(0));
                } else if (dateInstance.parse(date).before(firstWeekDate))
                    return dateTimeInstance.parse(monthDateByMonthday + " " + timePoints.get(0));
            }
            return dateTimeInstance.parse(parseDate.format(DateService.offset(firstWeekDate, DateField.MONTH, 1)) + " " + timePoints.get(0));
        }
    }

    //给定一个assignDate和一个startDate，另外的参数是时间间隔，按月的第几天计算
    //assignDate是否在startDate之前
    private static Date getDateGreaterAssignInMonthdays(Date assignDate, Date startDate, List<String> monthdays, List<String> timePoints) throws ParseException {
        if (assignDate.before(startDate)) {
            return parseMonthPoints(startDate, monthdays, timePoints);
        } else {
            return parseMonthPoints(assignDate, monthdays, timePoints);
        }
    }

    public static List<String> calculateWeekDateByWeekPoints(Date date, List<String> weekdays) throws ParseException {
        Calendar instance = Calendar.getInstance();
        String format = dateInstance.format(date);
        instance.setTime(dateInstance.parse(format));
        int weekNum = instance.get(Calendar.DAY_OF_WEEK) - 1;   //这里周日是1所以需要减一
        return weekdays.stream()
                .map(weekday -> dateInstance.format(DateService.offset(date, DateField.DAY_OF_WEEK, Integer.valueOf(weekday) - weekNum)))
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * 将mothdays处理成为date当月的时间格式 例如mothdays={8,9} date=2019-4-17 result={2019-4-8,2019-4-9}
     * 如果result的时间在date之前，需要将月份加一了
     *
     * @param date
     * @param mothdays
     * @return
     * @throws ParseException
     */
    public static List<String> calculateMonthDateByMonthdays(Date date, List<String> mothdays) throws ParseException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(dateInstance.parse(dateInstance.format(date))); //需要将date对象转为yyyy-MM-dd格式
        int monthNum = instance.get(Calendar.DAY_OF_MONTH);
        return mothdays.stream()
                .map(monthday -> {
                    int diff = Integer.valueOf(monthday) - monthNum;    //如果monthDateByMonthdays的时间都在assignDate之前
                    if (diff < 0) {
                        Date offset = DateService.offset(date, DateField.MONTH, 1);
                        return dateInstance.format(DateService.offset(offset, DateField.DAY_OF_MONTH, diff));
                    }
                    return dateInstance.format(DateService.offset(date, DateField.DAY_OF_MONTH, diff));
                })
                .sorted()
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws ParseException {
        Calendar instance = Calendar.getInstance();
//        Date date = new Date();
//        String format = dateInstance.format(date);
//        instance.setTime(dateInstance.parse(format));
//        int i = instance.get(Calendar.DAY_OF_MONTH);
//        Date offset = DateService.offset(dateInstance.parse(format), DateField.MONTH, 17 - i);
//        Date offset1 = DateService.offset(date, DateField.MONTH, 1);
//        Date offset2 = DateService.offset(date, DateField.DAY_OF_WEEK, 7);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = sdf2.parse("2019-04-22");
        String format = sdf2.format(parse);
        Date offset = DateService.offset(sdf2.parse("2019-04-22"), DateField.MONTH, 1);
        String format1 = sdf2.format(offset);
        System.out.println();
    }

}
