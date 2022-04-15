package com.example.cse3310project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AnimalInfo extends AppCompatActivity {
    Button btn_goAnimalData;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_data);

        btn_goAnimalData = findViewById(R.id.btn_goAnimalData);

        btn_goAnimalData.setOnClickListener(v -> {
            //Toast.makeText(AnimalActivity.this, "Viewed", Toast.LENGTH_SHORT).show();
            openAnimalActivity();
        });

    }

    private void openAnimalActivity(){
        Intent intent = new Intent(this, AnimalActivity.class);
        startActivity(intent);
    }
}
