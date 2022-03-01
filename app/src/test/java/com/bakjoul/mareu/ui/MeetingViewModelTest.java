package com.bakjoul.mareu.ui;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.FilterParametersRepository;
import com.bakjoul.mareu.data.repository.MeetingRepository;
import com.bakjoul.mareu.ui.list.MeetingItemViewState;
import com.bakjoul.mareu.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class MeetingViewModelTest {

    public static final int MEETING_COUNT = 3;

    public static final String DEFAULT_SUBJECT = "DEFAULT_SUBJECT";
    public static final LocalDate DEFAULT_DATE = LocalDate.of(2022, 3, 1);
    public static final LocalTime DEFAULT_START = LocalTime.of(14, 30);
    public static final LocalTime DEFAULT_END = LocalTime.of(15, 30);

    public static final int PARTICIPANTS_COUNT = 4;
    public static final String DEFAULT_PARTICIPANT = "DEFAULT_PARTICIPANT_%d_%d@mail.com";

    private static final String EXPECTED_DATE = "0%d/03";
    private static final String EXPECTED_TIME = "%dh30";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final MeetingRepository meetingRepository = Mockito.mock(MeetingRepository.class);

    private final FilterParametersRepository filterParametersRepository = Mockito.mock(FilterParametersRepository.class);

    private MutableLiveData<List<Meeting>> meetingsLiveData;
    private MutableLiveData<Map<Room, Boolean>> selectedRoomsLiveData;
    private MutableLiveData<LocalDate> selectedDateLiveData;
    private MutableLiveData<LocalTime> selectedStartTimeLiveData;
    private MutableLiveData<LocalTime> selectedEndTimeLiveData;

    private MeetingViewModel viewModel;

    @Before
    public void setUp() {
        // Réinitialise les LiveDatas
        meetingsLiveData = new MutableLiveData<>();
        selectedRoomsLiveData = new MutableLiveData<>();
        selectedDateLiveData = new MutableLiveData<>();
        selectedStartTimeLiveData = new MutableLiveData<>();
        selectedEndTimeLiveData = new MutableLiveData<>();

        // Mock les LiveDatas retournées par les repositories
        given(meetingRepository.getMeetingsLiveData()).willReturn(meetingsLiveData);
        given(filterParametersRepository.getSelectedRoomsLiveData()).willReturn(selectedRoomsLiveData);
        given(filterParametersRepository.getSelectedDateLiveData()).willReturn(selectedDateLiveData);
        given(filterParametersRepository.getSelectedStartTimeLiveData()).willReturn(selectedStartTimeLiveData);
        given(filterParametersRepository.getSelectedEndTimeLiveData()).willReturn(selectedEndTimeLiveData);

        // Initialise les valeurs par défaut des LiveDatas
        meetingsLiveData.setValue(getDefaultMeetingList());

        Map<Room, Boolean> selectedRooms = new HashMap<>();
        for (Room room : Room.values()) {
            selectedRooms.put(room, false);
        }
        selectedRoomsLiveData.setValue(selectedRooms);

        viewModel = new MeetingViewModel(meetingRepository, filterParametersRepository);
    }

    @Test
    public void nominal_case() {
        // When
        MeetingViewState meetingViewState = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(), meetingViewState.getMeetingItemViewStateList());
    }

    @Test
    public void initial_case() {
        // Given
        meetingsLiveData.setValue(new ArrayList<>());

        // When
        MeetingViewState meetingViewState = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(0, meetingViewState.getMeetingItemViewStateList().size());
    }

    @Test
    public void nominal_case_one_meeting() {
        // Given
        meetingsLiveData.setValue(getDefaultMeetingList(1));

        // When
        MeetingViewState meetingViewState = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(1), meetingViewState.getMeetingItemViewStateList());
    }

    // region IN
    public List<Meeting> getDefaultMeetingList() {
        return getDefaultMeetingList(MEETING_COUNT);
    }

    public List<Meeting> getDefaultMeetingList(int count) {
        List<Meeting> meetings = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            meetings.add(
                new Meeting(
                    i,
                    DEFAULT_SUBJECT + i,
                    DEFAULT_DATE.plusDays(i),
                    DEFAULT_START.plusHours(i),
                    DEFAULT_END.plusHours(i),
                    Room.values()[i],
                    getDefaultParticipants(i)
                )
            );
        }

        return meetings;
    }

    public List<String> getDefaultParticipants(int meetingIndex) {
        return getDefaultParticipants(PARTICIPANTS_COUNT, meetingIndex);
    }

    public List<String> getDefaultParticipants(int count, int meetingIndex) {
        List<String> participants = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            participants.add(String.format(DEFAULT_PARTICIPANT, meetingIndex, i));
        }

        return participants;
    }
    // endregion IN

    // region OUT
    private List<MeetingItemViewState> getExpectedMeetingItemViewStates() {
        return getExpectedMeetingItemViewStates(MEETING_COUNT);
    }

    private List<MeetingItemViewState> getExpectedMeetingItemViewStates(int count) {
        List<MeetingItemViewState> meetingViewStateItems = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            meetingViewStateItems.add(
                new MeetingItemViewState(
                    i,
                    DEFAULT_SUBJECT + i,
                    String.format(EXPECTED_DATE, 1 + i),
                    String.format(EXPECTED_TIME, 14 + i),
                    String.format(EXPECTED_TIME, 15 + i),
                    Room.values()[i],
                    getExpectedParticipants(i)
                )
            );
        }

        return meetingViewStateItems;
    }

    private String getExpectedParticipants(int meetingIndex) {
        return getExpectedParticipants(PARTICIPANTS_COUNT, meetingIndex);
    }

    private String getExpectedParticipants(int count, int meetingIndex) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < PARTICIPANTS_COUNT; i++) {
            result.append(String.format(DEFAULT_PARTICIPANT, meetingIndex, i));

            if (i + 1 < PARTICIPANTS_COUNT) {
                result.append(", ");
            }
        }

        return result.toString();
    }
    // endregion OUT
}
