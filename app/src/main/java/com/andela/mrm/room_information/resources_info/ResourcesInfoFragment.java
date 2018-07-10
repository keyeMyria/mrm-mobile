package com.andela.mrm.room_information.resources_info;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.mrm.R;
import com.andela.mrm.fragment.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResourcesInfoFragment extends Fragment {

    private final GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 4);
    private final RoomResourcesAdapter mResourcesAdapter = new
            RoomResourcesAdapter(new ArrayList<Room.Resource>(0));

    private RecyclerView mRecyclerView;

    private Callbacks mCallbacks;

    /**
     * New instance resources info fragment.
     *
     * @return the resources info fragment
     */
    public static ResourcesInfoFragment newInstance() {
        Bundle args = new Bundle();
        ResourcesInfoFragment fragment = new ResourcesInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_room_resources, container, false);

        mRecyclerView = view.findViewById(R.id.room_resources_recycler_view);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mCallbacks.onFragmentViewsLoaded();

        return view;
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * Show resources list.
     *
     * @param resources the resources
     */
    public void showResourcesList(List<Room.Resource> resources) {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(new RoomResourcesAdapter(resources));
        }
    }

    /**
     * Callback interface.
     */
    public interface Callbacks {
        /**
         * onFragmentViewsLoaded method.
         */
        void onFragmentViewsLoaded();
    }
}
