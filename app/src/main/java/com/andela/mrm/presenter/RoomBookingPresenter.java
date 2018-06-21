package com.andela.mrm.presenter;

import android.support.v4.app.Fragment;

import com.andela.mrm.AllLocationsQuery;
import com.andela.mrm.room_booking.building.BuildingFragment;
import com.andela.mrm.room_booking.country.CountryFragment;
import com.andela.mrm.room_booking.floor.FloorSelectionFragment;
import com.andela.mrm.room_booking.meeting_room.MeetingRoomFragment;
import com.andela.mrm.service.MyApolloClient;
import com.andela.mrm.util.EspressoIdlingResource;
import com.andela.mrm.util.NetworkConnectivityChecker;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * RoomBookingPresenter class.
 */

public class RoomBookingPresenter {

    CountryFragment view;
    BuildingFragment mView;
    FloorSelectionFragment floorView;
    MeetingRoomFragment mrfView;

    /**
     * RoomBookingPresenter class constructor method.
     *
     * @param view - fragment view context
     */
    public RoomBookingPresenter(CountryFragment view) {

        this.view = view;
    }

    /**
     * RoomBookingPresenter class constructor method.
     *
     * @param mView - fragment view context
     */
    public RoomBookingPresenter(BuildingFragment mView) {
        this.mView = mView;
    }

    /**
     * Room booking presenter for floor selection.
     *
     * @param floorView floorView.
     */
    public RoomBookingPresenter(FloorSelectionFragment floorView) {
        this.floorView = floorView;
    }

    /**
     * RoomBookingPresenter class constructor method.
     *
     * @param mrfView - fragment view context
     */
    public RoomBookingPresenter(MeetingRoomFragment mrfView) {

        this.mrfView = mrfView;
    }

    /**
     * Class method for querying graphQL api endpoint through the apollo client.
     *
     * @param currentFragment    - current fragment in view
     * @param dataLoadedCallback - callback interface
     * @param countryID          - current country id
     * @param buildingID         - current building id
     * @param floorID            - current floor id
     */
    public void getAllLocations(Fragment currentFragment,
                                @Nullable final DataLoadedCallback dataLoadedCallback,
                                @Nullable final String countryID,
                                @Nullable final String buildingID,
                                @Nullable final String floorID) {

        EspressoIdlingResource.increment();

        MyApolloClient.getMyApolloClient(currentFragment.getContext(),
                "https://api.graph.cool/simple/v1/cjgdrz83r1t4k0173p13insaj")
                .query(AllLocationsQuery.builder().build())
                .enqueue(new ApolloCall.Callback<AllLocationsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<AllLocationsQuery.Data> response) {

                        if (view != null) {
                            view.displayCountries(response.data().allLocations());
                            dataLoadedCallback.onDataLoaded(true);
                        }

                        if (mView != null) {
                            mView.displayBuildings(response.data().allLocations()
                                    .get(Integer.parseInt(countryID)).blocks());
                        }

                        if (floorView != null) {
                            floorView.displayFloors(response.data().allLocations()
                                    .get(Integer.parseInt(countryID)).blocks()
                                    .get(Integer.parseInt(buildingID)).floors());
                        }

                        if (mrfView != null) {
                            mrfView.displayMeetingRooms(response.data().allLocations()
                                    .get(Integer.parseInt(countryID)).blocks()
                                    .get(Integer.parseInt(buildingID)).floors()
                                    .get(Integer.parseInt(floorID)).rooms());
                        }

                        EspressoIdlingResource.decrement();
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        view.dismissDialog();
                        view.displaySnackBar(
                                NetworkConnectivityChecker
                                        .isDeviceOnline(view.getContext())
                        );
                        dataLoadedCallback.onDataLoaded(false);

                        EspressoIdlingResource.decrement();
                    }
                });

    }

    /**
     * Asynchronous callback interface.
     * Triggered either on queried Api success or failure response
     */
    public interface DataLoadedCallback {
        /**
         * Indicates if the queried api response data is available or not.
         *
         * @param dataLoaded - boolean variable
         */
        void onDataLoaded(boolean dataLoaded);
    }
}
