package com.andela.mrm.data;

import com.andela.mrm.room_availability.FreeBusy;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.TimePeriod;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.verify;

public class CalendarDataRepositoryTest {

    private CalendarDataRepository mDataRepository;
    @Mock
    private Calendar mCalendar;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mDataRepository = new CalendarDataRepository(mCalendar);
    }


    @Test
    public void getCalendarFreeBusySchedule() {
        FreeBusyRequest request = CalendarDataRepository.buildRoomFreeBusyRequest("", null, null);
        Flowable<List<FreeBusy>> schedule = mDataRepository.getRoomFreeBusySchedule(request);
        schedule.subscribe(new TestSubscriber<>());

        verify(mCalendar).freebusy();


    }
}