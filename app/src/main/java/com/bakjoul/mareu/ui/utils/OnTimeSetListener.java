package com.bakjoul.mareu.ui.utils;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

public interface OnTimeSetListener {
    void onTimeSet(TimePickerDialog view, int hour, int minute, int second);
}
