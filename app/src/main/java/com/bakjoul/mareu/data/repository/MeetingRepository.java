package com.bakjoul.mareu.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.BuildConfig;
import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MeetingRepository {

    private final MutableLiveData<List<Meeting>> meetingsLiveData = new MutableLiveData<>();
    private int id = 0;

    @Inject
    public MeetingRepository() {
        if (BuildConfig.DEBUG) {
            addDummyMeetings();
        }
    }

    public LiveData<List<Meeting>> getMeetingsLiveData() {
        return meetingsLiveData;
    }

    // Ajoute une réunion
    public void addMeeting(
            @NonNull String subject,
            @NonNull LocalDate date,
            @NonNull LocalTime start,
            @NonNull LocalTime end,
            @NonNull Room room,
            @NonNull List<String> participants
    ) {
        // Récupère la valeur actuelle de LiveData
        List<Meeting> meetings = meetingsLiveData.getValue();

        if (meetings == null)
            meetings = new ArrayList<>();
        // Ajoute la réunion
        meetings.add(
                new Meeting(
                        id,
                        subject,
                        date,
                        start,
                        end,
                        room,
                        participants
                ));

        // Incrémente le compteur id
        id++;

        // Met à jour LiveData
        meetingsLiveData.setValue(meetings);
    }

    // Supprime une réunion
    public void deleteMeeting(int id) {
        // Récupère la valeur actuelle de LiveData
        List<Meeting> meetings = meetingsLiveData.getValue();

        if (meetings == null)
            meetings = new ArrayList<>();
        // Supprime la réunion
        meetings.removeIf(meeting -> meeting.getId() == id);

        // Met à jour LiveData
        meetingsLiveData.setValue(meetings);
    }

    // Liste de réunions de démonstration
    public final List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(
                    id,
                    "Réunion A",
                    LocalDate.now(),
                    LocalTime.of(14, 0, 0),
                    LocalTime.of(15, 0),
                    Room.Pink,
                    new ArrayList<>(Arrays.asList("maxime@lamzone.com", "alex@lamzone.com"))
            ),
            new Meeting(
                    id,
                    "Réunion B",
                    LocalDate.now(),
                    LocalTime.of(16, 0),
                    LocalTime.of(17, 0),
                    Room.Red,
                    new ArrayList<>(Arrays.asList("paul@lamzone.com", "viviane@lamzone.com"))
            ),
            new Meeting(
                    id,
                    "Réunion C",
                    LocalDate.now(),
                    LocalTime.of(19, 0),
                    LocalTime.of(19, 45),
                    Room.Green,
                    new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
            ),
            new Meeting(
                    id,
                    "Réunion D",
                    LocalDate.now().plusDays(1),
                    LocalTime.of(9, 0),
                    LocalTime.of(10, 0),
                    Room.Blue,
                    new ArrayList<>(Arrays.asList("maxime@lamzone.com", "alex@lamzone.com"))
            ),
            new Meeting(
                    id,
                    "Réunion E",
                    LocalDate.now().plusDays(1),
                    LocalTime.of(11, 0),
                    LocalTime.of(12, 0),
                    Room.Orange,
                    new ArrayList<>(Arrays.asList("paul@lamzone.com", "viviane@lamzone.com"))
            ),
            new Meeting(
                    id,
                    "Réunion F",
                    LocalDate.now().plusDays(2),
                    LocalTime.of(16, 0),
                    LocalTime.of(17, 0),
                    Room.Purple,
                    new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
            ),
            new Meeting(
                    id,
                    "Réunion G",
                    LocalDate.now().plusDays(2),
                    LocalTime.of(17, 30),
                    LocalTime.of(18, 0),
                    Room.Brown,
                    new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
            )
    );

    // Crée les réunions de démonstration
    private void addDummyMeetings() {
        for (Meeting m : DUMMY_MEETINGS)
            addMeeting(
                    m.getSubject(),
                    m.getDate(),
                    m.getStart(),
                    m.getEnd(),
                    m.getRoom(),
                    m.getParticipants());
    }
}
