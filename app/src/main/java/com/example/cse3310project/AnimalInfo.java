package com.example.cse3310project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

public class AnimalInfo extends AppCompatActivity {
    Button btn_goAnimalData;
    Button btn_goHome;
    TextView breedInfo;
    TextView typeInfo;
    TextView descInfo;
    //Cursor c = null;
    AnimalDataHelper animalDataHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_data);

        Intent intent = getIntent();
        String breedString = intent.getStringExtra(ImageUploadActivity.PACKAGE_EXTRA);
        String typeString = intent.getStringExtra(ImageUploadActivity.PACKAGE_EXTRA2);
        //String typeString = "Dog";
        //String breedString = "German Shepard";
        breedInfo = (TextView) findViewById(R.id.breedInfo);
        typeInfo = (TextView) findViewById(R.id.typeInfo);
        descInfo = (TextView) findViewById(R.id.descInfo);
        ImageView animalView = (ImageView) findViewById(R.id.animalView);



        String newBreedString = breedString.replaceAll("\\s+","").toLowerCase();
        String newDesc = "Breed info not found.";



        int resId = getResources().getIdentifier(newBreedString , "drawable", getPackageName());

        if(breedString.equalsIgnoreCase("Labrador")){
            newDesc = "-Energetic loving dogs who are highly social. \n-Fits in well with families who have children and other pets.\n" +
                    "-Labradors are great for positive reinforcement.";
        }
        if(breedString.equalsIgnoreCase("Pitbull")){
            newDesc = "-Pitbull-types have been prominent as therapy dogs, K9 police dogs and household pets." +
                    "\n-Pitbulls tend to achieve high temperament (nature through biological behavior) compared" +
                    "to other breeds.";
        }
        String a = "Golden Retriever";
        if(breedString.replaceAll("\\s+","").equalsIgnoreCase(a.replaceAll("\\s+",""))){
            newDesc = "-Devoted people loving (and most beloved) dogs who make great exercise buddies.\n" +
                    "-Golden Retrievers needs at least an hour of exercise a day, which can affect their mental health if neglected.";
        }
        a = "German Shepard";
        if(breedString.replaceAll("\\s+","").equalsIgnoreCase(a.replaceAll("\\s+",""))){
            newDesc = "-An energetic loyal dog that gets his training from outdoor exercises.\n" +
                    "-Best if the dog is not left alone too often, as their happiness lies within active owners.";
        }
        if(breedString.equalsIgnoreCase("Chihuahua")){
            newDesc = "-Loyal attention seeking lap dogs and loyal to owners when treated respectfully.\n" +
                    "-Known for barking at strangers and larger dogs, due to their small size.";
        }
        if(breedString.equalsIgnoreCase("Bombay")){
            newDesc = "";
        }
        if(breedString.equalsIgnoreCase("Sphinx")){
            newDesc = "";
        }
        if(breedString.equalsIgnoreCase("Abyssinia")){
            newDesc = "";
        }
        if(breedString.equalsIgnoreCase("Macaws")){
            newDesc = "";
        }
        if(breedString.equalsIgnoreCase("Cockatoo")){
            newDesc = "";
        }
        if(breedString.equalsIgnoreCase("Parakeet")){
            newDesc = "";
        }

        typeInfo.setText(typeString);
        breedInfo.setText(breedString);
        descInfo.setText(newDesc);
        animalView.setBackgroundResource(resId);

        /*
        AnimalDataHelper animalData = new AnimalDataHelper(AnimalInfo.this, "animal.db");


        try {
            animalData.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = animalData.loadHandler();


        c = animalData.query("AnimalTable", null, null, null, null, null, null);
        if (c.moveToNext()) {
            do {
                Toast.makeText(AnimalInfo.this,
                        "_id: " + c.getString(0) + "\n" +
                                "Type: " + c.getString(1) + "\n" +
                                "Breed: " + c.getString(2) + "\n" +
                                "Description:  " + c.getString(3),
                        Toast.LENGTH_LONG).show();
            } while (c.moveToNext());
        }
*/


//      btn_goAnimalData = findViewById(R.id.btn_goAnimalData);
        btn_goHome = findViewById(R.id.btn_goHome);

/*
        btn_goAnimalData.setOnClickListener(v -> {
            //Toast.makeText(AnimalActivity.this, "Viewed", Toast.LENGTH_SHORT).show();
            openAnimalActivity();
        });

 */
        btn_goHome.setOnClickListener(v -> {
            //Toast.makeText(AnimalActivity.this, "Viewed", Toast.LENGTH_SHORT).show();
            openHomeActivity();
        });

    }
/*
    private void openAnimalActivity(){
        Intent intent = new Intent(this, AnimalActivity.class);
        startActivity(intent);
    }

 */
    private void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
