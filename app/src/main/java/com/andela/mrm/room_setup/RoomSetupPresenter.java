package com.andela.mrm.room_setup;

import android.support.v4.app.Fragment;

import com.andela.mrm.AllLocationsQuery;
import com.andela.mrm.room_setup.building.BuildingFragment;
import com.andela.mrm.room_setup.country.CountryFragment;
import com.andela.mrm.room_setup.floor.FloorSelectionFragment;
import com.andela.mrm.room_setup.meeting_room.MeetingRoomFragment;
import com.andela.mrm.service.ApiService;
import com.andela.mrm.util.EspressoIdlingResource;
import com.andela.mrm.util.NetworkConnectivityChecker;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * RoomSetupPresenter class.
 */

public class RoomSetupPresenter {

    CountryFragment view;
    BuildingFragment mView;
    FloorSelectionFragment floorView;
    MeetingRoomFragment mrfView;

    /**
     * RoomSetupPresenter class constructor method.
     *
     * @param view - fragment view context
     */
    public RoomSetupPresenter(CountryFragment view) {

        this.view = view;
    }

    /**
     * RoomSetupPresenter class constructor method.
     *
     * @param mView - fragment view context
     */
    public RoomSetupPresenter(BuildingFragment mView) {
        this.mView = mView;
    }

    /**
     * Room booking presenter for floor selection.
     *
     * @param floorView floorView.
     */
    public RoomSetupPresenter(FloorSelectionFragment floorView) {
        this.floorView = floorView;
    }

    /**
     * RoomSetupPresenter class constructor method.
     *
     * @param mrfView - fragment view context
     */
    public RoomSetupPresenter(MeetingRoomFragment mrfView) {

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

        ApiService.getApolloClient(currentFragment.getContext())
                .query(AllLocationsQuery.builder().build())
                .enqueue(new ApolloCall.Callback<AllLocationsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<AllLocationsQuery.Data> response) {
                        viewNotNull(response, dataLoadedCallback);
                        mViewNotNull(response, countryID);
                        floorViewNotNull(response, countryID, buildingID);
                        mrfViewNotNull(response, countryID, buildingID, floorID);
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
     * Extracted Method for mrfView Not being null.
     * @param response Response from AllLocationsQuery
     * @param countryID Unique Identifier for the country selected
     * @param buildingID Unique Identifier for the building selected
     * @param floorID Unique Identifier for the floor
     */
    public void mrfViewNotNull(@Nonnull Response<AllLocationsQuery.Data> response,
                               @Nullable String countryID,
                               @Nullable String buildingID,
                               @Nullable String floorID) {
        if (mrfView != null) {
            mrfView.displayMeetingRooms(response.data().allLocations()
                    .get(Integer.parseInt(countryID)).blocks()
                    .get(Integer.parseInt(buildingID)).floors()
                    .get(Integer.parseInt(floorID)).rooms());
        }
    }

    /**
     * Extracted method to handle instance of floorView not being null.
     * @param response Response data from AllLocationsQuery
     * @param countryID Unique Identifier for the country selected
     * @param buildingID Unique Identifier for the building chosen
     */
    public void floorViewNotNull(@Nonnull Response<AllLocationsQuery.Data> response,
                                 @Nullable String countryID, @Nullable String buildingID) {
        if (floorView != null) {
            floorView.displayFloors(response.data().allLocations()
                    .get(Integer.parseInt(countryID)).blocks()
                    .get(Integer.parseInt(buildingID)).floors());
        }
    }
    /**
     * Extracted method to handle Instance of mView not being null.
     * @param response Response from AllLocationsQuery
     * @param countryID Integer that acts as identifier for which country to query.
     */
    public void mViewNotNull(@Nonnull Response<AllLocationsQuery.Data> response,
                             @Nullable String countryID) {
        if (mView != null) {
            mView.displayBuildings(response.data().allLocations()
                    .get(Integer.parseInt(countryID)).blocks());
        }
    }

    /**
     * Extracted method to deal with view not being null.
     * @param response Response from AllLocations Query
     * @param dataLoadedCallback Response from DataLoadedCallback
     */
    public void viewNotNull(@Nonnull Response<AllLocationsQuery.Data> response,
                            @Nullable DataLoadedCallback dataLoadedCallback) {
        if (view != null) {
            view.displayCountries(response.data().allLocations());
            dataLoadedCallback.onDataLoaded(true);
        }
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
