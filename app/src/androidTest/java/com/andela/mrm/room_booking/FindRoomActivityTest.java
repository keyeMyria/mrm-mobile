package com.andela.mrm.room_booking;

import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;

import com.andela.mrm.R;
import com.andela.mrm.room_booking.room_availability.views.FindRoomActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
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
public class FindRoomActivityTest {
    private UiDevice mDevice;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    public void setUp() throws Exception {
        mDevice = UiDevice.getInstance(getInstrumentation());
        accountSelector();
    }

    /**
     * The Activity test rule.
     */
    @Rule
    public ActivityTestRule<FindRoomActivity> activityTestRule =
            new ActivityTestRule<>(FindRoomActivity.class);

    /**
     * Find room activity is displayed correctly.
     */
    @Test
    public void FindRoomActivityIsDisplayedCorrectly() {
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
    public void filtersAreDisplayedCorrectly() {
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
    public void filteredResultLayoutIsDisplayed() {
        View view = activityTestRule.getActivity().findViewById(R.id.layout_filters_display);
        assertNotNull(view);
    }


    /**
     * Dropdowns are present.
     */
    @Test
    public void dropdownsArePresent() {
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
    public void closeButtonFinishesActivity() {
        onView(withId(R.id.close_find_room))
                .perform((click()));
        assertTrue(activityTestRule.getActivity().isDestroyed());
    }

    /**
     * Account selector.
     *
     * @throws UiObjectNotFoundException the ui object not found exception
     */
    public void accountSelector() throws UiObjectNotFoundException {
        UiObject selectAccountDialogue = mDevice.findObject(new UiSelector()
                .text("Choose an account"));

        UiObject selectAccount = mDevice.findObject(new UiSelector()
                .textStartsWith("LYQ774V3KKK"));

        UiObject clickOButton = mDevice.findObject(new UiSelector().text("OK"));

        if (selectAccountDialogue.exists()) {
            selectAccount.click();
            clickOButton.click();
        }
    }
}
