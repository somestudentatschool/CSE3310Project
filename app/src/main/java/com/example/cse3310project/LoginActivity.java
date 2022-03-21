package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public static boolean loggedIn;
    private TextInputEditText userNameEdit, passwordEdit;
    public Button loginButton;
    private ProgressBar loadingBar;
    public TextView registerTV;
    private FirebaseAuth mAuth;
    public TextView forgotpassTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEdit = findViewById(R.id.idEditUserName);
        passwordEdit = findViewById(R.id.idEditPassword);
        loginButton = findViewById(R.id.idLoginButton);
        loadingBar = findViewById(R.id.idProgressBarLoad);
        registerTV = findViewById(R.id.idTVRegister);
        forgotpassTV = findViewById(R.id.idTVforgotPassword);
        loggedIn = false;
        mAuth = FirebaseAuth.getInstance();
        //setTitle("Animal Identifier: Login");

        registerTV.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            //if registration button is clicked, move to registration class
            startActivity(i);
        });

        forgotpassTV.setOnClickListener(v -> {
            //To-Do: implement dialog box to enter email and send password to email from Firebase
        });

        loginButton.setOnClickListener(view -> {

            loadingBar.setVisibility(View.VISIBLE);
            String userName = Objects.requireNonNull(userNameEdit.getText()).toString();
            String password = Objects.requireNonNull(passwordEdit.getText()).toString();

            if(TextUtils.isEmpty(userName) && TextUtils.isEmpty(password)){
                Toast.makeText(LoginActivity.this, "Enter your information", Toast.LENGTH_SHORT).show();
                //if username or password is empty, toast message to enter info
            }
            else {
                mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        loadingBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        //if sign in with firebase is correct, notify user login is successful and go to homepage class
                        loggedIn = true;
                        startActivity(i);
                        finish();
                    }
                    else{
                        loadingBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Login Failed"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            this.finish();
        }
    }
}