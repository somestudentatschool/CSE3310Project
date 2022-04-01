package com.example.cse3310project;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AnimalActivity extends AppCompatActivity {
    Button btn_addAnimal, btn_viewAllAnimals, btn_viewAnimal;
    EditText aType, aBreed, aDescription;
    ListView lv_animalList;

    ArrayAdapter<AnimalModel> animalArrayAdapter;
    AnimalDataHelper animalDataHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_animal);


    btn_addAnimal = findViewById(R.id.btn_addAnimal);
    btn_viewAllAnimals = findViewById(R.id.btn_viewAllAnimals);
    btn_viewAnimal = findViewById(R.id.btn_viewAnimal);
    aType = findViewById(R.id.aType);
    aBreed = findViewById(R.id.aBreed);
    aDescription = findViewById(R.id.aDescription);
    lv_animalList = findViewById(R.id.lv_animalList);

    animalDataHelper = new AnimalDataHelper(AnimalActivity.this);

        ShowAllAnimalView(animalDataHelper);

        btn_addAnimal.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            AnimalModel animalModel;

            try {
                animalModel = new AnimalModel(-1, aType.getText().toString(), aBreed.getText().toString(), aDescription.getText().toString());
                Toast.makeText(AnimalActivity.this, animalModel.toString(), Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                Toast.makeText(AnimalActivity.this, "Error can't add", Toast.LENGTH_SHORT).show();
                animalModel = new AnimalModel(-1, "error", "error", "error");
            }

            AnimalDataHelper animalDataHelper = new AnimalDataHelper(AnimalActivity.this);

            boolean success = animalDataHelper.addOne(animalModel);
            Toast.makeText(AnimalActivity.this, "Success " + success, Toast.LENGTH_SHORT).show();

            ShowAllAnimalView(animalDataHelper);
        }
    });

    btn_viewAnimal.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){



            //Toast.makeText(AnimalActivity.this, dAnimal.toString(), Toast.LENGTH_SHORT).show();
            openAnimalData();
            }
    });

    btn_viewAllAnimals.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){

            animalDataHelper = new AnimalDataHelper(AnimalActivity.this);

            ShowAllAnimalView(animalDataHelper);

            //Toast.makeText(AnimalActivity.this, all.toString(), Toast.LENGTH_SHORT).show();
        }
    });

    lv_animalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            AnimalModel animalClicked = (AnimalModel) adapterView.getItemAtPosition(i);
            animalDataHelper.deleteOne(animalClicked);
            ShowAllAnimalView(animalDataHelper);
            Toast.makeText(AnimalActivity.this, "Deleted " + animalClicked.toString(), Toast.LENGTH_SHORT).show();
        }
    });


    }

    private void ShowAllAnimalView(AnimalDataHelper animalDataHelper2) {
        animalArrayAdapter = new ArrayAdapter<AnimalModel>(AnimalActivity.this, android.R.layout.simple_list_item_1, animalDataHelper2.getAllAnimals());
        lv_animalList.setAdapter(animalArrayAdapter);
    }

    private void openAnimalData(){
        Intent intent = new Intent(this, AnimalInfo.class);
        startActivity(intent);
    }
}
