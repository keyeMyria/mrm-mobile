package com.andela.mrm.room_availability;

/**
 * Schedule TimeLine Stripe Model class.
 */
public class TimeLine {
    private final int meetingDuration;
    private final boolean mIsBusy;

    /**
     * Creates object instance of the TimeLine class.
     *
     * @param meetingDuration - time duration for event
     * @param isBusy - specifies if a period of time is available or booked
     */
    public TimeLine(int meetingDuration, boolean isBusy) {
        this.meetingDuration = meetingDuration;
        this.mIsBusy = isBusy;
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
     * Meeting room availability.
     *
     * @return - boolean value indicating availability of meeting room
     */
    public boolean isBusy() {
        return mIsBusy;
    }

    @Override
    public String toString() {
        return "TimeLine{" +
                "meetingDuration=" + meetingDuration +
                ", mIsBusy=" + mIsBusy +
                '}';
    }
}
