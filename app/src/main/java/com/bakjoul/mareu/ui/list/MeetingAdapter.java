package com.bakjoul.mareu.ui.list;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bakjoul.mareu.databinding.MeetingItemBinding;

public class MeetingAdapter extends ListAdapter<MeetingItemViewState, MeetingAdapter.ViewHolder> {

    @NonNull
    private final OnDeleteClickedListener listener;

    public MeetingAdapter(@NonNull OnDeleteClickedListener listener) {
        super(new MeetingAdapterDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MeetingItemBinding b = MeetingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MeetingItemBinding b;

        public ViewHolder(@NonNull MeetingItemBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        public void bind(@NonNull final MeetingItemViewState meetingItemViewState, @NonNull final OnDeleteClickedListener listener) {
            // Meeting room icon
            b.itemIcon.setColorFilter(Color.parseColor(meetingItemViewState.getRoom().getColor()));
            // Meeting subject
            b.itemSubject.setText(meetingItemViewState.getSubject());
            // Meeting time and location
            b.itemInfo.setText(String.format("%s – %s-%s – %s", meetingItemViewState.getDate(), meetingItemViewState.getStartTime(), meetingItemViewState.getEndTime(), meetingItemViewState.getRoom()));
            // Meeting participants
            b.itemParticipants.setText(meetingItemViewState.getParticipants());

            // Delete button action
            b.itemDeleteButton.setOnClickListener(view -> listener.onDeleteMeetingClick(meetingItemViewState.getId()));
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
