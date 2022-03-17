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

    private static final String EIGHTH_SUBJECT = "Réunion H";
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
        // Les réunions par défaut seront générées
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        ActivityScenario.launch(MeetingActivity.class);

        // Assertion : Vérifie qu'il y a au moins 1 item dans le recycler view
        onView(withId(R.id.meeting_list)).check(matches(hasMinimumChildCount(1)));
        // Assertion : Vérifie qu'il y a 7 items dans le recycler view
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(7));
    }

    @Test
    public void createOneMeeting() {
        ActivityScenario.launch(MeetingActivity.class);

        // Crée une réunion
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);

        // Vérifie qu'elle a été créée
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        // Vérifie que le contenu correspond à ce qu'on voulait créer
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);

        // Ouvre le dialog de création puis revient
        onView(withId(R.id.fab_add)).perform(click());
        pressBack();

        // Revérifications
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);
    }

    @Test
    public void deleteOneMeeting() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Crée une première réunion
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        // Assertion : Vérifie qu'elle a été créée
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        // Assertion : Vérifie que le contenu correspond à ce qu'on voulait créer
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);

        // Action : Crée une deuxième réunion
        createMeeting(SECOND_SUBJECT, SECOND_ROOM, SECOND_DATE, SECOND_START, SECOND_END, SECOND_PARTICIPANTS);
        // Assertion : Vérifie qu'elle a été créée
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(2));
        // Assertion : Vérifie que les contenus correspondent à ce qu'on voulait créer
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);
        assertItemContent(1, SECOND_SUBJECT, SECOND_DATE, SECOND_START, SECOND_END, SECOND_ROOM, SECOND_PARTICIPANTS);

        // Action : Supprime la première réunion
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ClickChildViewWithId(R.id.item_delete_button)));
        Thread.sleep(SLEEP_TIME);
        // Assertion : Vérifie que la réunion a été supprimée
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        // Assertion : Vérifie que la seconde réunion créée est maintenant en position 0
        assertItemContent(0, SECOND_SUBJECT, SECOND_DATE, SECOND_START, SECOND_END, SECOND_ROOM, SECOND_PARTICIPANTS);
    }

    @Test
    public void filterByRoom() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Crée 4 réunions
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        createMeeting(SECOND_SUBJECT, SECOND_ROOM, SECOND_DATE, SECOND_START, SECOND_END, SECOND_PARTICIPANTS);
        createMeeting(THIRD_SUBJECT, THIRD_ROOM, THIRD_DATE, THIRD_START, THIRD_END, THIRD_PARTICIPANTS);
        createMeeting(FOURTH_SUBJECT, FOURTH_ROOM, FOURTH_DATE, FOURTH_START, FOURTH_END, FOURTH_PARTICIPANTS);
        // Assertion : Vérifie qu'il y a 4 réunions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(4));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de salle
        openFilterDialog("room");
        // Action : Clic sur la salle Red
        onView(withId(R.id.filter_room_list)).perform(
                RecyclerViewActions.actionOnItem(hasDescendant(withText("Red")), click()));
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : Vérifie que seule la réunion en Red (Réunion B) est affichée
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, SECOND_SUBJECT, SECOND_DATE, SECOND_START, SECOND_END, SECOND_ROOM, SECOND_PARTICIPANTS);
    }

    @Test
    public void filterByDate() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Crée 3 réunions chacune à une date différente
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        createMeeting(FOURTH_SUBJECT, FOURTH_ROOM, FOURTH_DATE, FOURTH_START, FOURTH_END, FOURTH_PARTICIPANTS);
        createMeeting(SIXTH_SUBJECT, SIXTH_ROOM, SIXTH_DATE, SIXTH_START, SIXTH_END, SIXTH_PARTICIPANTS);
        // Assertion : Vérifie qu'il y a 3 réunions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(3));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de date
        openFilterDialog("date");
        // Action : Affiche le picker et entre la date à filtrer
        onView(withId(R.id.date_filter_input_date_edit)).perform(click());
        onView(isAssignableFrom(DayPickerView.class)).perform(MaterialPickerActions.setDate(FOURTH_DATE.getYear(), FOURTH_DATE.getMonthValue() - 1, FOURTH_DATE.getDayOfMonth()));
        onView(withText("OK")).perform(click());
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : Vérifie que seule la réunion à la date entrée est affiché (Réunion D)
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, FOURTH_SUBJECT, FOURTH_DATE, FOURTH_START, FOURTH_END, FOURTH_ROOM, FOURTH_PARTICIPANTS);
    }

    @Test
    public void filterByStart() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Crée 3 réunions chacune avec une heure de début différente
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        createMeeting(SECOND_SUBJECT, SECOND_ROOM, SECOND_DATE, SECOND_START, SECOND_END, SECOND_PARTICIPANTS);
        createMeeting(THIRD_SUBJECT, THIRD_ROOM, THIRD_DATE, THIRD_START, THIRD_END, THIRD_PARTICIPANTS);
        // Assertion : Vérifie qu'il y a 3 réunions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(3));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de date
        openFilterDialog("date");

        // Action : Affiche le picker et entre l'heure de début à filtrer
        onView(withId(R.id.date_filter_input_start_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(THIRD_START.getHour(), THIRD_START.getMinute()));
        onView(withText("OK")).perform(click());
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : Vérifie que seule la réunion commençant après l'heure entrée est affichée (Réunion C)
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, THIRD_SUBJECT, THIRD_DATE, THIRD_START, THIRD_END, THIRD_ROOM, THIRD_PARTICIPANTS);
    }

    @Test
    public void filterByEnd() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Crée 3 réunions chacune avec une heure de fin différente
        createMeeting(FIRST_SUBJECT, FIRST_ROOM, FIRST_DATE, FIRST_START, FIRST_END, FIRST_PARTICIPANTS);
        createMeeting(SECOND_SUBJECT, SECOND_ROOM, SECOND_DATE, SECOND_START, SECOND_END, SECOND_PARTICIPANTS);
        createMeeting(THIRD_SUBJECT, THIRD_ROOM, THIRD_DATE, THIRD_START, THIRD_END, THIRD_PARTICIPANTS);

        // Assertion : Vérifie qu'il y a 3 réunions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(3));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de date
        openFilterDialog("date");

        // Action : Affiche le picker et entre l'heure de fin à filtrer
        onView(withId(R.id.date_filter_input_end_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(SECOND_END.getHour(), SECOND_END.getMinute()));
        onView(withText("OK")).perform(click());
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : Vérifie que seules les réunions terminant avant l'heure entrée sont affichées (Réunions A et B)
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(2));
        assertItemContent(0, FIRST_SUBJECT, FIRST_DATE, FIRST_START, FIRST_END, FIRST_ROOM, FIRST_PARTICIPANTS);
        assertItemContent(1, SECOND_SUBJECT, SECOND_DATE, SECOND_START, SECOND_END, SECOND_ROOM, SECOND_PARTICIPANTS);
    }

    @Test
    public void filterWithAllFilters() throws InterruptedException {
        ActivityScenario.launch(MeetingActivity.class);

        // Action : Crée 5 réunions
        createMeeting(FOURTH_SUBJECT, FOURTH_ROOM, FOURTH_DATE, FOURTH_START, FOURTH_END, FOURTH_PARTICIPANTS);
        createMeeting(FIFTH_SUBJECT, FIFTH_ROOM, FIFTH_DATE, FIFTH_START, FIFTH_END, FIFTH_PARTICIPANTS);
        createMeeting(SIXTH_SUBJECT, SIXTH_ROOM, SIXTH_DATE, SIXTH_START, SIXTH_END, SIXTH_PARTICIPANTS);
        createMeeting(SEVENTH_SUBJECT, SEVENTH_ROOM, SEVENTH_DATE, SEVENTH_START, SEVENTH_END, SEVENTH_PARTICIPANTS);
        createMeeting(EIGHTH_SUBJECT, EIGHTH_ROOM, EIGHTH_DATE, EIGHTH_START, EIGHTH_END, EIGHTH_PARTICIPANTS);
        // Assertion : Vérifie qu'il y a 5 réunions dans la liste
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(5));

        // Action : Clic sur le menu des filtres
        onView(withId(R.id.menu_filters)).perform(click());
        // Action : Clic sur le filtre de date
        openFilterDialog("date");

        // Action : Affiche le picker et entre la date à filtrer
        onView(withId(R.id.date_filter_input_date_edit)).perform(click());
        onView(isAssignableFrom(DayPickerView.class)).perform(MaterialPickerActions.setDate(SEVENTH_DATE.getYear(), SEVENTH_DATE.getMonthValue() - 1, SEVENTH_DATE.getDayOfMonth()));
        onView(withText("OK")).perform(click());

        // Action : Affiche le picker et entre l'heure de début à filtrer
        onView(withId(R.id.date_filter_input_start_edit)).perform(click());
        onView(isAssignableFrom(RadialPickerLayout.class)).perform(MaterialPickerActions.setTime(SIXTH_START.getHour(), SIXTH_START.getMinute()));
        onView(withText("OK")).perform(click());

        // Action : Affiche le picker et entre l'heure de fin à filtrer
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
        onView(withId(R.id.filter_room_list)).perform(
                RecyclerViewActions.actionOnItem(hasDescendant(withText("Brown")), click()));
        // Action : Ferme le dialog
        closeDialog();
        Thread.sleep(SLEEP_TIME);

        // Assertion : Vérifie que les seules réunions correspondants aux filtres sont affichées (Réunion G et H)
        onView(withId(R.id.meeting_list)).check(new RecyclerViewItemCountAssertion(1));
        assertItemContent(0, SEVENTH_SUBJECT, SEVENTH_DATE, SEVENTH_START, SEVENTH_END, SEVENTH_ROOM, SEVENTH_PARTICIPANTS);
    }

    // region Utils
    // Crée une réunion
    private void createMeeting(
            @NonNull String subject,
            @NonNull Room room,
            @NonNull LocalDate date,
            @NonNull LocalTime start,
            @NonNull LocalTime end,
            @NonNull List<String> participants) {

        // Appuie sur le fab pour ouvrir le dialog de création de réunion
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

        // Ajoute les participants
        for (int i = 0; i < participants.size(); i++) {
            onView(withId(R.id.input_participants_edit)).perform(
                    click(),
                    replaceText(participants.get(i))
            );
            onView(withContentDescription("Add participant")).perform(click());
        }
        onView(withId(R.id.input_participants_edit)).perform(closeSoftKeyboard());

        // Créer la réunion
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
                        withText(formatDate(date) + " – " + formatTime(start) + "-" + formatTime(end) + " – " + room)
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
