package com.bakjoul.mareu;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.bakjoul.mareu.data.model.Room.Blue;
import static com.bakjoul.mareu.data.model.Room.Brown;
import static com.bakjoul.mareu.data.model.Room.Green;
import static com.bakjoul.mareu.data.model.Room.Orange;
import static com.bakjoul.mareu.data.model.Room.Pink;
import static com.bakjoul.mareu.data.model.Room.Purple;
import static com.bakjoul.mareu.data.model.Room.Red;
import static org.hamcrest.CoreMatchers.is;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bakjoul.mareu.data.BuildConfigResolver;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.ui.MeetingActivity;
import com.bakjoul.mareu.utils.MaterialPickerActions;
import com.wdullaer.materialdatetimepicker.date.DayPickerView;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class MeetingActivityTest {

    private MeetingActivity meetingActivity;

    // region CONSTANTS
    private static final LocalDate DATE_OF_THE_DAY = LocalDate.now();

    private static final String FIRST_SUBJECT = "Réunion A";
    private static final Room FIRST_ROOM = Pink;
    private static final LocalDate FIRST_DATE = DATE_OF_THE_DAY.plusDays(1);
    private static final LocalTime FIRST_START = LocalTime.of(14, 0);
    private static final LocalTime FIRST_END = LocalTime.of(15, 0);
    private static final List<String> FIRST_PARTICIPANTS = Arrays.asList("maxime@lamzone.com", "alex@lamzone.com");

    private static final String SECOND_SUBJECT = "Réunion B";
    private static final Room SECOND_ROOM = Red;
    private static final LocalDate SECOND_DATE = DATE_OF_THE_DAY.plusDays(1);
    private static final LocalTime SECOND_START = LocalTime.of(16, 0);
    private static final LocalTime SECOND_END = LocalTime.of(17, 0);
    private static final List<String> SECOND_PARTICIPANTS = Arrays.asList("paul@lamzone.com", "viviane@lamzone.com");

    private static final String THIRD_SUBJECT = "Réunion C";
    private static final Room THIRD_ROOM = Green;
    private static final LocalDate THIRD_DATE = DATE_OF_THE_DAY.plusDays(1);
    private static final LocalTime THIRD_START = LocalTime.of(19, 0);
    private static final LocalTime THIRD_END = LocalTime.of(19, 45);
    private static final List<String> THIRD_PARTICIPANTS = Arrays.asList("amandine@lamzone.com", "luc@lamzone.com");

    private static final String FOURTH_SUBJECT = "Réunion D";
    private static final Room FOURTH_ROOM = Blue;
    private static final LocalDate FOURTH_DATE = DATE_OF_THE_DAY.plusDays(2);
    private static final LocalTime FOURTH_START = LocalTime.of(9, 0);
    private static final LocalTime FOURTH_END = LocalTime.of(10, 0);
    private static final List<String> FOURTH_PARTICIPANTS = Arrays.asList("maxime@lamzone.com", "alex@lamzone.com");

    private static final String FIFTH_SUBJECT = "Réunion E";
    private static final Room FIFTH_ROOM = Orange;
    private static final LocalDate FIFTH_DATE = DATE_OF_THE_DAY.plusDays(2);
    private static final LocalTime FIFTH_START = LocalTime.of(11, 0);
    private static final LocalTime FIFTH_END = LocalTime.of(12, 0);
    private static final List<String> FIFTH_PARTICIPANTS = Arrays.asList("paul@lamzone.com", "viviane@lamzone.com");

    private static final String SIXTH_SUBJECT = "Réunion F";
    private static final Room SIXTH_ROOM = Purple;
    private static final LocalDate SIXTH_DATE = DATE_OF_THE_DAY.plusDays(3);
    private static final LocalTime SIXTH_START = LocalTime.of(16, 0);
    private static final LocalTime SIXTH_END = LocalTime.of(17, 45);
    private static final List<String> SIXTH_PARTICIPANTS = Arrays.asList("amandine@lamzone.com", "luc@lamzone.com");

    private static final String SEVENTH_SUBJECT = "Réunion G";
    private static final Room SEVENTH_ROOM = Brown;
    private static final LocalDate SEVENTH_DATE = DATE_OF_THE_DAY.plusDays(3);
    private static final LocalTime SEVENTH_START = LocalTime.of(17, 30);
    private static final LocalTime SEVENTH_END = LocalTime.of(18, 0);
    private static final List<String> SEVENTH_PARTICIPANTS = Arrays.asList("maxime@lamzone.com", "alex@lamzone.com");
    // endregion

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
    public void createOneMeeting() {
        createMeeting(FIRST_SUBJECT,
                FIRST_ROOM,
                FIRST_DATE,
                FIRST_START,
                FIRST_END,
                FIRST_PARTICIPANTS
        );
    }


    private void createMeeting(
            @NonNull String subject,
            @NonNull Room room,
            @NonNull LocalDate date,
            @NonNull LocalTime start,
            @NonNull LocalTime end,
            @NonNull List<String> participants) {

        // Appuie sur le fab pour ouvrir le dialog de création de réunion
        onView(withId(R.id.fab_add)).perform(click());

        // Remplit le sujet
        onView(withId(R.id.input_subject_edit))
                .perform(
                        click(),
                        replaceText(subject),
                        closeSoftKeyboard()
                );

        // Définit la salle
        onView(withId(R.id.input_room_autoCompleteTextView)).perform(click());
        onData(is(room)).inRoot(isPlatformPopup()).perform(scrollTo(), click());

        // Définit la date
        onView(withId(R.id.create_input_date_edit)).perform(click());
        onView(isAssignableFrom(DayPickerView.class)).perform(MaterialPickerActions.setDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()));
        onView(withText("OK")).perform(click());

        // Définit l'heure de début
        onView(withId(R.id.create_input_start_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(start.getHour(), start.getMinute()));
        onView(withText("OK")).perform(click());

        // Définit l'heure de fin
        onView(withId(R.id.create_input_end_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(end.getHour(), end.getMinute()));
        onView(withText("OK")).perform(click());

        for (int i = 0; i < FIRST_PARTICIPANTS.size(); i++) {
            onView(withId(R.id.input_participants_edit)).perform(
                    click(),
                    replaceText(FIRST_PARTICIPANTS.get(i))
            );
            onView(withContentDescription("Add participant")).perform(click());
        }

    }

}
