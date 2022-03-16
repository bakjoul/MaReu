package com.bakjoul.mareu.ui.list;

import androidx.annotation.NonNull;

import com.bakjoul.mareu.data.model.Room;

import java.util.Objects;

public class MeetingItemViewState {
    private final int id;
    @NonNull
    private final String subject;
    @NonNull
    private final String date;
    @NonNull
    private final String startTime;
    @NonNull
    private final String endTime;
    @NonNull
    private final Room room;
    @NonNull
    private final String participants;

    public MeetingItemViewState(int id,
                                @NonNull String subject,
                                @NonNull String date,
                                @NonNull String startTime,
                                @NonNull String endTime,
                                @NonNull Room room,
                                @NonNull String participants) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
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
    public String getDate() {
        return date;
    }

    @NonNull
    public String getStartTime() {
        return startTime;
    }

    @NonNull
    public String getEndTime() {
        return endTime;
    }

    @NonNull
    public Room getRoom() {
        return room;
    }

    @NonNull
    public String getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingItemViewState that = (MeetingItemViewState) o;
        return id == that.id && subject.equals(that.subject) && date.equals(that.date) && startTime.equals(that.startTime) && endTime.equals(that.endTime) && room == that.room && participants.equals(that.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, date, startTime, endTime, room, participants);
    }

    @NonNull
    @Override
    public String toString() {
        return "MeetingItemViewState{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", room=" + room +
                ", participants='" + participants + '\'' +
                '}';
    }
}
