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
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_activity);

        imageView = findViewById(R.id.viewImageView);
        dateTextView = findViewById(R.id.viewDateText);
        descriptionTextView = findViewById(R.id.viewDescriptionText);
        Button buttonPrevious = findViewById(R.id.previousButton);
        Button buttonNext = findViewById(R.id.nextButton);

        loadAllEntries();//loads all entries from database

        buttonPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {//checks if there is a previous entry
                currentIndex--;//decrements
                displayEntry(currentIndex);//dispalys entry
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (currentIndex < photoEntries.size() - 1) {//check if there is a next entry
                currentIndex++;//Increments
                displayEntry(currentIndex);//displays entry
            }
        });
    }

    private void loadAllEntries() {
        new Thread(() -> {
            photoEntries = database.progressEntryDao().getAllEntries();
            runOnUiThread(() -> {
                if (!photoEntries.isEmpty()) {
                    displayEntry(0);//displays first entry brought on intent if empty
                }
            });
        }).start();
    }

    private void displayEntry(int index) {
        if (index >= 0 && index < photoEntries.size()) {//checks if index is valid
            ProgressEntry entry = photoEntries.get(index);//gets entry from list
            if (entry.getPhoto() != null) {
                //REFERENCE https://syntaxfix.com/question/18791/android-set-bitmap-to-imageview
                //converts byte array to bitmap and displays in imageview
                Bitmap bitmap = BitmapFactory.decodeByteArray(entry.getPhoto(), 0, entry.getPhoto().length);
                imageView.setImageBitmap(bitmap);
                //reference complete
            }
            //REFERENCE: https://stackoverflow.com/questions/18480633/java-util-date-format-conversion-yyyy-mm-dd-to-mm-dd-yyyy
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(entry.getDate());//formats date
            //reference complete
            dateTextView.setText(formattedDate);
            descriptionTextView.setText(entry.getDescription());
        }
    }
    private AppDatabase getAppDatabase() {
        return AppDatabase.getInstance(getApplicationContext());
    }
}
