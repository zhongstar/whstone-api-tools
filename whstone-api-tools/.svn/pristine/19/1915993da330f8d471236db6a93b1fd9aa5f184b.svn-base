package com.whstone.utils.date;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DateConfigure {

    private Integer timeStrategy;

    private String startTime;

    private List<String> dayTimes;

    private List<String> weekdays;

    private List<String> monthDays;

    private String timeInterval;

    public DateConfigure() {
    }

    public DateConfigure(Integer timeStrategy, String startTime, String dayTime, String weekday, String monthDay, String timeInterval) {
        //必须得排序
        String[] dayTimes = dayTime == null ? null : dayTime.trim().split(" ");
        String[] weekdays = weekday == null ? null : weekday.trim().split(" ");
        String[] monthDays = monthDay == null ? null : monthDay.trim().split(" ");
        if (dayTimes != null) {
            for (int i = 0; i < dayTimes.length; i++)
                dayTimes[i] += ":00";
        }
        this.timeStrategy = timeStrategy;
        this.startTime = startTime;
        this.dayTimes = dayTimes == null ? null : Arrays.asList(dayTimes).stream().sorted().collect(Collectors.toList());
        this.weekdays = weekdays == null ? null : Arrays.asList(weekdays).stream().sorted().collect(Collectors.toList());
        this.monthDays = monthDays == null ? null : Arrays.asList(monthDays).stream().sorted().collect(Collectors.toList());
        this.timeInterval = timeInterval;
    }

    public Integer getTimeStrategy() {
        return timeStrategy;
    }

    public void setTimeStrategy(Integer timeStrategy) {
        this.timeStrategy = timeStrategy;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public List<String> getDayTimes() {
        return dayTimes;
    }

    public void setDayTimes(String dayTimes) {
        this.dayTimes = dayTimes == null ? null : Arrays.asList(dayTimes == null ? null : dayTimes.trim().split(" ")).stream().sorted().collect(Collectors.toList());
    }

    public List<String> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(String weekdays) {
        this.weekdays = weekdays == null ? null : Arrays.asList(weekdays == null ? null : weekdays.trim().split(" ")).stream().sorted().collect(Collectors.toList());
    }

    public List<String> getMonthDays() {
        return monthDays;
    }

    public void setMonthDays(String monthDays) {
        this.monthDays = monthDays == null ? null : Arrays.asList(monthDays == null ? null : monthDays.trim().split(" ")).stream().sorted().collect(Collectors.toList());
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public int getTimeIntervelOfInt() {
        return Integer.valueOf(timeInterval);
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval == null ? null : timeInterval.trim();
    }

    @Override
    public String toString() {
        return "DateConfigure{" +
                "timeStrategy=" + timeStrategy +
                ", startTime='" + startTime + '\'' +
                ", dayTimes=" + dayTimes +
                ", weekdays=" + weekdays +
                ", monthDays=" + monthDays +
                ", timeInterval='" + timeInterval + '\'' +
                '}';
    }
}