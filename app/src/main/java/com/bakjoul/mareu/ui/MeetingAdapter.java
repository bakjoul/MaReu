package com.bakjoul.mareu.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bakjoul.mareu.databinding.MeetingItemBinding;

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

    @Override
    public void onBindViewHolder(@NonNull MeetingAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MeetingItemBinding b;

        public ViewHolder(MeetingItemBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        public void bind(@NonNull final MeetingItemViewState meetingItemViewState) {
            b.itemIcon.setColorFilter(Color.parseColor(meetingItemViewState.getRoomColor()));
            b.itemTitle.setText(meetingItemViewState.getSubject());
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
