package com.example.cse3310project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cse3310project.ml.ModelUnquant;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.os.Bundle;

public class CameraActivity extends AppCompatActivity {
    Button button; //take picture button
    ImageView camView; //image of picture taken
    TextView animal, breed; //for text of animal and breed
    private final int size = 224;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setTitle("Camera");
        camView = findViewById(R.id.myImage);
        button = findViewById(R.id.pictureButton);
        breed = findViewById(R.id.breed);
        animal = findViewById(R.id.animal);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1); //will change later in the following month
                }
                else
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }
    public void imgClassify(Bitmap image)
    {
        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*size*size*3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intVal = new int[size*size];
            image.getPixels(intVal, 0, image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixel = 0;
            for(int i = 0; i < size; i++) //RGB values
            {
                int value = intVal[pixel++];
                byteBuffer.putFloat(((value >> 16) & 0xFF) *(1.f/255.f));
                byteBuffer.putFloat(((value >> 8) & 0xFF) *(1.f/255.f));
                byteBuffer.putFloat((value & 0xFF) *(1.f/255.f));
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeature0.getFloatArray();
            int maxP = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidence.length; i++) //gets confidence for each breed
            {
                if(confidence[i] > maxConfidence)
                {
                    maxConfidence = confidence[i];
                    maxP = i;
                }
            }
            String[] animals = {"Dog"}; //will assign more animals as the model is trained
            String[] breeds = {"Labrador", "Pitbull", "German Shepard", "Chihuahua", "Golden Retriever"}; //same as animals
            animal.setText(animals[0]);
            breed.setText(breeds[maxP]);
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            System.out.println("Something went wrong with model");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            Bitmap img = (Bitmap) data.getExtras().get("data");
            int dim = Math.min(img.getWidth(), img.getHeight());
            img = ThumbnailUtils.extractThumbnail(img, dim, dim);
            camView.setImageBitmap(img);
            img = Bitmap.createScaledBitmap(img, size, size, false); //shows the image taken
            imgClassify(img); //classifies image
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
