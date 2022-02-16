package com.bakjoul.mareu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.databinding.ActivityMainBinding;
import com.bakjoul.mareu.ui.create.CreateMeetingDialogFragment;
import com.bakjoul.mareu.ui.list.MeetingAdapter;
import com.bakjoul.mareu.ui.list.OnDeleteClickedListener;
import com.bakjoul.mareu.ui.room_filter.RoomFilterDialogFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MeetingActivity extends AppCompatActivity implements OnDeleteClickedListener {

    private ActivityMainBinding b;
    private MeetingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        viewModel = new ViewModelProvider(this).get(MeetingViewModel.class);

        b.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.room_filter) {
                showDialog("filter");
                return true;
            } else
                return false;
        });

        initFab();
        initRecyclerView();

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

    // Initialise le fab
    private void initFab() {
        b.fabAdd.setOnClickListener(view -> showDialog("dialog"));
    }

    // Affiche le dialog de création de réunion
    private void showDialog(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CreateMeetingDialogFragment createFragment = new CreateMeetingDialogFragment();
        RoomFilterDialogFragment roomFilterDialogFragment = new RoomFilterDialogFragment();

        if (tag.equals("dialog"))
            createFragment.show(fragmentManager, tag);
        else if (tag.equals("filter"))
            roomFilterDialogFragment.show(fragmentManager, tag);
    }

    // Supprime une réunion
    @Override
    public void onDeleteMeetingClick(int id) {
        viewModel.onDeleteClicked(id);
    }
}