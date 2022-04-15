package com.example.cse3310project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    TextView userProfileHeader, fullNameText, dateOfBirthText, userText, emailText, passText;
    public static String prof_username, prof_password, prof_email, prof_dob, prof_fullname = null;
    Button signOutButton, updateButton;
    private ProgressBar loadingBar;
    final Calendar myCalendar= Calendar.getInstance();
    TextInputEditText editText;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FragmentActivity activity = requireActivity();

        dateOfBirthText = view.findViewById(R.id.userDateOfBirthText);
        DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            modifyDob();
        };
        editText.setOnClickListener(view12 -> new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        //loadingBar = (ProgressBar) findViewById(R.id.idProgressBarProf); add loading bar later

        if (currentUser == null) {
            Intent i = new Intent(activity, LoginActivity.class);
            Toast.makeText(activity, "Not logged in", Toast.LENGTH_SHORT).show();
            startActivity(i);
            requireActivity().finish();
            return;
        }

        userProfileHeader = view.findViewById(R.id.userProfileHeaderText);
        fullNameText = view.findViewById(R.id.userFullNameText);
        userText = view.findViewById(R.id.userNameText);
        emailText = view.findViewById(R.id.emailText);
        passText = view.findViewById(R.id.passText);
        signOutButton = view.findViewById(R.id.signOutButton);
        updateButton = view.findViewById(R.id.updateButton);
        showProfileData();

        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(activity, LoginActivity.class);
            startActivity(i);
            Toast.makeText(activity, "Signed out", Toast.LENGTH_SHORT).show();
            activity.finish();
        });

        showProfileData();
        //String email = mAuth.getCurrentUser().getEmail();
        //get data from DB here
        //String name = email.split("@")[0];
    }

    private void showProfileData() {

        System.out.println("Prof_username is null");

        userProfileHeader.setText(prof_username);
        fullNameText.setText(prof_fullname);
        dateOfBirthText.setText(prof_dob);
        userText.setText(prof_username);
        emailText.setText(prof_email);
        passText.setText(prof_password);

        updateButton.setOnClickListener(view -> updateProfileData());
    }


    public void updateProfileData() {
        if (isUserNameModified()) {
            Toast.makeText(getActivity(), "Profile data has been updated.", Toast.LENGTH_SHORT).show();
        }
        else if(isFullNameModified()){
            Toast.makeText(getActivity(), "Profile data has been updated.", Toast.LENGTH_SHORT).show();
        }
        /*else if(isEmailModified()){
            Toast.makeText(getActivity(), "Profile data has been updated.", Toast.LENGTH_SHORT).show();
            //not currently working
        }*/
        //password can be changed through login screen
        else{
            Toast.makeText(getActivity(), "Profile data cannot be updated.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUserNameModified() {
        final boolean[] mod = {false};
        if (!prof_username.equals(userText.getText().toString())){
            root.orderByChild("password").equalTo(prof_password).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String currentPass = ds.child("password").getValue(String.class);

                            if (currentPass != null) {
                                if (currentPass.equals(prof_password)) {
                                    ds.child("username").getRef().setValue(userText.getText().toString());
                                    mod[0] = true;
                                    userProfileHeader.setText(userText.getText().toString());
                                    userText.setText(userText.getText().toString());
                                } else {
                                    //current user does not match FBRTDB
                                    Toast.makeText(getActivity(), "Could not find user ", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                    else {
                        //snapshot does not exist
                        Toast.makeText(getActivity(), "Snapshot of DB does not exist ", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error accessing Realtime Database ", Toast.LENGTH_LONG).show();
                }
            });
        }
        return mod[0];
    }

    public boolean isFullNameModified(){
        final boolean[] mod = {false};
        if (!prof_fullname.equals(fullNameText.getText().toString())){
            root.orderByChild("username").equalTo(prof_username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String currentUser = ds.child("username").getValue(String.class);

                            if (currentUser != null) {
                                if (currentUser.equals(prof_username)) {
                                    ds.child("fullname").getRef().setValue(fullNameText.getText().toString());
                                    mod[0] = true;
                                    fullNameText.setText(fullNameText.getText().toString());
                                } else {
                                    //current user does not match FBRTDB
                                    Toast.makeText(getActivity(), "Could not find user ", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                    else {
                        //snapshot does not exist
                        Toast.makeText(getActivity(), "Snapshot of DB does not exist ", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error accessing Realtime Database ", Toast.LENGTH_LONG).show();
                }
            });
        }
        return mod[0];
    }

    private void modifyDob(){

        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
        //use datepicker to choose value, then register it in RTDB

        if (!prof_dob.equals(dateOfBirthText.getText().toString())){
            root.orderByChild("username").equalTo(prof_username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String currentUser = ds.child("username").getValue(String.class);

                            if (currentUser != null) {
                                if (currentUser.equals(prof_username)) {
                                    ds.child("dob").getRef().setValue(dateOfBirthText.getText().toString());
                                    dateOfBirthText.setText(dateOfBirthText.getText().toString());
                                } else {
                                    //current user does not match FBRTDB
                                    Toast.makeText(getActivity(), "Could not find user ", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                    else {
                        //snapshot does not exist
                        Toast.makeText(getActivity(), "Snapshot of DB does not exist ", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error accessing Realtime Database ", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public boolean isEmailModified(){
        //work in progress
        return false;
    }

    public String getProf_username(){
        return prof_username;
    }
    public void setProf_username(String str){
        prof_username = str;
    }

    public void setProf_password(String str){
        prof_password = str;
    }
    public void setProf_email(String str){
        prof_email = str;
    }
    public void setProf_dob(String str){
        prof_dob = str;
    }
    public void setProf_fullname(String str){
        prof_fullname = str;
    }

        /*@Override
    public void onDestroy(){
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
        To be implemented, currently when a user swipes out their info in firebase database
        is reset and their DB info cannot be accessed*/

}

