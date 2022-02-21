package com.bakjoul.mareu.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.data.model.Room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FilterParametersRepository {

    // LiveData de la hashmap de l'état de sélection des salles dans le filtre
    private final MutableLiveData<Map<Room, Boolean>> selectedRoomsLiveData = new MutableLiveData<>(initSelectedRooms());

    // LiveData de la date selectionnée dans le filtre
    private final MutableLiveData<LocalDate> selectedDateLiveData = new MutableLiveData<>();

    // LiveData de l'heure de début selectionnée dans le filtre
    private final MutableLiveData<LocalTime> selectedStartTimeLiveData = new MutableLiveData<>();

    // LiveData de l'heure de fin selectionnée dans le filtre
    private final MutableLiveData<LocalTime> selectedEndTimeLiveData = new MutableLiveData<>();

    @Inject
    public FilterParametersRepository() {
        selectedDateLiveData.setValue(null);
    }

    public MutableLiveData<Map<Room, Boolean>> getSelectedRoomsLiveData() {
        return selectedRoomsLiveData;
    }

    public LiveData<LocalDate> getSelectedDateLiveData() {
        return selectedDateLiveData;
    }

    public MutableLiveData<LocalTime> getSelectedStartTimeLiveData() {
        return selectedStartTimeLiveData;
    }

    public MutableLiveData<LocalTime> getSelectedEndTimeLiveData() {
        return selectedEndTimeLiveData;
    }

    public void onFilterDateSelected(@NonNull LocalDate date) {
        selectedDateLiveData.setValue(date.plusMonths(1));
    }

    public void onFilterStartTimeSelected(@NonNull LocalTime start) {
        selectedStartTimeLiveData.setValue(start);
    }

    public void onFilterEndTimeSelected(@NonNull LocalTime end) {
        selectedEndTimeLiveData.setValue(end);
    }

    // Retourne l'état initial de la HashMap des salles avec leur état de sélection
    private Map<Room, Boolean> initSelectedRooms() {
        Map<Room, Boolean> rooms = new LinkedHashMap<>();
        for (Room r : Room.values())
            rooms.put(r, false);
        return rooms;
    }

    // Au clic sur une salle dans le filtre, change l'état de sélection de la salle
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

    public void clearRoomFilter() {
        selectedRoomsLiveData.setValue(initSelectedRooms());
    }

    public void clearDateFilter() {
        selectedDateLiveData.setValue(null);
    }

    public void clearStartTimeFilter() {
        selectedStartTimeLiveData.setValue(null);
    }

    public void clearEndTimeFilter() {
        selectedEndTimeLiveData.setValue(null);
    }

    public void clearAllDateFilters() {
        clearDateFilter();
        clearStartTimeFilter();
        clearStartTimeFilter();
    }
}
