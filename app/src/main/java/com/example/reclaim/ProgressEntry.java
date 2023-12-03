package com.example.reclaim;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.util.Date;

@Entity(tableName = "progress_entries")
public class ProgressEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "photo")
    public byte[] photo;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "date")
    public Date date;
    public ProgressEntry(byte[] photo, String description, Date date) {
        this.photo = photo;
        this.description = description;
        this.date = date;
    }
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}



