package com.andela.mrm.room_information;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.andela.mrm.Injection;
import com.andela.mrm.R;
import com.andela.mrm.room_information.resources_info.MockResourcesInfoRepo;

import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * The Room Information Activity test.
 */
@RunWith(AndroidJUnit4.class)
public class RoomInformationActivityTest {

    private static final int ROOM_ID = 1;

    private MockResourcesInfoRepo mMockResourcesInfoRepo = (MockResourcesInfoRepo)
            Injection.provideResourcesInfoData(null, ROOM_ID);

    /**
     * Activity test rule - Instantiates the activity under test.
     */
    @Rule
    public ActivityTestRule<RoomInformationActivity> mActivityTestRule =
            new ActivityTestRule<>(RoomInformationActivity.class, true, false);

    /**
     * Launches the activity with the required intent.
     */
    @Before
    public void setUpWithIntent() {
        Intent intent = RoomInformationActivity
                .newIntent(InstrumentationRegistry.getTargetContext(), ROOM_ID);
        mActivityTestRule.launchActivity(intent);
    }

    /**
     * Room details displayed in ui.
     */
    @Test
    public void roomDetailsDisplayedInUi() {
        String roomLocation = mMockResourcesInfoRepo.getRoom().floor().block().name()
                + ", "
                + mMockResourcesInfoRepo.getRoom().floor().name();

        onView(allOf(withId(R.id.text_room_name_activity_room_info),
                withText(mMockResourcesInfoRepo.getRoom().name())))
                .check(matches(isDisplayed()));
        onView(withId(R.id.text_capacity_number_activity_room_info))
                .check(matches(
                        withText(String.valueOf(mMockResourcesInfoRepo.getRoom().capacity()))));
        onView(allOf(withId(R.id.text_room_location_activity_room_info),
                withText(roomLocation))).check(matches(isDisplayed()));
    }

    /**
     * Similar rooms tab click.
     */
    @Test
    public void testSimilarRoomsTabClick() {
        Matcher<View> matcher = AllOf.allOf(withText("Similar Rooms"),
                isDescendantOfA(withId(R.id.tablayout_room_info)));
        onView(matcher).perform(click());
        SystemClock.sleep(800);
        onView(withId(R.id.similar_room_recycler_view))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.similar_room_recycler_view))
                .check(matches(hasDescendant(withId(R.id.similar_room_card))));
    }

    /**
     * Close button finishes activity.
     */
    @Test
    public void testCloseButtonFinishesActivity() {
        onView(withId(R.id.button_close_room_info))
                .perform(click());
        assertTrue(mActivityTestRule.getActivity().isDestroyed());
    }
}
