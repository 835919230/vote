package com.hc.util;

import java.util.Date;

/**
 * Created by hexi on 16-10-3.
 */
public class TimeUtils {
    public static long getSecond() {
        return 1000L;
    }

    public static long getSeconds(long s) {
        return s * getSecond();
    }

    public static long getMinute() {
        return getSecond()*60L;
    }

    public static long getMinutes(long m) {
        return getMinute() * m;
    }

    public static long getHour() {
        return getMinute()*60L;
    }

    public static long getHours(long h) {
        return getHour() * h;
    }

    public static long getDay() {
        return getHour()*24;
    }

    public static long getDays(long d) {
        return getDay() * d;
    }

    public static Date afterDay(long day) {
        Date now = new Date();
        return new Date(now.getTime() + getDays(day));
    }
}
