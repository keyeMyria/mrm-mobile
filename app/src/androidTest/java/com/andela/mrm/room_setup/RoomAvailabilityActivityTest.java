package com.andela.mrm.room_setup;


import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andela.mrm.R;
import com.andela.mrm.room_availability.RoomAvailabilityActivity;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Fred Adewole on 03/05/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RoomAvailabilityActivityTest {


    /**
     * The Activity test rule.
     */
    @Rule
    public ActivityTestRule<RoomAvailabilityActivity> activityTestRule =
            new ActivityTestRule<>(RoomAvailabilityActivity.class, true, false);


    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        String name;
        try {
            name = new GoogleAccountManager(InstrumentationRegistry.getTargetContext())
                    .getAccounts()[0].name;
        } catch (ArrayIndexOutOfBoundsException exception) {
            name = "";
        }

        PreferenceManager
                .getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext())
                .edit()
                .putString(RoomAvailabilityActivity.PREF_ACCOUNT_NAME, name)
                .apply();
        activityTestRule.launchActivity(new Intent());
    }

    /**
     * Count down timer is displayed.
     *
     * @throws Exception the exception
     */
    @Test
    public void countDownTimerIsDisplayed() throws Exception {
        onView(ViewMatchers.withId(R.id.frame_room_availability_countdown_timer))
                .check(matches(isDisplayed()));
        onView(withId(R.id.text_time_left))
                .check(matches(withText("Time Till Next Meeting")));
        onView(withId(R.id.frame_room_availability_countdown_timer))
                .check(matches(allOf(isDisplayed(), not(isClickable()))));

    }


    /**
     * Linear layout holding navigation is present.
     */
    @Test
    public void linearLayoutHoldingNavigationIsPresent() {
        onView(withId(R.id.linearLayout))
                .check(matches(allOf(isDisplayed(), hasChildCount(4), not(isClickable()))));
    }

    /**
     * Schedule button is displayed properly.
     */
    @Test
    public void scheduleButtonIsDisplayedProperly() {
        onView(withId(R.id.layout_schedule))
                .check(matches(allOf(isDisplayed(), hasChildCount(2), isClickable())));
    }

    /**
     * Find room button displayed.
     */
    @Test
    public void findRoomButtonDisplayed() {
        //TODO: UPDATE WHEN BUTTON IS CLICKABLE
        onView(withId(R.id.layout_find_room))
                .check(matches(allOf(isDisplayed(), hasChildCount(2), isClickable())));
    }

    /**
     * Support button displayed.
     */
    @Test
    public void supportButtonDisplayed() {
        //TODO: UPDATE WHEN BUTTON IS CLICKABLE
        onView(withId(R.id.layout_support))
                .check(matches(allOf(isDisplayed(), hasChildCount(2), not(isClickable()))));
    }

    /**
     * Room info button displayed.
     */
    @Test
    public void roomInfoButtonDisplayed() {
        //TODO: UPDATE WHEN BUTTON IS CLICKABLE
        onView(withId(R.id.layout_room_info))
                .check(matches(allOf(isDisplayed(), hasChildCount(2), isClickable())));
    }

    /**
     * Schedule button works as required.
     */
    @Test
    public void scheduleButtonWorksAsRequired() {
        Intents.init();
        onView(withId(R.id.layout_schedule))
                .check(matches(allOf(isDisplayed(), isClickable())));
        onView(withId(R.id.layout_schedule))
                .check(matches(hasChildCount(2)));
        Intents.release();
    }


    /**
     * Time line stripe is displayed and works as required.
     * TODO: test ignored as it would fail on FTL since data cannot be fetched for the the strip
     * This would be re-enabled once an hermetic test has been implemented or using a MockWebServer
     */
    @Test
    @Ignore
    public void timeLineStripeIsDisplayedAndWorksAsRequired() {

        onView(allOf(withId(R.id.view_time_line_strip),
                isDescendantOfA(allOf(withId(R.id.frame_room_availability_time_line),
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        not(isClickable()))),
                isClickable(),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.frame_room_availability_time_line),
                hasChildCount(1),
                hasDescendant(withId(R.id.view_time_line_strip)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                not(isClickable())))
                .check(matches(isDisplayed()))
                .perform(swipeLeft(), swipeRight());
    }
}
