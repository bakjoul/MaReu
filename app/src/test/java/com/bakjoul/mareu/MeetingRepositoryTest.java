package com.bakjoul.mareu;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.MeetingRepository;
import com.bakjoul.mareu.utils.LiveDataTestUtil;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeetingRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MeetingRepository meetingRepository;

    @Before
    public void setUp() {
        meetingRepository = new MeetingRepository();
    }

    // Test de récupération de la liste de réunion
    @Test
    public void getMeetingsWithSuccess() throws InterruptedException {
        // Vide la liste de réunions
        meetingRepository.deleteAllMeetings();

        // Ajoute les réunions factices
        meetingRepository.addDummyMeetings();

        // Liste attendue contenant les réunions factices
        List<Meeting> expected = meetingRepository.DUMMY_MEETINGS;

        // Récupère la liste de réunions actuelle (censée contenir les réunions factices)
        List<Meeting> current = LiveDataTestUtil.getOrAwaitValue(meetingRepository.getMeetingsLiveData());

        // Vérifie que les deux listes sont égales
        assertEquals(current, expected);
    }

    // Test d'ajout de réunion
    @Test
    public void addMeetingWithSuccess() throws InterruptedException {
        // Vide la liste de réunions
        meetingRepository.deleteAllMeetings();

        // Champs de la réunion test à ajouter
        String subject = "subject";
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.now();
        LocalTime end = start.plusMinutes(30);
        Room room = Room.Blue;
        List<String> participants = new ArrayList<>(Arrays.asList("toto@test.fr", "tata@test.fr"));

        // Résultat attendu
        Meeting expected = new Meeting(0, subject, date, start, end, room, participants);

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
        List<Meeting> current = LiveDataTestUtil.getOrAwaitValue(meetingRepository.getMeetingsLiveData());

        // Vérifie que la liste ne contient qu'un seul élément
        assertEquals(1, current.size());

        // Vérifie que la réunion de la liste récupérée est égale à celle ajoutée
        assertEquals(expected, current.get(0));
    }

    // Test de suppression d'une réunion
    @Test
    public void deleteMeetingWithSuccess() throws InterruptedException {
        // Vide la liste de réunions
        meetingRepository.deleteAllMeetings();

        // Champs de la réunion test à ajouter
        String subject = "subject";
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.now();
        LocalTime end = start.plusMinutes(30);
        Room room = Room.Blue;
        List<String> participants = new ArrayList<>(Arrays.asList("toto@test.fr", "tata@test.fr"));

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
        List<Meeting> current = LiveDataTestUtil.getOrAwaitValue(meetingRepository.getMeetingsLiveData());

        // Vérifie que la liste est vide
        assertEquals(0, current.size());
    }

}
