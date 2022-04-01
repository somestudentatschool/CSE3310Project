package com.example.cse3310project;

public class AnimalModel {

    private int id;
    private String aType, aBreed, aDescription;

    public AnimalModel(int id, String aType, String aBreed, String aDescription) {
        this.id = id;
        this.aType = aType;
        this.aBreed = aBreed;
        this.aDescription = aDescription;
    }

    public AnimalModel(String aType, String aBreed){
        this.aType = aType;
        this.aBreed = aBreed;
        aDescription = "REDACTED";
        id = -2;
    }

    @Override
    public String toString() {
        return "AnimalModel{" +
                "id=" + id +
                ", aType='" + aType + '\'' +
                ", aBreed='" + aBreed + '\'' +
                ", aDescription='" + aDescription + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public String getaBreed() {
        return aBreed;
    }

    public void setaBreed(String aBreed) {
        this.aBreed = aBreed;
    }

    public String getaDescription() {
        return aDescription;
    }

    public void setaDescription(String aDescription) {
        this.aDescription = aDescription;
    }
}


