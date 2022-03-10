package com.bakjoul.mareu.ui.create;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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

@RunWith(MockitoJUnitRunner.class)
public class CreateMeetingViewModelTest {

    private static final String DEFAULT_SUBJECT = "DEFAULT_SUBJECT";
    private static final LocalDate DEFAULT_DATE = LocalDate.now();
    private static final LocalTime DEFAULT_START = LocalTime.of(10, 0);
    private static final LocalTime DEFAULT_END = LocalTime.of(11, 0);
    private static final List<String> DEFAULT_PARTICIPANTS_LIST =
            new ArrayList<>(Arrays.asList("testparticipant1@lamzone.com", "testparticipant2@lamzone.com"));

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private MeetingRepository meetingRepository;

    private CreateMeetingViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new CreateMeetingViewModel(meetingRepository);
    }

    @Test
    public void given_inputs_are_correct_then_livedata_should_expose_no_error_and_single_live_event_should_expose_dismiss_toast() {
        // Given
        // Modifie les champs de la réunion à ajouter
        viewModel.onSubjectChanged(DEFAULT_SUBJECT);
        viewModel.onRoomChanged(Room.Black);
        viewModel.onDateChanged(DEFAULT_DATE.getYear(), DEFAULT_DATE.getMonthValue() - 1, DEFAULT_DATE.getDayOfMonth() + 1);
        viewModel.onStartTimeChanged(DEFAULT_START.getHour(), DEFAULT_START.getMinute());
        viewModel.onEndTimeChanged(DEFAULT_END.getHour(), DEFAULT_END.getMinute());
        viewModel.onParticipantsChanged(DEFAULT_PARTICIPANTS_LIST);

        // Mock le retour de la méthode du repository qui indique que la réunion n'en chevauche pas une autre
        when(meetingRepository.addMeeting(
                anyString(),
                any(LocalDate.class),
                any(LocalTime.class),
                any(LocalTime.class),
                any(Room.class),
                anyList())).thenReturn(true);   // Retourne vrai si la réunion n'en chevauche pas une autre

        // When
        viewModel.createMeeting();  // Vérifie et ajoute la réunion
        CreateMeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateLiveData());
        MeetingViewEvent viewEventResult = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        // Erreurs attendues
        assertNull(result.getSubjectError());
        assertNull(result.getParticipantsError());
        assertNull(result.getRoomError());
        assertNull(result.getDateError());
        assertNull(result.getStartError());
        assertNull(result.getEndError());

        // Vérifie que la livedata SingleLiveEvent expose le view event correspondant
        assertEquals(MeetingViewEvent.DISMISS_CREATE_MEETING_DIALOG.name(), viewEventResult.name());
    }

    @Test
    public void given_inputs_are_correct_then_livedata_should_expose_no_error_but_single_live_event_should_expose_overlapping_meetings_toast() {
        // Given
        // Modifie les champs de la réunion à ajouter
        viewModel.onSubjectChanged(DEFAULT_SUBJECT);
        viewModel.onRoomChanged(Room.Blue);
        viewModel.onDateChanged(DEFAULT_DATE.getYear(), DEFAULT_DATE.getMonthValue() - 1, DEFAULT_DATE.getDayOfMonth() + 1);    // Les mois commencent à 0
        viewModel.onStartTimeChanged(DEFAULT_START.getHour(), DEFAULT_START.getMinute());
        viewModel.onEndTimeChanged(DEFAULT_END.getHour(), DEFAULT_END.getMinute());
        viewModel.onParticipantsChanged(DEFAULT_PARTICIPANTS_LIST);

        // Mock le retour de la méthode du repository qui indique que la réunion en chevauche une autre déjà existante
        when(meetingRepository.addMeeting(
                anyString(),
                any(LocalDate.class),
                any(LocalTime.class),
                any(LocalTime.class),
                any(Room.class),
                anyList())).thenReturn(false);  // Retourne faux si la réunion en chevauche une autre

        // When
        viewModel.createMeeting();  // Vérifie et ici n'ajoutera pas la réunion
        CreateMeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateLiveData());
        MeetingViewEvent viewEventResult = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        // Erreurs attendues
        assertNull(result.getSubjectError());
        assertNull(result.getParticipantsError());
        assertNull(result.getRoomError());
        assertNull(result.getDateError());
        assertNull(result.getStartError());
        assertNull(result.getEndError());

        // Vérifie que la livedata SingleLiveEvent expose le view event correspondant
        assertEquals(MeetingViewEvent.DISPLAY_OVERLAPPING_MEETING_TOAST.name(), viewEventResult.name());
    }

    @Test
    public void given_inputs_are_null_then_livedata_should_expose_all_errors_and_single_live_event_should_be_null() {
        // When
        viewModel.createMeeting();
        CreateMeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateLiveData());

        // Then
        // Erreurs attendues
        assertEquals("Veuillez saisir un sujet", result.getSubjectError());
        assertEquals("Veuillez saisir au moins un participant", result.getParticipantsError());
        assertEquals("Veuillez sélectionner une salle", result.getRoomError());
        assertEquals("Veuillez sélectionner une date", result.getDateError());
        assertEquals("Veuillez définir une heure de début", result.getStartError());
        assertEquals("Veuillez définir une heure de fin", result.getEndError());

        try {
            LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());
        } catch (AssertionError expectedError) {
            // Vérifie que la livedata SingleLiveEvent retourne l'erreur indiquant qu'elle est null
            assertEquals("LiveData value is null !", expectedError.getMessage());
        }
    }

    @Test
    public void given_start_time_is_past_then_livedata_should_expose_no_error_but_single_live_event_should_expose_start_time_passed_toast() {
        // Given
        viewModel.onSubjectChanged(DEFAULT_SUBJECT);
        viewModel.onRoomChanged(Room.Brown);
        viewModel.onDateChanged(DEFAULT_DATE.getYear(), DEFAULT_DATE.getMonthValue() - 1, DEFAULT_DATE.getDayOfMonth() - 5);    // Les mois commencent à 0
        viewModel.onStartTimeChanged(9, 0);
        viewModel.onEndTimeChanged(10, 0);
        viewModel.onParticipantsChanged(DEFAULT_PARTICIPANTS_LIST);

        // When
        viewModel.createMeeting();
        CreateMeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateLiveData());
        MeetingViewEvent viewEventResult = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        // Erreurs attendues
        assertNull(result.getSubjectError());
        assertNull(result.getParticipantsError());
        assertNull(result.getRoomError());
        assertNull(result.getDateError());
        assertNull(result.getStartError());
        assertNull(result.getEndError());

        // Vérifie que la livedata SingleLiveEvent expose le view event correspondant
        assertEquals(MeetingViewEvent.DISPLAY_MEETING_START_TIME_PASSED_TOAST.name(), viewEventResult.name());
    }

    @Test
    public void given_inputs_are_correct_then_livedata_should_expose_no_error_but_single_live_event_should_expose_minimum_duration_toast() {
        // Given
        viewModel.onSubjectChanged(DEFAULT_SUBJECT);
        viewModel.onRoomChanged(Room.Pink);
        viewModel.onDateChanged(DEFAULT_DATE.getYear(), DEFAULT_DATE.getMonthValue() - 1, DEFAULT_DATE.getDayOfMonth() + 1);
        viewModel.onStartTimeChanged(14, 0);
        viewModel.onEndTimeChanged(13, 0);
        viewModel.onParticipantsChanged(DEFAULT_PARTICIPANTS_LIST);

        // When
        viewModel.createMeeting();
        CreateMeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateLiveData());
        MeetingViewEvent viewEventResult = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        // Erreurs attendues
        assertNull(result.getSubjectError());
        assertNull(result.getParticipantsError());
        assertNull(result.getRoomError());
        assertNull(result.getDateError());
        assertNull(result.getStartError());
        assertNull(result.getEndError());

        // Vérifie que la livedata SingleLiveEvent expose le view event correspondant
        assertEquals(MeetingViewEvent.DISPLAY_MINIMUM_MEETING_DURATION_TOAST.name(), viewEventResult.name());
    }

    @Test
    public void given_inputs_are_correct_then_livedata_should_expose_no_error_but_single_live_event_should_expose_maximum_duration_toast() {
        // Given
        viewModel.onSubjectChanged(DEFAULT_SUBJECT);
        viewModel.onRoomChanged(Room.Orange);
        viewModel.onDateChanged(DEFAULT_DATE.getYear(), DEFAULT_DATE.getMonthValue() - 1, DEFAULT_DATE.getDayOfMonth() + 1);
        viewModel.onStartTimeChanged(15, 0);
        viewModel.onEndTimeChanged(20, 0);
        viewModel.onParticipantsChanged(DEFAULT_PARTICIPANTS_LIST);

        // When
        viewModel.createMeeting();
        CreateMeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getViewStateLiveData());
        MeetingViewEvent viewEventResult = LiveDataTestUtil.getValueForTesting(viewModel.getSingleLiveEvent());

        // Then
        // Erreurs attendues
        assertNull(result.getSubjectError());
        assertNull(result.getParticipantsError());
        assertNull(result.getRoomError());
        assertNull(result.getDateError());
        assertNull(result.getStartError());
        assertNull(result.getEndError());

        // Vérifie que la livedata SingleLiveEvent expose le view event correspondant
        assertEquals(MeetingViewEvent.DISPLAY_MAXIMUM_MEETING_DURATION_TOAST.name(), viewEventResult.name());
    }

}
