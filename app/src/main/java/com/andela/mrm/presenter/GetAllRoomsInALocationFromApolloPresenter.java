package com.andela.mrm.presenter;

import android.content.Context;

import com.andela.mrm.GetAllRoomsInALocationQuery;
import com.andela.mrm.service.ApiService;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;


/**
 * The type Get all rooms in a location from apollo presenter.
 */
public class GetAllRoomsInALocationFromApolloPresenter {
    public List<GetAllRoomsInALocationQuery.Floor> floorsFromApollo = new ArrayList<>();
    public final List<GetAllRoomsInALocationQuery.Room> allRoomsInBlockFromApollo =
            new ArrayList<>();
    public final List<String> meetingRoomCalendarIds = new ArrayList<>();

    /**
     * The Context.
     */
    private final Context context;

    /**
     * Instantiates a new Get all rooms in a location from apollo presenter.
     *
     * @param context the context
     */
    public GetAllRoomsInALocationFromApolloPresenter(Context context) {
        this.context = context;
    }

    /**
     * Gets all rooms.
     *
     * @param locationId                       the location id
     * @param iOnGetAllRoomsFromApolloCallback the on get all rooms from apollo callback
     */
    public void getAllRooms(int locationId, final IOnGetAllRoomsFromApolloCallback
                                    iOnGetAllRoomsFromApolloCallback) {
        ApiService.getApolloClient(context)
                .query(GetAllRoomsInALocationQuery.builder().locationId((long) locationId).build())
                .enqueue(new ApolloCall.Callback<GetAllRoomsInALocationQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetAllRoomsInALocationQuery.Data>
                                                   response) {
                        assert response.data() != null;
                        floorsFromApollo = response.data().getRoomsInALocation().get(0).blocks()
                                .get(0).floors();
                        assert floorsFromApollo != null;
                        for (GetAllRoomsInALocationQuery.Floor floor : floorsFromApollo) {
                            for (GetAllRoomsInALocationQuery.Room rooms : floor.rooms()) {
                                allRoomsInBlockFromApollo.add(rooms);
                                meetingRoomCalendarIds.add(rooms.calendarId());
                            }
                        }
                        iOnGetAllRoomsFromApolloCallback.onGetAllRoomsFromApolloSuccess(
                                meetingRoomCalendarIds, allRoomsInBlockFromApollo);
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        iOnGetAllRoomsFromApolloCallback.onGetAllRoomsFromApolloError(e);
                    }
                });
    }

    /**
     * The interface On get all rooms from apollo callback.
     */
    public interface IOnGetAllRoomsFromApolloCallback {
        /**
         * On get all rooms from apollo error.
         *
         * @param error the error
         */
        void onGetAllRoomsFromApolloError(ApolloException error);

        /**
         * On get all rooms from apollo success.
         *
         * @param listOfAllRoomsId the list of all rooms id
         * @param rooms            the rooms
         */
        void onGetAllRoomsFromApolloSuccess(List<String> listOfAllRoomsId,
                                            List<GetAllRoomsInALocationQuery.Room> rooms);
    }
}
