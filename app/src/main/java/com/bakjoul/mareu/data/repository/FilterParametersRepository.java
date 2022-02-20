package com.bakjoul.mareu.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FilterParametersRepository {

    private final MutableLiveData<LocalDate> selectedDateLiveData = new MutableLiveData<>();

    @Inject
    public FilterParametersRepository() {
        selectedDateLiveData.setValue(LocalDate.now());
    }

    public void onFilterDateSelected(@NonNull LocalDate date) {
            selectedDateLiveData.setValue(date.plusMonths(1));
        Log.d("test", date.toString());
    }

    public LiveData<LocalDate> getSelectedDateLiveData() {
        return selectedDateLiveData;
    }
}
