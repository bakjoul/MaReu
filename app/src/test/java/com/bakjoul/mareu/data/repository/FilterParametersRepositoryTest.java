package com.bakjoul.mareu.data.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.FilterParametersRepository;
import com.bakjoul.mareu.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.Map;

public class FilterParametersRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private FilterParametersRepository filterParametersRepository;


    @Before
    public void setUp() {
        filterParametersRepository = new FilterParametersRepository();
    }

    // Vérifie qu'à la sélection d'une salle dans le filtre de salle, sa valeur dans la hashmap passe à son opposée
    @Test
    public void given_one_room_selected_should_set_its_hashmap_value_to_opposite() throws InterruptedException {
        // Given
        filterParametersRepository.onRoomSelected(Room.Black);

        // When
        Map<Room, Boolean> currentlySelectedRooms = LiveDataTestUtil.getOrAwaitValue(filterParametersRepository.getSelectedRoomsLiveData());

        // Then
        assertEquals(true, currentlySelectedRooms.get(Room.Black));
    }
}
