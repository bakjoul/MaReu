package com.bakjoul.mareu.ui.create;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

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
import java.util.Arrays;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeetingViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private MeetingRepository meetingRepository;

    private CreateMeetingViewModel viewModel;

    private LocalDate date;

    @Before
    public void setUp() {
        viewModel = new CreateMeetingViewModel(meetingRepository);
        date = LocalDate.now().plusDays(7);
    }

    @Test
    public void given_inputs_are_ok_then_livedata_should_expose_no_error_and_single_live_event_should_expose_dismiss() {
        // Given
        viewModel.onSubjectChanged("Test subject");
        viewModel.onParticipantsChanged(Arrays.asList("testparticipant1@lamzone.com", "testparticipant2@lamzone.com"));
        viewModel.onRoomChanged(Room.Blue);
        viewModel.onDateChanged(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        viewModel.onStartTimeChanged(9,0);
        viewModel.onEndTimeChanged(10,0);
        viewModel.createMeeting();

        // When
        CreateMeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateLiveData());
        MeetingViewEvent viewEventResult = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        assertNull(result.getSubjectError());
    }

}
