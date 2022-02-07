package com.bakjoul.mareu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.databinding.ActivityMainBinding;
import com.bakjoul.mareu.ui.create.CreateMeetingDialog;
import com.bakjoul.mareu.ui.list.MeetingAdapter;
import com.bakjoul.mareu.ui.list.OnDeleteClickedListener;

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
        b.fabAdd.setOnClickListener(view -> showDialog());
    }

    // Affiche le dialog de création de réunion
    private void showDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CreateMeetingDialog fragment = new CreateMeetingDialog();

        fragment.show(fragmentManager, "dialog");

    }

    // Supprime une réunion
    @Override
    public void onDeletedMeetingClicked(int id) {
        viewModel.onDeleteClicked(id);
    }
}