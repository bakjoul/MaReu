package com.bakjoul.mareu.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.FilterParametersRepository;
import com.bakjoul.mareu.data.repository.MeetingRepository;
import com.bakjoul.mareu.ui.list.MeetingItemViewState;
import com.bakjoul.mareu.ui.room_filter.RoomFilterItemViewState;
import com.bakjoul.mareu.utils.SingleLiveEvent;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MeetingViewModel extends ViewModel {
    @NonNull
    private final MeetingRepository meetingRepository;

    @NonNull
    private final FilterParametersRepository filterParametersRepository;

    private final MediatorLiveData<MeetingListViewState> meetingListViewStateMediatorLiveData = new MediatorLiveData<>();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.FRENCH);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH'h'mm", Locale.FRENCH);

    public SingleLiveEvent<MeetingViewEvent> singleLiveEvent = new SingleLiveEvent<>();

    @Inject
    public MeetingViewModel(
            @NonNull final MeetingRepository meetingRepository,
            @NonNull final FilterParametersRepository filterParametersRepository
    ) {
        this.meetingRepository = meetingRepository;
        this.filterParametersRepository = filterParametersRepository;
        init(filterParametersRepository);
    }

    private void init(@NonNull FilterParametersRepository filterParametersRepository) {
        // Récupère la LiveData de la liste des réunions
        final LiveData<List<Meeting>> meetingsLiveData = meetingRepository.getMeetingsLiveData();
        LiveData<Map<Room, Boolean>> selectedRoomsLiveData = filterParametersRepository.getSelectedRoomsLiveData();
        LiveData<LocalDate> selectedDateLiveData = filterParametersRepository.getSelectedDateLiveData();
        LiveData<LocalTime> selectedStartLiveData = filterParametersRepository.getSelectedStartTimeLiveData();
        LiveData<LocalTime> selectedEndLiveData = filterParametersRepository.getSelectedEndTimeLiveData();

        // Ajoute comme source la LiveData de la liste des réunions
        meetingListViewStateMediatorLiveData.addSource(meetingsLiveData, meetings ->
                meetingListViewStateMediatorLiveData.setValue(
                        getMeetings(
                                meetings,
                                selectedRoomsLiveData.getValue(),
                                selectedDateLiveData.getValue(),
                                selectedStartLiveData.getValue(),
                                selectedEndLiveData.getValue()
                        )
                )
        );

        // Ajoute comme source la LiveData de la liste des salles à filtrer
        meetingListViewStateMediatorLiveData.addSource(selectedRoomsLiveData, selectedRooms ->
                meetingListViewStateMediatorLiveData.setValue(
                        getMeetings(
                                meetingsLiveData.getValue(),
                                selectedRooms,
                                selectedDateLiveData.getValue(),
                                selectedStartLiveData.getValue(),
                                selectedEndLiveData.getValue()
                        )
                )
        );

        // Ajoute comme source la LiveData de la date des réunions à filtrer
        meetingListViewStateMediatorLiveData.addSource(selectedDateLiveData, selectedDate ->
                meetingListViewStateMediatorLiveData.setValue(
                        getMeetings(
                                meetingsLiveData.getValue(),
                                selectedRoomsLiveData.getValue(),
                                selectedDate,
                                selectedStartLiveData.getValue(),
                                selectedEndLiveData.getValue()
                        )
                ));

        // Ajoute comme source la LiveData de l'heure de début des réunions à filtrer
        meetingListViewStateMediatorLiveData.addSource(selectedStartLiveData, selectedStart ->
                meetingListViewStateMediatorLiveData.setValue(
                        getMeetings(
                                meetingsLiveData.getValue(),
                                selectedRoomsLiveData.getValue(),
                                selectedDateLiveData.getValue(),
                                selectedStart,
                                selectedEndLiveData.getValue()
                        )
                ));

        // Ajoute comme source la LiveData de l'heure de fin des réunions à filtrer
        meetingListViewStateMediatorLiveData.addSource(selectedEndLiveData, selectedEnd ->
                meetingListViewStateMediatorLiveData.setValue(
                        getMeetings(
                                meetingsLiveData.getValue(),
                                selectedRoomsLiveData.getValue(),
                                selectedDateLiveData.getValue(),
                                selectedStartLiveData.getValue(),
                                selectedEnd
                        )
                ));
    }

    @NonNull
    public LiveData<MeetingListViewState> getMeetingListViewStateLiveData() {
        return meetingListViewStateMediatorLiveData;
    }

    public SingleLiveEvent<MeetingViewEvent> getSingleLiveEvent() {
        return singleLiveEvent;
    }

    @NonNull
    private MeetingListViewState getMeetings(@Nullable final List<Meeting> meetings,
                                             @Nullable final Map<Room, Boolean> selectedRooms,
                                             @Nullable final LocalDate selectedDate,
                                             @Nullable final LocalTime selectedStart,
                                             @Nullable final LocalTime selectedEnd) {

        // Récupère la liste de réunions filtrées
        assert selectedRooms != null;   // À MODIFIER
        List<Meeting> filtered = getFilteredMeetings(meetings, selectedRooms, selectedDate, selectedStart, selectedEnd);

        // Transforme la liste filtrée en liste de MeetingItemViewState
        List<MeetingItemViewState> meetingItemViewStates = new ArrayList<>();
        for (Meeting meeting : filtered) {
            meetingItemViewStates.add(
                    new MeetingItemViewState(
                            meeting.getId(),
                            meeting.getSubject(),
                            formatDate(meeting.getDate()),
                            formatTime(meeting.getStart()),
                            meeting.getRoom(),
                            formatParticipants(meeting.getParticipants())
                    )
            );
        }

        // Récupère la liste des états de filtrage des salles
        List<RoomFilterItemViewState> roomFilterItemViewStateList = getRoomFilterItemViewState(selectedRooms);

        // Retourne le MeetingListViewState qui contient les différentes listes de ViewStates à utiliser pour l'affichage
        return new MeetingListViewState(meetingItemViewStates, roomFilterItemViewStateList);
    }

    // Retourne la liste de réunions filtrées par salles et par date
    private List<Meeting> getFilteredMeetings(@Nullable List<Meeting> meetings,
                                              @NonNull Map<Room, Boolean> selectedRooms,
                                              @Nullable LocalDate selectedDate,
                                              @Nullable LocalTime selectedStart,
                                              @Nullable LocalTime selectedEnd) {
        List<Meeting> filteredMeetings = new ArrayList<>();

        if (meetings == null)
            return filteredMeetings;
        // Parcourt la liste de réunions
        for (Meeting m : meetings) {
            boolean roomSelected = false;   // Au moins une salle selectionnée
            boolean roomMatches = false;    // La salle correspond
            boolean dateMatches = false;    // La date correspond
            boolean startMatches = false;   // L'heure de début correspond
            boolean endMatches = false;     // L'heure de fin correspond

            // Parcourt la HashMap salle-état de filtrage
            for (Map.Entry<Room, Boolean> e : selectedRooms.entrySet()) {
                Room r = e.getKey();
                boolean isSelected = e.getValue();

                // Si la salle de l'itération en cours est sélectionnée
                if (isSelected)
                    roomSelected = true;    // Indique qu'une salle est sélectionnée

                // Si la salle itérée dans la HashMap correspond à celle de la réunion parcourue
                if (r == m.getRoom())
                    roomMatches = isSelected;   // Indique que la salle correspond ou non
            }

            // Si aucune salle n'est sélectionnée dans le filtre
            if (!roomSelected)
                roomMatches = true; // Passe la correspondance des salles à vrai pour ajouter toutes les réunions à l'affichage

            // Si la date du filtre correspond
            if (selectedDate != null && m.getDate().isEqual(selectedDate))
                dateMatches = true;

            // Si la date de début du filtre est égale au début de la réunion ou que cette dernière démarre après
            if (selectedStart != null && (m.getStart().equals(selectedStart) || m.getStart().isAfter(selectedStart)))
                startMatches = true;

            // Si la date de fin du filtre est égale à la fin de la réunion ou que cette dernière finit avant
            if (selectedEnd != null && (m.getEnd().equals(selectedEnd) || m.getEnd().isBefore(selectedEnd)))
                endMatches = true;

            // Algorithme qui ajoute ou non la réunion parcourue à la liste filtrée en fonction des filtres définis
            if (roomMatches && dateMatches && startMatches && endMatches)
                filteredMeetings.add(m);
            else if (roomMatches && dateMatches && selectedStart == null && selectedEnd == null)
                filteredMeetings.add(m);
            else if (roomMatches && dateMatches && startMatches && selectedEnd == null)
                filteredMeetings.add(m);
            else if (roomMatches && dateMatches && selectedStart == null && endMatches)
                filteredMeetings.add(m);
            else if (roomMatches && selectedDate == null && startMatches && endMatches)
                filteredMeetings.add(m);
            else if (roomMatches && selectedDate == null && startMatches && selectedEnd == null)
                filteredMeetings.add(m);
            else if (roomMatches && selectedDate == null && selectedStart == null && endMatches)
                filteredMeetings.add(m);
            else if (roomMatches && selectedDate == null && selectedStart == null && selectedEnd == null)
                filteredMeetings.add(m);
        }

        return filteredMeetings;
    }

    // Retourne la liste de ViewState des salles leur état de sélection dans le filtre
    private List<RoomFilterItemViewState> getRoomFilterItemViewState(@NonNull Map<Room, Boolean> rooms) {
        List<RoomFilterItemViewState> roomFilterItemViewStates = new ArrayList<>();
        for (Map.Entry<Room, Boolean> entry : rooms.entrySet()) {
            Room room = entry.getKey();
            Boolean isSelected = entry.getValue();
            roomFilterItemViewStates.add(new RoomFilterItemViewState(room, isSelected, "#000000"));
        }
        return roomFilterItemViewStates;
    }

    private String formatDate(@NonNull LocalDate date) {
        return date.format(dateFormatter);
    }

    private String formatTime(@NonNull LocalTime time) {
        return time.format(timeFormatter);
    }

    private String formatParticipants(@NonNull List<String> participants) {
        return StringUtils.join(participants, ", ");
    }

    // Au clic sur une salle dans le dialog du filtre, passe son état à vrai/faux dans le hashmap des salles filtrées
    public void onRoomSelected(@NonNull Room room) {
        filterParametersRepository.onRoomSelected(room);
    }

    // Supprime une réunion
    public void onDeleteClicked(int id) {
        meetingRepository.deleteMeeting(id);
    }

    public void onDisplayCreateMeetingClicked() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_CREATE_MEETING);
    }

    public void onDisplayDateFilterClicked() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_DATE_FILTER);
    }

    public void onDisplayRoomFilterClicked() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_ROOM_FILTER);
    }

    public void onClearAllFiltersClicked() {
        filterParametersRepository.clearAllFilters();
    }

    public void onClearRoomFilterClicked() {
        filterParametersRepository.clearRoomFilter();
    }

    public void onClearDateFilterClicked() {
        filterParametersRepository.clearDateFilter();
    }
}
