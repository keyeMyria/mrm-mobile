package com.andela.mrm.data;

import com.andela.mrm.room_availability.FreeBusy;
import com.andela.mrm.room_events.CalendarEvent;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.FreeBusyRequest;

import java.util.List;

import io.reactivex.Flowable;

public interface ICalendarDataRepository {
    Flowable<List<FreeBusy>> getRoomFreeBusySchedule(FreeBusyRequest request);

    Flowable<List<CalendarEvent>> getRoomCalendarEvents(String roomCalendarId,
                                                        DateTime startTime, DateTime endTime);
}
