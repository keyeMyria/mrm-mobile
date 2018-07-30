package com.andela.mrm.find_rooms;

/**
 * Created by Fred Adewole on 03/06/2018.
 */

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.mrm.GetAllRoomsInALocationQuery;
import com.andela.mrm.Injection;
import com.andela.mrm.R;
import com.andela.mrm.service.ApiService;
import com.apollographql.apollo.exception.ApolloException;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

import static com.andela.mrm.room_availability.RoomAvailabilityActivity.REQUEST_PERMISSION_GET_ACCOUNTS;


/**
 * The type Find room activity.
 */
public class FindRoomActivity extends AppCompatActivity implements
        GetAllRoomsInALocationFromApolloPresenter.IOnGetAllRoomsFromApolloCallback,
        GsuitePresenter.IOnGsuitePresenterResponse, EasyPermissions.PermissionCallbacks {

    static int LAGOS_LOCATION_ID = 2;

    final List<String> availabilityOptions = Arrays.asList("Available", "Unavailable");
    final List<String> locationOptions = Arrays.asList("Block A, First Floor", "Gold Coast, First Floor",
            "Big Apple, Fourth Floor", "Naija, Third Floor");
    final List<String> capacityOptions = Arrays.asList("5 participants", "10 participants", "15 participants",
            "20 participants");
    final List<String> amenitiesOption = Arrays.asList("Apple TV", "Jabra speaker", "Headphones", "Projector");
    final List<String> filters = Arrays.asList("Available", "Block A, First Floor", "10 participants", "Headphones");

    public ImageView filterAvailability;
    public ImageView filterLocation;
    public ImageView filterCapacity;
    public ImageView filterAmenities;

    @BindView(R.id.filter_dropdown_availability)
    public RecyclerView availabilityFilterDropdown;

    @BindView(R.id.filter_dropdown_location)
    public RecyclerView locationFilterDropdown;

    @BindView(R.id.filter_dropdown_capacity)
    public RecyclerView capacityFilterDropdown;

    @BindView(R.id.filter_dropdown_amenities)
    public RecyclerView amenitiesFilterDropdown;

    public RecyclerView selectedFiltersDisplay;

    @BindView(R.id.layout_shimmer)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.text_result_count)
    TextView numberOfAvailableRooms;
    @BindView(R.id.recycler_view_filter_result)
    RecyclerView findRoomRecyclerView;
    @BindView(R.id.close_find_room)
    ImageView closeActivity;

    /**
     * Prepares the intent required to launch this activity.
     *
     * @param packageContext the package context
     * @return the intent
     */
    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, FindRoomActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);

        ButterKnife.bind(this);

        shimmerFrameLayout.startShimmerAnimation();
        numberOfAvailableRooms.setVisibility(View.GONE);

        getAllData();

        onClickListenerGenerator();
        updateFilters(filters);
        closeActivity.setOnClickListener(v -> finish());
    }

    /**
     * Extracted method to deal with the onclick listeners.
     */
    public void onClickListenerGenerator() {
        setOnClickListenerForAvailabilityFilter();
        setOnClickListenerForLocationFilter();
        setOnClickListenerForCapacityFilter();
        setOnClickListenerForAmenititesFilter();
    }
    /**
     * gets all data.
     */
    private void getAllData() {
        new GetAllRoomsInALocationFromApolloPresenter(FindRoomActivity.this)
                .getAllRooms(LAGOS_LOCATION_ID, this);
    }
    /**
     * sets room adapter.
     *
     * @param rooms the list of rooms.
     */
    public void setRoomsAdapter(List<GetAllRoomsInALocationQuery.Room> rooms) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        findRoomRecyclerView.setAdapter(new FindRoomAdapter(rooms, this));
        findRoomRecyclerView.setLayoutManager(layoutManager);
    }
    /**
     * Sets on click listener for availability filter.
     */
    public void setOnClickListenerForAvailabilityFilter() {
        filterAvailability = findViewById(R.id.dropdown_availability_filter);
        RecyclerView.LayoutManager availabilityFilterLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        availabilityFilterDropdown.setLayoutManager(availabilityFilterLayoutManager);
        availabilityFilterDropdown.setAdapter(new DropdownFilterAdapter(availabilityOptions));
        filterAvailability.setOnClickListener(v -> {
            if (availabilityFilterDropdown.getVisibility() == View.GONE) {
                availabilityFilterDropdown.setVisibility(View.VISIBLE);
            } else {
                availabilityFilterDropdown.setVisibility(View.GONE);
            }
        });
    }
    /**
     * Sets on click listener for location filter.
     */
    public void setOnClickListenerForLocationFilter() {
        RecyclerView.LayoutManager locationFilterLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        locationFilterDropdown.setLayoutManager(locationFilterLayoutManager);
        locationFilterDropdown.setAdapter(new DropdownFilterAdapter(locationOptions));
        filterLocation = findViewById(R.id.dropdown_location_filter);
        filterLocation.setOnClickListener(v -> {
            if (locationFilterDropdown.getVisibility() == View.GONE) {
                locationFilterDropdown.setVisibility(View.VISIBLE);
            } else {
                locationFilterDropdown.setVisibility(View.GONE);
            }
        });
    }
    /**
     * Sets on click listener for capacity filter.
     */
    public void setOnClickListenerForCapacityFilter() {
        RecyclerView.LayoutManager capacityFilterLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        capacityFilterDropdown.setLayoutManager(capacityFilterLayoutManager);
        capacityFilterDropdown.setAdapter(new DropdownFilterAdapter(capacityOptions));
        filterCapacity = findViewById(R.id.dropdown_capacity_filter);
        filterCapacity.setOnClickListener(v -> {
            if (capacityFilterDropdown.getVisibility() == View.GONE) {
                capacityFilterDropdown.setVisibility(View.VISIBLE);
            } else {
                capacityFilterDropdown.setVisibility(View.GONE);
            }
        });
    }
    /**
     * Sets on click listener for amenitites filter.
     */
    public void setOnClickListenerForAmenititesFilter() {
        RecyclerView.LayoutManager amenitiesFilterLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        amenitiesFilterDropdown.setLayoutManager(amenitiesFilterLayoutManager);
        amenitiesFilterDropdown.setAdapter(new DropdownFilterAdapter(amenitiesOption));

        filterAmenities = findViewById(R.id.dropdown_amenities_filter);
        filterAmenities.setOnClickListener(v -> {
            if (amenitiesFilterDropdown.getVisibility() == View.GONE) {
                amenitiesFilterDropdown.setVisibility(View.VISIBLE);
            } else {
                amenitiesFilterDropdown.setVisibility(View.GONE);
            }
        });
    }
    /**
     * Update filters.
     * @param filtersList the filters list
     */
    public void updateFilters(List<String> filtersList) {
        if (selectedFiltersDisplay == null) {
            selectedFiltersDisplay = findViewById(R.id.layout_filters_display);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        selectedFiltersDisplay.setAdapter(new SelectedFilterAdapter(filtersList));
        selectedFiltersDisplay.setLayoutManager(layoutManager);
    }
    /**
     * gets room schedule.
     * @param listOfAllRoomsId         list of room ids.
     * @param listOfAllRoomsFromApollo list of all rooms in a location from apollo.
     */
    private void getResourcesFreeBusySchedule(
            List<String> listOfAllRoomsId,
            List<GetAllRoomsInALocationQuery.Room> listOfAllRoomsFromApollo) {
        new GsuitePresenter(credential, this, listOfAllRoomsId,
                listOfAllRoomsFromApollo).execute();
    }
    @Override
    public void onGetAllRoomsFromApolloError(ApolloException error) {
        // TODO notify do something about error
    }
    @Override
    public void onGetAllRoomsFromApolloSuccess(List<String> listOfAllRoomsId,
                                               List<GetAllRoomsInALocationQuery.Room> rooms) {
        getResourcesFreeBusySchedule(listOfAllRoomsId, rooms);
    }
    @Override
    public void onGsuitePresenterSuccess(final List<GetAllRoomsInALocationQuery.Room>
                                                 listOfAvailableRooms) {
        runOnUiThread(() -> {
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
            numberOfAvailableRooms.setVisibility(View.VISIBLE);
            String text = String.format(
                    Locale.getDefault(),
                    "Available Rooms (%d)", listOfAvailableRooms.size());
            numberOfAvailableRooms.setText(text);
            setRoomsAdapter(listOfAvailableRooms);
        });
        Log.e("All Available", listOfAvailableRooms.toString());
    }
    @Override
    public void onGsuitePresenterError(Exception error) {
        // TODO notify do something about error
        Log.e("Gsuite error", error.toString());
    }
    @Override
    public void onGetSelectedName() {
        chooseAccount();
    }
    /**
     * choose Google account.
     */
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                credential.setSelectedAccountName(accountName);
                getAllData();
            } else {
                startActivityForResult(// Start a dialog from which the user can choose an account
                        credential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(//Request the GET_ACCOUNTS permission via user dialog
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming activity result.
     * @param data        Intent (containing result data) returned by incoming activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.v("GooglePlay services", "This app requires Google "
                            + "Play Services."
                            + " Please install Google Play Services on "
                            + "your device and relaunch this app.");
                } else {
                    getAllData();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                resultCodeEqualsResultOkDataNotNull(resultCode, data);
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getAllData();
                }
                break;
            default:
                //intentionally left blank
        }
    }

    /**
     * Instance where resultCode Equals ResultOk and Data isn't null.
     * @param resultCode integer value of the result code
     * @param data Data recieved via intent
     */
    public void resultCodeEqualsResultOkDataNotNull(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null
                && data.getExtras() != null) {
            String accountName =
                    data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            if (accountName != null) {
                PreferenceManager
                        .getDefaultSharedPreferences(this)
                        .edit()
                        .putString(PREF_ACCOUNT_NAME, accountName)
                        .apply();
                credential.setSelectedAccountName(accountName);
                getAllData();
            }
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Intentionally left blank
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // Intentionally left blank
    }
}
