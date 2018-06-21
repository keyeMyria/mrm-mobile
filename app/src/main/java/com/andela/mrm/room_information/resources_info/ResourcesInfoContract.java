package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.fragment.Room;

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
         * @param message the message
         */
        void showErrorMessage(String message);
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
         * @param callback the callback
         */
        void loadRoom(Callback callback);

        /**
         * The interface Callback.
         */
        interface Callback {
            /**
             * On data load success.
             *
             * @param room the room
             */
            void onDataLoadSuccess(Room room);

            /**
             * On data load failed.
             *
             * @param e the e
             */
            void onDataLoadFailed(Exception e);
        }

    }
}
