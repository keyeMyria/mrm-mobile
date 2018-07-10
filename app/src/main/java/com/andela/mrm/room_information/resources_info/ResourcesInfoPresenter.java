package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.GetRoomsInABlockQuery;
import com.andela.mrm.R;
import com.andela.mrm.fragment.Room;
import com.andela.mrm.room_information.resources_info.ResourcesInfoContract.Data;
import com.andela.mrm.room_information.resources_info.ResourcesInfoContract.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Room resources info presenter class.
 */
public class ResourcesInfoPresenter implements ResourcesInfoContract.Actions {

    final View mView;
    final Data mData;
    private final int mRoomId;

    /**
     * Instantiates a new Resources info presenter.
     *
     * @param view the view
     * @param data the data
     * @param roomId - the roomId
     */
    public ResourcesInfoPresenter(View view, Data data, int roomId) {
        mView = view;
        mData = data;
        mRoomId = roomId;
    }

    @Override
    public void fetchRoomDetails() {
        mView.showLoadingIndicator(true);
        mData.loadRoom(new Data.Callback<Room>() {
            @Override
            public void onDataLoadSuccess(final Room room) {
                final String id = room.floor().block().id();
                int blockId = Integer.parseInt(id);
                mData.loadSimilarRooms(new Data.Callback<GetRoomsInABlockQuery.GetRoomsInABlock>() {
                    @Override
                    public void onDataLoadSuccess(GetRoomsInABlockQuery.GetRoomsInABlock data) {
                        mView.showLoadingIndicator(false);
                        mView.showRoomInfo(room);
                        mView.showSimilarRooms(flatOutRoomsFromResults(data.floors()));
                    }

                    @Override
                    public void onDataLoadFailed() {
                        viewShowErrorMessage();
                    }
                }, blockId);
            }

            @Override
            public void onDataLoadFailed() {
                viewShowErrorMessage();
            }
        }, mRoomId);
    }

    /**
     * Show error message.
     */
    void viewShowErrorMessage() {
        mView.showLoadingIndicator(false);
        if (!mView.isNetworkAvailable()) {
            mView.showErrorMessage(R.string.error_internet_connection);
            return;
        }
        mView.showErrorMessage(R.string.error_data_fetch_message);
    }

    /**
     * Rooms in a block.
     *
     * @param floors - floors
     * @return rooms
     */
    List<GetRoomsInABlockQuery.Room>
    flatOutRoomsFromResults(List<GetRoomsInABlockQuery.Floor> floors) {
        List<GetRoomsInABlockQuery.Room> rooms = new ArrayList<>();
        for (GetRoomsInABlockQuery.Floor floor : floors) {
            rooms.addAll(floor.rooms());
        }
        return rooms;
    }
}
