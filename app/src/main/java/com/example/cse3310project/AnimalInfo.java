package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

public class AnimalInfo extends AppCompatActivity {
    Button btn_goAnimalData;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_data);

        btn_goAnimalData = findViewById(R.id.btn_goAnimalData);

        btn_goAnimalData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(AnimalActivity.this, "Viewed", Toast.LENGTH_SHORT).show();
                openAnimalActivity();
            }
        });

    }

    private void openAnimalActivity(){
        Intent intent = new Intent(this, AnimalActivity.class);
        startActivity(intent);
    }
}
