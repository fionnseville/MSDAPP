package com.example.reclaim;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TrackCaloriesActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listItems;
    private Button AddFood;
    private EditText editTextFoodItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_calories_activity);
        listView = findViewById(R.id.FoodList);
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.textViewItem, listItems);
        listView.setAdapter(adapter);
        editTextFoodItem=findViewById(R.id.foodEditText);
        AddFood=findViewById(R.id.Addfoodbutton);
        AddFood.setOnClickListener(v -> {
            String foodItem = editTextFoodItem.getText().toString();
            if (!foodItem.isEmpty()) {
                listItems.add(foodItem);
                adapter.notifyDataSetChanged();
                editTextFoodItem.setText("");
            }
        });
}}



