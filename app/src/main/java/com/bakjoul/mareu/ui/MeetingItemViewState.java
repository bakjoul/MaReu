package com.bakjoul.mareu.ui;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class MeetingItemViewState {
    private final int id;
    @ColorRes
    private final int roomColor;
    @NonNull
    private final String subject;
    @NonNull
    private final List<String> participants;

    public MeetingItemViewState(int id, int roomColor, @NonNull String subject, @NonNull List<String> participants) {
        this.id = id;
        this.roomColor = roomColor;
        this.subject = subject;
        this.participants = participants;
    }

    public int getId() {
        return id;
    }

    public int getRoomColor() {
        return roomColor;
    }

    @NonNull
    public String getSubject() {
        return subject;
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
        return id == that.id && roomColor == that.roomColor && subject.equals(that.subject) && participants.equals(that.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomColor, subject, participants);
    }
}
