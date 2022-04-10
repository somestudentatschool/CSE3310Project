package com.example.cse3310project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    TextView userProfileHeader, fullNameText, dateOfBirthText, userIdText, emailText;
    Button signOutButton;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FragmentActivity activity = requireActivity();

        if(currentUser == null) {
            Intent i = new Intent(activity, LoginActivity.class);
            Toast.makeText(activity, "Not logged in", Toast.LENGTH_SHORT).show();
            startActivity(i);
            requireActivity().finish();
            return;
        }

        userProfileHeader = view.findViewById(R.id.userProfileHeaderText);
        fullNameText = view.findViewById(R.id.userFullNameText);
        dateOfBirthText = view.findViewById(R.id.userDateOfBirthText);
        userIdText = view.findViewById(R.id.userIdText);
        emailText = view.findViewById(R.id.emailText);
        signOutButton = view.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(activity, LoginActivity.class);
            startActivity(i);
            Toast.makeText(activity, "Signed out", Toast.LENGTH_SHORT).show();
            activity.finish();
        });

        String email = mAuth.getCurrentUser().getEmail();
        // TODO: Temporary, for now we're just setting the users name as the characters before the @ in their email.
        String name = email.split("@")[0];
        userProfileHeader.setText(String.format("%s's Profile", name));
        fullNameText.setText(name);
        emailText.setText(email);
    }
}
