package com.example.reclaim;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {ProgressEntry.class,UserEntry.class,CalorieTrackerEntry.class}, version = 7)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProgressEntryDao progressEntryDao();
    public abstract UserEntryDao userDetailsDao();

    public abstract CalorieTrackerDao calorieTrackerDao();


}

