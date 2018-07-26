package com.andela.mrm.util;

import com.google.api.client.util.DateTime;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Util functions for working with {@link DateTime}.
 */
public final class DateTimeUtils {
    /**
     * Instantiates a new Date time utils.
     */
    private DateTimeUtils() {
        // Utility class
    }

    /**
     * Creates a new {@link DateTime} with the set of params passed in.
     *
     * @param hour    the hour
     * @param minute  the minute
     * @param seconds the seconds
     * @return {@link DateTime}
     */
    public static DateTime getTime(int hour, int minute, int seconds) {
        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);

        return new DateTime(calendar.getTime());
    }


    /**
     * Computes the duration from {@link DateTime} objects.
     *
     * @param start the start
     * @param end   the end
     * @return the duration in minutes
     */
    public static int getDuration(DateTime start, DateTime end) {
        return (int) Math.abs((end.getValue() - start.getValue()) / 60000);
    }

    /**
     * Gets the hour for a {@link DateTime}.
     *
     * @param time the time
     * @return the hour
     */
    public static int getHour(DateTime time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getValue());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Gets current time.
     *
     * @return the current time
     */
    public static DateTime getCurrentTime() {
        return new DateTime(System.currentTimeMillis());
    }
}
