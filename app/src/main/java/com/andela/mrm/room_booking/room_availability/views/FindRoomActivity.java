package com.andela.mrm.room_booking.room_availability.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.andela.mrm.R;
import com.andela.mrm.adapter.DropdownFilterAdapter;
import com.andela.mrm.adapter.FindRoomAdapter;
import com.andela.mrm.adapter.SelectedFilterAdapter;
import com.andela.mrm.room_booking.room_availability.models.Rooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Find room activity.
 */
public class FindRoomActivity extends AppCompatActivity {
    private ImageView closeActivity;
    /**
     * The Find room recycler view.
     */
    RecyclerView findRoomRecyclerView;
    /**
     * The Room.
     */
    Rooms room;
    public ImageView filterAvailability, filterLocation, filterCapacity, filterAmenities;
    public RecyclerView availabilityFilterDropdown, locationFilterDropdown, capacityFilterDropdown,
    amenitiesFilterDropdown, selectedFiltersDisplay;
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
    final List<String> filters = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);
        String[] available = {"Available", "Unavailable"};
        availabiltyOptions.addAll(Arrays.asList(available));
        String[] locations = {"Block A, First Floor", "Gold Coast, First Floor",
                "Big Apple, Fourth Floor", "Naija, Third Floor"};
        locationOptions.addAll(Arrays.asList(locations));
        String[] capacities = {"5 participants", "10 participants", "15 participants",
                "20 participants"};
        capacityOptions.addAll(Arrays.asList(capacities));
        String[] amenities = {"Apple TV", "Jabra speaker", "Headphones", "Projector"};
        amenitiesOption.addAll(Arrays.asList(amenities));
        String[] filter = {"Available", "Block A, First Floor", "10 participants", "Headphones"};
        filters.addAll(Arrays.asList(filter));
        closeActivity = findViewById(R.id.close_find_room);
        findRoomRecyclerView = findViewById(R.id.recycler_view_filter_result);
        availabilityFilterDropdown = findViewById(R.id.filter_dropdown_availability);
        locationFilterDropdown = findViewById(R.id.filter_dropdown_location);
        capacityFilterDropdown = findViewById(R.id.filter_dropdown_capacity);
        amenitiesFilterDropdown = findViewById(R.id.filter_dropdown_amenities);
        onClickListenerGenerator();
        updateFilters(filters);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        findRoomRecyclerView.setAdapter(new FindRoomAdapter(room.initializeRooms(), this));
        findRoomRecyclerView.setLayoutManager(layoutManager);
        closeActivity.setOnClickListener(v -> finish());
    }

    /**
     * Method to create all onclick listeners.
     */
    public void onClickListenerGenerator() {
        setOnClickListenerForAvailabilityFilter();
        setOnClickListenerForLocationFilter();
        setOnClickListenerForCapacityFilter();
        setOnClickListenerForAmenititesFilter();
    }

    /**
     * Sets on click listener for availability filter.
     */
    public void setOnClickListenerForAvailabilityFilter() {
        filterAvailability = findViewById(R.id.dropdown_availability_filter);
        RecyclerView.LayoutManager availabilityFilterLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                        false);
        availabilityFilterDropdown.setLayoutManager(availabilityFilterLayoutManager);
        availabilityFilterDropdown.setAdapter(new DropdownFilterAdapter(availabiltyOptions));

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
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                        false);
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
}
