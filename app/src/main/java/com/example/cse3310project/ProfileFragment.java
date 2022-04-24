package com.example.cse3310project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    TextView userProfileHeader, fullNameText, dateOfBirthText, userText, emailText, passText;
    public static String prof_username, prof_password, prof_email, prof_dob, prof_fullname = null;
    Button signOutButton, updateButton;
    private ProgressBar loadingBar;
    private ImageView profilePic, profileButton;
    private Uri prof_imageuri;
    final Calendar myCalendar= Calendar.getInstance();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");
    StorageReference strroot = FirebaseStorage.getInstance().getReference();

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
        dateOfBirthText = view.findViewById(R.id.userDateOfBirthText);
        profilePic = view.findViewById(R.id.profile_image);
        profileButton = view.findViewById(R.id.profile_button);

        DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            modifyDob();
        };
        dateOfBirthText.setOnClickListener(view12 -> new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        showProfileData();

        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(activity, LoginActivity.class);
            startActivity(i);
            Toast.makeText(activity, "Signed out", Toast.LENGTH_SHORT).show();
            activity.finish();
        });

        showProfileData();
    }

    private void showProfileData() {

        userProfileHeader.setText(prof_username);
        fullNameText.setText(prof_fullname);
        dateOfBirthText.setText(prof_dob);
        userText.setText(prof_username);
        emailText.setText(prof_email);
        passText.setText(prof_password);

        updateButton.setOnClickListener(view -> updateProfileData());
    }


    public void updateProfileData() {

        if(isModified()){
            Toast.makeText(getActivity(), "Profile data has been updated.", Toast.LENGTH_SHORT).show();
        }

        /*else if(isEmailModified()){
            Toast.makeText(getActivity(), "Profile data has been updated.", Toast.LENGTH_SHORT).show();
            //not currently working
        }*/
        //password can be changed through login screen
    }

    private boolean isModified(){
        final boolean[] mod = {false};
            root.orderByChild("email").equalTo(prof_email).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String currentEmail = ds.child("email").getValue(String.class);
                            //String currentPass = ds.child("password").getValue(String.class);
                            //String currentEmail = ds.child("email").getValue(String.class);
                            if (currentEmail != null) {
                                if (currentEmail.equals(prof_email)) {
                                    ds.child("username").getRef().setValue(userText.getText().toString());
                                    ds.child("fullname").getRef().setValue(fullNameText.getText().toString());
                                    userProfileHeader.setText(userText.getText().toString());
                                    userText.setText(userText.getText().toString());
                                    fullNameText.setText(fullNameText.getText().toString());
                                    setProf_username(userText.getText().toString());
                                    setProf_fullname(fullNameText.getText().toString());
                                    SharedPreferences mPrefs = getActivity().getSharedPreferences("NamePref", 0);
                                    SharedPreferences.Editor mEditor = mPrefs.edit();
                                    mEditor.putString("HomeName", fullNameText.getText().toString()).apply();
                                    mod[0] = true;
                                    Toast.makeText(getActivity(), "Profile data has been updated.", Toast.LENGTH_SHORT).show();
                                    /*update profile data for username and name one at a time, must press update button to save changes
                                      or a crash could occur*/
                                }
                                else {
                                    //current user does not match FBRTDB
                                    Toast.makeText(getActivity(), "Could not find user ", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                    else {
                        //snapshot does not exist
                        Toast.makeText(getActivity(), "Snapshot of DB does not exist(username) ", Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error accessing Realtime Database ", Toast.LENGTH_LONG).show();
                }
            });


        return mod[0];
    }

    private void modifyDob(){

        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateOfBirthText.setText(dateFormat.format(myCalendar.getTime()));
        //use datepicker to choose value, then register it in RTDB
            root.orderByChild("username").equalTo(prof_username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String currentEmail = ds.child("email").getValue(String.class);

                            if (currentEmail != null) {
                                if (currentEmail.equals(prof_email)) {
                                    ds.child("dob").getRef().setValue(dateOfBirthText.getText().toString());
                                    dateOfBirthText.setText(dateOfBirthText.getText().toString());
                                    setProf_dob(dateOfBirthText.getText().toString());
                                } else {
                                    //current user does not match FBRTDB
                                    Toast.makeText(getActivity(), "Could not find user ", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                    else {
                        //snapshot does not exist, shows up when update profile has
                        //Toast.makeText(getActivity(), "Snapshot of DB does not exist(dob) ", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error accessing Realtime Database ", Toast.LENGTH_LONG).show();
                }
            });

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
    public void setProf_imageuri(Uri imageUri){ prof_imageuri = imageUri;}

}

