package com.bakjoul.mareu.ui.date_filter;

import static org.mockito.BDDMockito.given;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.data.repository.FilterParametersRepository;
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

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DateFilterViewModelTest {

    private static final LocalDate DATE = LocalDate.of(2027, 2, 22);
    private static final LocalTime START_TIME = LocalTime.of(10,0);
    private static final LocalTime END_TIME = LocalTime.of(11,0);

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
        assertEquals(getExpectedDateFilterViewState(null, null , null), result);
    }

    // Vérifie que si on remplit les champs alors la livedata expose le   avec les valeurs saisies
    @Test
    public void given_all_inputs_are_filled_livedata_should_expose_the_inputted_values() {
        // Given
        viewModel.onDateChanged(DATE.getYear(), DATE.getMonthValue(), DATE.getDayOfMonth());
        viewModel.onStartTimeChanged(START_TIME.getHour(), START_TIME.getMinute());
        viewModel.onEndTimeChanged(END_TIME.getHour(), END_TIME.getMinute());

        // When
        DateFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateMutableLiveData());

        // Then
        assertEquals(getExpectedDateFilterViewState(DATE, START_TIME, END_TIME), result);
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
