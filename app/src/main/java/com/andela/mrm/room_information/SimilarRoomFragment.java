package com.andela.mrm.room_information;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.mrm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimilarRoomFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private static final String PROJECTOR = "Projector";

    /**
     * The Layout manager.
     */
    RecyclerView.LayoutManager layoutManager;


    /**
     * Instantiates a new Similar room fragment.
     */
    public SimilarRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_similar_room, container, false);
        mRecyclerView = view.findViewById(R.id.similar_room_recycler_view);
        mRecyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new SimilarRoomAdapter(setRooms(), getContext());
        mRecyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Sets rooms.
     *
     * @return the rooms
     */
    public List<RoomInformation> setRooms() {
        List<RoomInformation> roomAmenitesInformation =
                new ArrayList<RoomInformation>();
        List<String> roomItems = new ArrayList<>();

        roomItems.add("Apple TV");
        roomItems.add(PROJECTOR);
        roomItems.add(PROJECTOR);
        roomItems.add("Headphones");
        roomItems.add(PROJECTOR);
        roomItems.add(PROJECTOR);

        roomAmenitesInformation.add(new
                RoomInformation("OL Karia",
                roomItems, 2, 23,
                "Block A, First Floor", "Jabra Speaker"));
        roomAmenitesInformation.add(new
                RoomInformation("Shire", roomItems,
                4, 10, "Block A, First Floor",
                "Jabra Speaker"));
        roomAmenitesInformation.add(new
                RoomInformation("Wasini", roomItems,
                8, 10, "Block A, First Floor",
                "Jabra Speaker"));
        roomAmenitesInformation.add(new
                RoomInformation("Naija", roomItems,
                8, 10, "Block B, First Floor",
                "Speaker"));
        return roomAmenitesInformation;
    }

}
