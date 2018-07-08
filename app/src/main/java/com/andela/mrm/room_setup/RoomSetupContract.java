package com.andela.mrm.room_setup;

import com.andela.mrm.AllLocationsQuery;

import java.util.List;

/**
 * Created by chike on 16/05/2018.
 */

public interface RoomSetupContract {
    /**
     * Interface binding/connecting the RoomSetupPresenter class to its Fragments view class.
     */
    interface CountryContract {
        /**
         * Abstract method for displaying list of countries gotten from api.
         *
         * @param mData - list of all data from graphQL api endpoint
         */
        void displayCountries(List<AllLocationsQuery.AllLocation> mData);

        /**
         * Abstract method called for dismissing progress dialog if present.
         */
        void dismissDialog();

        /**
         * Abstract method called for Displaying snackbar notification.
         * for poor or no network connections
         *
         * @param isNetworkAvailable - boolean value which determines if there is network or not
         */
        void displaySnackBar(Boolean isNetworkAvailable);
    }

    /**
     * Interface binding/connecting the RoomSetupPresenter class to its building view class.
     */
    interface BuildingView {
        /**
         *
         * @param mBlock - number of buildings/blocks
         */
        void displayBuildings(List<AllLocationsQuery.Block> mBlock);
    }

    /**
     * Interface binding the RoomSetupPresenter class to its floor view class.
     */
    interface FloorContract {
        /**
         * Display floor method.
         * @param mFloor floor
         */
        void displayFloors(List<AllLocationsQuery.Floor> mFloor);
    }

    /**
     * Interface binding the RoomSetupPresenter class to its floor view class.
     */
    interface MeetingRoomsContract {
        /**
         * Display list of meeting rooms for a particular floor.
         * @param mRooms -  list of rooms
         */
        void displayMeetingRooms(List<AllLocationsQuery.Room> mRooms);
    }
}
