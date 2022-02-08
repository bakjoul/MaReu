package com.bakjoul.mareu.ui.create;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.MeetingRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CreateMeetingViewModel extends ViewModel {

    @NonNull
    private final MeetingRepository meetingRepository;

    private final MutableLiveData<Boolean>  datePickerDialogData = new MutableLiveData<>();

    private final MutableLiveData<CreateMeetingViewState> createMeetingViewStateMutableLiveData = new MutableLiveData<>();

    private String subject;
    @NonNull
    private final List<String> participants = new ArrayList<>();
    private Room room;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;

    @Inject
    public CreateMeetingViewModel(@NonNull MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;

        this.start = LocalDateTime.now();
        this.end = start.plusMinutes(30);

        createMeetingViewStateMutableLiveData.setValue(
                new CreateMeetingViewState(
                        Room.values(),
                        start,
                        end
                ));
    }

    public LiveData<CreateMeetingViewState> getViewStateLiveData() {
        return createMeetingViewStateMutableLiveData;
    }

    public LiveData<Boolean> getDatePickerDialogData() {
        return datePickerDialogData;
    }

    // Met à jour la LiveData quand le champ sujet change
    public void onSubjectChanged(String subject) {
        this.subject = subject;
        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (!subject.isEmpty() && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getStart(),
                            viewState.getEnd()
                    )
            );
        }
    }

    // Met à jour la LiveData quand le champ participants change
    public void onParticipantsChanged(String participants) {
        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (!participants.isEmpty() && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getStart(),
                            viewState.getEnd()
                    )
            );
        }
    }

    // Met à jour la LiveData quand la salle change
    public void onRoomChanged(Room room) {
        this.room = room;
        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (room != null && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getStart(),
                            viewState.getEnd()
                    )
            );
        }
    }

    public void onDisplayDatePickerClick() {
        datePickerDialogData.setValue(true);
    }

    public void createMeeting() {

    }
}
