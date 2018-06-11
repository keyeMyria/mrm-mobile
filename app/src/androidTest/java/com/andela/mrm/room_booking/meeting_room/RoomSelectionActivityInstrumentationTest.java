package com.andela.mrm.room_booking.meeting_room;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.andela.mrm.R;
import com.andela.mrm.room_booking.room_availability.views.RoomAvailabilityActivity;
import com.andela.mrm.util.EspressoIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * RoomSelectionActivity UI/Instrumentation Test.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class RoomSelectionActivityInstrumentationTest {
    public UiDevice mDevice;

    @Rule
    public ActivityTestRule<RoomSelectionActivity> mActivityTestRule =
            new ActivityTestRule<>(RoomSelectionActivity.class, true, false);


    /**
     * Runs before each test case, to register the espresso idling resource.
     *
     * @throws Exception - throws an exception error when method fails
     */
    @Before
    public void setUp() throws Exception {
        mDevice = UiDevice.getInstance(getInstrumentation());

        assertThat(mDevice, notNullValue());

        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());

        Intent intent = new Intent();
        intent.putExtra("countryID", "1");
        intent.putExtra("buildingID", "0");
        intent.putExtra("floorID", "3");

        mActivityTestRule.launchActivity(intent);
    }

    /**
     * Test case that describes/tests for the visibility(properly loaded) of the layout header text.
     *
     * @throws Exception - throws an exception error when test case fails
     */
    @Test
    public void testForCorrectLayoutHeaderText() throws Exception {
        String headerText =
                getTargetContext().getString(R.string.meeting_rooms_text);

        onView(withId(R.id.text_select_meeting_room))
                .check(matches(allOf(withText(headerText), isDisplayed())));
    }

    /**
     * Test case that checks the visibility of the recycler view.
     *
     * @throws Exception - throws an exception error when test case fails
     */
    @Test
    public void recyclerViewDisplayed() throws Exception {

        onView(withId(R.id.meeting_rooms_grid_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * Test case that describes/tests for the visibility(properly loaded) of the recyclerview items.
     * And tests that at most one(1) button is clicked or clickable
     *
     * @throws Exception - throws an exception error when test case fails
     */
    @Test
    public void testForCorrectDisplayedRecyclerViewItemsAndOnClickOfAny() throws Exception {
        int itemCount = getCount(R.id.meeting_rooms_grid_view);

        String[] rooms = {"Empire State", "Cognitio", "Ubuntu", "Naivasha"};

        onView(withId(R.id.meeting_rooms_grid_view))
                .check(matches(hasDescendant(withText("Ubuntu"))));

        onView(withId(R.id.meeting_rooms_grid_view)).check(matches(hasChildCount(itemCount)));

        for (int i = 0; i < itemCount; i++) {
            onView(withId(R.id.meeting_rooms_grid_view))
                    .check(matches(
                            allOf(
                                    atPositionOnView(
                                            i, withText(rooms[i]), R.id.meeting_room_button),
                                    isDisplayed())));
        }

        clickButton(R.id.meeting_rooms_grid_view, 0);

    }

    /**
     * Runs after each test case, to unregister the espresso idling resource.
     *
     * @throws Exception - throws an exception error when method fails
     */
    @After
    public void unregisterIdlingResource() throws Exception {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * Returns a view matcher that matches the targeted view hierarchy(position).
     *
     * @param position     - position of a specified item in the recyclerview
     * @param itemMatcher  - the current recyclerview child target view
     * @param targetViewId - resource id for the item in view (recyclerview's child element)
     * @return view matcher
     */
    public static Matcher<View> atPositionOnView(final int position,
                                                 final Matcher<View> itemMatcher,
                                                 final int targetViewId) {

        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has view id " + itemMatcher + " at position " + position);
            }

            @Override
            public boolean matchesSafely(final RecyclerView recyclerView) {
                RecyclerView.ViewHolder viewHolder =
                        recyclerView.findViewHolderForAdapterPosition(position);

                View targetView = viewHolder.itemView.findViewById(targetViewId);

                return itemMatcher.matches(targetView);
            }
        };
    }

    /**
     * Returns the current count/number of recyclerview items.
     *
     * @param recyclerViewId - resource id for the recyclerview in view
     * @return - int
     */
    public static int getCount(@IdRes int recyclerViewId) {
        final int[] itemCount = {0};
        Matcher<View> matcher = new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                itemCount[0] = ((RecyclerView) item).getAdapter().getItemCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        };

        onView(allOf(withId(recyclerViewId), isDisplayed())).check(matches(matcher));
        return itemCount[0];
    }

    /**
     * Clicks a particular button in the recyclerview.
     *
     * @param recyclerViewId - resource id for the recyclerview in view
     * @param position       - position of a specified item in the recyclerview
     * @throws Exception - throws an exception error when method fails
     */
    public void clickButton(int recyclerViewId, int position) throws Exception {
        Intents.init();

        onView(withId(recyclerViewId))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));

        intended(hasComponent(RoomAvailabilityActivity.class.getName()));

        allowPermissionsIfNeeded();

        Intents.release();
    }

    /**
     * Performs a click on the cancel button, when google's select account dialog appears.
     */
    public void allowPermissionsIfNeeded() {
        UiObject selectedEmailAccount = mDevice.findObject(new UiSelector()
                .textStartsWith("LYQ774V3KKK"));

        UiObject clickOk = mDevice.findObject(new UiSelector().text("OK"));

        if (selectedEmailAccount.exists()) {
            try {
                selectedEmailAccount.click();
                if (clickOk.exists()) {
                    clickOk.click();
                }
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.v("NO MATCH", "No match found for this UI");
        }
    }
}
