package com.bakjoul.mareu.data.model;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Meeting {
    private final int id;
    @NonNull
    private final String subject;
    @NonNull
    private final LocalDate date;
    @NonNull
    private final LocalTime start;
    @NonNull
    private final LocalTime end;
    @NonNull
    private final Room room;
    @NonNull
    private final List<String> participants;

    public Meeting(int id,
                   @NonNull String subject,
                   @NonNull LocalDate date,
                   @NonNull LocalTime start,
                   @NonNull LocalTime end,
                   @NonNull Room room,
                   @NonNull List<String> participants) {
        this.id = id;
        this.subject = subject;
        this.date = date;
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
    public LocalDate getDate() {
        return date;
    }

    @NonNull
    public LocalTime getStart() {
        return start;
    }

    @NonNull
    public LocalTime getEnd() {
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
        return id == meeting.id && subject.equals(meeting.subject) && date.equals(meeting.date) && start.equals(meeting.start) && end.equals(meeting.end) && room == meeting.room && participants.equals(meeting.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, date, start, end, room, participants);
    }

    @NonNull
    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", date=" + date +
                ", start=" + start +
                ", end=" + end +
                ", room=" + room +
                ", participants=" + participants +
                '}';
    }
}
