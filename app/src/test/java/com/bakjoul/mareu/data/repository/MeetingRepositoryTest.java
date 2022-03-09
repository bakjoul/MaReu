package com.bakjoul.mareu.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bakjoul.mareu.data.BuildConfigResolver;
import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MeetingRepositoryTest {

    private static final String TEST_SUBJECT = "TEST_SUBJECT";
    private static final LocalDate TEST_DATE = LocalDate.now();
    private static final LocalTime TEST_START = LocalTime.now();
    private static final LocalTime TEST_END = TEST_START.plusMinutes(30);
    private static final Room TEST_ROOM = Room.Blue;
    private static final List<String> TEST_PARTICIPANTS = new ArrayList<>(Arrays.asList("toto@test.fr", "tata@test.fr"));

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);

    @Before
    public void setUp() {
        Mockito.doReturn(false).when(buildConfigResolver).isDebug();
    }

    // Vérifie la récupération de la liste de réunions vide en build variant release
    @Test
    public void nominal_case_release() {
        // Given
        MeetingRepository meetingRepository = new MeetingRepository(buildConfigResolver);

        // When
        // Récupère la liste de réunions actuelle (censée contenir les réunions factices)
        List<Meeting> result = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());

        // Then
        // Vérifie que les deux listes sont égales
        assertEquals(0, result.size());
    }

    // Vérifie la récupération de la liste de réunions générées en build variant debug
    @Test
    public void nominal_case_debug() {
        // Given
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        MeetingRepository meetingRepository = new MeetingRepository(buildConfigResolver);

        // When
        // Récupère la liste de réunions actuelle (censée contenir les réunions factices)
        List<Meeting> result = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());

        // Then
        // Vérifie que les deux listes sont égales
        assertEquals(getExpectedDebugMeetings(), result);
    }

    // Vérifie l'ajout d'une réunion
    @Test
    public void addMeetingWithSuccess() {
        // Given
        MeetingRepository meetingRepository = new MeetingRepository(buildConfigResolver);

        // When
        // Ajoute une réunion
        boolean addMeetingResult = meetingRepository.addMeeting(
                TEST_SUBJECT,
                TEST_DATE,
                TEST_START,
                TEST_END,
                TEST_ROOM,
                TEST_PARTICIPANTS
        );
        // Récupère la liste de réunions
        List<Meeting> result = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());

        // Then
        // Vérifie que addMeeting a retourné true
        assertTrue(addMeetingResult);

        // Vérifie que la liste ne contient qu'un seul élément
        assertEquals(1, result.size());

        // Vérifie que la réunion de la liste récupérée est la seule dans la liste
        assertEquals(
                Collections.singletonList(new Meeting(0, TEST_SUBJECT, TEST_DATE, TEST_START, TEST_END, TEST_ROOM, TEST_PARTICIPANTS)),
                result
        );
    }

    // Vérifie qu'on ne peut pas ajouter une réunion qui en chevauche une autre
    @Test
    public void addMeetingWithFailure() {
        // Given
        MeetingRepository meetingRepository = new MeetingRepository(buildConfigResolver);
        // Ajoute une réunion une première fois
        boolean addFirstMeetingResult = meetingRepository.addMeeting(
                TEST_SUBJECT,
                TEST_DATE,
                TEST_START,
                TEST_END,
                TEST_ROOM,
                TEST_PARTICIPANTS
        );

        // Vérifie que la réunion a bien été ajoutée
        assertTrue(addFirstMeetingResult);
        List<Meeting> result = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());
        assertEquals(1, result.size());
        assertEquals(
                Collections.singletonList(new Meeting(0, TEST_SUBJECT, TEST_DATE, TEST_START, TEST_END, TEST_ROOM, TEST_PARTICIPANTS)),
                result
        );

        // When
        // Essaie d'ajouter la même réunion une deuxième fois
        boolean addSecondMeetingResult = meetingRepository.addMeeting(
                TEST_SUBJECT,
                TEST_DATE,
                TEST_START,
                TEST_END,
                TEST_ROOM,
                TEST_PARTICIPANTS
        );

        result = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());

        // Then
        // Vérifie que la liste ne contient toujours qu'une seule réunion
        assertEquals(1, result.size());

        // Vérifie que addMeeting a retourné faux
        assertFalse(addSecondMeetingResult);
    }

    // Vérifie la suppression d'une réunion
    @Test
    public void deleteMeetingWithSuccess() {
        // Given
        MeetingRepository meetingRepository = new MeetingRepository(buildConfigResolver);

        // Ajoute la réunion
        meetingRepository.addMeeting(
                TEST_SUBJECT,
                TEST_DATE,
                TEST_START,
                TEST_END,
                TEST_ROOM,
                TEST_PARTICIPANTS
        );

        // When
        // Supprime la réunion
        meetingRepository.deleteMeeting(0);

        // Récupère la liste de réunions
        List<Meeting> result = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());

        // Then
        // Vérifie que la liste est vide
        assertEquals(0, result.size());
    }

    // region OUT
    // Retourne la liste attendue de réunion par défaut
    @NonNull
    private List<Meeting> getExpectedDebugMeetings() {
        List<Meeting> meetings = new ArrayList<>();

        meetings.add(
                new Meeting(
                        0,
                        "Réunion A",
                        LocalDate.now(),
                        LocalTime.of(14, 0, 0),
                        LocalTime.of(15, 0),
                        Room.Pink,
                        new ArrayList<>(Arrays.asList("maxime@lamzone.com", "alex@lamzone.com"))
                )
        );
        meetings.add(
                new Meeting(
                        1,
                        "Réunion B",
                        LocalDate.now(),
                        LocalTime.of(16, 0),
                        LocalTime.of(17, 0),
                        Room.Red,
                        new ArrayList<>(Arrays.asList("paul@lamzone.com", "viviane@lamzone.com"))
                )
        );
        meetings.add(
                new Meeting(
                        2,
                        "Réunion C",
                        LocalDate.now(),
                        LocalTime.of(19, 0),
                        LocalTime.of(19, 45),
                        Room.Green,
                        new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
                )
        );
        meetings.add(
                new Meeting(
                        3,
                        "Réunion D",
                        LocalDate.now().plusDays(1),
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0),
                        Room.Blue,
                        new ArrayList<>(Arrays.asList("maxime@lamzone.com", "alex@lamzone.com"))
                )
        );
        meetings.add(
                new Meeting(
                        4,
                        "Réunion E",
                        LocalDate.now().plusDays(1),
                        LocalTime.of(11, 0),
                        LocalTime.of(12, 0),
                        Room.Orange,
                        new ArrayList<>(Arrays.asList("paul@lamzone.com", "viviane@lamzone.com"))
                )
        );
        meetings.add(
                new Meeting(
                        5,
                        "Réunion F",
                        LocalDate.now().plusDays(2),
                        LocalTime.of(16, 0),
                        LocalTime.of(17, 0),
                        Room.Purple,
                        new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
                )
        );
        meetings.add(
                new Meeting(
                        6,
                        "Réunion G",
                        LocalDate.now().plusDays(2),
                        LocalTime.of(17, 30),
                        LocalTime.of(18, 0),
                        Room.Brown,
                        new ArrayList<>(Arrays.asList("maxime@lamzone.com", "alex@lamzone.com"))
                )
        );

        return meetings;
    }
    // endregion
}
