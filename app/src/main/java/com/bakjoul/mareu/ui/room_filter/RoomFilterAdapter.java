package com.bakjoul.mareu.ui.room_filter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bakjoul.mareu.databinding.RoomFilterItemBinding;

public class RoomFilterAdapter extends ListAdapter<RoomFilterItemViewState, RoomFilterAdapter.ViewHolder> {

    @NonNull
    private final OnItemClickedListener listener;

    public RoomFilterAdapter(@NonNull OnItemClickedListener listener) {
        super(new RoomFilterAdapterDiffCallback());

        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RoomFilterItemBinding b = RoomFilterItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final RoomFilterItemBinding b;

        public ViewHolder(RoomFilterItemBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        public void bind(@NonNull final RoomFilterItemViewState roomFilterItemViewState, @NonNull final OnItemClickedListener listener) {
            b.roomFilterItemIcon.setColorFilter(Color.parseColor(roomFilterItemViewState.getRoom().getColor()));
            b.roomFilterItemName.setText(roomFilterItemViewState.getRoom().name());
            // À MODIFIER
            if (roomFilterItemViewState.isSelected())
                b.roomFilterItemview.setBackgroundColor(Color.parseColor("#000000"));
            else
                b.roomFilterItemview.setBackgroundColor(Color.parseColor("#EEEEEE"));

            b.roomFilterItemview.setOnClickListener(view ->
                    listener.onRoomSelected(roomFilterItemViewState.getRoom()));
        }

    }

    private static class RoomFilterAdapterDiffCallback extends DiffUtil.ItemCallback<RoomFilterItemViewState> {

        @Override
        public boolean areItemsTheSame(@NonNull RoomFilterItemViewState oldItem, @NonNull RoomFilterItemViewState newItem) {
            return oldItem.getRoom().equals(newItem.getRoom());
        }

        @Override
        public boolean areContentsTheSame(@NonNull RoomFilterItemViewState oldItem, @NonNull RoomFilterItemViewState newItem) {
            return oldItem.isSelected() == newItem.isSelected();
        }
    }
}
