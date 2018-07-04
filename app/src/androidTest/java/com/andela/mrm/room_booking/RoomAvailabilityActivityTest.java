
//Todo: to be implemented when a fix for the UiAutomator is found
//package com.andela.mrm.room_booking;
//
//
//import android.content.Intent;
//import android.support.test.espresso.intent.Intents;
//import android.support.test.espresso.matcher.ViewMatchers;
//import android.support.test.filters.LargeTest;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//
//import com.andela.mrm.R;
//import com.andela.mrm.room_booking.room_availability.views.EventScheduleActivity;
//import com.andela.mrm.room_booking.room_availability.views.RoomAvailabilityActivity;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.intent.Intents.intended;
//import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
//import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.CoreMatchers.allOf;
//import static org.hamcrest.core.IsNot.not;
//
///**
// * Created by Fred Adewole on 03/05/2018.
// */
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class RoomAvailabilityActivityTest {
//
//    /**
//     * The Activity test rule.
//     */
//    @Rule
//    public ActivityTestRule<RoomAvailabilityActivity> activityTestRule =
//            new ActivityTestRule<>(RoomAvailabilityActivity.class);
//
//    /**
//     * Sets up.
//     *
//     * @throws Exception the exception
//     */
//    @Before
//    public void setUp() throws Exception {
//        activityTestRule.getActivity();
//    }
//
//    /**
//     * Tear down.
//     *
//     * @throws Exception the exception
//     */
//    @After
//    public void tearDown() throws Exception {
//        activityTestRule.finishActivity();
//    }
//
//    /**
//     * Count down timer is displayed.
//     *
//     * @throws Exception the exception
//     */
//    @Test
//    public void countDownTimerIsDisplayed() throws Exception {
//        Intents.init();
//        onView(ViewMatchers.withId(R.id.frame_room_availability_countdown_timer))
//                .check(matches(isDisplayed()));
//        onView(withId(R.id.text_time_left))
//                .check(matches(withText("Time Till Next Meeting")));
//        onView(withId(R.id.frame_room_availability_countdown_timer))
//                .check(matches(allOf(isDisplayed(), not(isClickable()))));
//
//    }
//
//
//    /**
//     * Linear layout holding navigation is present.
//     */
//    @Test
//    public void linearLayoutHoldingNavigationIsPresent() {
//        Intents.init();
//        onView(withId(R.id.linearLayout))
//                .check(matches(allOf(isDisplayed(), hasChildCount(4), not(isClickable()))));
//    }
//
//    /**
//     * Schedule button is displayed properly.
//     */
//    @Test
//    public void scheduleButtonIsDisplayedProperly() {
//        Intents.init();
//        onView(withId(R.id.layout_schedule))
//                .check(matches(allOf(isDisplayed(), hasChildCount(2), isClickable())));
//    }
//
//    /**
//     * Find room button displayed.
//     */
//    @Test
//    public void findRoomButtonDisplayed() {
//        Intents.init();
//        onView(withId(R.id.layout_find_room))
//                .check(matches(allOf(isDisplayed(), hasChildCount(2), isClickable())));
//    }
//
//    /**
//     * Support button displayed.
//     */

//    @Test
//    public void supportButtonDisplayed() {
//        Intents.init();
//        onView(withId(R.id.layout_support))
//                .check(matches(allOf(isDisplayed(), hasChildCount(2), not(isClickable()))));
//    }

//
//    /**
//     * Room info button displayed.
//     */
//    @Test
//    public void roomInfoButtonDisplayed() {
//        Intents.init();
//        onView(withId(R.id.layout_room_info))
//                .check(matches(allOf(isDisplayed(), hasChildCount(2), isClickable())));
//    }
//
//    /**
//     * Schedule button works as required.
//     */
//    @Test
//    public void scheduleButtonWorksAsRequired() {
//        Intents.init();
//        onView(withId(R.id.layout_schedule))
//                .check(matches(allOf(isDisplayed(), isClickable())));
//        onView(withId(R.id.layout_schedule))
//                .check(matches(hasChildCount(2)));
//    }
//
//    /**
//     * Schedule button displays another activity.
//     */
//    @Test
//    public void scheduleButtonDisplaysAnotherActivity() {
//        Intents.init();
//        onView(withId(R.id.layout_schedule))
//                .check(matches(isDisplayed()))
//                .perform((click()));
//        onView(allOf(withId(R.id.layout_event_recycler_view)))
//                .check(matches(isDisplayed()));
//        onView(withId(R.id.text_current_date))
//                .check(matches(isDisplayed()));
//        onView(withId(R.id.layout_close_event_schedule))
//                .check(matches(isDisplayed()))
//                .perform(click());
//        activityTestRule.launchActivity(new Intent());
//        intended(hasComponent(EventScheduleActivity.class.getName()));
//        Intents.release();
//    }
//}
//

