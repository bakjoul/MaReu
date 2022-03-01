package com.bakjoul.mareu.ui;

import static org.mockito.BDDMockito.given;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bakjoul.mareu.data.BuildConfigResolver;
import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.FilterParametersRepository;
import com.bakjoul.mareu.data.repository.MeetingRepository;
import com.bakjoul.mareu.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MeetingViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MeetingRepository meetingRepository = Mockito.mock(MeetingRepository.class);

    //private BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);

    private FilterParametersRepository filterParametersRepository = Mockito.mock(FilterParametersRepository.class);

    private MutableLiveData<List<Meeting>> meetingsLiveData;
    private MutableLiveData<Map<Room, Boolean>> selectedRoomsLiveData;
    private MutableLiveData<LocalDate> selectedDateLiveData;
    private MutableLiveData<LocalTime> selectedStartTimeLiveData;
    private MutableLiveData<LocalTime> selectedEndTimeLiveData;

    private MeetingViewModel viewModel;

    @Before
    public void setUp() {
        //Mockito.doReturn(false).when(buildConfigResolver).isDebug();
        //meetingRepository = new MeetingRepository(buildConfigResolver);
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

        // Initialise les valeurs par défaut des LiveDatas
        List<Meeting> meetings = meetingRepository.DUMMY_MEETINGS;
        meetingsLiveData.setValue(meetings);
        meetingRepository.addDummyMeetings();

        Map<Room, Boolean> selectedRooms = new LinkedHashMap<>();
        for (Room r : Room.values())
            selectedRooms.put(r, false);
        selectedRoomsLiveData.setValue(selectedRooms);

        selectedDateLiveData.setValue(null);
        selectedStartTimeLiveData.setValue(null);
        selectedEndTimeLiveData.setValue(null);

        viewModel = new MeetingViewModel(meetingRepository, filterParametersRepository);
    }

    @Test
    public void given_repository_has_7_meetings_livedata_should_expose_7_item_viewstates() throws InterruptedException {
        MeetingViewState meetingViewState = LiveDataTestUtil.getOrAwaitValue(viewModel.getMeetingListViewStateLiveData());

        assertEquals(7, meetingViewState.getMeetingItemViewStateList().size());
    }
}
