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
            "Regular exercise improves mental health and reduces the risk of depression",
            "A good night's sleep is crucial for overall well-being",
            "Eating a balanced diet promotes better immune function",
            "Reducing sugar intake lowers the risk of type 2 diabetes",
            "Stress management is important for heart health",
            "Laughing is good for your heart and can increase blood flow",
            "Spending time in nature has been linked to improved mental health",
            "Eating dark chocolate in moderation may have heart health benefits",
            "Yoga can help improve flexibility and reduce stress",
            "Regular dental check-ups are essential for overall health",
            "Socializing with friends and family can boost your mood",
            "Consuming Omega-3 fatty acids supports brain health",
            "Adequate Vitamin D is essential for bone health",
            "Practicing mindfulness can reduce stress and anxiety",
            "Maintaining a healthy weight is beneficial for overall health",
            "Eating fruits and vegetables provides essential vitamins and minerals",
            "Limiting processed food intake reduces sodium and added sugar consumption",
            "Getting regular check-ups can catch potential health issues early"
    };

    //REFERENCE: https://www.baeldung.com/java-random-list-element
    private Random random = new Random();
    //reference complete
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