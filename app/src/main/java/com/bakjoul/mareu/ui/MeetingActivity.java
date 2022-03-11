package com.bakjoul.mareu.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private final String TAG_CREATE_DIALOG = "CREATE";
    private final String TAG_ROOM_DIALOG = "ROOM";
    private final String TAG_DATE_DIALOG = "DATE";

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
                    viewModel.onClearAllDateFiltersClicked();
                    return true;
                default:
                    return false;
            }
        });
    }

    // Observe le résultat des clics sur les boutons et ouvre le dialog adéquat
    private void initDialogs() {
        viewModel.getSingleLiveEvent().observe(this, viewEvent -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev;
            switch (viewEvent) {
                case DISPLAY_CREATE_MEETING_DIALOG:
                    prev = getSupportFragmentManager().findFragmentByTag(TAG_CREATE_DIALOG);
                    if (prev == null) {
                        ft.addToBackStack(null);
                        CreateMeetingDialogFragment.newInstance().show(ft, TAG_CREATE_DIALOG);
                    }
                    break;
                case DISPLAY_DATE_FILTER_DIALOG:
                    prev = getSupportFragmentManager().findFragmentByTag(TAG_DATE_DIALOG);
                    if (prev == null) {
                        ft.addToBackStack(null);
                        DateFilterDialogFragment.newInstance().show(getSupportFragmentManager(), TAG_DATE_DIALOG);
                    }
                    break;
                case DISPLAY_ROOM_FILTER_DIALOG:
                    prev = getSupportFragmentManager().findFragmentByTag(TAG_ROOM_DIALOG);
                    if (prev == null) {
                        ft.addToBackStack(null);
                        RoomFilterDialogFragment.newInstance().show(getSupportFragmentManager(), TAG_ROOM_DIALOG);
                    }
                    break;
            }
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