package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.GetRoomsInABlockQuery;
import com.andela.mrm.fragment.Room;

import java.util.List;

/**
 * Room resources info contract interface.
 */
public interface ResourcesInfoContract {
    /**
     * View interface.
     */
    interface View {
        /**
         * Show room info.
         *
         * @param room the room
         */
        void showRoomInfo(Room room);

        /**
         * Show loading indicator.
         *
         * @param isLoading the is loading
         */
        void showLoadingIndicator(boolean isLoading);

        /**
         * Show error message.
         *
         * @param messageResourceId the message
         */
        void showErrorMessage(int messageResourceId);

        /**
         * Show similar rooms method.
         *
         * @param rooms - List of rooms
         */
        void showSimilarRooms(List<GetRoomsInABlockQuery.Room> rooms);

        /**
         * Gets network availability Status from the view.
         *
         * @return the boolean
         */
        boolean isNetworkAvailable();
    }

    /**
     * The interface Actions.
     */
    interface Actions {
        /**
         * Fetch room details.
         */
        void fetchRoomDetails();
    }

    /**
     * The interface Data.
     */
    interface Data {
        /**
         * Load room.
         *
         * @param callback - the callback
         * @param roomId - roomId
         */
        void loadRoom(Callback<Room> callback, int roomId);

        /**
         * Load similar rooms method.
         * @param rooms - Rooms in a block
         * @param blockId - Block id
         */
        void loadSimilarRooms(Callback<GetRoomsInABlockQuery.GetRoomsInABlock> rooms, int blockId);


        /**
         * The interface Callback.
         *
         * @param <T> - Callback parameter
         */
        interface Callback<T> {
            /**
             * On data load success.
             *
             * @param data the room
             */
            void onDataLoadSuccess(T data);

            /**
             * On data load failed.
             */
            void onDataLoadFailed();
        }

    }
}
