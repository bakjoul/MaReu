package com.bakjoul.mareu.ui.date_filter;

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

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.databinding.DateFilterFragmentBinding;
import com.bakjoul.mareu.ui.MeetingViewEvent;
import com.bakjoul.mareu.ui.create.CreateMeetingDialogFragment;
import com.bakjoul.mareu.utils.OnDateSetListener;
import com.bakjoul.mareu.utils.OnTimeSetListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DateFilterDialogFragment extends DialogFragment implements OnDateSetListener, OnTimeSetListener {

    @NonNull
    public static DateFilterDialogFragment newInstance() {
        return new DateFilterDialogFragment();
    }

    private DateFilterFragmentBinding b;
    private DateFilterViewModel viewModel;
    private boolean isStartPicker = true;

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
        b = DateFilterFragmentBinding.inflate(inflater, container, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(b.getRoot()).create();
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DateFilterViewModel.class);

        viewModel.getViewStateMutableLiveData().observe(getViewLifecycleOwner(), viewState -> {
            b.dateFilterInputDateEdit.setText(viewState.getDate());
            b.dateFilterInputStartEdit.setText(viewState.getStart());
            b.dateFilterInputEndEdit.setText(viewState.getEnd());

            b.dateFilterInputDate.setEndIconVisible(viewState.getDate() != null);
            b.dateFilterInputDate.setEndIconActivated(viewState.getDate() != null);
            b.dateFilterInputStart.setEndIconVisible(viewState.getStart() != null);
            b.dateFilterInputStart.setEndIconActivated(viewState.getStart() != null);
            b.dateFilterInputEnd.setEndIconVisible(viewState.getEnd() != null);
            b.dateFilterInputEnd.setEndIconActivated(viewState.getEnd() != null);

        });

        setDialogButtonsActions();
        observePickers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    private void setDialogButtonsActions() {
        b.dateFilterButtonReinit.setOnClickListener(v -> viewModel.onClearAllDateFilters());
        b.dateFilterButtonClose.setOnClickListener(v -> dismiss());
    }

    private void observePickers() {
        b.dateFilterInputDateEdit.setOnClickListener(view -> viewModel.onDisplayDatePickerClicked());
        b.dateFilterInputStartEdit.setOnClickListener(view -> viewModel.onDisplayStartTimePickerClicked());
        b.dateFilterInputEndEdit.setOnClickListener(view -> viewModel.onDisplayEndTimePickerClicked());

        viewModel.getSingleLiveEvent().observe(getViewLifecycleOwner(), viewEvent -> {
            if (viewEvent == MeetingViewEvent.DISPLAY_CREATE_MEETING_DATE_PICKER)
                initDatePicker();
            else if (viewEvent == MeetingViewEvent.DISPLAY_CREATE_MEETING_START_PICKER) {
                isStartPicker = true;
                initTimePicker();
            } else if (viewEvent == MeetingViewEvent.DISPLAY_CREATE_MEETING_END_PICKER) {
                isStartPicker = false;
                initTimePicker();
            }
        });

        b.dateFilterInputDate.setEndIconOnClickListener(view -> viewModel.onClearDateFilter());
        b.dateFilterInputStart.setEndIconOnClickListener(view -> viewModel.onClearStartTimeFilter());
        b.dateFilterInputEnd.setEndIconOnClickListener(view -> viewModel.onClearEndTimeFilter());
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
        now.add(Calendar.DAY_OF_MONTH, CreateMeetingDialogFragment.MEETING_MAX_DATE);
        dpd.setMaxDate(now);

        dpd.show(getParentFragmentManager(), null);
    }

    private void initTimePicker() {
        int hour = 8;
        if (!isStartPicker)
            hour = 22;

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this::onTimeSet,
                hour,
                0,
                true
        );
        tpd.setMinTime(8, 0, 0);
        tpd.setMaxTime(22, 0, 0);
        tpd.setTimeInterval(1, 15, 60);

        tpd.show(getParentFragmentManager(), null);
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
}
