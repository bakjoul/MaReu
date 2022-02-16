package com.bakjoul.mareu.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.MeetingRepository;
import com.bakjoul.mareu.ui.list.MeetingItemViewState;
import com.bakjoul.mareu.ui.room_filter.RoomFilterItemViewState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MeetingViewModel extends ViewModel {
    @NonNull
    private final MeetingRepository meetingRepository;

    private final MediatorLiveData<MeetingListViewState> meetingListViewStateMediatorLiveData = new MediatorLiveData<>();

    // LiveData de la HashMap des salles pour le filtre
    private final MutableLiveData<Map<Room, Boolean>> selectedRoomsLiveData = new MutableLiveData<>(initRooms());

    @Inject
    public MeetingViewModel(@NonNull MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
        init();
    }

    private void init() {
        // Récupère la LiveData de la liste des réunions
        final LiveData<List<Meeting>> meetingsLiveData = meetingRepository.getMeetingsLiveData();

        // Ajoute comme source la LiveData de la liste des réunions
        meetingListViewStateMediatorLiveData.addSource(meetingsLiveData, meetings ->
                meetingListViewStateMediatorLiveData.setValue(
                        getMeetings(meetings, selectedRoomsLiveData.getValue())));

        // Ajoute comme source la LiveData de la liste des salles à filtrer
        meetingListViewStateMediatorLiveData.addSource(selectedRoomsLiveData, selectedRooms ->
                meetingListViewStateMediatorLiveData.setValue(
                        getMeetings(meetingsLiveData.getValue(), selectedRooms)));
    }

    @NonNull
    public LiveData<MeetingListViewState> getMeetingListViewStateLiveData() {
        return meetingListViewStateMediatorLiveData;
    }

    @NonNull
    private MeetingListViewState getMeetings(@Nullable final List<Meeting> meetings,
                                             @Nullable final Map<Room, Boolean> selectedRooms) {

        // Récupère la liste de réunions filtrées
        assert selectedRooms != null;   // À MODIFIER
        List<Meeting> filtered = getFilteredMeetings(meetings, selectedRooms);

        // Transforme la liste filtrée en liste de MeetingItemViewState
        List<MeetingItemViewState> meetingItemViewStates = new ArrayList<>();
        for (Meeting meeting : filtered) {
            meetingItemViewStates.add(
                    new MeetingItemViewState(
                            meeting.getId(),
                            meeting.getSubject(),
                            meeting.getStart(),
                            meeting.getRoom(),
                            meeting.getParticipants()
                    )
            );
        }

        // Récupère la liste des états de filtrage des salles
        List<RoomFilterItemViewState> roomFilterItemViewStateList = getRoomFilterItemViewState(selectedRooms);

        // Retourne le MeetingListViewState qui contient les différentes listes de ViewStates à utiliser pour l'affichage
        return new MeetingListViewState(meetingItemViewStates, roomFilterItemViewStateList);
    }

    // Retourne la liste de réunions filtrées par salles et par date
    private List<Meeting> getFilteredMeetings(@Nullable List<Meeting> meetings, @NonNull Map<Room, Boolean> selectedRooms) {
        List<Meeting> filteredMeetings = new ArrayList<>();

        if (meetings == null)
            return filteredMeetings;
        // Parcourt la liste de réunions
        for (Meeting m : meetings) {
            boolean roomSelected = false;   // Au moins une salle selectionnée
            boolean roomMatches = false;    // La salle correspond

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
                roomMatches = true; // Passe le booléen à vrai pour ajouter toutes les réunions à l'affichage

            // Si la salle correspond, l'ajoute à la liste des réunions filtrées
            if (roomMatches)
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

    // Supprime une réunion
    public void onDeleteClicked(int id) {
        meetingRepository.deleteMeeting(id);
    }

    // Retourne l'état initial de la HashMap des salles avec leur état de sélection
    private Map<Room, Boolean> initRooms() {
        Map<Room, Boolean> rooms = new LinkedHashMap<>();
        for (Room r : Room.values())
            rooms.put(r, false);
        return rooms;
    }

    // Au clic, change l'état de sélection de la salle dans le filtre
    public void onRoomSelected(@NonNull Room room) {
        Map<Room, Boolean> selectedRooms = selectedRoomsLiveData.getValue();

        if (selectedRooms != null) {
            for (Map.Entry<Room, Boolean> e : selectedRooms.entrySet()) {
                if (e.getKey() == room) {
                    e.setValue(!e.getValue());
                    break;
                }
            }
        }
        selectedRoomsLiveData.setValue(selectedRooms);

    }
}
