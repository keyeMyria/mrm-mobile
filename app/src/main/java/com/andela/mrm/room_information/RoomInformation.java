package com.andela.mrm.room_information;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Room information.
 */
public class RoomInformation {
    private static final String COGNITIO = "Cognitio";
    private static final String LAGOS = "Lagos";
    private static final String JABRA = "Jabra Speaker";
    private static final String PROJECTOR = "Projector";
    private final String roomName;
    private final List<String> roomItem;
    private final int roomItemQuantity;
    private final int roomAttendees;
    private final String roomLocation;
    private final String singleRoomItem;

    /**
     * Gets single room item.
     *
     * @return the single room item
     */
    public String getSingleRoomItem() {
        return singleRoomItem;
    }


    /**
     * Gets room name.
     *
     * @return the room name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Gets room attendees.
     *
     * @return the room attendees
     */
    public int getRoomAttendees() {
        return roomAttendees;
    }

    /**
     * Gets room location.
     *
     * @return the room location
     */
    public String getRoomLocation() {
        return roomLocation;
    }

    /**
     * Instantiates a new Room information.
     *
     * @param roomName         the room name
     * @param roomItem         the room item
     * @param roomItemQuantity the room item quantity
     * @param roomAttendees    the room attendees
     * @param roomLocation     the room location
     * @param singleRoomItem   the single room item
     */
    public RoomInformation(String roomName, List<String> roomItem,
                           int roomItemQuantity, int roomAttendees, String roomLocation,
                           String singleRoomItem) {
        this.roomName = roomName;
        this.roomItem = roomItem;
        this.roomItemQuantity = roomItemQuantity;
        this.roomAttendees = roomAttendees;
        this.roomLocation = roomLocation;
        this.singleRoomItem = singleRoomItem;
    }


    /**
     * Gets room item.
     *
     * @return the room item
     */
    public List<String> getRoomItem() {
        return roomItem;
    }

    /**
     * Sets rooms.
     *
     * @return the rooms
     */
    public static List<RoomInformation> setRooms() {
        List<RoomInformation> roomInfo = new ArrayList<>();
        List<String> roomItems = new ArrayList<>();
        String[] roomItem = {"Apple TV", "Projector", "Jabra Speaker", "Headphones"};
        roomItems.addAll(Arrays.asList(roomItem));
        roomInfo.add(new
                RoomInformation(COGNITIO,
                roomItems, 2, 10, LAGOS, JABRA));
        roomInfo.add(new
                RoomInformation(COGNITIO, roomItems,
                4, 10, LAGOS, PROJECTOR));
        roomInfo.add(new
                RoomInformation(COGNITIO, roomItems,
                8, 10, LAGOS, JABRA));
        roomInfo.add(new
                RoomInformation(COGNITIO, roomItems,
                1, 10, LAGOS, "Headphones"));
        roomInfo.add(new
                RoomInformation(COGNITIO, roomItems,
                2, 10, LAGOS, JABRA));
        roomInfo.add(new
                RoomInformation(COGNITIO, roomItems,
                1, 10, LAGOS, JABRA));
        roomInfo.add(new
                RoomInformation(COGNITIO, roomItems,
                1, 10, LAGOS, PROJECTOR));

        return roomInfo;
    }

    /**
     * Gets room item quantity.
     *
     * @return the room item quantity
     */
    public int getRoomItemQuantity() {
        return roomItemQuantity;
    }


}
