package com.andela.mrm.room_booking.building;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andela.mrm.R;
import com.andela.mrm.room_booking.floor.FloorSelectionActivity;
import com.andela.mrm.util.EspressoIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * BuildingActivity test.
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class BuildingActivityInstrumentationTest {

    /**
     * Building Activity Test Rule.
     */
    @Rule
    public ActivityTestRule<BuildingActivity> mActivityTestRule =
            new ActivityTestRule<BuildingActivity>(BuildingActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, BuildingActivity.class);
                    result.putExtra("countryID", "0");
                    return result;
                }
            };

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
     * Runs before each test case, to register the espresso idling resource.
     */
    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * Test case that describes/tests for the visibility(properly loaded) of the layout header text.
     */
    @Test
    public void testForCorrectLayoutHeaderText() {
        String headerText = InstrumentationRegistry
                .getTargetContext().getString(R.string.building_text);

        onView(withId(R.id.building_text))
                .check(matches(allOf(withText(headerText), isDisplayed())));
    }

    /**
     * Test case that checks the visibility of the recycler view.
     */
    @Test
    public void recyclerViewDisplayed() {

        onView(withId(R.id.building_grid_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * Test case that describes/tests for the visibility(properly loaded) of the recyclerview items.
     * And tests that at least one(1) button is clicked or clickable
     */
    @Test
    public void testForCorrectDisplayedRecyclerViewItemsAndOnClickOfAny() { // failing
        int itemCount = getCount(R.id.building_grid_view);

        onView(withId(R.id.building_grid_view))
                .check(matches(isDisplayed()));

        onView(withId(R.id.building_grid_view)).check(matches(hasChildCount(itemCount)));
        Intents.init();

        onView(withId(R.id.building_grid_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(FloorSelectionActivity.class.getName()));

        Intents.release();

    }

    /**
     * Runs after each test case, to unregister the espresso idling resource.
     */
    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }
}

