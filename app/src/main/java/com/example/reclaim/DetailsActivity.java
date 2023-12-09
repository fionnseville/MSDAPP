package com.example.reclaim;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.room.Room;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    EditText editTextWeight, editTextHeight,nameview,ageview,emailview,phoneview;
    TextView textViewBmiResult;
    Button bmiButton;
    Button viewDetailsButton;
    Button Submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Button returnbutton=findViewById(R.id.returnbutton);
        returnbutton.setOnClickListener(view -> {
            Intent intent = new Intent(DetailsActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        Submit =findViewById(R.id.buttonSubmit);
        Submit.setOnClickListener(view -> submitUserDetails());
        editTextWeight = findViewById(R.id.weightview);
        editTextHeight = findViewById(R.id.heightview);
        textViewBmiResult = findViewById(R.id.bmireturn);
        bmiButton = findViewById(R.id.bmibutton);
        viewDetailsButton = findViewById(R.id.viewdetailsbutton);
        viewDetailsButton.setOnClickListener(view -> {
            Intent intent = new Intent(DetailsActivity.this, ViewDetailsActivity.class);
            startActivity(intent);
        });
        //viewDetailsButton.setOnClickListener(view -> loadAndDisplayUserDetails(1));
        bmiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateAndDisplayBMI();
            }
        });
    }
    private void submitUserDetails() {
        String weightStr = editTextWeight.getText().toString();
        String heightStr = editTextHeight.getText().toString();

        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr);
            nameview = findViewById(R.id.Nameview);
            ageview = findViewById(R.id.ageview);
            emailview = findViewById(R.id.emailview);
            phoneview = findViewById(R.id.Numberview);
            String name = nameview.getText().toString().trim();
            int age = Integer.parseInt(ageview.getText().toString().trim());
            String email = emailview.getText().toString();
            Long pnum = Long.parseLong(phoneview.getText().toString());

            new Thread(() -> {
                AppDatabase database = getAppDatabase();

                UserEntry existingUser = database.userDetailsDao().getUserDetails(1);
                if (existingUser != null) {
                    existingUser.setUname(name);
                    existingUser.setAge(age);
                    existingUser.setHeight(height);
                    existingUser.setWeight(weight);
                    existingUser.setEmail(email);
                    existingUser.setPhoneNo(pnum);
                    existingUser.setDate(new Date());

                    database.userDetailsDao().update(existingUser);
                } else {
                    UserEntry newUser = new UserEntry(name, age, height, weight, email, pnum, new Date());
                    database.userDetailsDao().insert(newUser);
                }
                //https://androidexample.com/show-toast-message-inside-run-method-of-thread#:~:text=you%20are%20try%20to%20show,queue%20of%20the%20UI%20thread.
                runOnUiThread(() -> {
                    Toast.makeText(this, "User details submitted successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                });
                //reference complete
            }).start();
        } else {
            Toast.makeText(this, "Please enter your weight and height.", Toast.LENGTH_LONG).show();
        }
    }
    private void calculateAndDisplayBMI() {
        new Thread(() -> {
            AppDatabase database = getAppDatabase();
            UserEntry userDetails = database.userDetailsDao().getUserDetails(1);

            if (userDetails != null) {
                float weight = userDetails.getWeight();
                float height = userDetails.getHeight() / 100;

                float bmi = weight / (height * height);

                runOnUiThread(() -> {
                    String bmiResultText = String.format(Locale.getDefault(), "Your BMI is: %.2f", bmi);
                    textViewBmiResult.setText(bmiResultText);
                    textViewBmiResult.setVisibility(View.VISIBLE);
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "User details not found", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    private void clearFields() {
        editTextWeight.setText("");
        editTextHeight.setText("");
        nameview.setText("");
        ageview.setText("");
        emailview.setText("");
        phoneview.setText("");
        textViewBmiResult.setText("");
    }
    //https://stackoverflow.com/questions/49629656/please-provide-a-migration-in-the-builder-or-call-fallbacktodestructivemigration
    private AppDatabase getAppDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "reclaim-database")
                .fallbackToDestructiveMigration()
                .build();
    }
    //Reference complete
}






