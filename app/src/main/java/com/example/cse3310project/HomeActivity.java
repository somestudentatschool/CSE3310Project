package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private Bundle results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Animal Identifier: Home");

        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(navListener);

        selectFragment(new HomeFragment());


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent i = new Intent(this, LoginActivity.class);
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            startActivity(i);
            finish();
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String home_username = bundle.getString("username");
            String home_password = bundle.getString("password");
            String home_email = bundle.getString("email");
            String home_fullname = bundle.getString("fullname");
            String home_dob = bundle.getString("dob");

            ProfileFragment fragment = new ProfileFragment();
            fragment.setProf_username(home_username);
            fragment.setProf_password(home_password);
            fragment.setProf_email(home_email);
            fragment.setProf_fullname(home_fullname);
            fragment.setProf_dob(home_dob);

        }
        else{
            Toast.makeText(this, "Error transferring DB data", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
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