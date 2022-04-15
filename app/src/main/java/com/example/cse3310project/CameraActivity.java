package com.example.cse3310project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse3310project.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CameraActivity extends AppCompatActivity {
    Button button, home; //take picture button
    ImageView camView; //image of picture taken
    TextView animal, breed; //for text of animal and breed
    ActivityResultLauncher<Intent> launcher;
    private int picturesTaken = 0;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setTitle("Camera");
        camView = findViewById(R.id.myImage);
        button = findViewById(R.id.pictureButton);
        breed = findViewById(R.id.breed);
        animal = findViewById(R.id.animal);
        home = findViewById(R.id.homeButtonCamera);

        home.setOnClickListener(view -> {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null)
            {
                Bundle data = result.getData().getExtras();
                Bitmap pic =  (Bitmap) data.get("data");
                camView.setImageBitmap(pic); //shows the image taken
                picturesTaken++;
                if(picturesTaken > 0)
                {
                    button.setText("Retake Picture");
                }
                classify(pic); //classifies image taken
            }
            else
            {
                System.out.println("Something went wrong with the result codes");
            }
        });

        //fixes an issue with minimum API being too low
        button.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launcher.launch(cameraIntent);
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });
    }

    public void classify(Bitmap image)
    {
        try {
            //size from model
            int size = 224;
            image = Bitmap.createScaledBitmap(image, size, size, false);
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4* size * size *3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] pixelVal = new int[size * size]; //pixels for RGB values; got code from link below
            image.getPixels(pixelVal, 0, image.getWidth(),0,0,image.getWidth(),image.getHeight()); //pixels for RGB values; got code from link below
            int numPixel = 0; //pixels for RGB values; got code from link below
            for(int i = 0; i < size; i++) //RGB values; got this code from https://stackoverflow.com/questions/55777086/converting-bitmap-to-bytebuffer-float-in-tensorflow-lite-android
            {
                for(int j = 0; j < size; j++)
                {
                    int value = pixelVal[numPixel++];
                    byteBuffer.putFloat(((value >> 16) & 0xFF) / 255.f); //formulas for RGB values for bytebuffer; code from link above
                    byteBuffer.putFloat(((value >> 8) & 0xFF) / 255.f);
                    byteBuffer.putFloat((value & 0xFF) / 255.f);
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            int maxBreed = 0;
            float maxConfidencePercent = 0;
            float[] confidence = outputFeature0.getFloatArray();
            for(int i = 0; i < confidence.length; i++) //gets max confidence
            {
                if(confidence[i] > maxConfidencePercent) //if confidence percent at i is greater than store percent, then it is replaced with new percent
                {
                    maxConfidencePercent = confidence[i]; //changes max breed percent
                    maxBreed = i; //index of maximum breed percent
                }
            }
            String[] animals = {"Dog"}; //will assign more animals as the model is trained
            String[] breeds = {"Labrador", "Pitbull", "German Shepard", "Chihuahua", "Golden Retriever"}; //same as animals
            animal.setText(animals[0]);
            breed.setText(breeds[maxBreed]);

            for(int i = 0; i < breeds.length; i++) //prints confidences into logcat
            {
                System.out.println(breeds[i]+"= "+confidence[i]*100);
            }

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            System.out.println("Something went wrong with model");
        }
    }
}
