package com.nd.auxo.recommend.core.util;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/22.
 */
public class DateUtils {

    /**
     * 取结束时间
     *
     * @param amount
     * @return
     */
    public static Date getDayEndTime(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.add(Calendar.DATE, amount);
        return calendar.getTime();
    }

    /**
     * 取当天结束时间
     *
     * @return
     */
    public static Date getTodayEndTime() {
        return getDayEndTime(0);
    }

    /**
     * 取时间差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long timeDiff(Date date1, Date date2) {
        return Math.abs(date1.getTime() - date2.getTime());
    }
}
