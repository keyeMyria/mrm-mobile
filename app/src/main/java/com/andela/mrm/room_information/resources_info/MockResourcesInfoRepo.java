package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.fragment.Room;

/**
 * The type Mock resources info repo.
 */
public class MockResourcesInfoRepo implements ResourcesInfoContract.Data {

    private final Room mRoom;

    /**
     * Instantiates a new Mock resources info repo.
     *
     * @param room the room
     */
    public MockResourcesInfoRepo(Room room) {
        mRoom = room;
    }

    /**
     * Load room.
     *
     * @param callback the callback
     */
    @Override
    public void loadRoom(Callback callback) {
        callback.onDataLoadSuccess(mRoom);
    }

    /**
     * Gets room.
     *
     * @return the room
     */
    public Room getRoom() {
        return mRoom;
    }
}
