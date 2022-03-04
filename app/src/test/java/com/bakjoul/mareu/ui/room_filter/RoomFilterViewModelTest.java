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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoomFilterViewModelTest {

    private static final List<Room> ROOMS_TO_FILTER = new ArrayList<>(Arrays.asList(Room.Pink, Room.Blue, Room.Brown));

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FilterParametersRepository filterParametersRepository;

    private MutableLiveData<Map<Room, Boolean>> selectedRoomsLiveData;

    private RoomFilterViewModel viewModel;

    @Before
    public void setUp() {
        // Réinitialise les LiveDatas
        selectedRoomsLiveData = new MutableLiveData<>();

        // Mock la LiveData retournée par le repository
        given(filterParametersRepository.getSelectedRoomsLiveData()).willReturn(selectedRoomsLiveData);

        // Initialise la HashMap de l'état de filtrage des salles
        Map<Room, Boolean> selectedRooms = new LinkedHashMap<>();
        for (Room room : Room.values()) {
            selectedRooms.put(room, false);
        }
        selectedRoomsLiveData.setValue(selectedRooms);

        viewModel = new RoomFilterViewModel(filterParametersRepository);
    }

    // Vérifie que la LiveData expose la liste de RoomFilterItemViewStates dans son état initial
    @Test
    public void initial_case() {
        // When
        RoomFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getRoomFilterViewState());

        // Then
        assertEquals(getExpectedRoomFilterItemViewStates(null), result.getRoomFilterItemViewStates());
    }

    @Test
    public void nominal_case_three_rooms_to_filter() {
        // Given
        selectedRoomsLiveData.setValue(getRoomFilterHashMap(ROOMS_TO_FILTER));
        when(filterParametersRepository.getSelectedRoomsLiveData()).thenReturn(selectedRoomsLiveData);

        // When
        RoomFilterViewState result = LiveDataTestUtil.getValueForTesting(viewModel.getRoomFilterViewState());

        // Then
        assertEquals(getExpectedRoomFilterItemViewStates(new ArrayList<>(Arrays.asList(Room.Blue, Room.Green))), result.getRoomFilterItemViewStates());
    }

    // region IN
    // Retourne un HashMap initial de salles avec leur état de sélection
    @NonNull
    private Map<Room, Boolean> getRoomFilterHashMap(@Nullable List<Room> roomsToFilter) {
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
