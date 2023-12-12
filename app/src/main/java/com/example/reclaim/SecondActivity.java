package com.example.reclaim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        String username = getIntent().getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            Toast.makeText(this, "Goodbye " + username+"See you soon", Toast.LENGTH_SHORT).show(); // displays goodbye message to the user with their name passed from previous intent
        }
        userdetailsButton.setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, DetailsActivity.class);
            startActivity(intent);
            });

            // Button to track calories
            button_track_calories.setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, TrackCaloriesActivity.class);
            startActivity(intent);
            });

            // Button for health advice
            button_health_advice.setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, HealthAdviceActivity.class);
            startActivity(intent);
            });

            // Button to find a gym
            button_find_gym.setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, FindGymActivity.class);
            startActivity(intent);
            });

            // Button to view progress
            button_progress.setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, ProgressActivity.class);
            startActivity(intent);
            });
        }
}