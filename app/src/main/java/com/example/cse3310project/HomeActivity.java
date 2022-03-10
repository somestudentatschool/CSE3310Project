package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    TextView helloTextView;
    Button uploadFromGalleryButton, uploadFromCameraButton, openProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Animal Identifier: Home");

        helloTextView = findViewById(R.id.helloTextView);
        uploadFromGalleryButton = findViewById(R.id.uploadFromGalleryButton);
        uploadFromCameraButton = findViewById(R.id.uploadFromCameraButton);
        openProfileButton = findViewById(R.id.openProfileButton);

        Bundle extra = getIntent().getExtras();
        String name = "name_here", email = "email_here";
        if(extra != null) {

            String newName = extra.getString("name");
            String newEmail = extra.getString("email");
            if(!newName.equals(""))
                name = newName;
            if(!newEmail.equals(""))
                email = newEmail;

            helloTextView.setText(String.format("Hello, %s! (%s)", name, email));
        }

        uploadFromCameraButton.setOnClickListener(view -> {
            // Open camera, get image uri, save to temporary file
            Toast.makeText(this, "TODO: Implement this", Toast.LENGTH_SHORT).show();
        });

        uploadFromGalleryButton.setOnClickListener(view -> {
            // Open gallery, read image data
            Toast.makeText(this, "TODO: Implement this", Toast.LENGTH_SHORT).show();
        });

        final String finalName = name;
        final String finalEmail = email;
        openProfileButton.setOnClickListener(view -> {
            Intent i = new Intent(this, ViewProfileActivity.class);
            i.putExtra("name", finalName);
            i.putExtra("email", finalEmail);
            startActivity(i);
        });
    }
}