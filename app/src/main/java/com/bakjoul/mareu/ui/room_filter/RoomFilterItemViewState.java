package com.bakjoul.mareu.ui.room_filter;

import androidx.annotation.NonNull;

import com.bakjoul.mareu.data.model.Room;

import java.util.Objects;

public class RoomFilterItemViewState {

    @NonNull
    private final Room room;
    private final boolean isSelected;
    private final String color;

    public RoomFilterItemViewState(@NonNull Room room, boolean isSelected, String color) {
        this.room = room;
        this.isSelected = isSelected;
        this.color = color;
    }

    @NonNull
    public Room getRoom() {
        return room;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomFilterItemViewState that = (RoomFilterItemViewState) o;
        return isSelected == that.isSelected && room == that.room && color.equals(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, isSelected, color);
    }
}
