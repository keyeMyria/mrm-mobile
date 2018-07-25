package com.andela.mrm.room_setup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.TextView;

import com.andela.mrm.R;
import com.andela.mrm.room_availability.MakeGoogleCalendarCallPresenter;
import com.andela.mrm.room_availability.RoomAvailabilityActivity;
import com.andela.mrm.room_events.CalendarEvent;
import com.andela.mrm.room_events.EventScheduleActivity;
import com.andela.mrm.room_events.EventScheduleAdapter;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.model.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

/**
 * The type Event schedule activity test.
 */
public class EventScheduleActivityTest {

    /**
     * The Activity test rule.
     */
    @Rule
    public ActivityTestRule<EventScheduleActivity> activityTestRule =
            new ActivityTestRule<>(EventScheduleActivity.class,
                    true, false);

    @Before
    public void setup() throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("dummy_calendar_events.json");
        Collection<Event> eventCollection = JacksonFactory
                .getDefaultInstance()
                .createJsonParser(inputStream)
                .parseArray(Event[].class, Event.class);
        ArrayList<Event> eventArrayList = new ArrayList<>(eventCollection);

        List<CalendarEvent> calendarEvents = MakeGoogleCalendarCallPresenter
                .populateCalendar(eventArrayList);

        String eventsToJson = new Gson().toJson(calendarEvents);

        Intent intent = new Intent();
        intent.putExtra(RoomAvailabilityActivity.EVENTS_IN_STRING, eventsToJson);
        activityTestRule.launchActivity(intent);
    }

    /**
     * Close button displayed.
     */
    @Test
    public void closeButtonDisplayed() {
        onView(withId(R.id.layout_close_event_schedule))
                .check(matches(allOf(isDisplayed(), isClickable(), hasChildCount(1))));
    }

    /**
     * Today text displayed.
     */
    @Test
    public void todayTextDisplayed() {
        onView(withId(R.id.text_current_date))
                .check(matches(isDisplayed()));
    }

    /**
     * Recycler view displayed.
     */
    @Test
    public void recyclerViewDisplayed() {
        View view = activityTestRule.getActivity().findViewById(R.id.layout_event_recycler_view);
        assertNotNull(view);
    }

    /**
     * Close button finishes activity.
     */
    @Test
    public void closeButtonFinishesActivity() {
        onView(withId(R.id.layout_close_event_schedule))
                .perform((click()));
        assertTrue(activityTestRule.getActivity().isDestroyed());
    }

    /**
     * Add available time slots should work correctly.
     */
    @Test
    public void addAvailableTimeSlotsShouldWorkCorrectly() {
        List<CalendarEvent> calendarEventList = new ArrayList<CalendarEvent>();
        assertTrue(activityTestRule.getActivity()
                .addAvailableTimeSlots(calendarEventList) != null);

        calendarEventList.add(new CalendarEvent("todaymeeting", null,
                null, null, null));
        assertTrue(activityTestRule.getActivity()
                .addAvailableTimeSlots(calendarEventList).size() == 2);

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Date startTime = calendar.getTime();
        CalendarEvent calendarEventOne = new CalendarEvent("EventOne", startTime.getTime(),
                startTime.getTime() + 1800000, null, null);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 00);
        Date eventTwo = calendar.getTime();
        CalendarEvent calendarEventTwo = new CalendarEvent("EventOne", eventTwo.getTime(),
                eventTwo.getTime() + 1800000, null, null);

        List<CalendarEvent> availableEvent = new ArrayList<CalendarEvent>();
        availableEvent.add(calendarEventOne);
        availableEvent.add(calendarEventTwo);
        assertTrue(activityTestRule.getActivity()
                .addAvailableTimeSlots(availableEvent).size() == 4);
    }

    /**
     * DisplayUpcoming events view sets text properly
     */
    @Test
    public void displayUpcomingEventsViewSetsText() throws Throwable {
        List<CalendarEvent> upcomingEvents = new ArrayList<>();
        int blankEvents = upcomingEvents.size();
        assertEquals(blankEvents, 0);

        TextView upComing = activityTestRule.getActivity()
                .findViewById(R.id.text_upcoming_events);

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Date startTime = calendar.getTime();
        CalendarEvent eventA = new CalendarEvent("Event A", startTime.getTime(),
                startTime.getTime() + 1800000, null, null);


        upcomingEvents.add(eventA);

        activityTestRule.runOnUiThread(() -> activityTestRule.getActivity()
                .displayUpcomingEventsView(upcomingEvents, upComing));

        onView(withId(R.id.text_upcoming_events))
                .check(matches(withText("Upcoming Events: 1")));
    }


    /**
     * Test if Event Schedule Adapter works well
     */
    @Test
    public void eventScheduleAdapterWorksWell() {

        onView(ViewMatchers.withId(R.id.layout_event_recycler_view))
                .check(matches(isDisplayed()));

        onView(withId(R.id.layout_event_recycler_view))
                .check(matches(allOf(isDisplayed(), hasChildCount(4))));

    }
}


