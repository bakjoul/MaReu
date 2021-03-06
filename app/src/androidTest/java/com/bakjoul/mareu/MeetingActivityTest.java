package com.bakjoul.mareu;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
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
import static com.bakjoul.mareu.data.model.Room.Yellow;
import static org.hamcrest.CoreMatchers.is;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bakjoul.mareu.data.BuildConfigResolver;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.ui.MeetingActivity;
import com.bakjoul.mareu.utils.ClickChildViewWithId;
import com.bakjoul.mareu.utils.DrawableMatcher;
import com.bakjoul.mareu.utils.MaterialPickerActions;
import com.bakjoul.mareu.utils.RecyclerViewItemAssertion;
import com.bakjoul.mareu.utils.RecyclerViewItemCountAssertion;
import com.bakjoul.mareu.utils.WaitForId;
import com.wdullaer.materialdatetimepicker.date.DayPickerView;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@RunWith(AndroidJUnit4.class)
@HiltAndroidTest
public class MeetingActivityTest {

    // region CONSTANTS
    private static final int SLEEP_TIME = 1000;
    private static final long WAIT_TIME = 10000;

    private static final LocalDate DATE_OF_THE_DAY = LocalDate.now();

    private static final String FIRST_SUBJECT = "R??union A";
    private static final Room FIRST_ROOM = Pink;
    private static final LocalDate FIRST_DATE = DATE_OF_THE_DAY.plusDays(1);
    private static final LocalTime FIRST_START = LocalTime.of(14, 0);
    private static final LocalTime FIRST_END = LocalTime.of(15, 0);
    private static final List<String> FIRST_PARTICIPANTS = Arrays.asList("maxime@lamzone.com", "alex@lamzone.com");

    private static final String SECOND_SUBJECT = "R??union B";
    private static final Room SECOND_ROOM = Red;
    private static final LocalDate SECOND_DATE = DATE_OF_THE_DAY.plusDays(1);
    private static final LocalTime SECOND_START = LocalTime.of(16, 0);
    private static final LocalTime SECOND_END = LocalTime.of(17, 0);
    private static final List<String> SECOND_PARTICIPANTS = Arrays.asList("paul@lamzone.com", "viviane@lamzone.com");

    private static final String THIRD_SUBJECT = "R??union C";
    private static final Room THIRD_ROOM = Green;
    private static final LocalDate THIRD_DATE = DATE_OF_THE_DAY.plusDays(1);
    private static final LocalTime THIRD_START = LocalTime.of(19, 0);
    private static final LocalTime THIRD_END = LocalTime.of(19, 45);
    private static final List<String> THIRD_PARTICIPANTS = Arrays.asList("amandine@lamzone.com", "luc@lamzone.com");

    private static final String FOURTH_SUBJECT = "R??union D";
    private static final Room FOURTH_ROOM = Blue;
    private static final LocalDate FOURTH_DATE = DATE_OF_THE_DAY.plusDays(2);
    private static final LocalTime FOURTH_START = LocalTime.of(9, 0);
    private static final LocalTime FOURTH_END = LocalTime.of(10, 0);
    private static final List<String> FOURTH_PARTICIPANTS = Arrays.asList("maxime@lamzone.com", "alex@lamzone.com");

    private static final String FIFTH_SUBJECT = "R??union E";
    private static final Room FIFTH_ROOM = Orange;
    private static final LocalDate FIFTH_DATE = DATE_OF_THE_DAY.plusDays(2);
    private static final LocalTime FIFTH_START = LocalTime.of(11, 0);
    private static final LocalTime FIFTH_END = LocalTime.of(12, 0);
    private static final List<String> FIFTH_PARTICIPANTS = Arrays.asList("paul@lamzone.com", "viviane@lamzone.com");

