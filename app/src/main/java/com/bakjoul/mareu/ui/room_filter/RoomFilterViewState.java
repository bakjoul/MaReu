package com.bakjoul.mareu.ui.room_filter;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class RoomFilterViewState {

    @NonNull
    private final List<RoomFilterItemViewState> roomFilterItemViewStates;

    public RoomFilterViewState(@NonNull List<RoomFilterItemViewState> roomFilterItemViewStates) {
        this.roomFilterItemViewStates = roomFilterItemViewStates;
    }

    @NonNull
    public List<RoomFilterItemViewState> getRoomFilterItemViewStates() {
        return roomFilterItemViewStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomFilterViewState that = (RoomFilterViewState) o;
        return roomFilterItemViewStates.equals(that.roomFilterItemViewStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomFilterItemViewStates);
    }
}
