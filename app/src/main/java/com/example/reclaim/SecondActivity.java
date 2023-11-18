package com.example.reclaim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        Button userdetailsButton = findViewById(R.id.userdetails);
        Button button_track_calories= findViewById(R.id.calorie_tracker);
        Button button_health_advice= findViewById(R.id.useradvice);
        Button button_find_gym= findViewById(R.id.Gymfinder);
        Button button_progress= findViewById(R.id.progress);
        findViewById(R.id.userdetails).setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, DetailsActivity.class);
            startActivity(intent);
            });

            // Button to track calories
            findViewById(R.id.calorie_tracker).setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, TrackCaloriesActivity.class);
            startActivity(intent);
            });

            // Button for health advice
            findViewById(R.id.useradvice).setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, HealthAdviceActivity.class);
            startActivity(intent);
            });

            // Button to find a gym
            findViewById(R.id.Gymfinder).setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, FindGymActivity.class);
            startActivity(intent);
            });

            // Button to view progress
            findViewById(R.id.progress).setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, ProgressActivity.class);
            startActivity(intent);
            });
        }
}