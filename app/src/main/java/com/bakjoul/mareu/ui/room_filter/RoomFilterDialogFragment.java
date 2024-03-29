package com.bakjoul.mareu.ui.room_filter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.databinding.RoomFilterFragmentBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RoomFilterDialogFragment extends DialogFragment implements OnItemClickedListener {

    @NonNull
    public static RoomFilterDialogFragment newInstance() {
        return new RoomFilterDialogFragment();
    }

    private RoomFilterFragmentBinding b;
    private RoomFilterViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        setDialogWindowParameters();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = RoomFilterFragmentBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(b.getRoot()).create();
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RoomFilterViewModel.class);

        RoomFilterAdapter adapter = new RoomFilterAdapter(this);
        RecyclerView recyclerView = view.findViewById(R.id.room_filter_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getRoomFilterViewState().observe(getViewLifecycleOwner(), roomFilterViewState ->
                adapter.submitList(roomFilterViewState.getRoomFilterItemViewStates()));

        setDialogButtonsActions();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    private void setDialogButtonsActions() {
        b.roomFilterButtonReinit.setOnClickListener(view -> viewModel.onClearRoomFilter());
        b.roomFilterButtonClose.setOnClickListener(view -> dismiss());
    }

    private void setDialogWindowParameters() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawableResource(R.color.white_f8f8ff);
            dialog.getWindow().setGravity(Gravity.END | Gravity.TOP);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_SlideDownScale);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            TypedValue tv = new TypedValue();
            // Récupère la hauteur de l'actionbar
            requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.actionBarSize, tv, true);

            // Aligne le haut du dialog avec le bas de l'actionbar
            params.y = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());

            dialog.getWindow().setAttributes(params);
        }
    }

    @Override
    public void onRoomSelected(Room room) {
        viewModel.onRoomSelected(room);
    }
}
