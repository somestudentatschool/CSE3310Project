package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button register;
    EditText emailText, passwordText, firstNameText, lastNameText, birthdateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Animal Identifier: Register");

        register = findViewById(R.id.registerSubmitButton);
        emailText = findViewById(R.id.registerEmailText);
        passwordText = findViewById(R.id.registerPasswordText);
        firstNameText = findViewById(R.id.firstNameText);
        lastNameText = findViewById(R.id.lastNameText);
        birthdateText = findViewById(R.id.birthdateText);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            String email = extra.getString("email");
            emailText.setText(email);
        }

        register.setOnClickListener(view -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            String birthdate = birthdateText.getText().toString();

            // TODO: Temporary
            Toast.makeText(this, String.format("%s %s %s", email, password, birthdate), Toast.LENGTH_SHORT).show();

            // Attempt to create a new account, using dedicated class
            // Go through email verification, maybe through different activity
            // Return to login page upon success. Finish current activity
            Intent i = new Intent(this, LoginActivity.class);
            // Send email back to login page
            i.putExtra("email", email);
            startActivity(i);
            this.finish();
        });
    }
}