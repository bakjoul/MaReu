package com.bakjoul.mareu.ui.room_filter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.FilterParametersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RoomFilterViewModel extends ViewModel {

    @NonNull
    FilterParametersRepository filterParametersRepository;

    private final MutableLiveData<RoomFilterViewState> roomFilterLiveData = new MutableLiveData<>();

    @Inject
    public RoomFilterViewModel(@NonNull FilterParametersRepository filterParametersRepository) {
        this.filterParametersRepository = filterParametersRepository;
        roomFilterLiveData.setValue(
                new RoomFilterViewState(
                        getRoomFilterItemViewState(
                                Objects.requireNonNull(filterParametersRepository.getSelectedRoomsLiveData().getValue())
                        )
                )
        );
    }

    public LiveData<RoomFilterViewState> getRoomFilterLiveData() {
        return roomFilterLiveData;
    }

    // Retourne la liste des salles avec leur état de sélection dans le filtre
    @NonNull
    private List<RoomFilterItemViewState> getRoomFilterItemViewState(@NonNull Map<Room, Boolean> rooms) {
        List<RoomFilterItemViewState> roomFilterItemViewStates = new ArrayList<>();
        for (Map.Entry<Room, Boolean> entry : rooms.entrySet()) {
            Room room = entry.getKey();
            Boolean isSelected = entry.getValue();
            String backgroundColor = "#F8F8FF";
            if (isSelected)
                backgroundColor = "#D6EAF8";
            roomFilterItemViewStates.add(new RoomFilterItemViewState(room, isSelected, backgroundColor));
        }
        return roomFilterItemViewStates;
    }

    // Au clic sur une salle dans le dialog du filtre, passe son état à vrai/faux dans le hashmap des salles filtrées
    public void onRoomSelected(@NonNull Room room) {
        filterParametersRepository.onRoomSelected(room);
        roomFilterLiveData.setValue(
                new RoomFilterViewState(
                        getRoomFilterItemViewState(
                                Objects.requireNonNull(filterParametersRepository.getSelectedRoomsLiveData().getValue()
                                )
                        )
                )
        );
    }
}
