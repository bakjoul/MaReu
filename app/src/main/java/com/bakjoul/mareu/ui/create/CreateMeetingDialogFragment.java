package com.bakjoul.mareu.ui.create;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.databinding.CreateMeetingDialogBinding;
import com.bakjoul.mareu.ui.utils.DatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalTime;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateMeetingDialogFragment extends DialogFragment {

    private CreateMeetingDialogBinding b;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullscreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = CreateMeetingDialogBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CreateMeetingViewModel viewModel = new ViewModelProvider(this).get(CreateMeetingViewModel.class);

        // Définit l'action du bouton "X" de la toolbar
        b.dialogToolbar.setNavigationOnClickListener(item -> dismiss());

        // Définit l'action du bouton "Créer" de la toolbar
        b.dialogToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.create_new) {
                dismiss();  // A MODIFIER
                return true;
            } else
                return false;
        });

        // Observe le champ sujet
        observeSubject(viewModel, b.inputSubjectEdit);
        // Observe le champ participants
        observeParticipants(viewModel, b.inputParticipantsEdit);

        viewModel.getViewStateLiveData().observe(getViewLifecycleOwner(), viewState -> {
            // Initialise et observe le room spinner
            initRoomSpinner(viewModel, viewState);




            // Observe et met à jour l'affichage de la date
            b.inputDateEdit.setText(viewState.getDate());
            b.inputStartEdit.setText(viewState.getStart());
            b.inputEndEdit.setText(viewState.getEnd());
        });

        // Initialise et observe le champ date
        observeDate(viewModel);

        observeTime(viewModel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    private void observeSubject(CreateMeetingViewModel viewModel, EditText subject) {
        subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.onSubjectChanged(editable.toString());
            }
        });
    }

    private void observeParticipants(CreateMeetingViewModel viewModel, EditText participants) {
        participants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.onParticipantsChanged(editable.toString());
            }
        });
    }

    private void initRoomSpinner(CreateMeetingViewModel viewModel, CreateMeetingViewState createMeetingViewState) {
        CreateMeetingRoomSpinnerAdapter adapter = new CreateMeetingRoomSpinnerAdapter(requireContext(), R.layout.create_meeting_spinner_item, createMeetingViewState.getRooms());
        b.autoCompleteTextView.setAdapter(adapter);
        b.autoCompleteTextView.setOnItemClickListener((adapterView, view1, i, l) ->
                viewModel.onRoomChanged(adapter.getItem(i)));
    }

    private void observeDate(CreateMeetingViewModel viewModel) {
        b.inputDateEdit.setOnClickListener(view -> viewModel.onDisplayDatePickerClick());
        viewModel.getDatePickerDialogData().observe(getViewLifecycleOwner(), display -> {
            if (display)
                DatePicker.setDatePicker(viewModel, getParentFragmentManager());
        });
    }

    private void initTimePicker(CreateMeetingViewModel viewModel, boolean start) {

        int hour = LocalTime.now().getHour();
        int minute = LocalTime.now().getMinute();
        if (!start) {
            hour = LocalTime.now().plusMinutes(30).getHour();
            minute = LocalTime.now().plusMinutes(30).getMinute();
        }

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .build();

        {
            timePicker.addOnPositiveButtonClickListener(selection -> {
                if (start)
                    viewModel.onStartTimeChanged(timePicker.getHour(), timePicker.getMinute());
                else
                    viewModel.onEndTimeChanged(timePicker.getHour(), timePicker.getMinute());
            });
        }

        timePicker.show(getParentFragmentManager(), null);
    }

    private void observeTime(CreateMeetingViewModel viewModel) {
        b.inputStartEdit.setOnClickListener(view -> viewModel.onDisplayStartTimePickerClick());
        viewModel.getStartTimePickerDialogData().observe(getViewLifecycleOwner(), display -> {
            if (display)
                initTimePicker(viewModel, true);
        });

        b.inputEndEdit.setOnClickListener(view -> viewModel.onDisplayEndTimePickerClick());
        viewModel.getEndTimePickerDialogData().observe(getViewLifecycleOwner(), display -> {
            if (display)
                initTimePicker(viewModel, false);
        });
    }
}
