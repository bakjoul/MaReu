package com.bakjoul.mareu.data.model;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Meeting {
    private final int id;
    @NonNull
    private final String subject;
    @NonNull
    private final LocalDateTime start;
    @NonNull
    private final LocalDateTime end;
    @NonNull
    private final Room room;
    @NonNull
    private final List<String> participants;

    public Meeting(int id, @NonNull String subject, @NonNull LocalDateTime start, @NonNull LocalDateTime end, @NonNull Room room, @NonNull List<String> participants) {
        this.id = id;
        this.subject = subject;
        this.start = start;
        this.end = end;
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
    public LocalDateTime getStart() {
        return start;
    }

    @NonNull
    public LocalDateTime getEnd() {
        return end;
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
        Meeting meeting = (Meeting) o;
        return id == meeting.id && subject.equals(meeting.subject) && start.equals(meeting.start) && end.equals(meeting.end) && room.equals(meeting.room) && participants.equals(meeting.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, start, end, room, participants);
    }
}
