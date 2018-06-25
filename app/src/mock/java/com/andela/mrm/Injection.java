package com.andela.mrm;

import android.content.Context;

import com.andela.mrm.fragment.Room;
import com.andela.mrm.room_information.resources_info.MockResourcesInfoRepo;
import com.andela.mrm.room_information.resources_info.ResourcesInfoContract;

import java.util.ArrayList;

/**
 * An injection module for providing different instances of diff class depending on build flavor.
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
        Room.Block block = new Room.Block("", "", "");
        Room.Floor floor = new Room.Floor("", "", "", block);
        final Room room = new Room(
                "",
                String.valueOf(roomId),
                "cognitio",
                "",
                Long.valueOf(roomId),
                "",
                floor,
                new ArrayList<Room.Resource>());
        return new MockResourcesInfoRepo(room);
    }
}
