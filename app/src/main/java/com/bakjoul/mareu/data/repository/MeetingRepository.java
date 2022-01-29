package com.bakjoul.mareu.data.repository;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

public class MeetingRepository {

    private final MutableLiveData<List<Meeting>> meetingsLiveData = new MutableLiveData<>();
    private int id = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MeetingRepository() {
        if (BuildConfig.DEBUG)
            generateRandomMeetings();
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

    // Génère des réunions de démonstration
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void generateRandomMeetings() {
        addMeeting(
                "Réunion A",
                LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0)),
                Room.PINK,
                new ArrayList<>(Arrays.asList("maxime@lamzone.com", "alex@lamzone.com"))
        );
        addMeeting("Réunion B",
                LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 0)),
                Room.RED,
                new ArrayList<>(Arrays.asList("paul@lamzone.com", "viviane@lamzone.com"))
        );
        addMeeting("Réunion C",
                LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 45)),
                Room.GREEN,
                new ArrayList<>(Arrays.asList("amandine@lamzone.com", "luc@lamzone.com"))
        );
    }
}
