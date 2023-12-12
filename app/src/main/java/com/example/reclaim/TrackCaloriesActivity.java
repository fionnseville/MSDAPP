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
    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_calories_activity);
        database = getAppDatabase();
        dailycaloriesTextView = findViewById(R.id.dailycalories);
        editTextFoodItem = findViewById(R.id.foodEditText);
        AddFood = findViewById(R.id.Addfoodbutton);
        ListView calorieListView = findViewById(R.id.CalorieList);
        calorieListItems = new ArrayList<>();
        calorieAdapter = new CustomList2Adapter(this, calorieListItems);
        //calorieAdapter = new ArrayAdapter<>(this, R.layout.custom_list_2, R.id.textViewItem, calorieListItems);
        calorieListView.setAdapter(calorieAdapter);//setting for listview
        calorieListItems.add("Calories:");//basically just a header
        calculateDailyCalorieIntake();//gets users weight and height calculates the daily cals
        ListView foodListView = findViewById(R.id.FoodList);
        foodListItems = new ArrayList<>();
        foodAdapter = new CustomList1Adapter(this, foodListItems);
        //foodAdapter = new ArrayAdapter<>(this, R.layout.custom_list_item, R.id.textViewItem, foodListItems);
        foodListView.setAdapter(foodAdapter);//setting for listview
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

        //retrieves previous inputs and displays in listview
        new Thread(() -> {
            List<CalorieTrackerEntry> entries = database.calorieTrackerDao().getAllEntries();
            runOnUiThread(() -> {
                for (CalorieTrackerEntry entry : entries) {
                    foodListItems.add(entry.getFoodName());
                    calorieListItems.add(entry.getCalories() + "");
                }
                foodAdapter.notifyDataSetChanged();//updating adapter
                calorieAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void updateLists() {
        new Thread(() -> {
            List<CalorieTrackerEntry> entries = database.calorieTrackerDao().getAllEntries();

            runOnUiThread(() -> {
                foodListItems.clear();//clears items displayed so it doesnt duplicate entries
                calorieListItems.clear();
                foodListItems.add("Food:");//adding the header again
                calorieListItems.add("Calories:");
                for (CalorieTrackerEntry entry : entries) {
                    foodListItems.add(entry.getFoodName());
                    calorieListItems.add("" + entry.getCalories());
                }

                foodAdapter.notifyDataSetChanged();//updates the adapter
                calorieAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void submitFooditem() {
        food_name = findViewById(R.id.foodEditText);
        String fname = food_name.getText().toString().trim();//getting food name
        calorie_num = findViewById(R.id.caloriesEditText);
        int calories = Integer.parseInt(calorie_num.getText().toString().trim());//getting calories
        new Thread(() -> {
            CalorieTrackerEntry newEntry = new CalorieTrackerEntry(fname, calories);//adding to the database
            database.calorieTrackerDao().insert(newEntry);
            updateLists();
            calculateDailyCalorieIntake();//updates calories needed

            runOnUiThread(() -> {
                Toast.makeText(this, " food added successfully", Toast.LENGTH_SHORT).show();
                clearFields();//clears the input fields
            });
        }).start();

    }

    private void deleteLatestEntry() {
        new Thread(() -> {
            List<CalorieTrackerEntry> entries = database.calorieTrackerDao().getAllEntries();
            //checks that theres an entry and then deletes the most recent
            if (!entries.isEmpty()) {
                CalorieTrackerEntry latestEntry = entries.get(entries.size() - 1);
                database.calorieTrackerDao().delete(latestEntry);
            }
        }).start();
    }

    private void calculateDailyCalorieIntake() {
        new Thread(() -> {
            UserEntry user = database.userDetailsDao().getUserDetails(1);
            if (user != null) {
                float weight = user.getWeight();//gets users weight
                float height = user.getHeight();//gets height
                int age = user.getAge();
                int basalMetabolicRate = (int) (66 + (13.7 * weight) + (5 * height) - (6.8 * age));//calculation for cals
                int totalCaloriesConsumed = database.calorieTrackerDao().getTotalCalories();//total calories added to list
                int dailyCalorieIntake = basalMetabolicRate - totalCaloriesConsumed;//takes the cals off daily total

                runOnUiThread(() -> {
                    dailycaloriesTextView.setText("Daily Calorie Intake left: " + dailyCalorieIntake);
                });
            }
        }).start();
    }
    private void clearAllEntries() {
        new Thread(() -> {
            database.calorieTrackerDao().deleteAllEntries();//deletes all entries from database
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



