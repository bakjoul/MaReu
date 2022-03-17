package com.bakjoul.mareu.ui.date_filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.data.repository.FilterParametersRepository;
import com.bakjoul.mareu.ui.MeetingViewEvent;
import com.bakjoul.mareu.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class DateFilterViewModelTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.of(2027, 2, 22);
    private static final LocalDate EXPECTED_DATE = DEFAULT_DATE.plusMonths(1);
    // Les mois commencent à 0 avec MaterialDateTimePicker donc on ajoute aussi 1 mois à la date attendue

    private static final LocalTime DEFAULT_START_TIME = LocalTime.of(10, 0);
    private static final LocalTime DEFAULT_END_TIME = LocalTime.of(11, 0);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FilterParametersRepository filterParametersRepository;

    private MutableLiveData<LocalDate> selectedDateLiveData;
    private MutableLiveData<LocalTime> selectedStartLiveData;
    private MutableLiveData<LocalTime> selectedEndLiveData;

    private DateFilterViewModel viewModel;

    @Before
    public void setUp() {
        // Réinitialise les LiveDatas
        selectedDateLiveData = new MutableLiveData<>();
        selectedStartLiveData = new MutableLiveData<>();
        selectedEndLiveData = new MutableLiveData<>();

        // Mock les LiveDatas retournées par le repository
        given(filterParametersRepository.getSelectedDateLiveData()).willReturn(selectedDateLiveData);
        given(filterParametersRepository.getSelectedStartTimeLiveData()).willReturn(selectedStartLiveData);
        given(filterParametersRepository.getSelectedEndTimeLiveData()).willReturn(selectedEndLiveData);

        viewModel = new DateFilterViewModel(filterParametersRepository);
    }

    // Vérifie que la LiveData expose le view state dans son état initial
    @Test
    public void initial_case() {
        // When
        DateFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateMutableLiveData());

        // Then
        assertEquals(getExpectedDateFilterViewState(null, null, null), result);
    }

    @Test
    public void given_on_date_picker_clicked_single_live_event_should_expose_display_date_picker_view_event() {
        // Given
        viewModel.onDisplayDatePickerClicked();

        // When
        MeetingViewEvent result = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        assertEquals(MeetingViewEvent.DISPLAY_CREATE_MEETING_DATE_PICKER, result);
    }

    @Test
    public void given_on_start_picker_clicked_single_live_event_should_expose_display_start_picker_view_event() {
        // Given
        viewModel.onDisplayStartTimePickerClicked();

        // When
        MeetingViewEvent result = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        assertEquals(MeetingViewEvent.DISPLAY_CREATE_MEETING_START_PICKER, result);
    }

    @Test
    public void given_on_end_picker_clicked_single_live_event_should_expose_display_end_picker_view_event() {
        // Given
        viewModel.onDisplayEndTimePickerClicked();

        // When
        MeetingViewEvent result = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        assertEquals(MeetingViewEvent.DISPLAY_CREATE_MEETING_END_PICKER, result);
    }

    @Test
    public void given_a_date_input_then_livedata_should_expose_viewstate_with_given_date() {
        // Given
        viewModel.onDateChanged(DEFAULT_DATE.getYear(), DEFAULT_DATE.getMonthValue(), DEFAULT_DATE.getDayOfMonth());

        // When
        DateFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateMutableLiveData());

        // Then
        assertEquals(getExpectedDateFilterViewState(EXPECTED_DATE, null, null), result);
    }

    @Test
    public void given_a_start_time_input_then_livedata_should_expose_viewstate_with_given_start_time() {
        // Given
        viewModel.onStartTimeChanged(DEFAULT_START_TIME.getHour(), DEFAULT_START_TIME.getMinute());

        // When
        DateFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateMutableLiveData());

        // Then
        assertEquals(getExpectedDateFilterViewState(null, DEFAULT_START_TIME, null), result);
    }

    @Test
    public void given_an_end_time_input_then_livedata_should_expose_viewstate_with_given_end_time() {
        // Given
        viewModel.onEndTimeChanged(DEFAULT_END_TIME.getHour(), DEFAULT_END_TIME.getMinute());

        // When
        DateFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateMutableLiveData());

        // Then
        assertEquals(getExpectedDateFilterViewState(null, null, DEFAULT_END_TIME), result);
    }

    @Test
    public void given_all_inputs_are_filled_livedata_should_expose_viewstate_with_inputted_values() {
        // Given
        viewModel.onDateChanged(DEFAULT_DATE.getYear(), DEFAULT_DATE.getMonthValue(), DEFAULT_DATE.getDayOfMonth());
        viewModel.onStartTimeChanged(DEFAULT_START_TIME.getHour(), DEFAULT_START_TIME.getMinute());
        viewModel.onEndTimeChanged(DEFAULT_END_TIME.getHour(), DEFAULT_END_TIME.getMinute());

        // When
        DateFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateMutableLiveData());

        // Then
        assertEquals(getExpectedDateFilterViewState(EXPECTED_DATE, DEFAULT_START_TIME, DEFAULT_END_TIME), result);
    }

    @Test
    public void given_on_clear_date_filter_called_livedata_should_expose_viewstate_with_null_date() {
        // Given
        viewModel.onDateChanged(DEFAULT_DATE.getYear(), DEFAULT_DATE.getMonthValue(), DEFAULT_DATE.getDayOfMonth());

        // When
        viewModel.onClearDateFilter();
        DateFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateMutableLiveData());

        // Then
        assertEquals(getExpectedDateFilterViewState(null, null, null), result);
    }

    @Test
    public void given_on_clear_start_time_filter_called_livedata_should_expose_viewstate_with_null_start_time() {
        // Given
        viewModel.onStartTimeChanged(DEFAULT_START_TIME.getHour(), DEFAULT_START_TIME.getMinute());

        // When
        viewModel.onClearStartTimeFilter();
        DateFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateMutableLiveData());

        // Then
        assertEquals(getExpectedDateFilterViewState(null, null, null), result);
    }

    @Test
    public void given_on_clear_end_time_filter_called_livedata_should_expose_viewstate_with_null_end_time() {
        // Given
        viewModel.onEndTimeChanged(DEFAULT_END_TIME.getHour(), DEFAULT_END_TIME.getMinute());

        // When
        viewModel.onClearEndTimeFilter();
        DateFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateMutableLiveData());

        // Then
        assertEquals(getExpectedDateFilterViewState(null, null, null), result);
    }

    // region OUT
    @NonNull
    private DateFilterViewState getExpectedDateFilterViewState(
            @Nullable LocalDate date,
            @Nullable LocalTime start,
            @Nullable LocalTime end
    ) {
        return new DateFilterViewState(formatDate(date), formatTime(start), formatTime(end));
    }

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH);

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
    // endregion
}
