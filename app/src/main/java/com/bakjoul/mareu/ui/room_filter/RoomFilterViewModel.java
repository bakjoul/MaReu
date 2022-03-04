package com.bakjoul.mareu.ui.room_filter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.FilterParametersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RoomFilterViewModel extends ViewModel {

    @NonNull
    FilterParametersRepository filterParametersRepository;

    @Inject
    public RoomFilterViewModel(@NonNull FilterParametersRepository filterParametersRepository) {
        this.filterParametersRepository = filterParametersRepository;
    }

    public LiveData<RoomFilterViewState> getRoomFilterViewState() {
        return Transformations.map(
                filterParametersRepository.getSelectedRoomsLiveData(),
                input -> {
                    List<RoomFilterItemViewState> roomFilterItemViewStates = new ArrayList<>();
                    for (Map.Entry<Room, Boolean> entry : input.entrySet()) {
                        Room room = entry.getKey();
                        Boolean isSelected = entry.getValue();
                        String backgroundColor = "#F8F8FF";
                        if (isSelected)
                            backgroundColor = "#D6EAF8";
                        roomFilterItemViewStates.add(new RoomFilterItemViewState(room, isSelected, backgroundColor));
                    }
                    return new RoomFilterViewState(roomFilterItemViewStates);
                });
    }

    // Au clic sur une salle dans le dialog du filtre, passe son état à vrai/faux dans le hashmap des salles filtrées
    public void onRoomSelected(@NonNull Room room) {
        filterParametersRepository.onRoomSelected(room);

    }
}
