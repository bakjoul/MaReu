package com.bakjoul.mareu.ui;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.CoreMatchers.is;

import android.os.Build;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.data.model.Room;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class MeetingActivityTest {

    private MeetingActivity meetingActivity;

    @Before
    public void setUp() {
        ActivityScenario<MeetingActivity> activityScenario = ActivityScenario.launch(MeetingActivity.class);
        activityScenario.onActivity(activity -> meetingActivity = activity);
    }

    @After
    public void tearDown() {
        meetingActivity = null;
    }

    @Test
    public void createMeeting() {
        createMeeting("test", Room.Pink, LocalDate.now().plusDays(3));
    }


    private void createMeeting(String subject, Room room, LocalDate date) {
        onView(withId(R.id.fab_add)).perform(click());

        onView(withId(R.id.input_subject_edit))
                .perform(
                        click(),
                        replaceText(subject),
                        closeSoftKeyboard()
                );

        onView(withId(R.id.input_room_autoCompleteTextView)).perform(click());
        onData(is(room)).inRoot(isPlatformPopup()).perform(scrollTo(), click());

        onView(withId(R.id.create_input_date_edit)).perform(click());

        if (Build.VERSION.SDK_INT >= 23) {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector().index(date.getDayOfMonth()-1));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    Log.e("ERROR",e.toString());
                }
            }
        }

        onView(withId(R.id.input_participants_edit)).perform(click());
    }

}
