package com.bakjoul.mareu.ui;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class MeetingViewModelTest {

    private static final int MEETING_COUNT = 8;

    private static final String DEFAULT_SUBJECT = "DEFAULT_SUBJECT";
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalTime DEFAULT_START = LocalTime.of(9, 0);
    private static final LocalTime DEFAULT_END = LocalTime.of(10, 0);

    private static final int PARTICIPANTS_COUNT = 4;
    private static final String DEFAULT_PARTICIPANT = "DEFAULT_PARTICIPANT_%d_%d@lamzone.com";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private FilterParametersRepository filterParametersRepository;

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

        // Initialise la LiveData de la liste des réunions avec une valeur par défaut
        meetingsLiveData.setValue(getDefaultMeetingList());

        // Initialise la HashMap de l'état de filtrage des salles
        Map<Room, Boolean> selectedRooms = new HashMap<>();
        for (Room room : Room.values()) {
            selectedRooms.put(room, false);
        }
        selectedRoomsLiveData.setValue(selectedRooms);

        viewModel = new MeetingViewModel(meetingRepository, filterParametersRepository);
    }

    // Vérifie que la LiveData expose la liste des réunions générée par défaut
    @Test
    public void nominal_case() {
        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(), result.getMeetingItemViewStateList());
    }

    // Vérifie que la LiveData expose une liste de réunions vide
    @Test
    public void initial_case() {
        // Given
        meetingsLiveData.setValue(new ArrayList<>());   // Définit la LiveData à une liste vide

        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(0, result.getMeetingItemViewStateList().size());
    }

    // Vérifie que la LiveData expose une liste de réunions contenant la seule réunion générée
    @Test
    public void nominal_case_one_meeting() {
        // Given
        meetingsLiveData.setValue(getDefaultMeetingList(1));    // Liste avec une seule réunion générée

        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(1, null, null, null, null), result.getMeetingItemViewStateList());
    }

    // Vérifie que si on donne une salle à filtrer, la LiveData expose une liste contenant les réunions dans cette salle
    @Test
    public void given_one_room_to_filter_then_livedata_should_expose_one_meeting() {
        // Given
        meetingsLiveData.setValue(getDefaultMeetingList(5));
        selectedRoomsLiveData.setValue(getDefaultRoomFilterHashMap(new ArrayList<>(Collections.singletonList(Room.Blue))));

        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(5, new ArrayList<>(Collections.singletonList(Room.Blue)), null, null, null), result.getMeetingItemViewStateList());
    }

    // Vérifie que si on donne 4 salles à filtrer, la LiveData expose la liste des réunions dans ces 4 salles
    @Test
    public void given_four_rooms_to_filter_then_livedata_should_expose_four_meetings() {
        // Given
        meetingsLiveData.setValue(getDefaultMeetingList(8));
        selectedRoomsLiveData.setValue(getDefaultRoomFilterHashMap(new ArrayList<>(Arrays.asList(Room.Black, Room.Blue, Room.Green, Room.Pink))));

        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(8, new ArrayList<>(Arrays.asList(Room.Black, Room.Blue, Room.Green, Room.Pink)), null, null, null), result.getMeetingItemViewStateList());
    }

    // Vérifie que si on filtre par date, la LiveData n'expose que la réunion de cette date
    @Test
    public void given_a_date_to_filter_then_livedata_should_expose_one_meeting() {
        // Given
        meetingsLiveData.setValue(getDefaultMeetingList(3));
        selectedDateLiveData.setValue(DEFAULT_DATE);

        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(3, null, DEFAULT_DATE, null, null), result.getMeetingItemViewStateList());
    }

    // Vérifie que si on filtre par heure de début, la LiveData expose toutes les réunions après cette heure
    @Test
    public void given_a_start_time_to_filter_then_livedata_should_expose_only_meetings_after_it() {
        // Given
        meetingsLiveData.setValue(getDefaultMeetingList(7));
        selectedStartTimeLiveData.setValue(LocalTime.of(12, 0));

        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(7, null, null, LocalTime.of(12, 0), null), result.getMeetingItemViewStateList());
    }

    // Vérifie que si on filtre par heure de fin, la LiveData expose toutes les réunions avant cette heure
    @Test
    public void given_an_end_time_to_filter_then_livedata_should_expose_only_meetings_before_it() {
        // Given
        meetingsLiveData.setValue(getDefaultMeetingList(7));
        selectedEndTimeLiveData.setValue(LocalTime.of(13, 0));

        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(7, null, null, null, LocalTime.of(13, 0)), result.getMeetingItemViewStateList());
    }

    // Vérifie que si on filtre par heure de début et heure de fin, la LiveData expose toutes les réunions entre les deux
    @Test
    public void given_start_and_end_times_to_filter_then_livedata_should_expose_only_meetings_in_between() {
        // Given
        meetingsLiveData.setValue(getDefaultMeetingList(8));
        selectedStartTimeLiveData.setValue(LocalTime.of(11, 0));
        selectedEndTimeLiveData.setValue(LocalTime.of(16, 0));

        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(8, null, null, LocalTime.of(11, 0), LocalTime.of(16, 0)), result.getMeetingItemViewStateList());
    }

    // Vérifie qu'avec de multiples filtres, la LiveData expose seulement les réunions correspondants à tous les filtres
    @Test
    public void given_multiple_filters_then_livedata_should_expose_only_meetings_matching_those_filters() {
        // Given
        meetingsLiveData.setValue(getDefaultMeetingList(10));
        selectedRoomsLiveData.setValue(getDefaultRoomFilterHashMap(new ArrayList<>(Collections.singletonList(Room.Green))));
        selectedDateLiveData.setValue(DEFAULT_DATE.plusDays(3));
        selectedStartTimeLiveData.setValue(LocalTime.of(11, 0));
        selectedEndTimeLiveData.setValue(LocalTime.of(16, 0));

        // When
        MeetingViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getMeetingListViewStateLiveData());

        // Then
        assertEquals(getExpectedMeetingItemViewStates(10, new ArrayList<>(Collections.singletonList(Room.Green)), DEFAULT_DATE.plusDays(3), LocalTime.of(11, 0), LocalTime.of(16, 0)), result.getMeetingItemViewStateList());
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

    // Retourne un HashMap initial de salles avec leur état de sélection
    @NonNull
    private Map<Room, Boolean> getDefaultRoomFilterHashMap(@Nullable List<Room> roomsToFilter) {
        Map<Room, Boolean> selectedRooms = new LinkedHashMap<>();
        for (Room room : Room.values()) {
            if (roomsToFilter != null && roomsToFilter.contains(room)) {
                selectedRooms.put(room, true);
            } else {
                selectedRooms.put(room, false);
            }
        }
        return selectedRooms;
    }
    // endregion

    // region OUT
    // Retourne la liste attendue de réunions
    @NonNull
    private List<MeetingItemViewState> getExpectedMeetingItemViewStates() {
        return getExpectedMeetingItemViewStates(MEETING_COUNT, null, null, null, null);
    }

    // Retourne une liste attendue de réunions de nombre count et filtrable par salle
    @NonNull
    private List<MeetingItemViewState> getExpectedMeetingItemViewStates(int count,
                                                                        @Nullable List<Room> roomsToFilter,
                                                                        @Nullable LocalDate dateToFilter,
                                                                        @Nullable LocalTime startToFilter,
                                                                        @Nullable LocalTime endToFilter) {
        List<MeetingItemViewState> meetingViewStateItems = new ArrayList<>();

        // Génère une liste de réunions de nombre count
        for (int i = 0; i < count; i++) {
            meetingViewStateItems.add(
                    new MeetingItemViewState(
                            i,
                            DEFAULT_SUBJECT + i,
                            formatDateToString(DEFAULT_DATE.plusDays(i)),
                            formatTimeToString(DEFAULT_START.plusHours(i)),
                            formatTimeToString(DEFAULT_END.plusHours(i)),
                            Room.values()[i],
                            getExpectedParticipants(i)
                    )
            );
        }

        // Filtre par salle(s)
        if (roomsToFilter != null) {
            meetingViewStateItems.removeIf(meetingItemViewState ->
                    !roomsToFilter.contains(meetingItemViewState.getRoom()));
        }

        // Filtre par date
        if (dateToFilter != null) {
            meetingViewStateItems.removeIf(meetingItemViewState ->
                    !formatStringToDate(meetingItemViewState.getDate()).isEqual(dateToFilter));
        }

        // Filtre par heure de début
        if (startToFilter != null) {
            meetingViewStateItems.removeIf(meetingItemViewState ->
                    formatStringToTime(meetingItemViewState.getStartTime()).isBefore(startToFilter));
        }

        // Filtre par heure de fin
        if (endToFilter != null) {
            meetingViewStateItems.removeIf(meetingItemViewState ->
                    formatStringToTime(meetingItemViewState.getEndTime()).isAfter(endToFilter));
        }

        return meetingViewStateItems;
    }

    // Retourne une liste attendue de participants
    @NonNull
    private String getExpectedParticipants(int meetingIndex) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < PARTICIPANTS_COUNT; i++) {
            result.append(String.format(DEFAULT_PARTICIPANT, meetingIndex, i));

            if (i + 1 < PARTICIPANTS_COUNT) {
                result.append(", ");
            }
        }

        return result.toString();
    }

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM", Locale.FRENCH);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH'h'mm", Locale.FRENCH);
    private final DateTimeFormatter StringToDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    private String formatDateToString(@Nullable LocalDate date) {
        String formattedDate = null;
        if (date != null)
            formattedDate = date.format(dateFormatter);
        return formattedDate;
    }

    private String formatTimeToString(@Nullable LocalTime time) {
        String formattedTime = null;
        if (time != null)
            formattedTime = time.format(timeFormatter);
        return formattedTime;
    }

    private LocalDate formatStringToDate(String date) {
        String year = String.valueOf(DEFAULT_DATE.getYear());
        date += "/" + year;
        return LocalDate.parse(date, StringToDateFormatter);
    }

    private LocalTime formatStringToTime(String time) {
        return LocalTime.parse(time, timeFormatter);
    }
    // endregion
}
