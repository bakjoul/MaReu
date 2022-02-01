package com.bakjoul.mareu.ui;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.repository.MeetingRepository;
import com.bakjoul.mareu.ui.list.MeetingItemViewState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MeetingViewModel extends ViewModel {
    @NonNull
    private final MeetingRepository meetingRepository;

    private final MediatorLiveData<MeetingListViewState> meetingListViewStateMediatorLiveData = new MediatorLiveData<>();

    @Inject
    public MeetingViewModel(@NonNull MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;

        init();
    }

    @NonNull
    public LiveData<MeetingListViewState> getMeetingListViewStateLiveData() {
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
                            meeting.getSubject(),
                            meeting.getStart(),
                            meeting.getRoom(),
                            meeting.getParticipants()
                    )
            );
        }
        return new MeetingListViewState(meetingItemViewStates);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onDeleteClicked(int id) {
        meetingRepository.deleteMeeting(id);
    }
}
