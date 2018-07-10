package com.andela.mrm.room_information;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andela.mrm.GetRoomsInABlockQuery;
import com.andela.mrm.Injection;
import com.andela.mrm.R;
import com.andela.mrm.fragment.Room;
import com.andela.mrm.room_information.resources_info.ResourcesInfoContract;
import com.andela.mrm.room_information.resources_info.ResourcesInfoFragment;
import com.andela.mrm.room_information.resources_info.ResourcesInfoPresenter;
import com.andela.mrm.room_information.similar_rooms.SimilarRoomsFragment;
import com.andela.mrm.util.NetworkConnectivityChecker;

import java.util.List;

/**
 * The Room information activity class.
 */
public class RoomInformationActivity extends AppCompatActivity implements
        ResourcesInfoContract.View,
        SimilarRoomsFragment.Callbacks, ResourcesInfoFragment.Callbacks {

    private static final String EXTRA_ROOM_ID = "com.andela.mrm.room_id";

    ResourcesInfoContract.Actions mPresenter;
    TextView mCapacityText;
    TextView mRoomNameText;
    TextView mLocationText;

    private List<GetRoomsInABlockQuery.Room> mRooms;
    private Room mRoom;

    private TabLayout mTabLayout;
    ViewPager mViewPager;
    ProgressBar mProgressBar;
    private ImageButton mCloseActivityBtn;

    /**
     * Prepares the intent required to launch this activity.
     *
     * @param packageContext the package context
     * @param roomId         the room id
     * @return the intent
     */
    public static Intent newIntent(Context packageContext, int roomId) {
        Intent intent = new Intent(packageContext, RoomInformationActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_information);

        int roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, 0);
        mPresenter = new ResourcesInfoPresenter(this,
                Injection.provideResourcesInfoData(this),
                roomId
        );

        mViewPager = findViewById(R.id.view_pager_fragment_container_activity_room_info);
        mTabLayout = findViewById(R.id.tablayout_room_info);
        mProgressBar = findViewById(R.id.progressbar_activity_room_information);

        mCloseActivityBtn = findViewById(R.id.button_close_room_info);
        mCapacityText = findViewById(R.id.text_capacity_number_activity_room_info);
        mRoomNameText = findViewById(R.id.text_room_name_activity_room_info);
        mLocationText = findViewById(R.id.text_room_location_activity_room_info);

        mCloseActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new ViewPagerAdapter(this, getSupportFragmentManager()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.fetchRoomDetails();
    }

    @Override
    public void showRoomInfo(final Room room) {
        mRoom = room;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCapacityText.setText(String.valueOf(room.capacity()));
                mRoomNameText.setText(room.name());
                String roomLocation = room.floor().block().name() + ", " + room.floor().name();
                mLocationText.setText(roomLocation);

                showRoomResources(room);
            }
        });
    }


    @Override
    public void showSimilarRooms(final List<GetRoomsInABlockQuery.Room> rooms) {
        mRooms = rooms;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragmentShowSimilarRooms(rooms);
            }
        });
    }

    /**
     * Show similar rooms fragment.
     * @param rooms - rooms
     */
    void fragmentShowSimilarRooms(List<GetRoomsInABlockQuery.Room> rooms) {
        SimilarRoomsFragment similarRoomsFragment = getSimilarRoomsFragment();
        if (similarRoomsFragment != null) {
            similarRoomsFragment.setSimilarRooms(rooms);
        }
    }

    /**
     * Displays list of room resources in the RoomResourcesFragment.
     *
     * @param room room
     */
    void showRoomResources(Room room) {
        ResourcesInfoFragment resourcesInfoFragment = getResourcesInfoFragment();
        if (resourcesInfoFragment != null) {
            resourcesInfoFragment.showResourcesList(room.resources());
        }
    }

    @Override
    public void showLoadingIndicator(final boolean isLoading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isLoading) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mViewPager.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void showErrorMessage(final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.content), message, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.snackbar_retry_text, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.fetchRoomDetails();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean isNetworkAvailable() {
        return NetworkConnectivityChecker.isDeviceOnline(this);
    }

    /**
     * Retrieves an instantiated instance of the ResourcesInfoFragment.
     *
     * @return an instance of ResourcesInfoFragment or null
     */
    ResourcesInfoFragment getResourcesInfoFragment() {
        int currentViewPagerFragment = mViewPager.getCurrentItem();
        if (currentViewPagerFragment == 0) {
            return (ResourcesInfoFragment)
                    mViewPager.getAdapter().instantiateItem(mViewPager, currentViewPagerFragment);
        }
        return null;
    }

    /**
     * Get similar rooms fragment method.
     *
     * @return null
     */
    SimilarRoomsFragment getSimilarRoomsFragment() {
        int currentViewPagerFragment = mViewPager.getCurrentItem();
        if (currentViewPagerFragment == 1) {
            return (SimilarRoomsFragment)
                    mViewPager.getAdapter().instantiateItem(mViewPager, currentViewPagerFragment);
        }
        return null;
    }

    @Override
    public void onViewLoaded() {
        if (mRooms != null) {
            fragmentShowSimilarRooms(mRooms);
        }
    }

    @Override
    public void onFragmentViewsLoaded() {
        if (mRoom != null) {
            showRoomResources(mRoom);
        }
    }

    /**
     * The type View pager adapter.
     */
    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private static final int NUM_ITEMS = 2;

        private final Context mContext;

        /**
         * Instantiates a new View pager adapter.
         *
         * @param context the context
         * @param fm      the fm
         */
        ViewPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return ResourcesInfoFragment.newInstance();
            } else if (position == 1) {
                return new SimilarRoomsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.room_amenities);
                case 1:
                    return mContext.getString(R.string.similar_rooms);
                default:
                    return null;
            }
        }
    }
}



