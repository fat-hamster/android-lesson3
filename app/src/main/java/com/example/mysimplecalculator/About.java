package com.example.mysimplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        MaterialButton buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(v -> finish());
    }
}