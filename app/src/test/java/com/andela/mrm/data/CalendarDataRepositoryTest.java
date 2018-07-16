package com.andela.mrm.data;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.TimePeriod;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.*;
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
        FreeBusyRequest request = CalendarDataRepository.buildFreeBusyRequest("", null, null);
        Flowable<List<TimePeriod>> schedule = mDataRepository.getCalendarFreeBusySchedule(request);
        schedule.subscribe(new TestSubscriber<>());

        verify(mCalendar).freebusy();


    }
}