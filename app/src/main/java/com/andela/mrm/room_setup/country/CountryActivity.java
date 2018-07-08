package com.andela.mrm.room_setup.country;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.andela.mrm.R;

/**
 * Created by andeladeveloper on 06/04/2018.
 */
public class CountryActivity extends AppCompatActivity {
    /**
     * The Manager.
     */
    FragmentManager manager;
    /**
     * The Transaction.
     */
    FragmentTransaction transaction;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_country);

        CountryFragment countryFragment = new CountryFragment();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        transaction.add(R.id.frame_layout, countryFragment, "countryFragment");
        transaction.commit();
    }
}
