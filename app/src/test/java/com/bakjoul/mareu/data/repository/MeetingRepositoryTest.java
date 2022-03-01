package com.bakjoul.mareu.data.repository;

import static org.junit.Assert.assertEquals;

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

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);

    @Before
    public void setUp() {
        Mockito.doReturn(false).when(buildConfigResolver).isDebug();
    }

    // Vérifie la récupération de la liste des réunions
    @Test
    public void nominal_case_release() {
        // Given
        MeetingRepository meetingRepository = new MeetingRepository(buildConfigResolver);

        // Récupère la liste de réunions actuelle (censée contenir les réunions factices)
        List<Meeting> current = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());

        // Vérifie que les deux listes sont égales
        assertEquals(0, current.size());
    }

    @Test
    public void nominal_case_debug() {
        // Given
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        MeetingRepository meetingRepository = new MeetingRepository(buildConfigResolver);

        // Récupère la liste de réunions actuelle (censée contenir les réunions factices)
        List<Meeting> current = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());

        // Vérifie que les deux listes sont égales
        assertEquals(getDefaultDebugMeetings(), current);
    }

    // Vérifie l'ajout d'une réunion
    @Test
    public void addMeetingWithSuccess() {
        // Champs de la réunion test à ajouter
        String subject = "subject";
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.now();
        LocalTime end = start.plusMinutes(30);
        Room room = Room.Blue;
        List<String> participants = new ArrayList<>(Arrays.asList("toto@test.fr", "tata@test.fr"));

        MeetingRepository meetingRepository = new MeetingRepository(buildConfigResolver);

        // Ajoute la réunion
        meetingRepository.addMeeting(
                subject,
                date,
                start,
                end,
                room,
                participants
        );

        // Récupère la liste de réunion
        List<Meeting> current = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());

        // Vérifie que la liste ne contient qu'un seul élément
        assertEquals(1, current.size());

        // Vérifie que la réunion de la liste récupérée est la seule dans la liste
        assertEquals(
            Collections.singletonList(new Meeting(0, subject, date, start, end, room, participants)),
            current
        );
    }

    // Vérifie la suppression d'une réunion
    @Test
    public void deleteMeetingWithSuccess() {
        // Champs de la réunion test à ajouter
        String subject = "subject";
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.now();
        LocalTime end = start.plusMinutes(30);
        Room room = Room.Blue;
        List<String> participants = new ArrayList<>(Arrays.asList("toto@test.fr", "tata@test.fr"));

        MeetingRepository meetingRepository = new MeetingRepository(buildConfigResolver);

        // Ajoute la réunion
        meetingRepository.addMeeting(
            subject,
            date,
            start,
            end,
            room,
            participants
        );

        // Supprime la réunion
        meetingRepository.deleteMeeting(0);

        // Récupère la liste de réunions
        List<Meeting> current = LiveDataTestUtil.getValueForTesting(meetingRepository.getMeetingsLiveData());

        // Vérifie que la liste est vide
        assertEquals(0, current.size());
    }

    // region OUT
    private List<Meeting> getDefaultDebugMeetings() {
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
                new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
            )
        );

        return meetings;
    }
    // endregion OUT
}
