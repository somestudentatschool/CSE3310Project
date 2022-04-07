package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private TextView helloTextView;
    private Button uploadFromGalleryButton, uploadFromCameraButton, openProfileButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Animal Identifier: Home");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent i = new Intent(this, LoginActivity.class);
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            startActivity(i);
            finish();
        }

        helloTextView = findViewById(R.id.helloTextView);
        uploadFromGalleryButton = findViewById(R.id.uploadFromGalleryButton);
        uploadFromCameraButton = findViewById(R.id.uploadFromCameraButton);
        openProfileButton = findViewById(R.id.openProfileButton);

        Bundle extra = getIntent().getExtras();
        final String email = currentUser.getEmail();
        final String name = email.split("@")[0];
        helloTextView.setText(String.format("Hello, %s!", name));

        uploadFromCameraButton.setOnClickListener(view -> {
            // Open camera activity
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(this, CameraActivity.class);
                startActivity(i);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });

        uploadFromGalleryButton.setOnClickListener(view -> {
            // Open gallery activity
            Intent i = new Intent(this, GalleryActivity.class);
            startActivity(i);
        });

        openProfileButton.setOnClickListener(view -> {
            Intent i = new Intent(this, ViewProfileActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100) {
            if(!Arrays.asList(grantResults).contains(PackageManager.PERMISSION_DENIED)) {
                Intent i = new Intent(this, CameraActivity.class);
                startActivity(i);
            }
        }
    }
}