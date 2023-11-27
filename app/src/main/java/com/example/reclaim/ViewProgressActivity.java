package com.example.reclaim;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.List;


public class ViewProgressActivity extends AppCompatActivity {
    private List<ProgressEntry> photoEntries;
    private int currentIndex;

    private ImageView imageView;
    private TextView dateTextView;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_activity);

        imageView = findViewById(R.id.viewImageView);
        dateTextView = findViewById(R.id.viewDateText);
        descriptionTextView = findViewById(R.id.viewDescriptionText);
        Button buttonPrevious = findViewById(R.id.previousButton);
        Button buttonNext = findViewById(R.id.nextButton);

        loadAllEntries();

        buttonPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayEntry(currentIndex);
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (currentIndex < photoEntries.size() - 1) {
                currentIndex++;
                displayEntry(currentIndex);
            }
        });
    }

    private void loadAllEntries() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "reclaim-database").build();
            photoEntries = db.progressEntryDao().getAllEntries();
            runOnUiThread(() -> {
                if (!photoEntries.isEmpty()) {
                    displayEntry(0);
                }
            });
        }).start();
    }

    private void displayEntry(int index) {
        if (index >= 0 && index < photoEntries.size()) {
            ProgressEntry entry = photoEntries.get(index);
            if (entry.getPhoto() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(entry.getPhoto(), 0, entry.getPhoto().length);
                imageView.setImageBitmap(bitmap);
            }
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(entry.getDate());
            dateTextView.setText(formattedDate);
            descriptionTextView.setText(entry.getDescription());
        }
    }
}
