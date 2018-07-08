package com.andela.mrm.room_events;


import android.support.annotation.Nullable;

import com.google.api.services.calendar.model.EventAttendee;

import java.util.List;

/**
 * Created by andeladeveloper on 01/05/2018.
 */
public class CalendarEvent {

    private final String summary;
    private final Long startTime;
    private final Long endTime;

    private final String creator;


    private final List<EventAttendee> attendees;

//    private final int noOfAttendees;

    /**
     * Instantiates a new Calendar event.
     *
     * @param summary   the summary
     * @param startTime the start time
     * @param endTime   the end time
     * @param attendees the attendees
     * @param creator   the creator
     */
    public CalendarEvent(String summary, @Nullable Long startTime,
                         @Nullable Long endTime,
                         @Nullable List<EventAttendee> attendees, @Nullable String creator) {
        this.summary = summary;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attendees = attendees;
        this.creator = creator;
    }

    /**
     * Gets attendees.
     *
     * @return the attendees
     */
    public List<EventAttendee> getAttendees() {
        return attendees;
    }

    /**
     * Gets summary.
     *
     * @return the summary
     */
    public String getSummary() {
        return this.summary;
    }

    /**
     * Gets creator.
     *
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public Long getStartTime() {
        return this.startTime;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public Long getEndTime() {
        return endTime;
    }
}
