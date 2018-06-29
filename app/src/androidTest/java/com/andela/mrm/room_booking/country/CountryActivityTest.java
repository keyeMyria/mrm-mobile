package com.andela.mrm.room_booking.country;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andela.mrm.R;
import com.andela.mrm.room_booking.building.BuildingActivity;
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
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;


/**
 * CountryActivity test.
 */
@RunWith(AndroidJUnit4.class)
public class CountryActivityTest {

    /**
     * Country Activity Test Rule.
     */
    @Rule
    public ActivityTestRule<CountryActivity> mCountryActivityTestRule =
            new ActivityTestRule<>(CountryActivity.class);

    /**
     * Runs before each test case, to register the espresso idling resource.
     *
     * @throws Exception - throws an exception error when method fails
     */
    @Before
    public void setUp() throws Exception {

        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * Country setup page header text is visible.
     */
    @Test
    public void countrySetupPageHeaderTextIsVisible() {
        onView(withId(R.id.text_country_selection_header))
                .check(matches(withText(R.string.country_selection_header)));
    }

    /**
     * Expected recycler view items are displayed.
     */
    @Test
    public void expectedRecyclerViewItemsAreDisplayed() {
        String[] expectedCountries = new String[]{"Kenya", "Nigeria", "Uganda"};

        for (String country : expectedCountries) {
            onView(withText(country)).check(matches(isDisplayed()));
        }
    }

    /**
     * Clicking recycler view item opens building activity with the expected intents.
     */
    @Test
    public void clickingRecyclerViewItemOpensBuildingActivity() {
        final int mockPosition = 1;
        Intents.init();

        onView(withId(R.id.country_grid_view)).perform(RecyclerViewActions
                .actionOnItemAtPosition(mockPosition, click()));
        intended(allOf(
                hasComponent(BuildingActivity.class.getName()),
                hasExtra(equalTo("countryID"), equalTo(String.valueOf(mockPosition)))
        ));
        Intents.release();
    }

    /**
     * Teardown method.
     *
     * Runs after each test case, to unregister the espresso idling resource.
     */
    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }
}
