package com.andela.mrm.room_setup;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.andela.mrm.R;
import com.andela.mrm.room_events.CalendarEvent;
import com.andela.mrm.room_events.EventScheduleActivity;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * The type Event schedule activity test.
 */
public class EventScheduleActivityTest {

    /**
     * The Activity test rule.
     */
    @Rule
    public ActivityTestRule<EventScheduleActivity> activityTestRule =
            new ActivityTestRule<>(EventScheduleActivity.class);

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


}
