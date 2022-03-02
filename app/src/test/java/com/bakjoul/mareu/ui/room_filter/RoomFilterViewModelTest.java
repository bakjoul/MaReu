package com.bakjoul.mareu.ui.room_filter;

import static org.mockito.BDDMockito.given;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.FilterParametersRepository;
import com.bakjoul.mareu.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RoomFilterViewModelTest {

    private static final List<Room> ROOMS_TO_FILTER = new ArrayList<>(Arrays.asList(Room.Pink, Room.Blue, Room.Brown));

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FilterParametersRepository filterParametersRepository;

    private MutableLiveData<Map<Room, Boolean>> selectedRoomsLiveData;
    private MutableLiveData<RoomFilterViewState> roomFilterLiveData;

    private RoomFilterViewModel viewModel;

    @Before
    public void setUp() {
        // Réinitialise les LiveDatas
        selectedRoomsLiveData = new MutableLiveData<>();
        roomFilterLiveData = new MutableLiveData<>();

        // Mock la LiveData retournée par le repository
        given(filterParametersRepository.getSelectedRoomsLiveData()).willReturn(selectedRoomsLiveData);

        // Initialise la HashMap de l'état de filtrage des salles
        Map<Room, Boolean> selectedRooms = new LinkedHashMap<>();
        for (Room room : Room.values()) {
            selectedRooms.put(room, false);
        }
        selectedRoomsLiveData.setValue(selectedRooms);

        // Initialise la LiveData du RoomFilterViewState
        roomFilterLiveData.setValue(
                new RoomFilterViewState(
                        getRoomFilterItemViewState(
                                (Objects.requireNonNull(selectedRoomsLiveData.getValue()))
                        )
                )
        );

        viewModel = new RoomFilterViewModel(filterParametersRepository);
    }

    // Vérifie que la LiveData expose la liste de RoomFilterItemViewStates dans son état initial
    @Test
    public void initial_case() {
        // When
        RoomFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getRoomFilterLiveData());

        // Then
        assertEquals(getExpectedRoomFilterItemViewStates(null), result.getRoomFilterItemViewStates());
    }

    @Test
    public void nominal_case_three_rooms_to_filter() {
        // Given
        viewModel.onRoomSelected(Room.Blue);
        viewModel.onRoomSelected(Room.Brown);
        viewModel.onRoomSelected(Room.Pink);

        // When
        RoomFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getRoomFilterLiveData());

        // Then
        assertEquals(getExpectedRoomFilterItemViewStates(ROOMS_TO_FILTER), result.getRoomFilterItemViewStates());
    }

    // region IN
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

    // endregion IN

    // region OUT

    @NonNull
    private List<RoomFilterItemViewState> getExpectedRoomFilterItemViewStates(@Nullable List<Room> roomToFilter) {
        List<RoomFilterItemViewState> expected = new ArrayList<>();
        for (int i = 0; i < Room.values().length; i++) {
            if (roomToFilter != null && roomToFilter.contains(Room.values()[i])) {
                expected.add(
                        new RoomFilterItemViewState(
                                Room.values()[i],
                                true,
                                "#D6EAF8"
                        )
                );
            } else {
                expected.add(
                        new RoomFilterItemViewState(
                                Room.values()[i],
                                false,
                                "#F8F8FF"
                        )
                );
            }
        }
        return expected;
    }
    // endregion OUT
}
