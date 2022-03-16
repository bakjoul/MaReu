package com.bakjoul.mareu.data.model;

import androidx.annotation.DrawableRes;

import com.bakjoul.mareu.R;

public enum Room {
    Black(R.drawable.ic_room_black),
    Blue(R.drawable.ic_room_blue),
    Brown(R.drawable.ic_room_brown),
    Green(R.drawable.ic_room_green),
    Grey(R.drawable.ic_room_grey),
    Orange(R.drawable.ic_room_orange),
    Pink(R.drawable.ic_room_pink),
    Purple(R.drawable.ic_room_purple),
    Red(R.drawable.ic_room_red),
    White(R.drawable.ic_room_white),
    Yellow(R.drawable.ic_room_yellow);

    @DrawableRes
    private final int iconRes;

    Room(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
    }

    public int getIconRes() {
        return iconRes;
    }
}
