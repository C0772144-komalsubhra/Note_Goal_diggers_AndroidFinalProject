package com.example.note_goal_diggers_android;

import java.io.Serializable;

public class CategoryModel  implements Serializable {
    int id;

    String title,description,date;
    double noteLat,noteLong;
    String audio;
    String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getNoteLat() {
        return noteLat;
    }

    public void setNoteLat(double noteLat) {
        this.noteLat = noteLat;
    }

    public double getNoteLong() {
        return noteLong;
    }

    public void setNoteLong(double noteLong) {
        this.noteLong = noteLong;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CategoryModel(int id, String title, String description, String date, double noteLat, double noteLong, String audio, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.noteLat = noteLat;
        this.noteLong = noteLong;
        this.audio = audio;
        this.image = image;
    }
}
