package com.example.mysimplecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.dark) {
            setTheme(R.style.Theme_MySimpleCalculatorDark);
        } else {
            setTheme(R.style.Theme_MySimpleCalculator);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SwitchMaterial switchTheme = findViewById(R.id.switchTheme);

        switchTheme.setChecked(MainActivity.dark);
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                MainActivity.dark = true;
            } else {
                MainActivity.dark = false;
            }
            Intent switchThemeIntent = getIntent();
            this.finish();
            startActivity(switchThemeIntent);
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}