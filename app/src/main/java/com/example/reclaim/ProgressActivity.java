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
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ProgressActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 2;
    private ImageView imageViewPhoto;
    private EditText editTextPhotoDescription;
    private Button buttonCapturePhoto;
    private Button buttonNextPhoto;
    private List<ProgressEntry> photoEntries;
    private int currentPhotoIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        editTextPhotoDescription = findViewById(R.id.editTextPhotoDescription);
        buttonCapturePhoto = findViewById(R.id.buttonCapturePhoto);

        buttonCapturePhoto.setOnClickListener(view -> dispatchTakePictureIntent());
        buttonNextPhoto = findViewById(R.id.buttonNextPhoto);
        photoEntries = new ArrayList<>();

        buttonNextPhoto.setOnClickListener(view -> displayNextPhoto());

        loadPhotosFromDatabase();
}
    private void loadPhotosFromDatabase() {
        new Thread(() -> {
            photoEntries = getAppDatabase().progressEntryDao().getAllEntries();
        }).start();
    }

    private void displayNextPhoto() {
        if (!photoEntries.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoEntries.get(currentPhotoIndex).getPhoto(), 0, photoEntries.get(currentPhotoIndex).getPhoto().length);
            imageViewPhoto.setImageBitmap(bitmap);

            currentPhotoIndex = (currentPhotoIndex + 1) % photoEntries.size();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewPhoto.setImageBitmap(imageBitmap);

            // Convert Bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Get description from EditText
            String description = editTextPhotoDescription.getText().toString();

            // Insert into database
            new Thread(() -> {
                ProgressEntry entry = new ProgressEntry(byteArray, description);
                getAppDatabase().progressEntryDao().insert(entry);
            }).start();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                // Permission was denied. Inform the user that the permission is necessary.
            }
        }
    }
    private AppDatabase getAppDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "reclaim-database").build();
    }
}
