package com.andela.mrm.room_setup;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.andela.mrm.R;
import com.andela.mrm.find_rooms.FindRoomActivity;
import com.andela.mrm.util.EspressoIdlingResource;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * The type Event schedule activity test.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4.class)
public class FindRoomActivityTest {

    /**
     * The Activity test rule.
     */
    @Rule
    public ActivityTestRule<FindRoomActivity> activityTestRule =
            new ActivityTestRule<>(FindRoomActivity.class, true, false);

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
                .putString(FindRoomActivity.PREF_ACCOUNT_NAME, name)
                .commit();
        activityTestRule.launchActivity(new Intent());
    }

    /**
     * Find room activity is displayed correctly.
     */
    @Test
    public void test1_FindRoomActivityIsDisplayedCorrectly() {

        onView(withId(R.id.close_find_room))
                .check(matches(isDisplayed()));

        onView(withId(R.id.filter_amenities))
                .check(matches(allOf(isDisplayed(), hasChildCount(2))));

        onView(withId(R.id.dropdown_capacity_filter))
                .check(matches(allOf(isDisplayed(), isClickable())));

        onView(withText("Find Available Room"))
                .check(matches(isDisplayed()));

        onView(withText("Clear Filters"))
                .check(matches(isDisplayed()));

        onView(withId(R.id.text_clear_filters))
                .check(matches(isDisplayed()));
    }

    /**
     * Filters are displayed correctly.
     */
    @Test
    public void test2_filtersAreDisplayedCorrectly() {
        onView(withId(R.id.layout_filters))
                .check(matches(allOf(isDisplayed(), hasChildCount(4))));

        onView(withId(R.id.filter_location))
                .check(matches(allOf(isDisplayed(), hasChildCount(2))));

        onView(withId(R.id.filter_availability))
                .check(matches(allOf(isDisplayed(), hasChildCount(2))));
    }

    /**
     * Filtered result layout is displayed.
     */
    @Test
    public void test3_filteredResultLayoutIsDisplayed() {
        View view = activityTestRule.getActivity().findViewById(R.id.layout_filters_display);
        assertNotNull(view);
    }


    /**
     * Dropdowns are present.
     */
    @Test
    public void test4_dropdownsArePresent() {
        View view = activityTestRule.getActivity().findViewById(R.id.layout_filters_display);
        assertNotNull(view);

        View availabilityFilter = activityTestRule.getActivity()
                .findViewById(R.id.filter_dropdown_availability);
        assertNotNull(availabilityFilter);

        View capacityDropdown = activityTestRule.getActivity()
                .findViewById(R.id.filter_dropdown_capacity);
        assertNotNull(capacityDropdown);

        View locationDropdown = activityTestRule.getActivity()
                .findViewById(R.id.filter_dropdown_location);
        assertNotNull(locationDropdown);

        View amenitiesDropdown = activityTestRule.getActivity()
                .findViewById(R.id.filter_dropdown_amenities);
        assertNotNull(amenitiesDropdown);
    }

    /**
     * Close button finishes activity.
     */
    @Test
    public void test5_closeButtonFinishesActivity() {
        onView(withId(R.id.close_find_room))
                .perform((click()));
        assertTrue(activityTestRule.getActivity().isDestroyed());
    }

}
