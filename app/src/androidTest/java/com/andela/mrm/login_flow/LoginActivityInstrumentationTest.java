package com.andela.mrm.login_flow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.andela.mrm.R;
import com.andela.mrm.room_setup.country.CountryActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * LoginActivity Instrumentation(UI) Test.
 */
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class LoginActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    /**
     * Test case that describes/tests for the visibility(properly loaded) of the LoginActivity.
     * And its fragment components
     *
     * @throws Exception - throws an exception error when test fails
     */
    @Test
    public void test1_ActivityFragmentsAreLoadedCorrectly() throws Exception {
        onView(withId(R.id.frame_first))
                .check(matches(allOf(
                        isCompletelyDisplayed(),
                        (not(isClickable())),
                        hasChildCount(1))));

        onView(withId(R.id.frame_second))
                .check(matches(allOf(
                        isCompletelyDisplayed(),
                        hasChildCount(3))));
    }

    /**
     * Test case that describes/tests for the visibility and functionality of the ViewPager adapter.
     *
     * @throws Exception - throws an exception error when test fails
     */
    @Test
    public void test2_ViewPagerDisplaysAndFunctionsCorrectly() throws Exception {
        onView(withId(R.id.view_pager_layout))
                .check(matches(isCompletelyDisplayed()));

        onView(withId(R.id.dot_layout))
                .check(matches(isCompletelyDisplayed()));

        final int numberOfPages = 3;

        final int[] headings = {
                R.string.onBoarding_header_one,
                R.string.onBoarding_header_two,
                R.string.onBoarding_header_three
        };

        final int[] descriptions = {
                R.string.onBoarding_description_one,
                R.string.onBoarding_description_two,
                R.string.onBoarding_description_three
        };
        for (int i = 0; i < numberOfPages; i++) {
            // Checks that the ViewPager is currently in its (i)th page/pane
            onView(withId(R.id.view_pager_layout)).check(matches(inPage(i)));

            if (i <= numberOfPages - 2) { // perform swipe if not on last page
                onView(allOf(withId(R.id.image_slide), withContentDescription("slider image"),
                        childAtPosition(withParent(withId(R.id.view_pager_layout)), 1),
                        isCompletelyDisplayed()))
                        .check(matches(isCompletelyDisplayed()));

                onView(allOf(withId(R.id.text_header), childAtPosition(
                        withParent(withId(R.id.view_pager_layout)),
                        2), isCompletelyDisplayed()))
                        .check(matches(withText(headings[i])));

                onView(allOf(withId(R.id.text_description), childAtPosition(
                        withParent(withId(R.id.view_pager_layout)),
                        3), isDisplayed(), withText(descriptions[i])))
                        .check(matches(withText(descriptions[i])));

                onView(allOf(withId(R.id.view_pager_layout), childAtPosition(allOf(
                        withId(R.id.frame_second), childAtPosition(
                                withId(R.id.layout_login), 1)),
                        0)))
                        .perform(swipeLeft());
            } else {

                onView(
                        allOf(withId(R.id.image_slide), withContentDescription("slider image"),
                                childAtPosition(
                                        withParent(withId(R.id.view_pager_layout)),
                                        1),
                                isCompletelyDisplayed()))
                        .check(matches(isCompletelyDisplayed()));

                onView(allOf(withId(R.id.text_header), childAtPosition(
                        withParent(withId(R.id.view_pager_layout)),
                        2), isCompletelyDisplayed()))
                        .check(matches(withText(headings[i])));

                onView(allOf(hasSibling(withId(R.id.text_header)),
                        hasSibling(withId(R.id.image_slide)),
                        withId(R.id.text_description),
                        withText(descriptions[i])
                )).check(matches(withText(descriptions[i])));
            }
        }
    }

    /**
     * Test case that describes/tests for the visibility and functionality of the login button.
     *
     * @throws Exception - throws an exception error when test fails
     */
    @Test
    public void test3_LoginButtonDisplaysAndFunctionsCorrectly() throws Exception {
        Intents.init();

        ViewInteraction loginButton = onView(
                allOf(withId(R.id.button_login),
                        withContentDescription("google login button"),
                        childAtPosition(childAtPosition(
                                withId(R.id.frame_first), 0), 0)));

        loginButton
                .check(matches(isDisplayed())) //checks that the button is displayed
                .perform(click()); // checks that the button is clickable

        mActivityTestRule.launchActivity(new Intent());

        intended(hasComponent(CountryActivity.class.getName())); // checks that the CountryActivity
                                                                 // is launched

        Intents.release();
    }

    /**
     * Returns a matcher that matches {@link ViewGroup}s based on targeted view hierarchy(position).
     *
     * @param parentMatcher - the current parent view target
     * @param position      - position of the view in hierarchy
     * @return boolean
     */
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /**
     * Returns a matcher that matches {@link ViewPager}s based on currentItem property value.
     *
     * @param page {@link Matcher} of ViewPager's currentItem
     * @return boolean
     */
    @NonNull
    public static Matcher<View> inPage(final int page) {

        return new BoundedMatcher<View, ViewPager>(ViewPager.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("in page: " + page);
            }

            @Override
            public boolean matchesSafely(final ViewPager viewPager) {
                return viewPager.getCurrentItem() == page;
            }
        };
    }
}
