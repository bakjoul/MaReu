package com.bakjoul.mareu.ui.date_filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class DateFilterViewState {

    @Nullable
    private final String date;
    @Nullable
    private final String start;
    @Nullable
    private final String end;

    public DateFilterViewState(@Nullable String date, @Nullable String start, @Nullable String end) {
        this.date = date;
        this.start = start;
        this.end = end;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    @Nullable
    public String getStart() {
        return start;
    }

    @Nullable
    public String getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateFilterViewState viewState = (DateFilterViewState) o;
        return Objects.equals(date, viewState.date) && Objects.equals(start, viewState.start) && Objects.equals(end, viewState.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, start, end);
    }

    @NonNull
    @Override
    public String toString() {
        return "DateFilterViewState{" +
                "date='" + date + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
