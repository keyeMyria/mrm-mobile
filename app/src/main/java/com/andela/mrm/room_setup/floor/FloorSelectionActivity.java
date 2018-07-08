package com.andela.mrm.room_setup.floor;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andela.mrm.R;

/**
 * Floor selection activity.
 */
public class FloorSelectionActivity extends AppCompatActivity {
    public static final String ARG_BUILDING_ID = "buildingID";
    public static final String ARG_COUNTRY_ID = "countryID";

    String countryID;
    String buildingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_selection);
        getValues();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BUILDING_ID, buildingID);
        args.putSerializable(ARG_COUNTRY_ID, countryID);
        FloorSelectionFragment fragment = new FloorSelectionFragment();
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.floor_layout_container) != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.floor_layout_container, fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * getValues() function for country and building intent.
     */
    public void getValues() {
        Intent intent = this.getIntent();
        countryID = intent.getExtras().getString("countryID");
        buildingID = intent.getExtras().getString("buildingID");
    }
}
