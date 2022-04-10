package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Animal Identifier: Home");

        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(navListener);

        selectFragment(new HomeFragment());


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent i = new Intent(this, LoginActivity.class);
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            startActivity(i);
            finish();
        }
    }

    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selected;
        switch(item.getItemId()) {
            case R.id.nav_profile:
                selected = new ProfileFragment();
                break;
            case R.id.nav_home:
            default:
                selected = new HomeFragment();
                break;
            // Can add more pages in the future
        }

        selectFragment(selected);
        return true;
    };

    private void selectFragment(Fragment selected) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
    }
}