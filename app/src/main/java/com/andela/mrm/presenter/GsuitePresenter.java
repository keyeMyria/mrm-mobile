package com.andela.mrm.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * The type Gsuite presenter.
 */
public class GsuitePresenter extends AsyncTask<Void, Void, FreeBusyResponse> {

    private static final String APPLICATION_NAME = "Google Admin SDK Directory API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = "credentials";
    /**
     * The constant cognitioMeetingRoomId.
     */
    public String
        cognitioMeetingRoomId = "andela.com_3339303430303931393530@resource.calendar.google.com",
    /**
     * The Eko meeting room id.
     */
    ekoMeetingRoomId = "andela.com_2d3630303638323533363632@resource.calendar.google.com";

    private final Calendar mservice;
    private Exception mLastError;

    private final GoogleAccountCredential credential;

    /**
     * Instantiates a new Gsuite presenter.
     *
     * @param credential the credential
     */
    public GsuitePresenter(GoogleAccountCredential credential) {
        this.credential = credential;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        mservice = new Calendar.Builder(
                transport, JSON_FACTORY, credential)
                .setApplicationName("Google GSuite API Android")
                .build();

    }


    @Override
    protected FreeBusyResponse doInBackground(Void... voids) {
        try {
            return getData();
        } catch (Exception error) {
            mLastError = error;
            return null;
        }
    }

    /**
     *
     * @return freebusyResponse.
     * @throws IOException IOException.
     */
    private FreeBusyResponse getData() throws IOException {
        FreeBusyRequest request = new FreeBusyRequest();
        List<FreeBusyRequestItem> requestItems = new ArrayList<>();
        DateTime now = new DateTime(System.currentTimeMillis());

        requestItems.add(new FreeBusyRequestItem().setId(cognitioMeetingRoomId));
        requestItems.add(new FreeBusyRequestItem().setId(ekoMeetingRoomId));

        java.util.Calendar calendar = new GregorianCalendar();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
        calendar.set(java.util.Calendar.MINUTE, 59);
        calendar.set(java.util.Calendar.SECOND, 59);
        Date midnight = calendar.getTime();

        request.setTimeMin(now)
                .setTimeMax(new DateTime(midnight.getTime()))
                .setTimeZone(TimeZone.getDefault().getID())
                .setItems(requestItems);

        Calendar.Freebusy.Query query = mservice.freebusy().query(request);
        query.setFields("calendars");
        FreeBusyResponse freeBusyResponse = query.execute();
        Log.e("FreeBusyyy", freeBusyResponse + "");
        return freeBusyResponse;
    }
}
