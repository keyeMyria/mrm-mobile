package com.andela.mrm.room_events;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andela.mrm.Injection;
import com.andela.mrm.R;
import com.andela.mrm.data.CalendarDataRepository;
import com.andela.mrm.service.ApiService;
import com.andela.mrm.util.DateTimeUtils;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * The type Event schedule activity.
 */
public class EventScheduleActivity extends Activity {

    /**
     * Prepares the intent required to launch this activity.
     *
     * @param packageContext the package context
     * @return the intent
     */
    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, EventScheduleActivity.class);
    }

    private EventScheduleAdapter mEventScheduleAdapter;

    @BindView(R.id.text_upcoming_events)
    TextView mTextUpcomingEventsCount;
    @BindView(R.id.text_current_date)
    TextView mCurrentDateText;
    @BindView(R.id.layout_close_event_schedule)
    LinearLayout mCloseActivityButton;
    @BindView(R.id.layout_event_recycler_view)
    RecyclerView mEventsRecyclerView;

    @OnClick(R.id.layout_close_event_schedule)
    void closeActivity() {
        finish();
    }

    private Disposable mDisposable;

    /**
     * The on Create method.
     *
     * @param savedInstanceState the savedInstance.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_schedule);

        ButterKnife.bind(this);
        setCurrentDateText();
        setUpRecyclerView();

        final Account account = Injection
                .provideGoogleSignInWrapperUtil(this).getSignedInAccount().getAccount();
        final Calendar calendarService = ApiService.getCalendarService(account, this);

        mDisposable =  new CalendarDataRepository(calendarService)
                .getRoomCalendarEvents("primary",
                        new DateTime(System.currentTimeMillis()),
                        DateTimeUtils.getTime(20, 0, 0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::addAvailableTimeSlots, (error) ->
                        Log.e(getClass().getSimpleName(), "Failed", error));
    }

    private void setCurrentDateText() {
        String currentDate = SimpleDateFormat.getDateInstance().format(new Date());
        mCurrentDateText.setText(currentDate);
    }

    private void setUpRecyclerView() {
        mEventScheduleAdapter = new EventScheduleAdapter(new ArrayList<>());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        mEventsRecyclerView.setLayoutManager(layoutManager);
        mEventsRecyclerView.setAdapter(mEventScheduleAdapter);
    }

    /**
     * Add available time slots list.
     *
     * @param calendarEvents the calendar events
     * @return the list
     */
    public List<CalendarEvent> addAvailableTimeSlots(List<CalendarEvent> calendarEvents) {
        displayUpcomingEventsView(calendarEvents);
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
     *
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
     *
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
     */
    private void displayUpcomingEventsView(List<CalendarEvent> calendarEvents) {
        int numberOfUpComingEvents = calendarEvents.size();
        String upcomingEventsDisplayText = "Upcoming Events: " + numberOfUpComingEvents;

        mTextUpcomingEventsCount.setText(upcomingEventsDisplayText);
    }

}
