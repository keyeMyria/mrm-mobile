package com.andela.mrm.room_availability;

/**
 * Schedule TimeLine Stripe Model class.
 */
public class TimeLine {
    private final int meetingDuration;
    private final int timeBarColor;
    private final boolean isAvailable;

    /**
     * Creates object instance of the TimeLine class.
     *
     * @param meetingDuration - time duration for event
     * @param timeBarColor - color for rectangular time bar
     * @param isAvailable - specifies if a period of time is available or booked
     */
    public TimeLine(int meetingDuration, int timeBarColor, boolean isAvailable) {
        this.meetingDuration = meetingDuration;
        this.timeBarColor = timeBarColor;
        this.isAvailable = isAvailable;
    }

    /**
     * Meeting duration in minutes.
     *
     * @return - length(time) of event in minutes
     */
    public int getMeetingDuration() {
        return meetingDuration;
    }

    /**
     * Time bar color.
     *
     * @return - color of custom view bar
     */
    public int getTimeBarColor() {
        return timeBarColor;
    }

    /**
     * Meeting room availability.
     *
     * @return - boolean value indicating availability of meeting room
     */
    public boolean isAvailable() {
        return isAvailable;
    }
}
