package com.bakjoul.mareu.data.repository;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.BuildConfig;
import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            generateRandomMeetings();
            generateRandomMeetings();
            generateRandomMeetings();
            generateRandomMeetings();
            generateRandomMeetings();
        }
    }

    public LiveData<List<Meeting>> getMeetingsLiveData() {
        return meetingsLiveData;
    }

    // Ajoute une réunion
    public void addMeeting(
            @NonNull String subject,
            @NonNull LocalDateTime start,
            @NonNull LocalDateTime end,
            @NonNull Room room,
            @NonNull List<String> participants
    ) {
        // Récupère la valeur actuelle de LiveData
        List<Meeting> meetings = meetingsLiveData.getValue();

        if (meetings == null)
            meetings = new ArrayList<>();
        // Ajoute la réunion
        meetings.add(new Meeting(id, subject, start, end, room, participants));

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

    // Génère des réunions de démonstration
    public void generateRandomMeetings() {
        addMeeting(
                "Réunion A",
                LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0)),
                Room.Pink,
                new ArrayList<>(Arrays.asList("maxime@lamzone.com", "alex@lamzone.com"))
        );
        addMeeting("Réunion B",
                LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 0)),
                Room.Red,
                new ArrayList<>(Arrays.asList("paul@lamzone.com", "viviane@lamzone.com"))
        );
        addMeeting("Réunion C",
                LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 45)),
                Room.Green,
                new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
        );
    }
}
