package com.example.reclaim;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {ProgressEntry.class,UserEntry.class}, version = 6)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProgressEntryDao progressEntryDao();
    public abstract UserEntryDao userDetailsDao();


}

