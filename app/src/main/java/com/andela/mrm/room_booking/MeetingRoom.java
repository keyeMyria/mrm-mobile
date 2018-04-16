package com.andela.mrm.room_booking;


import java.util.ArrayList;
import java.util.List;

/**
 * The type Meeting rooms.
 */
public class MeetingRoom {

    private final String meetingRoomName;

    /**
     * Instantiates a new Meeting rooms.
     *
     * @param meetingRoomName the meeting room name
     */
    public MeetingRoom(String meetingRoomName) {
        this.meetingRoomName = meetingRoomName;
    }

    /**
     * Initialize meeting rooms array list.
     *
     * @return the array list
     */
    public static List<MeetingRoom> initializeMeetingRooms() {
        List<MeetingRoom> allMeetingRooms = new ArrayList<>();

        allMeetingRooms.add(new MeetingRoom("Maya"));
        allMeetingRooms.add(new MeetingRoom("Wall Street"));
        allMeetingRooms.add(new MeetingRoom("Ubuntu"));
        allMeetingRooms.add(new MeetingRoom("Aso"));
        allMeetingRooms.add(new MeetingRoom("Charley"));
        allMeetingRooms.add(new MeetingRoom("Cognitio"));
        allMeetingRooms.add(new MeetingRoom("Obudu"));
        allMeetingRooms.add(new MeetingRoom("Kente"));

        return allMeetingRooms;
    }

    /**
     * Gets room name.
     *
     * @return the room name
     */
    public String getmeetingRoomName() {
        return this.meetingRoomName;
    }
}
