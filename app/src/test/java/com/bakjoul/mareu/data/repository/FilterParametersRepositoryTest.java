package com.bakjoul.mareu.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class FilterParametersRepositoryTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.now();
    private static final LocalDate EXPECTED_DATE = DEFAULT_DATE.plusMonths(1);
    // Les mois commencent à 0 avec MaterialDateTimePicker donc on ajoute aussi 1 mois à la date attendue

    private static final LocalTime DEFAULT_START = LocalTime.of(11, 0);
    private static final LocalTime DEFAULT_END = LocalTime.of(12, 0);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private FilterParametersRepository filterParametersRepository;


    @Before
    public void setUp() {
        filterParametersRepository = new FilterParametersRepository();
    }

    // Vérifie qu'après sélection de la date à filtrer, la livedata expose la date à filtrer
    @Test
    public void given_a_date_to_filter_then_livedata_should_expose_given_date() {
        // Given
        filterParametersRepository.onFilterDateSelected(DEFAULT_DATE);

        // When
        LocalDate result = LiveDataTestUtil.getValueForTesting(filterParametersRepository.getSelectedDateLiveData());

        // Then
        assertEquals(EXPECTED_DATE, result);
    }

    // Vérifie qu'après sélection de l'heure de début à filtrer, la livedata expose l'heure de début à filtrer
    @Test
    public void given_a_start_time_to_filter_then_livedata_should_expose_given_time() {
        // Given
        filterParametersRepository.onFilterStartTimeSelected(DEFAULT_START);

        // When
        LocalTime result = LiveDataTestUtil.getValueForTesting(filterParametersRepository.getSelectedStartTimeLiveData());

        // Then
        assertEquals(DEFAULT_START, result);
    }

    // Vérifie qu'après sélection de l'heure de fin à filtrer, la livedata expose l'heure de fin à filtrer
    @Test
    public void given_an_end_time_to_filter_then_livedata_should_expose_given_time() {
        // Given
        filterParametersRepository.onFilterEndTimeSelected(DEFAULT_END);

        // When
        LocalTime result = LiveDataTestUtil.getValueForTesting(filterParametersRepository.getSelectedEndTimeLiveData());

        // Then
        assertEquals(DEFAULT_END, result);
    }

    // Vérifie qu'à la sélection d'une salle dans le filtre de salle, sa valeur dans la hashmap de la livedata passe à son opposée
    @Test
    public void given_one_room_selected_then_livedata_hashmap_value_should_be_opposite() {
        // Given
        filterParametersRepository.onRoomSelected(Room.Black);

        // When
        Map<Room, Boolean> result = LiveDataTestUtil.getValueForTesting(filterParametersRepository.getSelectedRoomsLiveData());

        // Then
        assertEquals(true, result.get(Room.Black));
    }

    // Vérifie qu'à l'appel de clearRoomFilter(), la livedata expose sa valeur initiale
    @Test
    public void given_clear_room_filter_is_called_should_set_livedata_to_initial_value() {
        // Given
        filterParametersRepository.onRoomSelected(Room.Black);
        filterParametersRepository.onRoomSelected(Room.Blue);
        filterParametersRepository.onRoomSelected(Room.Brown);

        // When
        filterParametersRepository.clearRoomFilter();
        Map<Room, Boolean> result = LiveDataTestUtil.getValueForTesting(filterParametersRepository.getSelectedRoomsLiveData());

        // Then
        assertEquals(expectedRoomFilterHashMap(), result);
    }

    // Vérifie qu'à l'appel de clearDateFilter(), la livedata est null
    @Test
    public void given_clear_date_filter_is_called_should_set_livedata_to_null() {
        // Given
        filterParametersRepository.onFilterDateSelected(DEFAULT_DATE);

        // When
        filterParametersRepository.clearDateFilter();
        LocalDate result;
        try {
            result = LiveDataTestUtil.getValueForTesting(filterParametersRepository.getSelectedDateLiveData());

            // Then
        } catch (AssertionError expectedError) {
            // Vérifie que la livedata est null
            assertEquals("LiveData value is null !", expectedError.getMessage());
            result = null;
        }
        assertNull(result);
    }

    // Vérifie qu'à l'appel de clearStartTimeFilter(), la livedata est null
    @Test
    public void given_clear_start_time_filter_is_called_should_set_livedata_to_null() {
        // Given
        filterParametersRepository.onFilterStartTimeSelected(DEFAULT_START);

        // When
        filterParametersRepository.clearStartTimeFilter();
        LocalTime result;
        try {
            result = LiveDataTestUtil.getValueForTesting(filterParametersRepository.getSelectedStartTimeLiveData());

            // Then
        } catch (AssertionError expectedError) {
            // Vérifie que la livedata est null
            assertEquals("LiveData value is null !", expectedError.getMessage());
            result = null;
        }
        assertNull(result);
    }

    // Vérifie qu'à l'appel de clearEndTimeFilter(), la livedata est null
    @Test
    public void given_clear_end_time_filter_is_called_should_set_livedata_to_null() {
        // Given
        filterParametersRepository.onFilterEndTimeSelected(DEFAULT_END);

        // When
        filterParametersRepository.clearEndTimeFilter();
        LocalTime result;
        try {
            result = LiveDataTestUtil.getValueForTesting(filterParametersRepository.getSelectedEndTimeLiveData());

            // Then
        } catch (AssertionError expectedError) {
            // Vérifie que la livedata est null
            assertEquals("LiveData value is null !", expectedError.getMessage());
            result = null;
        }
        assertNull(result);
    }

    // region OUT
    // Retourne l'état initial attendu de la HashMap des salles avec leur état de sélection
    @NonNull
    private Map<Room, Boolean> expectedRoomFilterHashMap() {
        Map<Room, Boolean> rooms = new LinkedHashMap<>();
        for (Room r : Room.values())
            rooms.put(r, false);
        return rooms;
    }
    // endregion
}
