package com.bakjoul.mareu.ui.date_filter;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.ui.MeetingViewEvent;
import com.bakjoul.mareu.utils.SingleLiveEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DateFilterViewModel extends ViewModel {

    private final MutableLiveData<DateFilterViewState> viewStateMutableLiveData = new MutableLiveData<>();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH);

    private final SingleLiveEvent<MeetingViewEvent> singleLiveEvent = new SingleLiveEvent<>();

    @Nullable
    private LocalDate date;
    @Nullable
    private LocalTime start;
    @Nullable
    private LocalTime end;

    @Inject
    public DateFilterViewModel() {
        viewStateMutableLiveData.setValue(
                new DateFilterViewState(
                        formatDate(date),
                        formatTime(start),
                        formatTime(end)
                )
        );

    }

    public LiveData<DateFilterViewState> getViewStateMutableLiveData() {
        return viewStateMutableLiveData;
    }

    public SingleLiveEvent<MeetingViewEvent> getSingleLiveEvent() {
        return singleLiveEvent;
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

    public void onDisplayDatePickerClicked() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_CREATE_MEETING_DATE_PICKER);
    }

    public void onDisplayStartTimePickerClicked() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_CREATE_MEETING_START_PICKER);
    }

    public void onDisplayEndTimePickerClicked() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_CREATE_MEETING_END_PICKER);
    }

    public void onDateChanged(int year, int month, int day) {
        date = LocalDate.of(year, month, day);

        DateFilterViewState viewState = viewStateMutableLiveData.getValue();
        if (date != null && viewState != null) {
            viewStateMutableLiveData.setValue(
                    new DateFilterViewState(
                            formatDate(date),
                            viewState.getStart(),
                            viewState.getEnd()
                    )
            );
        }
    }

    public void onStartTimeChanged(int hour, int minute) {
        start = LocalTime.of(hour, minute);

        DateFilterViewState viewState = viewStateMutableLiveData.getValue();
        if (viewState != null) {
            viewStateMutableLiveData.setValue(
                    new DateFilterViewState(
                            viewState.getDate(),
                            formatTime(start),
                            viewState.getEnd()
                    )
            );
        }
    }

    public void onEndTimeChanged(int hour, int minute) {
        end = LocalTime.of(hour, minute);

        DateFilterViewState viewState = viewStateMutableLiveData.getValue();
        if (viewState != null) {
            viewStateMutableLiveData.setValue(
                    new DateFilterViewState(
                            viewState.getDate(),
                            viewState.getStart(),
                            formatTime(end)
                    )
            );
        }
    }
}
