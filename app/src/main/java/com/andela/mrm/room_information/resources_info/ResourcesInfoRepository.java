package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.GetRoomsInABlockQuery;
import com.andela.mrm.RoomQuery;
import com.andela.mrm.fragment.Room;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

/**
 * Room resources info repository class.
 */
public class ResourcesInfoRepository implements ResourcesInfoContract.Data {

    private final ApolloClient mApolloClient;

    /**
     * Instantiates a new Resources info repository.
     *
     * @param apolloClient the apollo call
     */
    public ResourcesInfoRepository(ApolloClient apolloClient) {
        mApolloClient = apolloClient;
    }

    @Override
    public void loadRoom(final Callback<Room> callback, int roomId) {
        RoomQuery roomQuery = RoomQuery.builder()
                .roomId(roomId).build();
        ApolloQueryCall<RoomQuery.Data> query = mApolloClient
                .query(roomQuery);
        query
                .enqueue(new ApolloCall.Callback<RoomQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<RoomQuery.Data> response) {
                        // TODO: do null and error check
                        callback.onDataLoadSuccess(response.data().getRoomById()
                                .fragments().room());
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        callback.onDataLoadFailed();
                    }
                });
    }

    @Override
    public void loadSimilarRooms(
            final Callback<GetRoomsInABlockQuery.GetRoomsInABlock> callback, int blockId) {
        GetRoomsInABlockQuery roomQuery = GetRoomsInABlockQuery.builder()
                .blockId(blockId).build();
        mApolloClient
                .query(roomQuery)
                .enqueue(new ApolloCall.Callback<GetRoomsInABlockQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetRoomsInABlockQuery.Data> response) {
                        // TODO: do null and error check on response
                        callback.onDataLoadSuccess(response.data().getRoomsInABlock().get(0));

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        callback.onDataLoadFailed();
                    }
                });

    }
}
