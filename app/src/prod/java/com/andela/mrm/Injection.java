package com.andela.mrm;

import android.content.Context;
import android.content.Intent;

import com.andela.mrm.login_flow.LoginContract;
import com.andela.mrm.room_information.resources_info.ResourcesInfoContract;
import com.andela.mrm.room_information.resources_info.ResourcesInfoRepository;
import com.andela.mrm.service.ApiService;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

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
                .query(RoomQuery.builder().roomId((long) roomId).build())
                .responseFetcher(ApolloResponseFetchers.NETWORK_ONLY);
        return new ResourcesInfoRepository(context, query);
    }

    /**
     * Provides an implementation of the {@link LoginContract.GoogleSignInWrapperUtil} for
     * prod flavor.
     *
     * @param c the context
     * @return the login contract . google sign in wrapper util
     */
    public static LoginContract.GoogleSignInWrapperUtil provideGoogleSignInWrapperUtil(Context c) {
        return new LoginContract.GoogleSignInWrapperUtil() {
            @Override
            public GoogleSignInAccount getSignedInAccount() {
                return GoogleSignIn.getLastSignedInAccount(c);
            }

            @Override
            public Task<GoogleSignInAccount> getSignedInAccountFromIntent(Intent intent) {
                return GoogleSignIn.getSignedInAccountFromIntent(intent);
            }
        };
    }
}
