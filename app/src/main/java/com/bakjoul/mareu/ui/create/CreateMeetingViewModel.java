package com.bakjoul.mareu.ui.create;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.MeetingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CreateMeetingViewModel extends ViewModel {

    private static final int MEETING_MINIMUM_DURATION = 30;

    @NonNull
    private final MeetingRepository meetingRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH);

    private final MutableLiveData<Boolean> datePickerDialogData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> startTimePickerDialogData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> endTimePickerDialogData = new MutableLiveData<>();

    private final MutableLiveData<CreateMeetingViewState> createMeetingViewStateMutableLiveData = new MutableLiveData<>();

    @Nullable
    private String subject;
    @NonNull
    private final List<String> participants = new ArrayList<>();
    @Nullable
    private Room room;
    @Nullable
    private LocalDate date;
    @Nullable
    private LocalTime start;
    @Nullable
    private LocalTime end;

    @Inject
    public CreateMeetingViewModel(@NonNull MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;

        createMeetingViewStateMutableLiveData.setValue(
                new CreateMeetingViewState(
                        Room.values(),
                        formatDate(date),
                        formatTime(start),
                        formatTime(end)
                ));
    }

    public LiveData<CreateMeetingViewState> getViewStateLiveData() {
        return createMeetingViewStateMutableLiveData;
    }

    public LiveData<Boolean> getDatePickerDialogData() {
        return datePickerDialogData;
    }

    public LiveData<Boolean> getStartTimePickerDialogData() {
        return startTimePickerDialogData;
    }

    public LiveData<Boolean> getEndTimePickerDialogData() {
        return endTimePickerDialogData;
    }

    // Met à jour la LiveData quand le champ sujet change
    public void onSubjectChanged(String subject) {
        this.subject = subject;
        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (!subject.isEmpty() && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getDate(),
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
                            viewState.getDate(),
                            viewState.getStart(),
                            viewState.getEnd()
                    )
            );
        }
    }

    // Met à jour la LiveData quand la salle change
    public void onRoomChanged(@Nullable Room room) {
        this.room = room;
        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (room != null && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getDate(),
                            viewState.getStart(),
                            viewState.getEnd()
                    )
            );
        }
    }

    public void onDisplayDatePickerClick() {
        datePickerDialogData.setValue(true);
    }

    public void onDateChanged(int year, int month, int day) {
        date = LocalDate.of(year, month + 1, day);
        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (date != null && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            formatDate(date),
                            viewState.getStart(),
                            viewState.getEnd()
                    )
            );
        }
    }

    public void onDisplayStartTimePickerClick() {
        startTimePickerDialogData.setValue(true);
    }

    public void onDisplayEndTimePickerClick() {
        endTimePickerDialogData.setValue(true);
    }

    public void onStartTimeChanged(int hour, int minute) {
        start = LocalTime.of(hour, minute);
        if (end == null)
            end = start.plusMinutes(MEETING_MINIMUM_DURATION);

        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (viewState != null) {
            assert end != null;
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getDate(),
                            formatTime(start),
                            formatTime(end)
                    )
            );
        }
    }

    public void onEndTimeChanged(int hour, int minute) {
        end = LocalTime.of(hour, minute);

        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (viewState != null) {
            assert end != null;
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getDate(),
                            viewState.getStart(),
                            formatTime(end)
                    )
            );
        }
    }

    private String formatTime(@Nullable LocalTime time) {
        String formattedTime = null;
        if (time != null)
            formattedTime = time.format(timeFormatter);
        return formattedTime;
    }

    private String formatDate(@Nullable LocalDate date) {
        String formattedDate = null;
        if (date != null)
            formattedDate = date.format(dateFormatter);
        return formattedDate;
    }

}
