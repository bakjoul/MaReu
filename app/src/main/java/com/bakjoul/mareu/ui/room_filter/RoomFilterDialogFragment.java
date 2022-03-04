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

    public static RoomFilterDialogFragment newInstance() {
        return new RoomFilterDialogFragment();
    }

    private RoomFilterFragmentBinding b;
    private RoomFilterViewModel viewModel;

    @Override
    public void onStart() {
        super.onStart();
        setDialogWindowParameters();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        b = RoomFilterFragmentBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Filtrer par salles").setView(b.getRoot());
        builder.setPositiveButton(R.string.dialog_dismiss_button, (dialogInterface, i) -> dismiss());

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

        viewModel = new ViewModelProvider(this).get(RoomFilterViewModel.class);

        RoomFilterAdapter adapter = new RoomFilterAdapter(this);
        RecyclerView recyclerView = view.findViewById(R.id.filter_room_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getRoomFilterViewState().observe(getViewLifecycleOwner(), roomFilterViewState ->
                adapter.submitList(roomFilterViewState.getRoomFilterItemViewStates()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    @Override
    public void onRoomSelected(Room room) {
        viewModel.onRoomSelected(room);
    }

    private void setDialogWindowParameters() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.65);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.45);
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawableResource(R.color.white_f8f8ff);
            dialog.getWindow().setGravity(Gravity.START | Gravity.TOP);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_SlideDownScale);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            TypedValue tv = new TypedValue();
            // Récupère la hauteur de l'actionbar
            requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.actionBarSize, tv, true);

            View roomItemView = requireActivity().findViewById(R.id.menu_room_filter);
            int[] roomItemWindowLocation = new int[2];
            // Récupère la position de l'icône meeting room du menu
            roomItemView.getLocationInWindow(roomItemWindowLocation);

            int roomIconItemX = roomItemWindowLocation[0];  // Coordonnée x de l'icône
            int roomIconWidth = roomItemView.getWidth();    // Largeur de l'icône

            // Aligne la droite du dialog avec la fin de l'icône
            params.x = roomIconItemX - width + roomIconWidth;
            // Aligne le haut du dialog avec le bas de l'actionbar
            params.y = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());

            dialog.getWindow().setAttributes(params);
        }
    }
}
