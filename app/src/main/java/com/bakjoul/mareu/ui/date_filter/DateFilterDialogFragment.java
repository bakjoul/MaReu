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

    public static DateFilterDialogFragment newInstance() {
        return new DateFilterDialogFragment();
    }

    private DateFilterFragmentBinding b;
    private DateFilterViewModel viewModel;
    private boolean isStartPicker = true;

    @Override
    public void onStart() {
        super.onStart();
        setDialogParameters();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        b = DateFilterFragmentBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.date_filter_dialog_title).setView(b.getRoot());
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

        viewModel = new ViewModelProvider(this).get(DateFilterViewModel.class);

        viewModel.getViewStateMutableLiveData().observe(getViewLifecycleOwner(), viewState -> {
            b.dateFilterInputDateEdit.setText(viewState.getDate());
            b.dateFilterInputStartEdit.setText(viewState.getStart());
            b.dateFilterInputEndEdit.setText(viewState.getEnd());
        });

        observePickers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        b = null;
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
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
        Calendar now = Calendar.getInstance();
        if (!isStartPicker)
            now.add(Calendar.HOUR_OF_DAY, 1);

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

    private void setDialogParameters() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.75);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawableResource(R.color.white_f8f8ff);
            dialog.getWindow().setGravity(Gravity.START | Gravity.TOP);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_SlideDownScale);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            TypedValue tv = new TypedValue();
            // Récupère la hauteur de l'actionbar
            requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.actionBarSize, tv, true);

            View dateItemView = requireActivity().findViewById(R.id.menu_date_filter);
            int[] dateItemWindowLocation = new int[2];
            // Récupère la position de l'icône date du menu
            dateItemView.getLocationInWindow(dateItemWindowLocation);

            int dateIconItemX = dateItemWindowLocation[0];  // Coordonnée x de l'icône
            int dateIconWidth = dateItemView.getWidth();    // Largeur de l'icône

            // Aligne la droite du dialog avec la fin de l'icône
            params.x = dateIconItemX - width + dateIconWidth;
            // Aligne le haut du dialog avec le bas de l'actionbar
            params.y = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());

            dialog.getWindow().setAttributes(params);
        }
    }
}