    private static final String SIXTH_SUBJECT = "R??union F";
    private static final Room SIXTH_ROOM = Purple;
    private static final LocalDate SIXTH_DATE = DATE_OF_THE_DAY.plusDays(3);
    private static final LocalTime SIXTH_START = LocalTime.of(16, 0);
    private static final LocalTime SIXTH_END = LocalTime.of(17, 45);
    private static final List<String> SIXTH_PARTICIPANTS = Arrays.asList("amandine@lamzone.com", "luc@lamzone.com");

    private static final String SEVENTH_SUBJECT = "R??union G";
    private static final Room SEVENTH_ROOM = Brown;
    private static final LocalDate SEVENTH_DATE = DATE_OF_THE_DAY.plusDays(3);
    private static final LocalTime SEVENTH_START = LocalTime.of(17, 30);
    private static final LocalTime SEVENTH_END = LocalTime.of(18, 0);
    private static final List<String> SEVENTH_PARTICIPANTS = Arrays.asList("maxime@lamzone.com", "alex@lamzone.com");

    private static final String EIGHTH_SUBJECT = "R??union H";
    private static final Room EIGHTH_ROOM = Yellow;
    private static final LocalDate EIGHTH_DATE = DATE_OF_THE_DAY.plusDays(3);
    private static final LocalTime EIGHTH_START = LocalTime.of(18, 30);
    private static final LocalTime EIGHTH_END = LocalTime.of(19, 0);
    private static final List<String> EIGHTH_PARTICIPANTS = Arrays.asList("paul@lamzone.com", "viviane@lamzone.com");
    // endregion

    @Rule
    public HiltAndroidRule hiltAndroidRule = new HiltAndroidRule(this);

