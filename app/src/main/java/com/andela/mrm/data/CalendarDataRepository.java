package com.andela.mrm.data;

import android.support.annotation.NonNull;

import com.andela.mrm.room_availability.FreeBusy;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.FreeBusyCalendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import java.io.IOException;
import java.util.ArrayList;
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
public class CalendarDataRepository {
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
    public static FreeBusyRequest buildFreeBusyRequest(String calendarId,
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
    public static List<FreeBusy> createFreeBusyList(List<TimePeriod> timePeriods,
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
     * Gets the schedule for a {@link FreeBusyRequest}.
     *
     * @param request FreeBusyRequest object
     * @return List of {@link TimePeriod}
     * @throws IOException - Exception
     */
    private List<TimePeriod> getSchedule(FreeBusyRequest request)
            throws IOException {
        String calendarId = request.getItems().get(0).getId();
        FreeBusyResponse response = mCalendar.freebusy()
                .query(request)
                .setFields(CALENDAR_QUERY_FIELD_ONLY)
                .execute();
        Map<String, FreeBusyCalendar> calendars = response.getCalendars();
        return calendars
                .get(calendarId)
                .getBusy();
    }

    /**
     * Gets calendar free busy schedule.
     *
     * @param request the request
     * @return the calendar free busy schedule
     */
    public Flowable<List<TimePeriod>> getCalendarFreeBusySchedule(FreeBusyRequest request) {
        return Flowable.fromCallable(() -> getSchedule(request));
    }
}
