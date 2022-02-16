package com.bakjoul.mareu.ui.room_filter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.databinding.RoomFilterDialogBinding;
import com.bakjoul.mareu.ui.MeetingViewModel;

public class RoomFilterDialogFragment extends DialogFragment implements OnItemClickedListener {

    private RoomFilterDialogBinding b;
    private MeetingViewModel meetingViewModel;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = RoomFilterDialogBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        meetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);

        RoomFilterAdapter adapter = new RoomFilterAdapter(this);
        RecyclerView recyclerView = view.findViewById(R.id.room_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        meetingViewModel.getMeetingListViewStateLiveData().observe(getViewLifecycleOwner(), meetingListViewState ->
                adapter.submitList(meetingListViewState.getRoomFilterItemViewStates()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    @Override
    public void onRoomSelected(@NonNull Room room) {
        meetingViewModel.onRoomSelected(room);
    }
}
