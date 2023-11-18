package com.example.reclaim;
import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {ProgressEntry.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract ProgressEntryDao progressEntryDao();
}

