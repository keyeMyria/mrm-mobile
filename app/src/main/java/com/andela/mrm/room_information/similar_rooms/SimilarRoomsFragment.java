package com.andela.mrm.room_information.similar_rooms;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.mrm.GetRoomsInABlockQuery;
import com.andela.mrm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimilarRoomsFragment extends Fragment {

    private Callbacks mCallback;
    /**
     * The Layout manager.
     */
    RecyclerView.LayoutManager layoutManager;
    SimilarRoomAdapter mSimilarRoomAdapter =
            new SimilarRoomAdapter(new ArrayList<GetRoomsInABlockQuery.Room>());
    private RecyclerView mRecyclerView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mCallback.onViewLoaded();
        }
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callbacks) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_similar_room, container, false);
        mRecyclerView = view.findViewById(R.id.similar_room_recycler_view);

        layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mSimilarRoomAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /**
     * Set similar rooms method.
     * @param rooms - Rooms
     */
    public void setSimilarRooms(List<GetRoomsInABlockQuery.Room> rooms) {
        mSimilarRoomAdapter.setRooms(rooms);
    }

    /**
     * Callback interface.
     */
    public interface Callbacks {
        /**
         * onViewLoaded method.
         */
        void onViewLoaded();
    }

}
