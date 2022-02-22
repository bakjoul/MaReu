package com.bakjoul.mareu.ui.room_filter;

import android.app.AlertDialog;
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

    public static RoomFilterDialogFragment newInstance() {
        return new RoomFilterDialogFragment();
    }

    private RoomFilterDialogBinding b;
    private MeetingViewModel meetingViewModel;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
            dialog.getWindow().setBackgroundDrawableResource(R.color.white_rfd);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        b = RoomFilterDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Filtrer par salles").setView(b.getRoot());
        builder.setPositiveButton(R.string.dialog_ok_button, (dialogInterface, i) -> dismiss());

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        meetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);

        RoomFilterAdapter adapter = new RoomFilterAdapter(this);
        RecyclerView recyclerView = view.findViewById(R.id.filter_room_list);
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
