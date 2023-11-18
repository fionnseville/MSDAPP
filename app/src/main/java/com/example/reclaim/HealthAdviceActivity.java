package com.example.reclaim;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class HealthAdviceActivity extends AppCompatActivity {

    private TextView textViewHealthFact;
    private Button buttonShowNextFact;
    private String[] healthFacts = {
            "Quitting smoking reduces your risk of heart attack by 40%",
            "Drinking water boosts your metabolism",
    };
    private Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_advice);

        textViewHealthFact = findViewById(R.id.textViewHealthFact);
        buttonShowNextFact = findViewById(R.id.buttonShowNextFact);

        buttonShowNextFact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomHealthFact();
            }
        });
    }

    private void showRandomHealthFact() {
        int randomIndex = random.nextInt(healthFacts.length);
        textViewHealthFact.setText(healthFacts[randomIndex]);
    }
}