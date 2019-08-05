package com.wbu.xiaowei.amyschedule.util;

import java.util.Calendar;

/**
 * @author XIAOWEI
 * @version 1.0
 */
public class TimeUtil {
    /**
     * 计算 startTime 与 endTime 相差的天数数
     *
     * @param startTime 时间 1
     * @param endTime   时间 2
     * @return 相差天数
     */
    public static int calcDayOffset(long startTime, long endTime) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(startTime);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(endTime);

        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);

        if (year1 != year2) { // 不同年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { // 闰年
                    timeDistance += 366;
                } else { // 不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else { // 同一年
            return day2 - day1;
        }
    }

    /**
     * 计算 startTime 与 endTime 相差的周数
     *
     * @param startTime 时间 1
     * @param endTime   时间 2
     * @return 相差周数
     */
    public static int calcWeekOffset(long startTime, long endTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTime);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }

        int dayOffset = calcDayOffset(startTime, endTime);
        int weekOffset = dayOffset / 7;

        int something;
        if (dayOffset > 0) {
            something = (dayOffset % 7 + dayOfWeek > 7) ? 1 : 0;
        } else {
            something = (dayOfWeek + dayOffset % 7 < 1) ? -1 : 0;
        }

        return weekOffset + something;
    }
}
