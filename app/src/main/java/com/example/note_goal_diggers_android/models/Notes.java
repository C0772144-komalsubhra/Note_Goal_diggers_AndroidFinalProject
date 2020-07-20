package com.example.note_goal_diggers_android.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class Notes {

    private Integer noteId;
    private Integer noteSubjectId;
    private String noteTitle;
    private String noteData;
    private Timestamp timestamp;
    private ArrayList<String> images;

    public Notes(Integer noteId, Integer noteSubjectId, String noteTitle, String noteData, Timestamp timestamp) {
        this.noteId = noteId;
        this.noteSubjectId = noteSubjectId;
        this.noteTitle = noteTitle;
        this.noteData = noteData;
        this.timestamp = timestamp;
    }

    public Notes(Integer noteSubjectId, String noteTitle, String noteData) {
        this.noteSubjectId = noteSubjectId;
        this.noteTitle = noteTitle;
        this.noteData = noteData;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public Integer getNoteSubjectId() {
        return noteSubjectId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteData() {
        return noteData;
    }

    public String getTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        return format.format(timestamp);
    }

    public Timestamp getTimestampObject() {
        return this.timestamp;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteData(String noteData) {
        this.noteData = noteData;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public static Comparator<Notes> getTitleComparator(){
        return new Comparator<Notes>() {
            @Override
            public int compare(Notes o1, Notes o2) {
                return o1.getNoteTitle().compareToIgnoreCase(o2.getNoteTitle());
            }
        };
    }

    public static Comparator<Notes> getDateComparator(){
        return new Comparator<Notes>() {
            @Override
            public int compare(Notes o1, Notes o2) {
                return o2.getTimestampObject().compareTo(o1.getTimestampObject());
            }
        };

    }
}
