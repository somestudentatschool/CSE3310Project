package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ViewProfileActivity extends AppCompatActivity {

    TextView userProfileHeader, fullNameText, dateOfBirthText, userIdText, emailText;
    Button signOutButton, openHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        setTitle("Animal Identifier: My Profile");

        userProfileHeader = findViewById(R.id.userProfileHeaderText);
        fullNameText = findViewById(R.id.userFullNameText);
        dateOfBirthText = findViewById(R.id.userDateOfBirthText);
        userIdText = findViewById(R.id.userIdText);
        emailText = findViewById(R.id.emailText);
        signOutButton = findViewById(R.id.signOutButton);
        openHomeButton = findViewById(R.id.homeButton);
        signOutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        });

        openHomeButton.setOnClickListener(view -> {
            Intent i = new Intent(ViewProfileActivity.this, HomeActivity.class);
            startActivity(i);
        });

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            String name = extra.getString("name");
            String email = extra.getString("email");
            userProfileHeader.setText(String.format("%s's Profile", name));
            fullNameText.setText(name);
            emailText.setText(email);
        }
    }
}