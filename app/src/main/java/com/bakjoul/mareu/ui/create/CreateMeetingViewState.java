package com.bakjoul.mareu.ui.create;

import androidx.annotation.NonNull;

import com.bakjoul.mareu.data.model.Room;

public class CreateMeetingViewState {

    @NonNull
    private final Room[] rooms;

    @NonNull
    private final String date;

    @NonNull
    private final String start;

    @NonNull
    private final String end;

    public CreateMeetingViewState(@NonNull Room[] rooms, @NonNull String date, @NonNull String start, @NonNull String end) {
        this.rooms = rooms;
        this.date = date;
        this.start = start;
        this.end = end;
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
}
