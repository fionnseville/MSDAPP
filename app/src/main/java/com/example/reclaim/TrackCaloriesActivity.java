package com.example.reclaim;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrackCaloriesActivity extends AppCompatActivity {
    private Button AddFood;
    private EditText editTextFoodItem,food_name,calorie_num;
    private TextView  dailycaloriesTextView ;
    private List<String> foodListItems;
    private ArrayAdapter<String> foodAdapter;
    private List<String> calorieListItems;
    private ArrayAdapter<String> calorieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_calories_activity);
        dailycaloriesTextView = findViewById(R.id.dailycalories);
        editTextFoodItem = findViewById(R.id.foodEditText);
        AddFood = findViewById(R.id.Addfoodbutton);
        ListView calorieListView = findViewById(R.id.CalorieList);
        calorieListItems = new ArrayList<>();
        calorieAdapter = new CustomList2Adapter(this, calorieListItems);
        //calorieAdapter = new ArrayAdapter<>(this, R.layout.custom_list_2, R.id.textViewItem, calorieListItems);
        calorieListView.setAdapter(calorieAdapter);
        calorieListItems.add("Calories:");
        calculateDailyCalorieIntake();
        ListView foodListView = findViewById(R.id.FoodList);
        foodListItems = new ArrayList<>();
        foodAdapter = new CustomList1Adapter(this, foodListItems);
        //foodAdapter = new ArrayAdapter<>(this, R.layout.custom_list_item, R.id.textViewItem, foodListItems);
        foodListView.setAdapter(foodAdapter);
        foodListItems.add("Food:");
        AddFood.setOnClickListener(v -> {
            String foodItem = editTextFoodItem.getText().toString();
            if (!foodItem.isEmpty()) {
                submitFooditem();
            }
        });
        Button deleteButton = findViewById(R.id.deleteButton);
        Button clearAllButton = findViewById(R.id.clearAllButton);

        deleteButton.setOnClickListener(v -> {
            deleteLatestEntry();
            updateLists();
            calculateDailyCalorieIntake();
        });

        clearAllButton.setOnClickListener(v -> {
            clearAllEntries();
            updateLists();
            calculateDailyCalorieIntake();
        });

        new Thread(() -> {
            AppDatabase database = getAppDatabase();
            List<CalorieTrackerEntry> entries = database.calorieTrackerDao().getAllEntries();
            runOnUiThread(() -> {
                for (CalorieTrackerEntry entry : entries) {
                    foodListItems.add(entry.getFoodName());
                    calorieListItems.add(entry.getCalories() + "");
                }
                foodAdapter.notifyDataSetChanged();
                calorieAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void updateLists() {
        new Thread(() -> {
            AppDatabase database = getAppDatabase();
            List<CalorieTrackerEntry> entries = database.calorieTrackerDao().getAllEntries();

            runOnUiThread(() -> {
                foodListItems.clear();
                calorieListItems.clear();
                foodListItems.add("Food:");
                calorieListItems.add("Calories:");
                for (CalorieTrackerEntry entry : entries) {
                    foodListItems.add(entry.getFoodName());
                    calorieListItems.add("" + entry.getCalories());
                }

                foodAdapter.notifyDataSetChanged();
                calorieAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void submitFooditem() {
        food_name = findViewById(R.id.foodEditText);
        String fname = food_name.getText().toString().trim();
        calorie_num = findViewById(R.id.caloriesEditText);
        int calories = Integer.parseInt(calorie_num.getText().toString().trim());
        new Thread(() -> {
            AppDatabase database = getAppDatabase();

            CalorieTrackerEntry newEntry = new CalorieTrackerEntry(fname, calories);
            database.calorieTrackerDao().insert(newEntry);
            updateLists();
            calculateDailyCalorieIntake();

            runOnUiThread(() -> {
                Toast.makeText(this, " food added successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            });
        }).start();

    }

    private void deleteLatestEntry() {
        new Thread(() -> {
            AppDatabase database = getAppDatabase();
            List<CalorieTrackerEntry> entries = database.calorieTrackerDao().getAllEntries();

            if (!entries.isEmpty()) {
                CalorieTrackerEntry latestEntry = entries.get(entries.size() - 1);
                database.calorieTrackerDao().delete(latestEntry);
            }
        }).start();
    }

    private void calculateDailyCalorieIntake() {
        new Thread(() -> {
            AppDatabase database = getAppDatabase();
            UserEntry user = database.userDetailsDao().getUserDetails(1);
            if (user != null) {
                float weight = user.getWeight();
                float height = user.getHeight();
                int age = user.getAge();
                int basalMetabolicRate = (int) (66 + (13.7 * weight) + (5 * height) - (6.8 * age));
                int totalCaloriesConsumed = database.calorieTrackerDao().getTotalCalories();
                int dailyCalorieIntake = basalMetabolicRate - totalCaloriesConsumed;

                runOnUiThread(() -> {
                    dailycaloriesTextView.setText("Daily Calorie Intake left: " + dailyCalorieIntake);
                });
            }
        }).start();
    }
    private void clearAllEntries() {
        new Thread(() -> {
            AppDatabase database = getAppDatabase();
            database.calorieTrackerDao().deleteAllEntries();
        }).start();
    }



    private void clearFields() {
        food_name.setText("");
        calorie_num.setText("");

    }

    private AppDatabase getAppDatabase() {
        return AppDatabase.getInstance(getApplicationContext());
    }
}



