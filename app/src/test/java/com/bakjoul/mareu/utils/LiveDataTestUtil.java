package com.bakjoul.mareu.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class LiveDataTestUtil {
    @NonNull
    public static <T> T getValueForTesting(@NonNull final LiveData<T> liveData) {
        liveData.observeForever(t -> {
        });

        T captured = liveData.getValue();

        if (captured == null) {
            throw new AssertionError("LiveData value is null !");
        }

        return captured;
    }
}
