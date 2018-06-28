package com.andela.mrm;

import android.content.Context;

import com.andela.mrm.room_information.resources_info.ResourcesInfoContract;
import com.andela.mrm.room_information.resources_info.ResourcesInfoRepository;
import com.andela.mrm.service.ApiService;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.fetcher.ApolloResponseFetchers;

/**
 * The type Injection.
 */
public class Injection {
    /**
     * Provides an implementation of Room Resources Info Data contract.
     *
     * @param context the context
     * @param roomId  the room id
     * @return the resources info contract . data
     */
    public static ResourcesInfoContract.Data provideResourcesInfoData(Context context, int roomId) {
        ApolloQueryCall<RoomQuery.Data> query = ApiService.getApolloClient(context)
                .query(RoomQuery.builder().roomId(Long.valueOf(roomId)).build())
                .responseFetcher(ApolloResponseFetchers.NETWORK_ONLY);
        return new ResourcesInfoRepository(context, query);
    }
}
