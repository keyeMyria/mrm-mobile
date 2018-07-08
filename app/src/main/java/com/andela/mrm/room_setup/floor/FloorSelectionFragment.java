package com.andela.mrm.room_setup.floor;

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
import com.andela.mrm.room_setup.RoomSetupContract;
import com.andela.mrm.room_setup.RoomSetupPresenter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class FloorSelectionFragment extends Fragment implements RoomSetupContract.FloorContract {

    RecyclerView mRecyclerView;
    RoomSetupPresenter mRoomSetupPresenter = new RoomSetupPresenter(this);

    String countryID;
    String buildingID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countryID = getArguments().getString(FloorSelectionActivity.ARG_COUNTRY_ID);
        buildingID = getArguments().getString(FloorSelectionActivity.ARG_BUILDING_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_floor_selection, container,
                false);
        queryApi();
        return view;
    }

    /**
     * Function for querying the API for booking rooms.
     */
    public void queryApi() {
        mRoomSetupPresenter
                .getAllLocations(this, null, countryID,
                        buildingID, null);
    }

    @Override
    public void displayFloors(final List<AllLocationsQuery.Floor> mFloor) {
        int floorSize = mFloor.size();
        int numCols = 5;

        if (floorSize < 5) {
            numCols = floorSize;
        }
        final RecyclerView.LayoutManager mlayoutManager;
        mRecyclerView = getView().findViewById(R.id.floor_grid_view);
        mRecyclerView.setHasFixedSize(true);
        mlayoutManager = new GridLayoutManager(getContext(), numCols);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setLayoutManager(mlayoutManager);
                RecyclerView.Adapter adapter =
                        new FloorAdapter(getContext(), mFloor, countryID, buildingID);
                mRecyclerView.setAdapter(adapter);
            }
        });
    }
}
