package com.example.cse3310project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AnimalDataHelper extends SQLiteOpenHelper{

    public static final String ANIMAL_TABLE = "ANIMAL_TABLE";
    public static final String COLUMN_ANIMAL_TYPE = "ANIMAL_TYPE";
    public static final String COLUMN_ANIMAL_BREED = "ANIMAL_BREED";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_ID = "ID";



    public AnimalDataHelper(@Nullable Context context) {
        super(context, "animal.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + ANIMAL_TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ANIMAL_TYPE + " TEXT, " +
                COLUMN_ANIMAL_BREED + " TEXT, " + COLUMN_DESCRIPTION + " TEXT)";

        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(AnimalModel animalModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ANIMAL_TYPE, animalModel.getaType());
        cv.put(COLUMN_ANIMAL_BREED, animalModel.getaBreed());
        cv.put(COLUMN_DESCRIPTION, animalModel.getaDescription());

        long insert = db.insert(ANIMAL_TABLE, null, cv);
        if(insert == -1)
            return false;
        else
            return true;
    }

    public boolean deleteOne(AnimalModel animalModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + ANIMAL_TABLE + " WHERE " + COLUMN_ID + " = " + animalModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public List<AnimalModel> getAllAnimals(){

        List<AnimalModel> animalList = new ArrayList<>();

        String queryString = "SELECT * FROM " + ANIMAL_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                int animalID = cursor.getInt(0);
                String animalType = cursor.getString(1);
                String animalBreed = cursor.getString(2);
                String animalDescription = cursor.getString(3);

                AnimalModel newAnimal = new AnimalModel(animalID, animalType, animalBreed, animalDescription);
                animalList.add(newAnimal);

            } while(cursor.moveToNext());
        }

        else{

        }

        cursor.close();
        db.close();

        return animalList;
    }
}
