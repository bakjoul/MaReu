package com.bakjoul.mareu.ui.utils;

import androidx.fragment.app.FragmentManager;

import com.bakjoul.mareu.ui.create.CreateMeetingViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

public class DatePicker {

    public static void setDatePicker(CreateMeetingViewModel viewModel, FragmentManager fragmentManager) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        builder.setTitleText("Veuillez choisir une date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection ->
                viewModel.onDateChanged(datePicker.getHeaderText())
        );

        datePicker.show(fragmentManager, null);
    }
}
