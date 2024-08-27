package com.example.comprasmu.test.activities;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import com.example.comprasmu.R;
import com.example.comprasmu.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test1Canceladas {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityTestRule = new ActivityScenarioRule <>(LoginActivity.class);

    @Test
    public void mainActivityTest2() {
       /* ViewInteraction skipBtn = onView(allOf(withId(R.id.skipButton), withText("Skip"),isDisplayed()));
        skipBtn.perform(click());¨*/

    /*    ViewInteraction loginBtn = onView(allOf(withId(R.id.username), withText("Login"),isDisplayed()));
        loginBtn.perform(click());*/

        ViewInteraction usernameTxt = onView(withId(R.id.username));
        usernameTxt.perform(typeText("monbi202010@gmail.com"), closeSoftKeyboard());

       ViewInteraction pwdTxt = onView(withId(R.id.password));
       // pwdTxt.perform(scrollTo(), replaceText("123456789MISO"), closeSoftKeyboard());
        pwdTxt.perform(typeText("123456789MISO"), closeSoftKeyboard());
        ViewInteraction confirmLoginBtn = onView(allOf(withId(R.id.login), withText(R.string.action_sign_in)));
        confirmLoginBtn.perform( click());
        /* confirmLoginBtn.perform(scrollTo(), click());*/
    }
}
