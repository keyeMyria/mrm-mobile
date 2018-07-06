package com.andela.mrm.room_booking.meeting_room;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.mrm.AllLocationsQuery;
import com.andela.mrm.R;
import com.andela.mrm.adapter.MeetingRoomAdapter;
import com.andela.mrm.presenter.RoomBookingPresenter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingRoomFragment extends Fragment {
    RecyclerView mRecyclerView;
    RoomBookingPresenter roomBookingPresenter = new RoomBookingPresenter(this);

    View view;

    String countryID;
    String buildingID;
    String floorID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countryID = getArguments().getString("countryID");
        buildingID = getArguments().getString("buildingID");
        floorID = getArguments().getString("floorID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meeting_room, container, false);

        queryApi();

        return view;
    }

    /**
     * Function for querying the API for booking rooms.
     */
    public void queryApi() {
        roomBookingPresenter
                .getAllLocations(this, null,
                        countryID, buildingID, floorID);
    }

    /**
     * displays a list of available meeting rooms in a particular floor.
     *
     * @param meetingRooms - list of available meeting rooms
     */
    public void displayMeetingRooms(final List<AllLocationsQuery.Room> meetingRooms) {
        int meetingRoomSize = meetingRooms.size();
        int numCols = 5;

        if (meetingRoomSize == 0) {
            getActivity().runOnUiThread(() -> {
                view.findViewById(R.id.text_select_meeting_room).setVisibility(View.GONE);
                view.findViewById(R.id.meeting_rooms_grid_view).setVisibility(View.GONE);
                view.findViewById(R.id.text_no_meeting_room).setVisibility(View.VISIBLE);
            });
        } else {
            if (meetingRoomSize > 0 && meetingRoomSize < 5) {
                numCols = meetingRoomSize;
            }

            final RecyclerView.LayoutManager mlayoutManager;
            mRecyclerView = getView().findViewById(R.id.meeting_rooms_grid_view);
            mRecyclerView.setHasFixedSize(true);
            mlayoutManager = new GridLayoutManager(getContext(), numCols);
            getActivity().runOnUiThread(() -> {
                mRecyclerView.setLayoutManager(mlayoutManager);
                RecyclerView.Adapter adapter = new MeetingRoomAdapter(meetingRooms);
                mRecyclerView.setAdapter(adapter);
            });
        }
    }
}
