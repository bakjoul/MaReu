package com.bakjoul.mareu.ui.create;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.databinding.CreateMeetingSpinnerItemBinding;

public class CreateMeetingRoomSpinnerAdapter extends ArrayAdapter<Room> {

    public CreateMeetingRoomSpinnerAdapter(@NonNull Context context, int resource, @NonNull Room[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    public View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CreateMeetingSpinnerItemBinding b;
        b = CreateMeetingSpinnerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Room room = getItem(position);

        // Définit la couleur de l'icône et le nom de la salle
        b.inputRoomSpinnerItemIcon.setColorFilter(Color.parseColor(room.getColor()));
        b.inputRoomSpinnerItemName.setText(room.name());

        return b.getRoot();
    }
}
