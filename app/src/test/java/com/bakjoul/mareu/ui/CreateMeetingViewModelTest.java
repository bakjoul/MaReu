package com.bakjoul.mareu.ui;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bakjoul.mareu.data.repository.MeetingRepository;
import com.bakjoul.mareu.ui.create.CreateMeetingViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

public class CreateMeetingViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private MeetingRepository meetingRepository;

    private CreateMeetingViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new CreateMeetingViewModel(meetingRepository);
    }

    @Test
    public void test() {

    }

}
