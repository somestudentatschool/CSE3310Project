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

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView helloTextView = view.findViewById(R.id.helloTextView);
        Button uploadFromGalleryButton = view.findViewById(R.id.uploadFromGalleryButton);
        Button uploadFromCameraButton = view.findViewById(R.id.uploadFromCameraButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FragmentActivity activity = requireActivity();

        if(currentUser == null) {
            Intent i = new Intent(activity, LoginActivity.class);
            Toast.makeText(activity, "Not logged in", Toast.LENGTH_SHORT).show();
            startActivity(i);
            activity.finish();
        }

        ProfileFragment pf = new ProfileFragment();
        String name = pf.getProf_username();

        helloTextView.setText(String.format("Hello, %s!", name));

        uploadFromCameraButton.setOnClickListener(view2 -> {
            // Open camera activity
            Intent i = new Intent(activity, ImageUploadActivity.class);
            i.putExtra("launch", "camera");
            startActivity(i);
        });

        uploadFromGalleryButton.setOnClickListener(v -> {
            // Open gallery activity
            Intent i = new Intent(activity, ImageUploadActivity.class);
            i.putExtra("launch", "gallery");
            startActivity(i);
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
