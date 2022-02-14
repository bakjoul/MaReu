package com.bakjoul.mareu.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.MeetingRepository;
import com.bakjoul.mareu.ui.list.MeetingItemViewState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MeetingViewModel extends ViewModel {
    @NonNull
    private final MeetingRepository meetingRepository;

    private final MediatorLiveData<MeetingListViewState> meetingListViewStateMediatorLiveData = new MediatorLiveData<>();

    private final MutableLiveData<Map<Room, Boolean>> selectedRoomsLiveData = new MutableLiveData<>(initRooms());

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

    public void onDeleteClicked(int id) {
        meetingRepository.deleteMeeting(id);
    }

    private Map<Room, Boolean> initRooms() {
        Map<Room, Boolean> rooms = new LinkedHashMap<>();
        for (Room r : Room.values())
            rooms.put(r, false);
        return rooms;
    }

    public void onRoomSelected(@NonNull Room room) {
        Map<Room, Boolean> selectedRooms = selectedRoomsLiveData.getValue();

        if (selectedRooms!=null) {
            for (Map.Entry<Room, Boolean> e : selectedRooms.entrySet()) {
                if (e.getKey() == room) {
                    e.setValue(!e.getValue());
                    break;
                }
            }
            selectedRoomsLiveData.setValue(selectedRooms);
        }
    }
}
