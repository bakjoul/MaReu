package com.bakjoul.mareu.ui.create;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

public interface OnTimeSetListener {
    void onTimeSet(TimePickerDialog view, int hour, int minute, int second);
}
