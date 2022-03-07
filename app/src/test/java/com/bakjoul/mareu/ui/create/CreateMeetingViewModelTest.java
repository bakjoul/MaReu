package com.bakjoul.mareu.ui.create;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.MeetingRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeetingViewModelTest {

    private static final int MEETING_COUNT = 5;

    private static final String DEFAULT_SUBJECT = "DEFAULT_SUBJECT";
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2022, 3, 7);
    private static final LocalTime DEFAULT_START = LocalTime.of(10, 0);
    private static final LocalTime DEFAULT_END = LocalTime.of(11, 0);

    private static final int PARTICIPANTS_COUNT = 4;
    private static final String DEFAULT_PARTICIPANT = "DEFAULT_PARTICIPANT_%d_%d@lamzone.com";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private MeetingRepository meetingRepository;

    private MutableLiveData<List<Meeting>> meetingsLiveData;

    private CreateMeetingViewModel viewModel;

    @Before
    public void setUp() {
        // Réinitialise la LiveData
        meetingsLiveData = new MutableLiveData<>();

        // Mock la LiveData retournée par le repository
        given(meetingRepository.getMeetingsLiveData()).willReturn(meetingsLiveData);

        // Initialise la LiveData de la liste des réunions avec une valeur par défaut
        meetingsLiveData.setValue(getDefaultMeetingList());

        viewModel = new CreateMeetingViewModel(meetingRepository);
    }

    @Test
    public void given_inputs_are_ok_then_livedata_should_expose_no_error_and_single_live_event_should_expose_dismiss() {
        // Given
        viewModel.onSubjectChanged("Test subject");
        viewModel.onParticipantsChanged(Arrays.asList("testparticipant1@lamzone.com", "testparticipant2@lamzone.com"));
        viewModel.onRoomChanged(Room.Black);
        viewModel.onDateChanged(2022, 2, 7);
        viewModel.onStartTimeChanged(10,0);
        viewModel.onEndTimeChanged(11,0);
        viewModel.createMeeting();

        // When
        CreateMeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateLiveData());
        MeetingViewEvent viewEventResult = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        assertNull(result.getSubjectError());
        assertNull(result.getParticipantsError());
        assertNull(result.getRoomError());
        assertNull(result.getDateError());
        assertNull(result.getStartError());
        assertNull(result.getEndError());
        assertEquals(MeetingViewEvent.DISMISS_CREATE_MEETING_DIALOG.name(), viewEventResult.name());
    }

    // region IN
    // Retourne la liste de réunion par défaut
    @NonNull
    private List<Meeting> getDefaultMeetingList() {
        return getDefaultMeetingList(MEETING_COUNT);
    }

    // Retourne une liste de réunion par défaut de taille "count"
    @NonNull
    private List<Meeting> getDefaultMeetingList(int count) {
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

    // Retourne la liste de participants
    @NonNull
    private List<String> getDefaultParticipants(int meetingIndex) {
        return getDefaultParticipants(PARTICIPANTS_COUNT, meetingIndex);
    }

    // Retourne une liste de participants du nombre "count"
    @NonNull
    private List<String> getDefaultParticipants(int count, int meetingIndex) {
        List<String> participants = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            participants.add(String.format(DEFAULT_PARTICIPANT, meetingIndex, i));
        }

        return participants;
    }
    // endregion

}
