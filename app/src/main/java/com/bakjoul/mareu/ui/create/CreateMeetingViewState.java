package com.bakjoul.mareu.ui.create;

import androidx.annotation.NonNull;

import com.bakjoul.mareu.data.model.Room;

import java.time.LocalDateTime;

public class CreateMeetingViewState {

    @NonNull
    private final Room[] rooms;

    @NonNull
    private final LocalDateTime start;

    @NonNull
    private final LocalDateTime end;

    public CreateMeetingViewState(@NonNull Room[] rooms, @NonNull LocalDateTime start, @NonNull LocalDateTime end) {
        this.rooms = rooms;
        this.start = start;
        this.end = end;
    }

    @NonNull
    public Room[] getRooms() {
        return rooms;
    }

    @NonNull
    public LocalDateTime getStart() {
        return start;
    }

    @NonNull
    public LocalDateTime getEnd() {
        return end;
    }
}
