package com.online.exams.system.webapp.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by quxiao on 2015/5/24.
 */
public class DateTimeUtil {

    public static Date getComingTime(Integer delta, TimeUnit unit) {
        Calendar cal = Calendar.getInstance();

        long base = cal.getTimeInMillis();
        long d = unit.toMillis(delta);

        cal.setTimeInMillis(base + d);

        return cal.getTime();
    }
}
