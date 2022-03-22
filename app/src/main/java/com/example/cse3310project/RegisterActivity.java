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
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText EmailEdit, PasswordEdit, ConfirmPassword;
    public Button registerButton;
    private ProgressBar loadingBar;
    public TextView loginTV;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailEdit = findViewById(R.id.idEditEmail);
        PasswordEdit = findViewById(R.id.idEditPassword);
        ConfirmPassword = findViewById(R.id.idEditConfirmPassword);
        registerButton = findViewById(R.id.idRegisterButton);
        loadingBar = findViewById(R.id.idProgressBarLoad);
        loginTV = findViewById(R.id.idTVLogin);
        mAuth = FirebaseAuth.getInstance();
        //setTitle("Animal Identifier: User Registration");

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            EmailEdit.setText(extra.getString("email"));
        }

        loginTV.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        registerButton.setOnClickListener(v -> {

            loadingBar.setVisibility(View.VISIBLE);
            String email = Objects.requireNonNull(EmailEdit.getText()).toString();
            String password = Objects.requireNonNull(PasswordEdit.getText()).toString();
            String confirmPassword = Objects.requireNonNull(ConfirmPassword.getText()).toString();

            if(!password.equals(confirmPassword)){
                Toast.makeText(RegisterActivity.this,"Passwords must match" ,Toast.LENGTH_SHORT).show();
                // System.out.println(password);
                // System.out.println(confirmPassword);
                //if password does not match, show toast
            }
            else if(TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmPassword)) {
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this,"Enter your information" ,Toast.LENGTH_SHORT).show();
                    //if email, password, and confirm password fields are empty, toast to prompt entry
                } else {
                    // Alert user in the case that email is not empty, but password fields are
                    Toast.makeText(RegisterActivity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        loadingBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        //if authorization with firebase is successful, register user and move to login class
                        i.putExtra("email", email);
                        startActivity(i);
                        finish();
                    }
                    else{
                        loadingBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this,"Registration Failed, " + Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                        //if authorization with firebase is not successful, notify user of fail, along with the reason
                    }
                });
            }

        });
    }
}