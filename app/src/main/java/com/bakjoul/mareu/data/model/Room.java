package com.bakjoul.mareu.data.model;

public enum Room {
    Black("#DA70D6"),
    Blue("#0000FF"),
    Brown("#A52A2A"),
    Green("#008000"),
    Grey("#808080"),
    Orange("#FFA500"),
    Pink("#FFC0CB"),
    Purple("#800080"),
    Red("#FF0000"),
    White("FFFFFF"),
    Yellow("#FFFF00");

    private final String color;

    Room(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
