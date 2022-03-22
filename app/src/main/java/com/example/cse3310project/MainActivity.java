package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Animal Identifier");

        // The boolean "loggedIn" is a placeholder. Will likely need to check with
        // a backend class to see if we are logged in
        Intent i;
        if(!LoginActivity.loggedIn) {
            // Switch to login activity
            i = new Intent(this, LoginActivity.class);
        } else {
            // Goto home page
            i = new Intent(this, HomeActivity.class);
        }
        startActivity(i);
        this.finish();

    }
}