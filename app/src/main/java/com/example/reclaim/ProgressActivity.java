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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        editTextPhotoDescription = findViewById(R.id.editTextPhotoDescription);
        buttonCapturePhoto = findViewById(R.id.buttonCapturePhoto);

        buttonCapturePhoto.setOnClickListener(view -> dispatchTakePictureIntent());
        buttonNextPhoto = findViewById(R.id.buttonNextPhoto);
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
        }
    }
    }


