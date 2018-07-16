package com.andela.mrm.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTime {
    /**
     * .
     *
     * @return w
     */
    public static com.google.api.client.util.DateTime getTime(int hour, int minute, int seconds) {
        java.util.Calendar calendar = new GregorianCalendar();

        // TODO: remove the month and day; this should use the current day
        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 13);

        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour);
        calendar.set(java.util.Calendar.MINUTE, minute);
        calendar.set(java.util.Calendar.SECOND, seconds);

        // TODO: probably pass in the timezone dynamically;
        calendar.setTimeZone(TimeZone.getDefault());

        return new com.google.api.client.util.DateTime(calendar.getTime());
    }

    public static int getDuration(long start, long end) {
        return (int) ((end - start) / 60000);
    }
}
