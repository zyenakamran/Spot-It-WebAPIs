package com.example.ass2_i200802_i200707;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
public class test {

    @Rule
    public ActivityScenarioRule<Login> activityRule = new ActivityScenarioRule<>(Login.class);

    @Test
    public void testCorrectLogin() {
        // Replace with the correct view IDs
        onView(withId(R.id.emailTextBox)).perform(typeText("zyenak@gmail.com"));
        onView(withId(R.id.passwordTextBox)).perform(typeText("spotit"));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.activity_home)).check(matches(isDisplayed()));
    }


    @Test
        public void testInCorrectLogin() {
            onView(withId(R.id.emailTextBox)).perform(typeText("invalid"));
            onView(withId(R.id.passwordTextBox)).perform(typeText("invalid"));
            onView(withId(R.id.loginButton)).perform(click());
            // Add assertion to check if the toast for sign-in failure is displayed
            onView(withId(R.id.activity_login)).check(matches(isDisplayed()));
        }

        @Test
        public void testSignUpFromLogin() {
            onView(withId(R.id.SignupText)).perform(click());
            // Add assertion to check if the sign-up activity is displayed
            onView(withId(R.id.activity_registration)).check(matches(isDisplayed()));
        }

}





