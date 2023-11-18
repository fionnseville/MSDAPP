package com.example.reclaim;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    EditText editTextWeight, editTextHeight;
    TextView textViewBmiResult;
    Button bmiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Button returnbutton=findViewById(R.id.returnbutton);
        Button viewdetailsbutton=findViewById(R.id.viewdetailsbutton);
        findViewById(R.id.returnbutton).setOnClickListener(view -> {
            Intent intent = new Intent(DetailsActivity.this, SecondActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.viewdetailsbutton).setOnClickListener(view -> {
            Intent intent = new Intent(DetailsActivity.this, SecondActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.buttonSubmit).setOnClickListener(view -> {
            Intent intent = new Intent(DetailsActivity.this, SecondActivity.class);
            startActivity(intent);
        });
        editTextWeight = findViewById(R.id.weightview);
        editTextHeight = findViewById(R.id.heightview);
        textViewBmiResult = findViewById(R.id.bmireturn);
        bmiButton = findViewById(R.id.bmibutton);

        bmiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateAndDisplayBMI();
            }
        });
    }

    private void calculateAndDisplayBMI() {
        String weightStr = editTextWeight.getText().toString();
        String heightStr = editTextHeight.getText().toString();

        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr) / 100; // Convert cm to meters
            float bmi = weight / (height * height);

            String bmiResultText = String.format(Locale.getDefault(), "Your BMI is: %.2f", bmi);
            textViewBmiResult.setText(bmiResultText);
            textViewBmiResult.setVisibility(View.VISIBLE);
            // Toast.makeText(this, bmiResultText, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enter your weight and height.", Toast.LENGTH_LONG).show();
        }
    }
}




