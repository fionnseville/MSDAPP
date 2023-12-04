package com.example.reclaim;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
@Entity(tableName = "user_calories_entries")
public class CalorieTrackerEntry {
    @PrimaryKey(autoGenerate = true)
    public int userid;
    @ColumnInfo(name = "foodName")
    public String foodName;
    @ColumnInfo(name = "calories")
    public int calories;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public CalorieTrackerEntry(String foodName, int calories) {
        this.foodName = foodName;
        this.calories = calories;

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}