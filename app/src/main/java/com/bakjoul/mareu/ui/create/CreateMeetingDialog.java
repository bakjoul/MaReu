package com.bakjoul.mareu.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.databinding.CreateMeetingDialogBinding;
import com.google.android.material.appbar.MaterialToolbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateMeetingDialog extends DialogFragment {

    private CreateMeetingDialogBinding b;
    MaterialToolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_meeting_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.dialog_toolbar);

        toolbar.setNavigationOnClickListener(item -> dismiss());

        CreateMeetingViewModel viewModel = new ViewModelProvider(this).get(CreateMeetingViewModel.class);

        viewModel.getViewStateLiveData().observe(getViewLifecycleOwner(), createMeetingViewState -> {
            CreateMeetingAdapter adapter = new CreateMeetingAdapter(requireContext(), R.layout.test, createMeetingViewState.getRooms());
            b.autoCompleteTextView.setAdapter(adapter);
        });
    }

}
