package com.example.cse3310project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class HomeFragment extends Fragment {

    private TextView helloTextView;
    private Button uploadFromGalleryButton, uploadFromCameraButton, openProfileButton;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helloTextView = view.findViewById(R.id.helloTextView);
        uploadFromGalleryButton = view.findViewById(R.id.uploadFromGalleryButton);
        uploadFromCameraButton = view.findViewById(R.id.uploadFromCameraButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FragmentActivity activity = requireActivity();

        if(currentUser == null) {
            Intent i = new Intent(activity, LoginActivity.class);
            Toast.makeText(activity, "Not logged in", Toast.LENGTH_SHORT).show();
            startActivity(i);
            activity.finish();
        }

        final String email = currentUser.getEmail();
        final String name = email.split("@")[0];
        helloTextView.setText(String.format("Hello, %s!", name));

        uploadFromCameraButton.setOnClickListener(view2 -> {
            // Open camera activity
            Intent i = new Intent(activity, CameraActivity.class);
            startActivity(i);
        });

        uploadFromGalleryButton.setOnClickListener(v -> {
            // Open gallery activity
            Intent i = new Intent(activity, GalleryActivity.class);
            startActivity(i);
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
