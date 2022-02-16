package com.bakjoul.mareu.ui;

import androidx.annotation.NonNull;

import com.bakjoul.mareu.ui.list.MeetingItemViewState;
import com.bakjoul.mareu.ui.room_filter.RoomFilterItemViewState;

import java.util.List;
import java.util.Objects;

public class MeetingListViewState {
    @NonNull
    private final List<MeetingItemViewState> meetingItemViewStateList;

    @NonNull
    private final List<RoomFilterItemViewState> roomFilterItemViewStates;

    public MeetingListViewState(@NonNull List<MeetingItemViewState> meetingItemViewStateList, @NonNull List<RoomFilterItemViewState> roomFilterItemViewStates) {
        this.meetingItemViewStateList = meetingItemViewStateList;
        this.roomFilterItemViewStates = roomFilterItemViewStates;
    }

    @NonNull
    public List<MeetingItemViewState> getMeetingItemViewStateList() {
        return meetingItemViewStateList;
    }

    @NonNull
    public List<RoomFilterItemViewState> getRoomFilterItemViewStates() {
        return roomFilterItemViewStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingListViewState that = (MeetingListViewState) o;
        return meetingItemViewStateList.equals(that.meetingItemViewStateList) && roomFilterItemViewStates.equals(that.roomFilterItemViewStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingItemViewStateList, roomFilterItemViewStates);
    }
}
