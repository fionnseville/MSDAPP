package com.example.reclaim;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;
import android.Manifest;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ProgressActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 2;
    private ImageView imageViewPhoto;
    private EditText editTextPhotoDescription;
    private Button buttonCapturePhoto;
    private Button buttonNextPhoto;
    private List<ProgressEntry> photoEntries;
    private int currentPhotoIndex = 0;
    private Button buttonSubmit;
    private byte[] currentPhoto;
    private String currentDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        editTextPhotoDescription = findViewById(R.id.editTextPhotoDescription);
        buttonCapturePhoto = findViewById(R.id.buttonCapturePhoto);

        buttonCapturePhoto.setOnClickListener(view -> dispatchTakePictureIntent());
        buttonNextPhoto = findViewById(R.id.buttonNextPhoto);

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(view -> submitEntry());

        buttonNextPhoto.setOnClickListener(view -> openViewProgressActivity());

        loadPhotosFromDatabase();
}

    private void submitEntry() {
        // Get the photo description
        String description = editTextPhotoDescription.getText().toString().trim();

        // Check if the description is empty
        if (description.isEmpty()) {
            Toast.makeText(this, "Please include a description", Toast.LENGTH_SHORT).show();
            return; // Exit the method if description is empty
        }

        // Check if the currentPhoto is null
        if (currentPhoto == null) {
            Toast.makeText(this, "Please capture a photo", Toast.LENGTH_SHORT).show();
            return; // Exit the method if the photo is null
        }

        // Save the entry to the database
        new Thread(() -> {
            ProgressEntry entry = new ProgressEntry(currentPhoto, description, new Date());
            getAppDatabase().progressEntryDao().insert(entry);
            runOnUiThread(() -> {
                Toast.makeText(this, "Entry submitted successfully", Toast.LENGTH_SHORT).show();
                clearFields(); // Clear fields after successful submission
            });
        }).start();
    }

    private void clearFields() {
        imageViewPhoto.setImageBitmap(null); // Clear the image view
        editTextPhotoDescription.setText(""); // Clear the description field
        currentPhoto = null; // Reset the current photo
    }


    private void loadPhotosFromDatabase() {
        new Thread(() -> {
            photoEntries = getAppDatabase().progressEntryDao().getAllEntries();
        }).start();
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, REQUEST_PERMISSION_CAMERA);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // Handle the exception
        }
    }
    private void openViewProgressActivity() {
        if (!photoEntries.isEmpty()) {
            ProgressEntry currentEntry = photoEntries.get(currentPhotoIndex);

            Intent intent = new Intent(this, ViewProgressActivity.class);
            intent.putExtra("image", currentEntry.getPhoto());
            intent.putExtra("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentEntry.getDate()));
            intent.putExtra("description", currentEntry.getDescription());
            startActivity(intent);

            currentPhotoIndex = (currentPhotoIndex + 1) % photoEntries.size();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewPhoto.setImageBitmap(imageBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            currentPhoto = stream.toByteArray();
            currentDescription = editTextPhotoDescription.getText().toString();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                //add toast later on****
            }
        }
    }

    private AppDatabase getAppDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "reclaim-database")
                .fallbackToDestructiveMigration()
                .build();
    }
}
