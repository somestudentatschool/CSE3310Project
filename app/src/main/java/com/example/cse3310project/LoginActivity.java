package com.example.cse3310project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public static boolean loggedIn;
    private TextInputLayout usernameLayout, passwordLayout;
    public Button loginButton;
    public TextView registerTV, forgotpassTV;
    private ProgressBar loadingBar;
    private FirebaseAuth mAuth;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CSE3310Project);
        setContentView(R.layout.activity_login);

        TextInputEditText userEdit = findViewById(R.id.idEditUser);
        TextInputEditText passwordEdit = findViewById(R.id.idEditPassword);
        loginButton = findViewById(R.id.idLoginButton);
        registerTV = findViewById(R.id.idTVRegister);
        forgotpassTV = findViewById(R.id.idTVforgotPassword);
        loadingBar = findViewById(R.id.idProgressBarLoad);
        usernameLayout = findViewById(R.id.idTempUser);
        passwordLayout = findViewById(R.id.idTempPassword);
        loggedIn = false;
        mAuth = FirebaseAuth.getInstance();

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            userEdit.setText(extra.getString("username"));
        }

        forgotpassTV.setOnClickListener(view -> {
            EditText resetMail = new EditText(view.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
            passwordResetDialog.setTitle("Reset Password?");
            passwordResetDialog.setMessage("Enter your email to reset your password.");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton(Html.fromHtml("<font color='#53FF33'>Send</font>"), (dialogInterface, i) -> {
                //parse email and send reset link to email
                String email = resetMail.getText().toString();
                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> {

                }).addOnFailureListener(e -> {

                });
                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> Toast.makeText(LoginActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Password reset link failed..." + e.getMessage(), Toast.LENGTH_SHORT).show());

            });

            passwordResetDialog.setNegativeButton(Html.fromHtml("<font color='#53FF33'>Exit</font>"), (dialogInterface, i) -> {
                //close dialog and don't send link
            });
            passwordResetDialog.create().show();
        });

        registerTV.setOnClickListener(view -> {
            loadingBar.setVisibility(View.VISIBLE);
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            //if registration button is clicked, move to registration class
            startActivity(i);
            loadingBar.setVisibility(View.INVISIBLE);
        });

        loginButton.setOnClickListener(view -> {

                    //display loading bar when logging while authorizing
                    loadingBar.setVisibility(View.VISIBLE);
                    String username = Objects.requireNonNull(usernameLayout.getEditText()).getText().toString().trim();
                    String password = Objects.requireNonNull(passwordLayout.getEditText()).getText().toString().trim();

                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                        Toast.makeText(LoginActivity.this, "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
                        //if username or password is empty, toast message to enter info
                        loadingBar.setVisibility(View.INVISIBLE);
                    }
                    else {
                        root.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String currentUser = ds.child("username").getValue(String.class);
                                        String currentPass = ds.child("password").getValue(String.class);

                                        if (currentUser != null) {
                                            if (currentPass != null) {
                                                if(currentUser.equals(username) && currentPass.equals(password)){
                                                    String currentEmail = ds.child("email").getValue(String.class);
                                                    String currentName = ds.child("fullname").getValue(String.class);
                                                    String currentDob = ds.child("dob").getValue(String.class);
                                                    String currentImageurl = ds.child("imageurl").getValue(String.class);
                                                    if (currentEmail != null) {
                                                        mAuth.signInWithEmailAndPassword(currentEmail, currentPass).addOnCompleteListener(task -> {
                                                            if(task.isSuccessful()){
                                                                loadingBar.setVisibility(View.INVISIBLE);
                                                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                                                //if sign in with firebase is correct, notify user login is successful and go to homepage class
                                                                i.putExtra("username", currentUser);
                                                                i.putExtra("password", currentPass);
                                                                i.putExtra("email", currentEmail);
                                                                i.putExtra("fullname", currentName);
                                                                i.putExtra("dob", currentDob);
                                                                i.putExtra("imageurl", currentImageurl);
                                                                loggedIn = true;
                                                                startActivity(i);
                                                                finish();
                                                            }
                                                            else{
                                                                loadingBar.setVisibility(View.INVISIBLE);
                                                                Toast.makeText(LoginActivity.this, "Login Failed, task failed"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    }
                                                }
                                                else{
                                                    //current user does not match FBRTDB
                                                    loadingBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(LoginActivity.this, "Login Failed, current user not matched", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }

                                    }
                                }
                                else{
                                    //snapshot does not exist
                                    loadingBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this, "Login Failed, snapshot error", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginActivity.this, "Login Failed, database error ", Toast.LENGTH_LONG).show();
                                loadingBar.setVisibility(View.INVISIBLE);
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