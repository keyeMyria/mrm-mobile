package com.andela.mrm.room_availability;

import android.os.AsyncTask;
import android.util.Log;

import com.andela.mrm.room_events.CalendarEvent;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class MakeGoogleCalendarCallPresenter extends AsyncTask<Void, Void, List<Event>> {
    private final com.google.api.services.calendar.Calendar mService;
    private final IGoogleCalenderCallListener googleCalenderCallListener;
    private Exception mLastError = null;

    /**
     * Instantiates a new Make request task.
     *
     * @param credential              the credential
     * @param makeRequestTaskListener the make request task listener
     */
    public MakeGoogleCalendarCallPresenter(GoogleAccountCredential credential,
                                           IGoogleCalenderCallListener makeRequestTaskListener) {
        this.googleCalenderCallListener = makeRequestTaskListener;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
    }

    /**
     * Background task to call Google Calendar API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<Event> doInBackground(Void... params) {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     *
     * @return List of Strings describing returned events.
     * @throws IOException ioException
     */
    private List<Event> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        List<Event> items;
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        DateTime now = new DateTime(System.currentTimeMillis());
        /**
         * This part is to get the midnight of current day.
         */
        Date midnight = getMidnight(); //Gets midnight of the current day
        items = getEvents(now, midnight);
        populateCalendar(items, calendarEvents);

        String listItemInGson = new Gson().toJson(calendarEvents);
        if (googleCalenderCallListener != null) {
            googleCalenderCallListener.onSuccess(listItemInGson);
        }
        return items;
    }

    /**
     * Method to populate the calendar entries for the day.
     *
     * @param items          list to store all the days events
     * @param calendarEvents list to store all calendar events for the day
     */
    public void populateCalendar(List<Event> items, List<CalendarEvent> calendarEvents) {
        List<EventAttendee> attendees;
        for (Event e : items) {
            DateTime start = e.getStart().getDateTime();
            DateTime end = e.getEnd().getDateTime();
            attendees = e.getAttendees();
            String creator = e.getCreator().getEmail();

            if (start == null) {
                start = e.getStart().getDate();
            }

            if (end == null) {
                end = e.getEnd().getDate();
            }
            calendarEvents.add(new CalendarEvent(e.getSummary(),
                    start.getValue(),
                    end.getValue(), attendees, creator));
        }
    }

    /**
     * Method to list all the events for the current day.
     *
     * @param now      DateTime value for the current point in time
     * @param midnight DateTime value for midnight of the current day
     * @return event Items of the day's events
     * @throws IOException Exception Handler
     */
    public List<Event> getEvents(DateTime now, Date midnight) throws IOException {
        List<Event> items;
        Events events = mService.events().list("primary")
                .setTimeMin(now)
                .setTimeMax(new DateTime(midnight.getTime())) // We need the midnight here
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        items = events.getItems();
        return items;
    }

    /**
     * Extracted Method to cater to the getting midnight of the current day.
     *
     * @return midnight of the current day
     */
    public Date getMidnight() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }


    @Override
    protected void onPreExecute() {
        Log.v("Loading", "Loading now...");
    }

    @Override
    protected void onPostExecute(List<Event> output) {
        if (output == null || output.isEmpty()) {
            Log.v("No results", "No results returned.");
        } else {
            for (int i = 0; i < output.size(); i++) {
                Log.v("output", "" + output.get(i).getId());
            }
        }
    }

    @Override
    protected void onCancelled() {
        if (googleCalenderCallListener != null) {
            googleCalenderCallListener.onCancelled(mLastError);
        }
    }
}
