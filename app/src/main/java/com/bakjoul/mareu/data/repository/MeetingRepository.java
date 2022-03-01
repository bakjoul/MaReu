package com.bakjoul.mareu.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.data.BuildConfigResolver;
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

    private final MutableLiveData<List<Meeting>> meetingsLiveData = new MutableLiveData<>(new ArrayList<>());
    private int id = 0;

    @Inject
    public MeetingRepository(@NonNull BuildConfigResolver buildConfigResolver) {
        if (buildConfigResolver.isDebug()) {
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

        if (meetings == null) {
            meetings = new ArrayList<>();
        }
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
                )
        );

        // Incrémente le compteur id
        id++;

        // Met à jour LiveData
        meetingsLiveData.setValue(meetings);
    }

    // Supprime une réunion
    public void deleteMeeting(int id) {
        // Récupère la valeur actuelle de LiveData
        List<Meeting> meetings = meetingsLiveData.getValue();

        if (meetings == null) {
            meetings = new ArrayList<>();
        }
        // Supprime la réunion
        meetings.removeIf(meeting -> meeting.getId() == id);

        // Met à jour LiveData
        meetingsLiveData.setValue(meetings);
    }

    // Supprime toutes les réunions (pour les tests)
    public void deleteAllMeetings() {
        // Récupère la valeur actuelle de LiveData
        List<Meeting> meetings = meetingsLiveData.getValue();

        if (meetings == null) {
            meetings = new ArrayList<>();
        }
        // Efface toutes les réunions
        meetings.clear();

        // Réinitialise id
        id = 0;

        // Met à jour la LiveData
        meetingsLiveData.setValue(meetings);
    }

    // Crée les réunions de démonstration
    private void addDummyMeetings() {
        addMeeting(
            "Réunion A",
            LocalDate.now(),
            LocalTime.of(14, 0, 0),
            LocalTime.of(15, 0),
            Room.Pink,
            new ArrayList<>(Arrays.asList("maxime@lamzone.com", "alex@lamzone.com"))
        );
        addMeeting(
            "Réunion B",
            LocalDate.now(),
            LocalTime.of(16, 0),
            LocalTime.of(17, 0),
            Room.Red,
            new ArrayList<>(Arrays.asList("paul@lamzone.com", "viviane@lamzone.com"))
        );
        addMeeting(
            "Réunion C",
            LocalDate.now(),
            LocalTime.of(19, 0),
            LocalTime.of(19, 45),
            Room.Green,
            new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
        );
        addMeeting(
            "Réunion D",
            LocalDate.now().plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            Room.Blue,
            new ArrayList<>(Arrays.asList("maxime@lamzone.com", "alex@lamzone.com"))
        );
        addMeeting(
            "Réunion E",
            LocalDate.now().plusDays(1),
            LocalTime.of(11, 0),
            LocalTime.of(12, 0),
            Room.Orange,
            new ArrayList<>(Arrays.asList("paul@lamzone.com", "viviane@lamzone.com"))
        );
        addMeeting(
            "Réunion F",
            LocalDate.now().plusDays(2),
            LocalTime.of(16, 0),
            LocalTime.of(17, 0),
            Room.Purple,
            new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
        );
        addMeeting(
            "Réunion G",
            LocalDate.now().plusDays(2),
            LocalTime.of(17, 30),
            LocalTime.of(18, 0),
            Room.Brown,
            new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
        );
    }
}
