package com.example.reclaim;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CalorieTrackerDao {
    @Insert
    void insert(CalorieTrackerEntry entry);
    @Update
    void update(CalorieTrackerEntry entry);
    @Delete
    void delete(CalorieTrackerEntry entry);
    @Query("SELECT * FROM user_calories_entries")
    List<CalorieTrackerEntry> getAllEntries();

    @Query("SELECT * FROM user_calories_entries WHERE userid = :userId")
    CalorieTrackerEntry getEntryById(int userId);

    @Query("DELETE FROM user_calories_entries")
    void deleteAllEntries();

    @Query("SELECT SUM(calories) FROM user_calories_entries")
    int getTotalCalories();
}