package com.andela.mrm.room_booking.room_availability.views;

/**
 * Created by Fred Adewole on 03/06/2018.
 */

import android.Manifest;
import android.accounts.AccountManager;
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
import com.andela.mrm.R;
import com.andela.mrm.adapter.DropdownFilterAdapter;
import com.andela.mrm.adapter.FindRoomAdapter;
import com.andela.mrm.adapter.SelectedFilterAdapter;
import com.andela.mrm.presenter.GetAllRoomsInALocationFromApolloPresenter;
import com.andela.mrm.presenter.GsuitePresenter;
import com.apollographql.apollo.exception.ApolloException;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.andela.mrm.room_booking.room_availability.views.RoomAvailabilityActivity.REQUEST_PERMISSION_GET_ACCOUNTS;


/**
 * The type Find room activity.
 */
public class FindRoomActivity extends AppCompatActivity implements
        GetAllRoomsInALocationFromApolloPresenter.IOnGetAllRoomsFromApolloCallback,
        GsuitePresenter.IOnGsuitePresenterResponse, EasyPermissions.PermissionCallbacks {
    public static final String PREF_ACCOUNT_NAME = "accountName";
    /**
     * The Lagos location id.
     */
    static final int LAGOS_LOCATION_ID = 2;
    /**
     * The Request account picker.
     */
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    /**
     * The Request google play services.
     */
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    /**
     * The Request authorization.
     */
    static final int REQUEST_AUTHORIZATION = 1001;
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};
    /**
     * The Availabilty options.
     */
    final List<String> availabiltyOptions = new ArrayList<>();
    /**
     * The Location options.
     */
    final List<String> locationOptions = new ArrayList<>();
    /**
     * The Capacity options.
     */
    final List<String> capacityOptions = new ArrayList<>();
    /**
     * The Amenities option.
     */
    final List<String> amenitiesOption = new ArrayList<>();
    /**
     * The Filters.
     */
    final List<String> filters = new ArrayList<>();
    /**
     * The Filter availability.
     */
    public ImageView filterAvailability, /**
     * The Filter location.
     */
    filterLocation, /**
     * The Filter capacity.
     */
    filterCapacity, /**
     * The Filter amenities.
     */
    filterAmenities;
    /**
     * The Availability filter dropdown.
     */
    public RecyclerView availabilityFilterDropdown, /**
     * The Location filter dropdown.
     */
    locationFilterDropdown, /**
     * The Capacity filter dropdown.
     */
    capacityFilterDropdown,
    /**
     * The Amenities filter dropdown.
     */
    amenitiesFilterDropdown, /**
     * The Selected filters display.
     */
    selectedFiltersDisplay;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView numberOfAvailableRooms;
    /**
     * The Find room recycler view.
     */
    RecyclerView findRoomRecyclerView;
    /**
     * The Credential.
     */
    GoogleAccountCredential credential;
    private ImageView closeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);
        shimmerFrameLayout = findViewById(R.id.layout_shimmer);
        numberOfAvailableRooms = findViewById(R.id.text_result_count);

        shimmerFrameLayout.startShimmerAnimation();
        numberOfAvailableRooms.setVisibility(View.GONE);

        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        getAllData();

        availabiltyOptions.add("Available");
        availabiltyOptions.add("Unavailable");

        locationOptions.add("Block A, First Floor");
        locationOptions.add("Gold Coast, First Floor");
        locationOptions.add("Big Apple, Fourth Floor");
        locationOptions.add("Naija, Third Floor");

        capacityOptions.add("5 participants");
        capacityOptions.add("10 participants");
        capacityOptions.add("15 participants");
        capacityOptions.add("20 participants");

        amenitiesOption.add("Apple TV");
        amenitiesOption.add("Jabra speaker");
        amenitiesOption.add("Headphones");
        amenitiesOption.add("Projector");

        filters.add("Available");
        filters.add("Block A, First Floor");
        filters.add("10 participants");
        filters.add("Headphones");

        closeActivity = findViewById(R.id.close_find_room);
        findRoomRecyclerView = findViewById(R.id.recycler_view_filter_result);
        availabilityFilterDropdown = findViewById(R.id.filter_dropdown_availability);
        locationFilterDropdown = findViewById(R.id.filter_dropdown_location);
        capacityFilterDropdown = findViewById(R.id.filter_dropdown_capacity);
        amenitiesFilterDropdown = findViewById(R.id.filter_dropdown_amenities);

        setOnClickListenerForAvailabilityFilter();
        setOnClickListenerForLocationFilter();
        setOnClickListenerForCapacityFilter();
        setOnClickListenerForAmenititesFilter();
        updateFilters(filters);

        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        availabilityFilterDropdown.setAdapter(new DropdownFilterAdapter(availabiltyOptions));

        filterAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availabilityFilterDropdown.getVisibility() == View.GONE) {
                    availabilityFilterDropdown.setVisibility(View.VISIBLE);
                } else {
                    availabilityFilterDropdown.setVisibility(View.GONE);
                }
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
        filterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationFilterDropdown.getVisibility() == View.GONE) {
                    locationFilterDropdown.setVisibility(View.VISIBLE);
                } else {
                    locationFilterDropdown.setVisibility(View.GONE);
                }
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
        filterCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capacityFilterDropdown.getVisibility() == View.GONE) {
                    capacityFilterDropdown.setVisibility(View.VISIBLE);
                } else {
                    capacityFilterDropdown.setVisibility(View.GONE);
                }
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
        filterAmenities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amenitiesFilterDropdown.getVisibility() == View.GONE) {
                    amenitiesFilterDropdown.setVisibility(View.VISIBLE);
                } else {
                    amenitiesFilterDropdown.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Update filters.
     *
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
     *
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);

                numberOfAvailableRooms.setVisibility(View.VISIBLE);
                numberOfAvailableRooms.setText("Available Rooms ("
                        + listOfAvailableRooms.size()
                        + ")");
                setRoomsAdapter(listOfAvailableRooms);

            }
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
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        credential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
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
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Intentionally left blank
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // Intentionally left blank
    }
}