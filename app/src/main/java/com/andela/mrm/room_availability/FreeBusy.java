package com.andela.mrm.room_availability;

/**
 * FreeBusy Model class.
 */
public class FreeBusy {
    private final int mDuration;
    private final Status mStatus;


    /**
     * Instantiates a new FreeBusy.
     *
     * @param mDuration the m duration
     * @param status    the status
     */
    public FreeBusy(int mDuration, Status status) {
        this.mDuration = mDuration;
        mStatus = status;
    }

    /**
     * Duration in minutes.
     *
     * @return - length(time) of event in minutes
     */
    public int getDuration() {
        return mDuration;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Status getStatus() {
        return mStatus;
    }

    @Override
    public String toString() {
        return "FreeBusy{"
                + "mDuration=" + mDuration
                + ", mStatus=" + mStatus
                + '}';
    }


    /**
     * The enum Status.
     */
    public enum Status {
        /**
         * Busy status.
         */
        BUSY,
        /**
         * Busy current status.
         */
        BUSY_CURRENT,
        /**
         * Busy current checked in status.
         */
        BUSY_CURRENT_CHECKED_IN,
        /**
         * Free status.
         */
        FREE
    }
}
