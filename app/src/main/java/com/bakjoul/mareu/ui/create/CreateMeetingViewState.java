package com.bakjoul.mareu.ui.create;

import androidx.annotation.NonNull;

import com.bakjoul.mareu.data.model.Room;

import javax.annotation.Nullable;

public class CreateMeetingViewState {

    @NonNull
    private final Room[] rooms;
    @NonNull
    private final String date;
    @NonNull
    private final String start;
    @NonNull
    private final String end;
    @Nullable
    private final String subjectError;
    @Nullable
    private final String participantsError;
    @Nullable
    private final String roomError;

    public CreateMeetingViewState(@NonNull Room[] rooms, @NonNull String date, @NonNull String start, @NonNull String end, @Nullable String subjectError, @Nullable String participantsError, @Nullable String roomError) {
        this.rooms = rooms;
        this.date = date;
        this.start = start;
        this.end = end;
        this.subjectError = subjectError;
        this.participantsError = participantsError;
        this.roomError = roomError;
    }

    @NonNull
    public Room[] getRooms() {
        return rooms;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getStart() {
        return start;
    }

    @NonNull
    public String getEnd() {
        return end;
    }

    @Nullable
    public String getSubjectError() {
        return subjectError;
    }

    @Nullable
    public String getParticipantsError() {
        return participantsError;
    }

    @Nullable
    public String getRoomError() {
        return roomError;
    }
}
