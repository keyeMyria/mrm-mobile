package com.andela.mrm.data;

import android.support.annotation.NonNull;

import com.andela.mrm.room_availability.FreeBusy;
import com.andela.mrm.room_events.CalendarEvent;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.FreeBusyCalendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

import static com.andela.mrm.room_availability.FreeBusy.Status.BUSY;
import static com.andela.mrm.room_availability.FreeBusy.Status.BUSY_CURRENT;
import static com.andela.mrm.room_availability.FreeBusy.Status.FREE;
import static com.andela.mrm.util.DateTimeUtils.getCurrentTime;
import static com.andela.mrm.util.DateTimeUtils.getDuration;

/**
 * Calendar Data Repository class.
 */
public class CalendarDataRepository implements ICalendarDataRepository {
    private static final String CALENDAR_QUERY_FIELD_ONLY = "calendars";
    private final Calendar mCalendar;

    /**
     * Instantiates a new Calendar data repository.
     *
     * @param calendar the calendar
     */
    public CalendarDataRepository(Calendar calendar) {
        mCalendar = calendar;
    }

    /**
     * Builds free busy request for a single calendar id.
     *
     * @param calendarId the calendar id
     * @param startTime  the start time
     * @param endTime    the end time
     * @return the free busy request
     */
// TODO: move this to the presenter
    @NonNull
    public static FreeBusyRequest buildRoomFreeBusyRequest(String calendarId,
                                                           DateTime startTime, DateTime endTime) {
        FreeBusyRequest request = new FreeBusyRequest();

        List<FreeBusyRequestItem> requestItems = new ArrayList<>();
        FreeBusyRequestItem requestItem = new FreeBusyRequestItem().setId(calendarId);
        requestItems.add(requestItem);

        request.setItems(requestItems);
        request.setTimeMin(startTime);
        request.setTimeMax(endTime);

        return request;
    }

    /**
     * Create free busy list.
     *
     * @param timePeriods the time periods
     * @param startTime   the start time
     * @param endTime     the end time
     * @return the list
     */
    @NonNull
    private static List<FreeBusy> createFreeBusyList(List<TimePeriod> timePeriods,
                                                     DateTime startTime, DateTime endTime) {
        List<FreeBusy> freeBusyList = new ArrayList<>();
        DateTime startNextFreePeriod = startTime;
        for (int i = 0; i < timePeriods.size(); i++) {
            TimePeriod period = timePeriods.get(i);
            DateTime periodStart = period.getStart();
            DateTime periodEnd = period.getEnd();
            FreeBusy free = new FreeBusy(getDuration(startNextFreePeriod, periodStart), FREE);
            FreeBusy busy;
            long currentTime = getCurrentTime().getValue();
            if (periodStart.getValue() <= currentTime && currentTime <= periodEnd.getValue()) {
                busy = new FreeBusy(getDuration(periodStart, periodEnd), BUSY_CURRENT);
            } else {
                busy = new FreeBusy(getDuration(periodStart, periodEnd), BUSY);
            }
            freeBusyList.add(free);
            freeBusyList.add(busy);
            if (i == timePeriods.size() - 1) {
                DateTime lastBusyEndTime = timePeriods.get(i).getEnd();
                FreeBusy lastFreeBusy = new FreeBusy(
                        getDuration(lastBusyEndTime, endTime), FREE);
                freeBusyList.add(lastFreeBusy);
            }
            startNextFreePeriod = periodEnd;
        }
        return freeBusyList;
    }

    /**
     * Method to populate the calendar entries for the day.
     *
     * @param items list to store all the days events
     * @return the list
     */
    public static List<CalendarEvent> createCalendarEvents(List<Event> items) {
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        List<EventAttendee> attendees;
        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            DateTime end = event.getEnd().getDateTime();
            attendees = event.getAttendees();
            String creator = event.getCreator().getEmail();

            if (start == null) {
                start = event.getStart().getDate();
            }

            if (end == null) {
                end = event.getEnd().getDate();
            }
            calendarEvents.add(new CalendarEvent(event.getSummary(),
                    start.getValue(),
                    end.getValue(), attendees, creator));
        }
        return calendarEvents;
    }

    /**
     * Gets the schedule for a {@link FreeBusyRequest}.
     *
     * @param request FreeBusyRequest object
     * @return List of {@link TimePeriod}
     * @throws IOException - Exception
     */
    private List<FreeBusy> getSchedule(FreeBusyRequest request)
            throws IOException {
        String calendarId = request.getItems().get(0).getId();
        FreeBusyResponse response = mCalendar.freebusy()
                .query(request)
                .setFields(CALENDAR_QUERY_FIELD_ONLY)
                .execute();
        Map<String, FreeBusyCalendar> calendars = response.getCalendars();
        List<TimePeriod> timePeriods = calendars
                .get(calendarId)
                .getBusy();
        return createFreeBusyList(timePeriods, request.getTimeMin(), request.getTimeMax());
    }

    /**
     * Method to list all the events for the current day.
     *
     * @param startTime      DateTime value for the current point in time
     * @param endTime DateTime value for endTime of the current day
     * @return event Items of the day's events
     * @throws IOException Exception Handler
     */
    private List<CalendarEvent> getEvents(String calendarId, DateTime startTime, DateTime endTime)
            throws IOException {
        List<Event> items;
        Events events = mCalendar.events().list(calendarId)
                .setTimeMin(startTime)
                .setTimeMax(endTime)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        items = events.getItems();
        return createCalendarEvents(items);
    }


    @Override
    public Flowable<List<FreeBusy>> getRoomFreeBusySchedule(FreeBusyRequest request) {
        return Flowable.fromCallable(() -> getSchedule(request));
    }

    @Override
    public Flowable<List<CalendarEvent>> getRoomCalendarEvents(String roomCalendarId,
                                                               DateTime startTime, DateTime endTime) {
        return Flowable.fromCallable(() -> getEvents(roomCalendarId, startTime, endTime));
    }
}
