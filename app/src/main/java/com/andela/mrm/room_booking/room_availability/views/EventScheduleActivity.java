package com.andela.mrm.room_booking.room_availability.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andela.mrm.R;
import com.andela.mrm.adapter.EventScheduleAdapter;
import com.andela.mrm.room_booking.room_availability.models.CalendarEvent;
import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Event schedule activity.
 */
public class EventScheduleActivity extends Activity {

    /**
     * The on Create method.
     * @param savedInstanceState the savedInstance.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_schedule);
        String currentDate = SimpleDateFormat.getDateInstance().format(new Date());
        TextView upcomingEventsView = findViewById(R.id.text_upcoming_events);
        TextView dataView = findViewById(R.id.text_current_date);
        dataView.setText(currentDate); // display current system date
        String eventsInStringFromIntent = getIntent()
                .getStringExtra(RoomAvailabilityActivity.EVENTS_IN_STRING);
        LinearLayout closeSchedule = findViewById(R.id.layout_close_event_schedule);
        if (closeSchedule != null) {
            closeSchedule.setOnClickListener(v -> finish());
        }
        if (eventsInStringFromIntent != null) {
            Type listType = new TypeToken<List<CalendarEvent>>() { }.getType();
            List<CalendarEvent> events = new Gson().fromJson(eventsInStringFromIntent, listType);
            displayUpcomingEventsView(events, upcomingEventsView);
            EventScheduleAdapter eventScheduleAdapter =
                    new EventScheduleAdapter(addAvailableTimeSlots(events),
                            EventScheduleActivity.this);
            RecyclerView eventScheduleRecyclerView = findViewById(R.id.layout_event_recycler_view);
            eventScheduleRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager eventScheduleLayoutManager =
                    new LinearLayoutManager(this,
                            LinearLayoutManager.HORIZONTAL, false);
            eventScheduleRecyclerView.setLayoutManager(eventScheduleLayoutManager);
            eventScheduleRecyclerView.setAdapter(eventScheduleAdapter);
        }
    }

    /**
     * Add available time slots list.
     *
     * @param calendarEvents the calendar events
     * @return the list
     */
    public List<CalendarEvent> addAvailableTimeSlots(List<CalendarEvent> calendarEvents) {
        List<CalendarEvent> eventListWithAvailableTimeSlots = new ArrayList<>();
        if (calendarEvents.isEmpty()) {
            return getCalendarEvents(eventListWithAvailableTimeSlots);
        } else {
            int size = calendarEvents.size();
            int i;
            for (i = 0; i < (size - 1); i++) {
                eventListWithAvailableTimeSlots.add(calendarEvents.get(i));
                if ((calendarEvents.get(i + 1).getStartTime() - calendarEvents.get(i).getEndTime())
                        > 60000) {
                    eventListWithAvailableTimeSlots.add(new CalendarEvent("Available",
                            calendarEvents.get(i).getEndTime(),
                            calendarEvents.get(i + 1).getStartTime(), null,
                            null));
                }
            }
            int lastEventPosition = calendarEvents.size() - 1; // add the last event for the day
            eventListWithAvailableTimeSlots.add(
                    new CalendarEvent(calendarEvents.get(lastEventPosition).getSummary(),
                            calendarEvents.get(lastEventPosition).getStartTime(),
                            calendarEvents.get(lastEventPosition).getEndTime(),
                            calendarEvents.get(lastEventPosition).getAttendees(),
                            calendarEvents.get(lastEventPosition).getCreator())
            );
//             Add an extra free event
            return addExtraFreeEvent(eventListWithAvailableTimeSlots);
        }

    }

    /**
     * Add an extra free event for the end of the calendar events.
     * @param eventListWithAvailableTimeSlots List containing all the day's events
     * @return new calendar with extra event at the end
     */
    @NonNull
    public List<CalendarEvent> addExtraFreeEvent(List<CalendarEvent>
                                                             eventListWithAvailableTimeSlots) {
        eventListWithAvailableTimeSlots.add(
                new CalendarEvent("Available",
                        eventListWithAvailableTimeSlots.
                                get(eventListWithAvailableTimeSlots.size() - 1).getEndTime(),
                        null, null, null));
        return eventListWithAvailableTimeSlots;
    }

    /**
     * Extracted Method to Populate an empty Calendar.
     * @param eventListWithAvailableTimeSlots list containing all created events
     * @return populated list with the
     */
    @NonNull
    public List<CalendarEvent> getCalendarEvents(
            List<CalendarEvent> eventListWithAvailableTimeSlots) {
        eventListWithAvailableTimeSlots.add(new CalendarEvent("Available",
                    new DateTime(System.currentTimeMillis()).getValue(),
                null, null,
                null));
        return eventListWithAvailableTimeSlots;
    }

    /**
     * Displays a view detailing the number of upcoming events.
     *
     * @param calendarEvents - list of calender events for a day
     * @param view - text view containing upcoming events count
     */
    private void displayUpcomingEventsView(List<CalendarEvent> calendarEvents, TextView view) {
        int numberOfUpComingEvents = calendarEvents.size();
        String upcomingEventsDisplayText = "Upcoming Events: " + numberOfUpComingEvents;

        view.setText(upcomingEventsDisplayText);
    }

}
