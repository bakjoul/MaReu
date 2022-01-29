package com.bakjoul.mareu.data.model;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import com.bakjoul.mareu.R;

public enum Room {
    BLACK(R.string.black, R.color.black),
    BLUE(R.string.blue, R.color.blue),
    BROWN(R.string.brown, R.color.brown),
    GREEN(R.string.green, R.color.green),
    GREY(R.string.grey, R.color.grey),
    ORANGE(R.string.orange, R.color.orange),
    PINK(R.string.pink, R.color.pink),
    PURPLE(R.string.purple, R.color.purple),
    RED(R.string.red, R.color.red),
    WHITE(R.string.white, R.color.white),
    YELLOW(R.string.yellow, R.color.yellow);

    @StringRes
    private final int name;

    @ColorRes
    private final int color;

    Room(@StringRes int name, @ColorRes int color) {
        this.name = name;
        this.color = color;
    }

    public int getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
