package com.jpkhawam.nabu;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.jpkhawam.nabu.util.EspressoIdlingResource;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EspressoScenarioUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void test1_firstRunTryTest() {
        try {
            onView(withResourceName("alertTitle")).check(doesNotExist());
        } catch (AssertionFailedError error) {
            onView(withResourceName("button2")).perform(click());
        }
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void test2_addNoteTest() {
        String testTitle = "Test note tile";
        String testContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                "Nulla ut ex iaculis, blandit velit eu, euismod ipsum. ";
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.floating_action_button)).check(matches(isDisplayed()));
        onView(withId(R.id.floating_action_button)).perform(click());

        onView(withId(R.id.note_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.input_note_title)).check(matches(isDisplayed()));
        onView(withId(R.id.input_note_title)).perform(typeText(testTitle));
        onView(withId(R.id.input_note_content)).check(matches(isDisplayed()));
        onView(withId(R.id.input_note_content)).perform(typeText(testContent));

        onView(withContentDescription("Go back to home page")).perform(click());

        onView(withId(R.id.notesRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.note_title)).check(matches(isDisplayed()));
        onView(withId(R.id.note_title)).check(matches(withText(testTitle)));
    }

    @Test
    public void test3_deleteNoteTest() {
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.notesRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.notesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.note_delete)).check(matches(isDisplayed()));
        onView(withId(R.id.note_delete)).perform(click());
        onView(withResourceName("alertTitle")).check(matches(isDisplayed()));
        onView(withResourceName("button2")).perform(click());

        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void test4_emptyTrashTest() {
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
        onView(withResourceName("main_toolbar")).check(matches(isDisplayed()));
        onView(withContentDescription("Navigation Drawer Open")).perform(click());
        onView(withId(R.id.trash)).check(matches(isDisplayed()));
        onView(withId(R.id.trash)).perform(click());

        onView(withId(R.id.notesRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.note_title)).check(matches(isDisplayed()));
        onView(withId(R.id.empty_trash)).check(matches(isDisplayed()));
        onView(withId(R.id.empty_trash)).perform(click());

        onView(withText(R.string.ask_are_you_sure)).check(matches(isDisplayed()));
        onView(withResourceName("button2")).perform(click());
    }
}