package com.bakjoul.mareu.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.repository.MeetingRepository;

import java.util.ArrayList;
import java.util.List;

public class MeetingViewModel {
    @NonNull
    private final MeetingRepository meetingRepository;

    private final MediatorLiveData<MeetingListViewState> meetingListViewStateMediatorLiveData = new MediatorLiveData<>();

    public MeetingViewModel(@NonNull MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;

        init();
    }

    public LiveData<MeetingListViewState> getMeetingListViewStateMediatorLiveData() {
        return meetingListViewStateMediatorLiveData;
    }

    private void init() {
        final LiveData<List<Meeting>> meetingsLiveData = meetingRepository.getMeetingsLiveData();

        meetingListViewStateMediatorLiveData.addSource(meetingsLiveData, meetings ->
                meetingListViewStateMediatorLiveData.setValue(mapMeeting(meetingsLiveData.getValue())));
    }

    @NonNull
    private MeetingListViewState mapMeeting(@NonNull final List<Meeting> meetings) {
        List<MeetingItemViewState> meetingItemViewStates = new ArrayList<>();
        for (Meeting meeting : meetings) {
            meetingItemViewStates.add(
                    new MeetingItemViewState(
                            meeting.getId(),
                            meeting.getRoom().getColor(),
                            meeting.getSubject(),
                            meeting.getParticipants()
                    )
            );
        }
        return new MeetingListViewState(meetingItemViewStates);
    }
}
