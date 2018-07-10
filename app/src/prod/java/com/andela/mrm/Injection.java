package com.andela.mrm;

import android.content.Context;

import com.andela.mrm.room_information.resources_info.ResourcesInfoContract;
import com.andela.mrm.room_information.resources_info.ResourcesInfoRepository;
import com.andela.mrm.service.ApiService;

/**
 * The type Injection.
 */
public class Injection {
    /**
     * Provides an implementation of Room Resources Info Data contract.
     *
     * @param context the context
     * @return the resources info contract . data
     */
    public static ResourcesInfoContract.Data provideResourcesInfoData(Context context) {
        return new ResourcesInfoRepository(ApiService.getApolloClient(context));
    }
}
