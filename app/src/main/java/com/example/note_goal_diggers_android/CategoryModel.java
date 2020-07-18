package com.example.note_goal_diggers_android;

import java.io.Serializable;

public class CategoryModel  implements Serializable {
    int id;

    String title,description,date;
    double noteLat,noteLong;
    String audio;
    String image;

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
