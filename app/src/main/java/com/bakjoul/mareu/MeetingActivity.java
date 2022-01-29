package com.bakjoul.mareu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bakjoul.mareu.databinding.ActivityMainBinding;

public class MeetingActivity extends AppCompatActivity {

    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
    }
}