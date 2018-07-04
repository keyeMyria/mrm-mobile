package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.R;
import com.andela.mrm.fragment.Room;
import com.andela.mrm.room_information.resources_info.ResourcesInfoContract.Data;
import com.andela.mrm.room_information.resources_info.ResourcesInfoContract.View;

/**
 * Room resources info presenter class.
 */
public class ResourcesInfoPresenter implements ResourcesInfoContract.Actions {

    final View mView;
    final Data mData;

    /**
     * Instantiates a new Resources info presenter.
     *
     * @param view the view
     * @param data the data
     */
    public ResourcesInfoPresenter(View view, Data data) {
        mView = view;
        mData = data;
    }

    @Override
    public void fetchRoomDetails() {
        mView.showLoadingIndicator(true);
        mData.loadRoom(new Data.Callback() {
            @Override
            public void onDataLoadSuccess(Room room) {
                mView.showLoadingIndicator(false);
                mView.showRoomInfo(room);
            }

            @Override
            public void onDataLoadFailed() {
                mView.showLoadingIndicator(false);
                if (!mView.isNetworkAvailable()) {
                    mView.showErrorMessage(R.string.error_internet_connection);
                    return;
                }
                mView.showErrorMessage(R.string.error_data_fetch_message);
            }
        });
    }
}
