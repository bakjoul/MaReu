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
import com.bakjoul.mareu.databinding.CreateMeetingFragmentBinding;
import com.bakjoul.mareu.ui.MeetingViewEvent;
import com.bakjoul.mareu.utils.OnDateSetListener;
import com.bakjoul.mareu.utils.OnTimeSetListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateMeetingDialogFragment extends DialogFragment implements OnDateSetListener, OnTimeSetListener {

    public static CreateMeetingDialogFragment newInstance() {
        return new CreateMeetingDialogFragment();
    }

    public static final int MEETING_MAX_DATE = 30;
    public static final int MEETING_MIN_DURATION = 30;

    private CreateMeetingFragmentBinding b;
    private CreateMeetingViewModel viewModel;
    private boolean isStartPicker = true;

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
        b = CreateMeetingFragmentBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CreateMeetingViewModel.class);

        // Définit l'action du bouton "X" de la toolbar
        b.dialogToolbar.setNavigationOnClickListener(item -> dismiss());

        // Définit l'action du bouton "Créer" de la toolbar
        b.dialogToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.create_new) {
                viewModel.createMeeting();
                return true;
            } else
                return false;
        });

        // Observe le champ sujet
        observeSubject(b.inputSubjectEdit);
        // Observe le champ participants
        observeParticipants(b.inputParticipantsEdit);

        viewModel.getViewStateLiveData().observe(getViewLifecycleOwner(), viewState -> {
            // Initialise et observe le room spinner
            initRoomSpinner(viewState);

            // Observe et met à jour l'affichage de la date et des heures
            b.createInputDateEdit.setText(viewState.getDate());
            b.createInputStartEdit.setText(viewState.getStart());
            b.createInputEndEdit.setText(viewState.getEnd());

            // Affiche les erreurs de saisie
            b.inputSubject.setError(viewState.getSubjectError());
            b.inputParticipants.setError(viewState.getParticipantsError());
            b.inputRoom.setError(viewState.getRoomError());
            b.createInputDate.setError(viewState.getDateError());
            b.createInputStart.setError(viewState.getStartError());
            b.createInputEnd.setError(viewState.getEndError());
        });

        // Initialise les OnClickListeners des champs date et heures
        setOnClickListeners();
        // Observe les actions sur les vues
        observeViewEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    private void observeSubject(EditText subject) {
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

    private void observeParticipants(EditText participants) {
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

    private void initRoomSpinner(CreateMeetingViewState createMeetingViewState) {
        CreateMeetingRoomSpinnerAdapter adapter = new CreateMeetingRoomSpinnerAdapter(requireContext(), R.layout.create_meeting_spinner_item, createMeetingViewState.getRooms());
        b.inputRoomAutoCompleteTextView.setAdapter(adapter);
        b.inputRoomAutoCompleteTextView.setOnItemClickListener((adapterView, view1, i, l) ->
                viewModel.onRoomChanged(adapter.getItem(i)));
    }

    private void initDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this::onDateSet,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        now.add(Calendar.DAY_OF_MONTH, MEETING_MAX_DATE);
        dpd.setMaxDate(now);

        dpd.show(getParentFragmentManager(), null);
    }

    private void initTimePicker() {
        Calendar now = Calendar.getInstance();
        int unroundedMinutes = now.get(Calendar.MINUTE);

        if (isStartPicker) {
            // Arrondit l'heure actuelle au quart d'heure supérieur
            now.add(Calendar.MINUTE, 15-(unroundedMinutes%15));
        }
        if (!isStartPicker) {
            // Ajoute 30mn à l'heure actuelle et arrondit au quart d'heure supérieur
            now.add(Calendar.MINUTE, MEETING_MIN_DURATION+(15-(unroundedMinutes%15)));
        }

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this::onTimeSet,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setMinTime(8, 0, 0);
        tpd.setMaxTime(22, 0, 0);
        tpd.setTimeInterval(1, 15, 60);

        tpd.show(getParentFragmentManager(), null);
    }

    private void observeViewEvents() {
        viewModel.getSingleLiveEvent().observe(getViewLifecycleOwner(), viewEvent -> {
            if (viewEvent == MeetingViewEvent.DISPLAY_CREATE_MEETING_DATE_PICKER)
                initDatePicker();
            else if (viewEvent == MeetingViewEvent.DISPLAY_CREATE_MEETING_START_PICKER) {
                isStartPicker = true;
                initTimePicker();
            } else if (viewEvent == MeetingViewEvent.DISPLAY_CREATE_MEETING_END_PICKER) {
                isStartPicker = false;
                initTimePicker();
            } else if (viewEvent == MeetingViewEvent.DISPLAY_OVERLAPPING_MEETING_TOAST) {
                viewModel.overlappingMeetingToast(getContext());
            } else if (viewEvent == MeetingViewEvent.DISMISS_CREATE_MEETING_DIALOG) {
                dismiss();
            }
        });
    }

    private void setOnClickListeners() {
        b.createInputDateEdit.setOnClickListener(view -> viewModel.onDisplayDatePickerClicked());
        b.createInputStartEdit.setOnClickListener(view -> viewModel.onDisplayStartTimePickerClicked());
        b.createInputEndEdit.setOnClickListener(view -> viewModel.onDisplayEndTimePickerClicked());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        viewModel.onDateChanged(year, month, day);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hour, int minute, int second) {
        if (isStartPicker)
            viewModel.onStartTimeChanged(hour, minute);
        else
            viewModel.onEndTimeChanged(hour, minute);
    }
}
