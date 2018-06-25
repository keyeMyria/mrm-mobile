package com.andela.mrm.room_information.resources_info;

import android.content.Context;

import com.andela.mrm.RoomQuery;
import com.andela.mrm.util.NetworkConnectivityChecker;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

/**
 * Room resources info repository class.
 */
public class ResourcesInfoRepository implements ResourcesInfoContract.Data {

    final Context mContext;
    private final ApolloCall<RoomQuery.Data> mRoomQuery;

    /**
     * Instantiates a new Resources info repository.
     *
     * @param context    the context
     * @param apolloCall the apollo call
     */
    public ResourcesInfoRepository(Context context, ApolloCall<RoomQuery.Data> apolloCall) {
        mContext = context;
        mRoomQuery = apolloCall;
    }

    @Override
    public void loadRoom(final Callback callback) {
        mRoomQuery.enqueue(new ApolloCall.Callback<RoomQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<RoomQuery.Data> response) {
                RoomQuery.GetRoomById result = response.data().getRoomById();
                if (result != null && !response.hasErrors()) {
                    callback.onDataLoadSuccess(
                            response.data().getRoomById().fragments().room());
                    return;
                }
                callback.onDataLoadFailed(new
                        Exception(response.errors().get(0).message()));
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                if (!NetworkConnectivityChecker.isDeviceOnline(mContext)) {
                    callback.onDataLoadFailed(new Exception("No network found"));
                    return;
                }
                callback.onDataLoadFailed(new Exception("Could not retrieve data"));
            }
        });
    }
}
