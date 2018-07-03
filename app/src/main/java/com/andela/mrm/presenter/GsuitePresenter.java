package com.andela.mrm.presenter;

/**
 * Created by Fred Adewole on 20/06/2018.
 */
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.andela.mrm.GetAllRoomsInALocationQuery;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.FreeBusyCalendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;


/**
 * The type Gsuite presenter.
 */
public class GsuitePresenter extends AsyncTask<Void, Void, List<String>> {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final Calendar mservice;
    private final GoogleAccountCredential credential;
    private IOnGsuitePresenterResponse iOnGsuitePresenterResponse;
    private List<String> listOfResourceCalendarIds;
    private List<GetAllRoomsInALocationQuery.Room> listOfRooms;

    /**
     * Instantiates a new Gsuite presenter.
     *
     * @param credential                 the credential
     * @param iOnGsuitePresenterResponse the on gsuite presenter response
     * @param listOfResourceCalendarIds  the list of resource calendar ids
     * @param listOfRooms                the list of rooms
     */
    public GsuitePresenter(GoogleAccountCredential credential,
                           @Nullable IOnGsuitePresenterResponse iOnGsuitePresenterResponse,
                           @Nullable List<String> listOfResourceCalendarIds,
                           @Nullable List<GetAllRoomsInALocationQuery.Room> listOfRooms) {
        this.credential = credential;
        if (iOnGsuitePresenterResponse != null) {
            this.iOnGsuitePresenterResponse = iOnGsuitePresenterResponse;
        }
        if (listOfResourceCalendarIds != null) {
            this.listOfResourceCalendarIds = listOfResourceCalendarIds;
        }
        if (listOfRooms != null) {
            this.listOfRooms = listOfRooms;
        }

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        mservice = new Calendar.Builder(
                transport, JSON_FACTORY, credential)
                .setApplicationName("Google GSuite API Android")
                .build();

    }


    @Override
    protected List<String> doInBackground(Void... voids) {
        try {
            return getData();
        } catch (Exception error) {
            if (iOnGsuitePresenterResponse != null) {
                iOnGsuitePresenterResponse.onGsuitePresenterError(error);
            }
            return null;
        }
    }

    /**
     *
     * @return freebusyResponse.
     * @throws IOException IOException.
     */
    private List<String> getData() throws IOException {
        FreeBusyRequest request = new FreeBusyRequest();
        List<FreeBusyRequestItem> requestItems = new ArrayList<>();
        List<String> listOfIdsOfCurrentlyAvailableRooms = new ArrayList<>();
        DateTime now = new DateTime(System.currentTimeMillis());

        for (String roomIds : listOfResourceCalendarIds) {
            requestItems.add(new FreeBusyRequestItem().setId(roomIds));
        }

        java.util.Calendar calendar = new GregorianCalendar();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
        calendar.set(java.util.Calendar.MINUTE, 59);
        calendar.set(java.util.Calendar.SECOND, 59);
        Date midnight = calendar.getTime();

        request.setTimeMin(now)
                .setTimeMax(new DateTime(midnight.getTime()))
                .setTimeZone("Africa/Lagos")
                .setItems(requestItems);

        Calendar.Freebusy.Query query = mservice.freebusy().query(request);
        query.setFields("calendars");

        FreeBusyResponse freeBusyResponse = new FreeBusyResponse();
        Map<String, FreeBusyCalendar> freeBusyCalendarMap;

        try {
            if (credential.getSelectedAccountName() == null) {
                iOnGsuitePresenterResponse.onGetSelectedName();
            }
            freeBusyResponse = query.execute();

        } catch (Exception e) {
            if (iOnGsuitePresenterResponse != null) {
                iOnGsuitePresenterResponse.onGsuitePresenterError(e);
            }
        }

        freeBusyCalendarMap = freeBusyResponse.getCalendars();

        final int upcomingEventPosition = 0;
        for (String id : this.listOfResourceCalendarIds) {
            Long nextEventStartTime;

            if (freeBusyCalendarMap.get(id).getErrors() == null) {

                if (freeBusyCalendarMap.get(id).getBusy().isEmpty()) {
                    listOfIdsOfCurrentlyAvailableRooms.add(id);
                } else {
                    nextEventStartTime = freeBusyCalendarMap.get(id).getBusy()
                            .get(upcomingEventPosition).getStart().getValue();
                    if (now.getValue() < nextEventStartTime) {
                        listOfIdsOfCurrentlyAvailableRooms.add(id);
                    }
                }

            }
        }

        assert iOnGsuitePresenterResponse != null;
        iOnGsuitePresenterResponse.onGsuitePresenterSuccess(
                getAvailableRooms(listOfIdsOfCurrentlyAvailableRooms, listOfRooms));
        return listOfIdsOfCurrentlyAvailableRooms;
    }

    /**
     * @param listOfRoomIds list of room ids.
     * @param rooms list of rooms.
     * @return availableRooms.
     */
    private List<GetAllRoomsInALocationQuery.Room> getAvailableRooms(
            List<String> listOfRoomIds, List<GetAllRoomsInALocationQuery.Room> rooms) {
        List<GetAllRoomsInALocationQuery.Room> availableRooms = new ArrayList<>();
        if (!rooms.isEmpty()) {
            for (GetAllRoomsInALocationQuery.Room room : rooms) {
                if (listOfRoomIds.contains(room.calendarId())) {
                    availableRooms.add(room);
                }
            }
        }
        return availableRooms;
    }


    /**
     * The interface On gsuite presenter response.
     */
    public interface IOnGsuitePresenterResponse {
        /**
         * On gsuite presenter success.
         *
         * @param listOfAvailableRooms the list of available rooms.
         */
        void onGsuitePresenterSuccess(List<GetAllRoomsInALocationQuery.Room> listOfAvailableRooms);

        /**
         * On gsuite presenter error.
         *
         * @param error the error
         */
        void onGsuitePresenterError(Exception error);

        /**
         * On get selected name.
         */
        void onGetSelectedName();
    }
}
