package com.bakjoul.mareu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.databinding.MeetingActivityBinding;
import com.bakjoul.mareu.ui.create.CreateMeetingDialogFragment;
import com.bakjoul.mareu.ui.date_filter.DateFilterDialogFragment;
import com.bakjoul.mareu.ui.list.MeetingAdapter;
import com.bakjoul.mareu.ui.list.OnDeleteClickedListener;
import com.bakjoul.mareu.ui.room_filter.RoomFilterDialogFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MeetingActivity extends AppCompatActivity implements OnDeleteClickedListener {

    private MeetingActivityBinding b;
    private MeetingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = MeetingActivityBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        viewModel = new ViewModelProvider(this).get(MeetingViewModel.class);

        initRecyclerView();
        initMenuItems();
        initDialogs();
        initFab();
    }

    // Initialise le RecyclerView
    private void initRecyclerView() {
        MeetingAdapter adapter = new MeetingAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.meeting_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel.getMeetingListViewStateLiveData().observe(this, meetingListViewState ->
                adapter.submitList(meetingListViewState.getMeetingItemViewStateList()));
    }

    // Initialise les actions des boutons du menu
    @SuppressLint("NonConstantResourceId")
    private void initMenuItems() {
        b.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_room_filter:
                    viewModel.onDisplayRoomFilterClicked();
                    return true;
                case R.id.menu_date_filter:
                    viewModel.onDisplayDateFilterClicked();
                    return true;
                case R.id.submenu_clear_all:
                    viewModel.onClearAllFiltersClicked();
                    return true;
                case R.id.submenu_clear_room:
                    viewModel.onClearRoomFilterClicked();
                    return true;
                case R.id.submenu_clear_date:
                    viewModel.onClearDateFilterClicked();
                    return true;
                default:
                    return false;
            }
        });
    }

    // Observe le résultat des clics sur les boutons et ouvre le dialog adéquat
    private void initDialogs() {
        viewModel.getSingleLiveEvent().observe(this, viewEvent -> {
            if (viewEvent == MeetingViewEvent.DISPLAY_CREATE_MEETING_DIALOG)
                CreateMeetingDialogFragment.newInstance().show(getSupportFragmentManager(), "create");
            else if (viewEvent == MeetingViewEvent.DISPLAY_DATE_FILTER_DIALOG)
                DateFilterDialogFragment.newInstance().show(getSupportFragmentManager(), "date");
            else if (viewEvent == MeetingViewEvent.DISPLAY_ROOM_FILTER_DIALOG)
                RoomFilterDialogFragment.newInstance().show(getSupportFragmentManager(), "room");
        });
    }

    // Initialise le fab
    private void initFab() {
        b.fabAdd.setOnClickListener(view -> viewModel.onDisplayCreateMeetingClicked());
    }

    // Supprime une réunion
    @Override
    public void onDeleteMeetingClick(int id) {
        viewModel.onDeleteClicked(id);
    }
}