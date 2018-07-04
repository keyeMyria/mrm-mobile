package com.andela.mrm.room_information.resources_info;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.andela.mrm.R;
import com.andela.mrm.adapter.RoomResourcesAdapter;
import com.andela.mrm.fragment.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResourcesInfoFragment extends Fragment {

    private Callbacks mCallbacks;

    private final GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 4);
    private final RoomResourcesAdapter mResourcesAdapter = new
            RoomResourcesAdapter(new ArrayList<Room.Resource>(0));

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mCallbacks != null) {
            mCallbacks.onViewLoaded();
        }
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

        mProgressBar = view.findViewById(R.id.progress_bar_loading_resources);
        mRecyclerView = view.findViewById(R.id.room_resources_recycler_view);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mResourcesAdapter);

        mCallbacks.onViewLoaded();

        return view;
    }

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
        mResourcesAdapter.setResources(resources);
    }

    /**
     * Show loading indicator.
     *
     * @param isLoading the is loading
     */
    public void showLoadingIndicator(boolean isLoading) {
        if (mProgressBar != null && mRecyclerView != null) {
            if (isLoading) {
                mProgressBar.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * The interface Callbacks.
     */
    public interface Callbacks {
        /**
         * On view loaded.
         */
        void onViewLoaded();
    }
}
