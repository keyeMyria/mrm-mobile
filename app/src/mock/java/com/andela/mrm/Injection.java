package com.andela.mrm;

import android.content.Context;
import android.content.Intent;

import com.andela.mrm.fragment.Room;
import com.andela.mrm.fragment.Room.Block;
import com.andela.mrm.fragment.Room.Floor;
import com.andela.mrm.fragment.Room.Resource;
import com.andela.mrm.login_flow.LoginContract;
import com.andela.mrm.room_information.resources_info.MockResourcesInfoRepo;
import com.andela.mrm.room_information.resources_info.ResourcesInfoContract;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

/**
 * An injection module for providing instances of different classes depending on build flavor.
 * Mainly useful for providing mock data for tests. (This can be replaced later with Dagger)
 */
public class Injection {
    /**
     * Provides a mock implementation of Room Resources Info Data contract.
     *
     * @param context the context
     * @param roomId  the room id
     * @return the resources info contract . data
     */
    public static ResourcesInfoContract.Data provideResourcesInfoData(Context context, int roomId) {
        List<Resource> resources = Arrays.asList(
                new Resource("", "", "Apple TV"),
                new Resource("", "", "Notebook")
        );
        Block block = new Block("", "", "EPIC Tower");
        Floor floor = new Floor("", "", "4th Floor", block);
        final Room room = new Room(
                "",
                String.valueOf(roomId),
                "cognitio",
                "",
                roomId,
                "",
                floor,
                resources);
        return new MockResourcesInfoRepo(room);
    }

    /**
     * Provides an implementation of the {@link LoginContract.GoogleSignInWrapperUtil} for
     * mock flavor.
     *
     * @param c the context
     * @return the login contract . google sign in wrapper util
     */
    public static LoginContract.GoogleSignInWrapperUtil provideGoogleSignInWrapperUtil(Context c) {
        return new LoginContract.GoogleSignInWrapperUtil() {
            @Override
            public GoogleSignInAccount getSignedInAccount() {
                return null;
            }

            @Override
            public Task<GoogleSignInAccount> getSignedInAccountFromIntent(Intent intent) {
                return GoogleSignIn.getSignedInAccountFromIntent(intent);
            }
        };
    }
}
