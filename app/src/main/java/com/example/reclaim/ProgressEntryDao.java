package com.example.reclaim;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ProgressEntryDao {
    @Insert
    void insert(ProgressEntry entry);

    @Query("SELECT * FROM progress_entries")
    List<ProgressEntry> getAllEntries();
}