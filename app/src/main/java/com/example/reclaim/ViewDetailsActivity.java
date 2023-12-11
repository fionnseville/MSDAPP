package com.example.reclaim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class ViewDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdetails);
        loadAndDisplayUserDetails(1);

        Button returnbutton=findViewById(R.id.returnButton);
        returnbutton.setOnClickListener(view -> {
            Intent intent = new Intent(ViewDetailsActivity.this, DetailsActivity.class);
            startActivity(intent);
        });
    }
   private void loadAndDisplayUserDetails(int userId) {
        new Thread(() -> {
            AppDatabase database = getAppDatabase();
            UserEntry userDetails = database.userDetailsDao().getUserDetails(userId);
            if (userDetails != null) {
                runOnUiThread(() -> {
                    TextView textViewName = findViewById(R.id.textViewName);
                    TextView textViewAge = findViewById(R.id.textViewAge);
                    TextView textViewHeight = findViewById(R.id.textViewHeight);
                    TextView textViewWeight = findViewById(R.id.textViewWeight);
                    TextView textViewEmail = findViewById(R.id.textViewEmail);
                    TextView textViewPhone = findViewById(R.id.textViewPhone);
                    textViewName.setText("Name: " + userDetails.getUname());
                    textViewAge.setText("Age: " + userDetails.getAge());
                    textViewHeight.setText("Height: " + userDetails.getHeight());
                    textViewWeight.setText("Weight: " + userDetails.getWeight());
                    textViewEmail.setText("Email: " + userDetails.getEmail());
                    textViewPhone.setText("Phone: 0" + userDetails.getPhoneNo());

                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "User details not found", Toast.LENGTH_SHORT).show());
            }
        }).start();

    }
    private AppDatabase getAppDatabase() {
        return AppDatabase.getInstance(getApplicationContext());
    }
}
