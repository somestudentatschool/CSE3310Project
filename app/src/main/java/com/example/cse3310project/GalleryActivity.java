package com.example.cse3310project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setTitle("Camera");
    }
}
