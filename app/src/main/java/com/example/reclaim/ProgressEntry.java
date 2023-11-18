package com.example.reclaim;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
@Entity(tableName = "progress_entries")
public class ProgressEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "photo")
    public byte[] photo;

    @ColumnInfo(name = "description")
    public String description;

    // Constructor, getters, and setters
    public ProgressEntry(byte[] photo, String description) {
        this.photo = photo;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
