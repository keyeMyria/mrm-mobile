package com.andela.mrm;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

/**
 * Building Activity Class.
 */
public class BuildingActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        fragmentManager = getSupportFragmentManager();
            if (findViewById(R.id.frame_layout_container) != null) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.frame_layout_container, new BuildingFragment())
                        .commit();
                   }
    }
}




