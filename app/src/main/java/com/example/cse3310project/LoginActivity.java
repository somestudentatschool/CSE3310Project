package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    // TODO: Temporary
    static boolean loggedIn = false;

    Button loginButton, registerButton;
    EditText emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Animal Identifier: Login");

        emailText = findViewById(R.id.loginEmailText);
        passwordText = findViewById(R.id.loginPasswordText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            String email = extra.getString("email");
            emailText.setText(email);
        }

        loginButton.setOnClickListener(view -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

            // TODO: Temporary
            Toast.makeText(this, String.format("%s %s", email, password), Toast.LENGTH_SHORT).show();

            // Handle checking email and password with dedicated class
            // Switch to home activity upon successful login

            // TODO: Temporary
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra("email", email);
            String name = email.split("@")[0]; // Should pull user's name from database instead
            i.putExtra("name", name);
            startActivity(i);
            this.finish();
        });

        registerButton.setOnClickListener(view -> {
            String email = emailText.getText().toString();

            // Switch to register page
            Intent i = new Intent(this, RegisterActivity.class);
            // Send email, assuming one was entered, to register activity
            i.putExtra("email", email);
            startActivity(i);
        });
    }
}