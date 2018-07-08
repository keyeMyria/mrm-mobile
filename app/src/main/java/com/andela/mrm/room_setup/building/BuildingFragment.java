package com.andela.mrm.room_setup.building;

/**
 * Created by baron on 06/04/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
 */
public class BuildingFragment extends Fragment implements RoomSetupContract.BuildingView {
    View view;
    String countryID;
    /**
     * The presenter.
     */
    RoomSetupPresenter roomPresenter = new RoomSetupPresenter(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          countryID = getArguments().getString("countryID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater
                .inflate(R.layout.fragment_building, container, false);

        roomPresenter.getAllLocations(this, null,
                countryID, null, null);

        return view;
    }

    @Override
    public void displayBuildings(final List<AllLocationsQuery.Block> mBlock) {

       int buildingSize = mBlock.size();
       int numCols = 3;
        if (buildingSize < 3) {
            numCols = buildingSize;
        }

        final RecyclerView mRecyclerView;
        final RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = view.findViewById(R.id.building_grid_view);
        mRecyclerView.setHasFixedSize(true);
        if (numCols != 0) {
            mLayoutManager = new GridLayoutManager(getContext(), numCols);
        } else {
            mLayoutManager = new LinearLayoutManager(getContext());
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setLayoutManager(mLayoutManager);
                RecyclerView.Adapter adapter = new BuildingAdapter(getContext(), mBlock, countryID);
                mRecyclerView.setAdapter(adapter);
            }
        });
    }
}