    @BindValue
    BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);

    @Test
    public void meetingsList_shouldBeEmpty() {
        ActivityScenario.launch(MeetingActivity.class);

        // Assertion
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void meetingsList_shouldNotBeEmpty() {
        // Les r??unions par d??faut seront g??n??r??es
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        ActivityScenario.launch(MeetingActivity.class);

        // Assertion : V??rifie qu'il y a au moins 1 item dans le recycler view
        onView(withId(R.id.meeting_list)).check(matches(hasMinimumChildCount(1)));
        // Assertion : V??rifie qu'il y a 7 items dans le recycler view
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(7));
    }

    @Test
    public void createOneMeeting() {
        ActivityScenario.launch(MeetingActivity.class);

        // Cr??e une r??union
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);

        // V??rifie qu'elle a ??t?? cr????e
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        // V??rifie que le contenu correspond ?? ce qu'on voulait cr??er
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);

        // Ouvre le dialog de cr??ation puis revient
        onView(withId(R.id.fab_add)).perform(click());
        pressBack();

        // Rev??rifications
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);
    }

    @Test
    public void deleteOneMeeting() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Cr??e une premi??re r??union
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        // Assertion : V??rifie qu'elle a ??t?? cr????e
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        // Assertion : V??rifie que le contenu correspond ?? ce qu'on voulait cr??er
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);

        // Action : Cr??e une deuxi??me r??union
        createMeeting(SECOND_SUBJECT, SECOND_ROOM, SECOND_DATE, SECOND_START, SECOND_END, SECOND_PARTICIPANTS);
        // Assertion : V??rifie qu'elle a ??t?? cr????e
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(2));
        // Assertion : V??rifie que les contenus correspondent ?? ce qu'on voulait cr??er
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);
        assertItemContent(1, SECOND_SUBJECT, SECOND_DATE, SECOND_START, SECOND_END, SECOND_ROOM, SECOND_PARTICIPANTS);

        // Action : Supprime la premi??re r??union
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ClickChildViewWithId(R.id.item_delete_button)));
        Thread.sleep(SLEEP_TIME);
        // Assertion : V??rifie que la r??union a ??t?? supprim??e
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        // Assertion : V??rifie que la seconde r??union cr????e est maintenant en position 0
        assertItemContent(0, SECOND_SUBJECT, SECOND_DATE, SECOND_START, SECOND_END, SECOND_ROOM, SECOND_PARTICIPANTS);
    }

    @Test
    public void filterByRoom() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Cr??e 4 r??unions
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        createMeeting(SECOND_SUBJECT, SECOND_ROOM, SECOND_DATE, SECOND_START, SECOND_END, SECOND_PARTICIPANTS);
        createMeeting(THIRD_SUBJECT, THIRD_ROOM, THIRD_DATE, THIRD_START, THIRD_END, THIRD_PARTICIPANTS);
        createMeeting(FOURTH_SUBJECT, FOURTH_ROOM, FOURTH_DATE, FOURTH_START, FOURTH_END, FOURTH_PARTICIPANTS);
        // Assertion : V??rifie qu'il y a 4 r??unions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(4));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de salle
        openFilterDialog("room");
        // Action : Clic sur la salle Red
        onView(withId(R.id.room_filter_list)).perform(
                RecyclerViewActions.actionOnItem(hasDescendant(withText("Red")), click()));
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : V??rifie que seule la r??union en Red (R??union B) est affich??e
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, SECOND_SUBJECT, SECOND_DATE, SECOND_START, SECOND_END, SECOND_ROOM, SECOND_PARTICIPANTS);
    }

    @Test
    public void filterByDate() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Cr??e 3 r??unions chacune ?? une date diff??rente
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        createMeeting(FOURTH_SUBJECT, FOURTH_ROOM, FOURTH_DATE, FOURTH_START, FOURTH_END, FOURTH_PARTICIPANTS);
        createMeeting(SIXTH_SUBJECT, SIXTH_ROOM, SIXTH_DATE, SIXTH_START, SIXTH_END, SIXTH_PARTICIPANTS);
        // Assertion : V??rifie qu'il y a 3 r??unions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(3));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de date
        openFilterDialog("date");
        // Action : Affiche le picker et entre la date ?? filtrer
        onView(withId(R.id.date_filter_input_date_edit)).perform(click());
        onView(isAssignableFrom(DayPickerView.class)).perform(MaterialPickerActions.setDate(FOURTH_DATE.getYear(), FOURTH_DATE.getMonthValue() - 1, FOURTH_DATE.getDayOfMonth()));
        onView(withText("OK")).perform(click());
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : V??rifie que seule la r??union ?? la date entr??e est affich?? (R??union D)
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, FOURTH_SUBJECT, FOURTH_DATE, FOURTH_START, FOURTH_END, FOURTH_ROOM, FOURTH_PARTICIPANTS);
    }

    @Test
    public void filterByStart() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Cr??e 3 r??unions chacune avec une heure de d??but diff??rente
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        createMeeting(SECOND_SUBJECT, SECOND_ROOM, SECOND_DATE, SECOND_START, SECOND_END, SECOND_PARTICIPANTS);
        createMeeting(THIRD_SUBJECT, THIRD_ROOM, THIRD_DATE, THIRD_START, THIRD_END, THIRD_PARTICIPANTS);
        // Assertion : V??rifie qu'il y a 3 r??unions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(3));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de date
        openFilterDialog("date");

        // Action : Affiche le picker et entre l'heure de d??but ?? filtrer
        onView(withId(R.id.date_filter_input_start_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(THIRD_START.getHour(), THIRD_START.getMinute()));
        onView(withText("OK")).perform(click());
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : V??rifie que seule la r??union commen??ant apr??s l'heure entr??e est affich??e (R??union C)
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, THIRD_SUBJECT, THIRD_DATE, THIRD_START, THIRD_END, THIRD_ROOM, THIRD_PARTICIPANTS);
    }

    @Test
    public void filterByEnd() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Cr??e 3 r??unions chacune avec une heure de fin diff??rente
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        createMeeting(SECOND_SUBJECT, SECOND_ROOM, SECOND_DATE, SECOND_START, SECOND_END, SECOND_PARTICIPANTS);
        createMeeting(THIRD_SUBJECT, THIRD_ROOM, THIRD_DATE, THIRD_START, THIRD_END, THIRD_PARTICIPANTS);

        // Assertion : V??rifie qu'il y a 3 r??unions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(3));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de date
        openFilterDialog("date");

        // Action : Affiche le picker et entre l'heure de fin ?? filtrer
        onView(withId(R.id.date_filter_input_end_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(SECOND_END.getHour(), SECOND_END.getMinute()));
        onView(withText("OK")).perform(click());
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : V??rifie que seules les r??unions terminant avant l'heure entr??e sont affich??es (R??unions A et B)
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(2));
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);
        assertItemContent(1, SECOND_SUBJECT, SECOND_DATE, SECOND_START, SECOND_END, SECOND_ROOM, SECOND_PARTICIPANTS);
    }

    @Test
    public void filterWithAllFilters() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Cr??e 5 r??unions
        createMeeting(FOURTH_SUBJECT, FOURTH_ROOM, FOURTH_DATE, FOURTH_START, FOURTH_END, FOURTH_PARTICIPANTS);
        createMeeting(FIFTH_SUBJECT, FIFTH_ROOM, FIFTH_DATE, FIFTH_START, FIFTH_END, FIFTH_PARTICIPANTS);
        createMeeting(SIXTH_SUBJECT, SIXTH_ROOM, SIXTH_DATE, SIXTH_START, SIXTH_END, SIXTH_PARTICIPANTS);
        createMeeting(SEVENTH_SUBJECT, SEVENTH_ROOM, SEVENTH_DATE, SEVENTH_START, SEVENTH_END, SEVENTH_PARTICIPANTS);
        createMeeting(EIGHTH_SUBJECT, EIGHTH_ROOM, EIGHTH_DATE, EIGHTH_START, EIGHTH_END, EIGHTH_PARTICIPANTS);
        // Assertion : V??rifie qu'il y a 5 r??unions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(5));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de date
        openFilterDialog("date");

        // Action : Affiche le picker et entre la date ?? filtrer
        onView(withId(R.id.date_filter_input_date_edit)).perform(click());
        onView(isAssignableFrom(DayPickerView.class)).perform(MaterialPickerActions.setDate(SEVENTH_DATE.getYear(), SEVENTH_DATE.getMonthValue() - 1, SEVENTH_DATE.getDayOfMonth()));
        onView(withText("OK")).perform(click());

        // Action : Affiche le picker et entre l'heure de d??but ?? filtrer
        onView(withId(R.id.date_filter_input_start_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(SIXTH_START.getHour(), SIXTH_START.getMinute()));
        onView(withText("OK")).perform(click());

        // Action : Affiche le picker et entre l'heure de fin ?? filtrer
        onView(withId(R.id.date_filter_input_end_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(EIGHTH_END.getHour(), EIGHTH_END.getMinute()));
        onView(withText("OK")).perform(click());

        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de salle
        openFilterDialog("room");
        // Action : Clic sur la salle Brown
        onView(withId(R.id.room_filter_list)).perform(
                RecyclerViewActions.actionOnItem(hasDescendant(withText("Brown")), click()));
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : V??rifie que les seules r??unions correspondants aux filtres sont affich??es (R??union G et H)
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, SEVENTH_SUBJECT, SEVENTH_DATE, SEVENTH_START, SEVENTH_END, SEVENTH_ROOM, SEVENTH_PARTICIPANTS);
    }

    // region Utils
    // Cr??e une r??union
    private void createMeeting(
            @NonNull String subject,
            @NonNull Room room,
            @NonNull LocalDate date,
            @NonNull LocalTime start,
            @NonNull LocalTime end,
            @NonNull List<String> participants) {

        // Appuie sur le fab pour ouvrir le dialog de cr??ation de r??union
        onView(isRoot()).perform(waitForId(R.id.fab_add, WAIT_TIME));
        onView(withId(R.id.fab_add)).perform(click());

        // Remplit le sujet
        onView(isRoot()).perform(waitForId(R.id.input_subject_edit, WAIT_TIME));
        onView(withId(R.id.input_subject_edit))
                .perform(
                        click(),
                        replaceText(subject),
                        closeSoftKeyboard()
                );

        // D??finit la salle
        onView(withId(R.id.input_room_autoCompleteTextView)).perform(click());
        onData(is(room)).inRoot(isPlatformPopup()).perform(scrollTo(), click());

        // D??finit la date
        onView(withId(R.id.create_input_date_edit)).perform(click());
        onView(isAssignableFrom(DayPickerView.class)).perform(MaterialPickerActions.setDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()));
        onView(withText("OK")).perform(click());

        // D??finit l'heure de d??but
        onView(withId(R.id.create_input_start_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(start.getHour(), start.getMinute()));
        onView(withText("OK")).perform(click());

        // D??finit l'heure de fin
        onView(withId(R.id.create_input_end_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(end.getHour(), end.getMinute()));
        onView(withText("OK")).perform(click());

        // Ajoute les participants
        for (int i = 0; i < participants.size(); i++) {
            onView(withId(R.id.input_participants_edit)).perform(
                    click(),
                    replaceText(participants.get(i))
            );
            onView(withContentDescription("Add participant")).perform(click());
        }
        onView(withId(R.id.input_participants_edit)).perform(closeSoftKeyboard());

        // Cr??er la r??union
        onView(withId(R.id.create_new_meeting)).perform(click());
    }

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM", Locale.FRENCH);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH'h'mm", Locale.FRENCH);

    private String formatDate(@NonNull LocalDate date) {
        return date.format(dateFormatter);
    }

    private String formatTime(@NonNull LocalTime time) {
        return time.format(timeFormatter);
    }

    private String formatParticipants(@NonNull List<String> participants) {
        return StringUtils.join(participants, ", ");
    }

    private void assertItemContent(
            int position,
            @NonNull String subject,
            @NonNull LocalDate date,
            @NonNull LocalTime start,
            @NonNull LocalTime end,
            @NonNull Room room,
            @NonNull List<String> participants) {
        onView(withId(R.id.meeting_list)).check(
                new RecyclerViewItemAssertion(
                        position,
                        R.id.item_icon,
                        new DrawableMatcher(room.getIconRes())
                )
        );
        onView(withId(R.id.meeting_list)).check(
                new RecyclerViewItemAssertion(
                        position,
                        R.id.item_subject,
                        withText(subject)
                )
        );
        onView(withId(R.id.meeting_list)).check(
                new RecyclerViewItemAssertion(
                        position,
                        R.id.item_info,
                        withText(formatDate(date) + " ??? " + formatTime(start) + "-" + formatTime(end) + " ??? " + room)
                )
        );
        onView(withId(R.id.meeting_list)).check(
                new RecyclerViewItemAssertion(
                        position,
                        R.id.item_participants,
                        withText(formatParticipants(participants))
                )
        );
    }

    private void openFilterDialog(@NonNull String filterDialog) {
        int position = -1;
        switch (filterDialog) {
            case "room":
                position = 0;
                break;
            case "date":
                position = 1;
                break;
        }
        onData(CoreMatchers.anything())
                .inRoot(RootMatchers.isPlatformPopup())
                .atPosition(position)
                .perform(click());
    }

    private void closeDialog() {
        onView(withText("Fermer")).inRoot(isDialog()).check(matches(isDisplayed())).perform(ViewActions.pressBack());
    }

    @NonNull
    private WaitForId waitForId(@IdRes int viewId, long millis) {
        return new WaitForId(viewId, millis);
    }
    // endregion
}
