package com.bakjoul.mareu.utils;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.core.AllOf.allOf;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.wdullaer.materialdatetimepicker.date.DatePickerController;
import com.wdullaer.materialdatetimepicker.date.DayPickerView;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.hamcrest.Matcher;

import java.lang.reflect.Field;

public class MaterialPickerActions {

    @NonNull
    public static ViewAction setDate(final int year, final int monthOfYear, final int dayOfMonth) {

        return new ViewAction() {

            @Override
            public void perform(UiController uiController, View view) {
                final DayPickerView dayPickerView = (DayPickerView) view;

                try {
                    Field f; //NoSuchFieldException
                    f = DayPickerView.class.getDeclaredField("mController");
                    f.setAccessible(true);
                    DatePickerController controller = (DatePickerController) f.get(dayPickerView); //IllegalAccessException
                    if (controller != null) {
                        controller.onDayOfMonthSelected(year, monthOfYear, dayOfMonth);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String getDescription() {
                return "set date";
            }

            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(DayPickerView.class), isDisplayed());
            }
        };
    }

    @NonNull
    public static ViewAction setTime(final int hours, final int minutes) {
        return new ViewAction() {

            @Override
            public void perform(UiController uiController, View view) {
                final RadialPickerLayout timePicker = (RadialPickerLayout) view;

                timePicker.setTime(new Timepoint(hours, minutes, 0));
            }

            @Override
            public String getDescription() {
                return "set time";
            }

            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(RadialPickerLayout.class), isDisplayed());
            }
        };
    }

}
