package com.bakjoul.mareu.ui;

import androidx.annotation.NonNull;

import com.bakjoul.mareu.ui.list.MeetingItemViewState;

import java.util.List;
import java.util.Objects;

public class MeetingListViewState {
    @NonNull
    private final List<MeetingItemViewState> meetingItemViewStateList;

    public MeetingListViewState(@NonNull List<MeetingItemViewState> meetingItemViewStateList) {
        this.meetingItemViewStateList = meetingItemViewStateList;
    }

    @NonNull
    public List<MeetingItemViewState> getMeetingItemViewStateList() {
        return meetingItemViewStateList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingListViewState that = (MeetingListViewState) o;
        return meetingItemViewStateList.equals(that.meetingItemViewStateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingItemViewStateList);
    }
}
