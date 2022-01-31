package com.bakjoul.mareu.ui;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bakjoul.mareu.databinding.MeetingItemBinding;

import java.time.format.DateTimeFormatter;

public class MeetingAdapter extends ListAdapter<MeetingItemViewState, MeetingAdapter.ViewHolder> {

    public MeetingAdapter() {
        super(new MeetingAdapterDiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MeetingItemBinding b = MeetingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MeetingAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MeetingItemBinding b;
        private static final String DATE_FORMATTER = "HH'h'mm";

        public ViewHolder(MeetingItemBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(@NonNull final MeetingItemViewState meetingItemViewState) {
            b.itemIcon.setColorFilter(Color.parseColor(meetingItemViewState.getRoom().getColor()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
            b.itemInfo.setText(String.format("%s - %s - %s", meetingItemViewState.getSubject(), meetingItemViewState.getTime().format(formatter), meetingItemViewState.getRoom()));
            b.itemParticipants.setText(meetingItemViewState.getParticipants().toString());
        }
    }

    private static class MeetingAdapterDiffCallback extends DiffUtil.ItemCallback<MeetingItemViewState> {

        @Override
        public boolean areItemsTheSame(@NonNull MeetingItemViewState oldItem, @NonNull MeetingItemViewState newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MeetingItemViewState oldItem, @NonNull MeetingItemViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
}
