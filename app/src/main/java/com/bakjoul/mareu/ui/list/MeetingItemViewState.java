package com.bakjoul.mareu.ui.list;

import androidx.annotation.NonNull;

import com.bakjoul.mareu.data.model.Room;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class MeetingItemViewState {
    private final int id;
    @NonNull
    private final String subject;
    @NonNull
    private final LocalTime time;
    @NonNull
    private final Room room;
    @NonNull
    private final List<String> participants;

    public MeetingItemViewState(
            int id,
            @NonNull String subject,
            @NonNull LocalTime time,
            @NonNull Room room,
            @NonNull List<String> participants) {
        this.id = id;
        this.subject = subject;
        this.time = time;
        this.room = room;
        this.participants = participants;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getSubject() {
        return subject;
    }

    @NonNull
    public LocalTime getTime() {
        return time;
    }

    @NonNull
    public Room getRoom() {
        return room;
    }

    @NonNull
    public List<String> getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingItemViewState that = (MeetingItemViewState) o;
        return id == that.id && subject.equals(that.subject) && time.equals(that.time) && room == that.room && participants.equals(that.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, time, room, participants);
    }
}
