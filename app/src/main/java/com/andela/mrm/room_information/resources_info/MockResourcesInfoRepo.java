package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.GetRoomsInABlockQuery;
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
     * @param roomId
     */
    @Override
    public void loadRoom(Callback<Room> callback, int roomId) {
        // TODO: implement later
    }

    @Override
    public void loadSimilarRooms(Callback<GetRoomsInABlockQuery.GetRoomsInABlock> rooms,
                                 int blockId) {
        // TODO: implement later
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
