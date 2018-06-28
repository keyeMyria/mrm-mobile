package com.andela.mrm.room_booking.floor;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andela.mrm.R;
import com.andela.mrm.room_booking.meeting_room.RoomSelectionActivity;
import com.andela.mrm.util.EspressoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by isioyemohammed on 05/06/2018.
 * mrm-mobile
 */
@RunWith(AndroidJUnit4.class)
public class FloorSelectionActivityTest {
    @Rule
    public ActivityTestRule<FloorSelectionActivity> mFloorSelectionActivityTestRule =
            new ActivityTestRule<>(FloorSelectionActivity.class, true,
                    false);

    /**
     * Stub for floors.
     */
    @Before
    public void intentWithStubbedFloors() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("countryID", "1");
        bundle.putString("buildingID", "0");
        intent.putExtras(bundle);

        mFloorSelectionActivityTestRule.launchActivity(intent);
    }

    /**
     * Test to check if Floor header text is visible.
     */
    @Test
    public void FloorHeaderTextVisible() {
        onView(withId(R.id.floor_text)).check(matches(isDisplayed()));
        onView(withId(R.id.floor_text)).check(matches(withText("What floor is the meeting room?")));
    }

    /**
     * Test to check if RecyclerView items are displayed.
     */
    @Test
    public void expectRecyclerViewItemsToBeDisplayed() {
        // the data at this point from the backend has changed.
        // The  recyclerview items now display the name of the wings instead of the floors which
        // might later change again
        // A permanent fix would be to use a dummy data for the activity instead of relying on what
        // the backend provides
        // TODO: Stated above
//        onView(withText("1st Floor")).check(matches(isDisplayed()));
//        onView(withText("2nd Floor")).check(matches(isDisplayed()));
//        onView(withText("3rd Floor")).check(matches(isDisplayed()));
//        onView(withText("4th Floor")).check(matches(isDisplayed()));
//        onView(withText("5th Floor")).check(matches(isDisplayed()));
        onView(withText("GoldCoast")).check(matches(isDisplayed()));
        onView(withText("Naija")).check(matches(isDisplayed()));
        onView(withText("Big Apple")).check(matches(isDisplayed()));


    }

    /**
     * Test suite to check of recycler view opens meeting room activity.
     */
    @Test
    public void clickingRecyclerViewLaunchesMeetingRoomActivity() {
        final int position = 2;
        Intents.init();
        onView(withId(R.id.floor_grid_view)).perform(RecyclerViewActions.actionOnItemAtPosition(
                position, click()));
        intended(allOf(
                hasComponent(RoomSelectionActivity.class.getName())
        ));
        Intents.release();
    }

    /**
     * TearDown method.
     * @throws Exception - Exception
     */
    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }
}
