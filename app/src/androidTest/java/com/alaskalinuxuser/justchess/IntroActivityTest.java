package com.alaskalinuxuser.justchess;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.widget.Spinner;

import java.util.HashMap;

import static android.support.v4.content.ContextCompat.startActivity;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

public class IntroActivityTest {
    @Rule
    public ActivityTestRule<IntroActivity> mActivityRule = new ActivityTestRule<>(IntroActivity.class);

    /**
     * Make sure that items are loaded into spinner and that changing spinner changes game mode
     */
    @Test
    public void view_updates() {
        IntroActivity activity = mActivityRule.getActivity();
        Spinner gameModeSpinner = (Spinner) activity.findViewById(R.id.chooseGameMode);
        HashMap<String, GameMode> gameModes = activity.gameModes;

        //Test that classic mode gets loaded into spinner
        assertTrue( gameModeSpinner.getAdapter().getCount() >= 1);
        assertTrue( gameModeSpinner.getSelectedItem().toString() == "Classic" );
        assertTrue( activity.currentGameMode == "Classic" );
        assertTrue( gameModes.get("Classic").getNewBoard() == "RNBQKBNRPPPPPPPP********************************pppppppprnbqkbnr" );

        if(gameModeSpinner.getAdapter().getCount() > 1) {
            //If program has modes other than classic, make sure that it can change between them
            String otherGameMode = gameModeSpinner.getItemAtPosition(1).toString();

            onView(withId(R.id.chooseGameMode)).perform(click());
            onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());

            assertTrue( gameModeSpinner.getSelectedItem().toString().equals(otherGameMode) );
            assertTrue( activity.currentGameMode.equals(otherGameMode) );
            assertTrue( gameModes.get(otherGameMode) instanceof GameMode );
            assertTrue( gameModes.get(otherGameMode).getName().equals(otherGameMode) );
        }
    }
}
