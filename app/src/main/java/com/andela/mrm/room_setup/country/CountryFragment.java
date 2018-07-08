package com.andela.mrm.room_setup.country;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
 * Created by andeladeveloper on 06/04/2018.
 */
public class CountryFragment extends Fragment implements RoomSetupContract.CountryContract {
    /**
     * The Country presenter.
     */
    RoomSetupPresenter mRoomSetupPresenter = new RoomSetupPresenter(this);

    View view;

    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_country, container, false);

        queryApi();
        view = inflater
                .inflate(R.layout.fragment_country, container, false);
        return view;
    }


    /**
     * Queries graphQL api for list of endpoint data.
     */
    public void queryApi() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading... Please wait!!!");
        progressDialog.isIndeterminate();
        progressDialog.show();

        mRoomSetupPresenter.getAllLocations(this,
                new RoomSetupPresenter.DataLoadedCallback() {
            @Override
            public void onDataLoaded(boolean dataLoaded) {
                if (dataLoaded) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.findViewById(R.id.fragment_flags_layout)
                                    .setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }, null, null, null);
    }

    @Override
    public void displayCountries(final List<AllLocationsQuery.AllLocation> mData) {

        int countrySize = mData.size();

        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = view.findViewById(R.id.country_grid_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), countrySize);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter adapter = new CountryAdapter(getContext(), mData);
        mRecyclerView.setAdapter(adapter);
        dismissDialog();
    }


    /**
     * Dismisses/closes any running progress dialogs after success or failed api queries.
     */
    @Override
    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * Displays a snackbar notification for poor or no network connections.
     *
     * @param isNetworkAvailable - boolean value which determines if there is network or not
     */
    @Override
    public void displaySnackBar(Boolean isNetworkAvailable) {
        String notificationText = "No Network Found";

        if (isNetworkAvailable) {
            notificationText = "Bad Network Connection";
        }

        Snackbar.make(getView(), notificationText, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queryApi();
                    }
                }).show();
    }
}
