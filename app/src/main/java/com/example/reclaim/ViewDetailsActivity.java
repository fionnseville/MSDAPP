package com.example.reclaim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewDetailsActivity extends AppCompatActivity {
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdetails);
        database = getAppDatabase();
        loadAndDisplayUserDetails(1);

        Button returnbutton=findViewById(R.id.returnButton);
        returnbutton.setOnClickListener(view -> {
            Intent intent = new Intent(ViewDetailsActivity.this, DetailsActivity.class);
            startActivity(intent);
        });
    }
   private void loadAndDisplayUserDetails(int userId) {
        new Thread(() -> {
            //gets user details using user id =1
            UserEntry userDetails = database.userDetailsDao().getUserDetails(userId);
            if (userDetails != null) {//checks if null
                runOnUiThread(() -> {
                    TextView textViewName = findViewById(R.id.textViewName);
                    TextView textViewAge = findViewById(R.id.textViewAge);
                    TextView textViewHeight = findViewById(R.id.textViewHeight);
                    TextView textViewWeight = findViewById(R.id.textViewWeight);
                    TextView textViewEmail = findViewById(R.id.textViewEmail);
                    TextView textViewPhone = findViewById(R.id.textViewPhone);
                    //sets users details for the textviews
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
