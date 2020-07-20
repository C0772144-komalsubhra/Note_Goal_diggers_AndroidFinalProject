package com.example.note_goal_diggers_android.models;

import android.location.Location;

public class NoteLocation {
    private Integer noteLoactionId;
    private Integer noteId;
    private Location location;
    private String noteTitle;

    public NoteLocation(Integer noteLoactionId, Integer noteId, Location location) {
        this.noteLoactionId = noteLoactionId;
        this.noteId = noteId;
        this.location = location;
    }

    public NoteLocation(Integer noteLoactionId, Integer noteId, Location location, String noteTitle) {
        this.noteLoactionId = noteLoactionId;
        this.noteId = noteId;
        this.location = location;
        this.noteTitle = noteTitle;
    }

    public Integer getNoteLoactionId() {
        return noteLoactionId;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public Location getLocation() {
        return location;
    }

    public String getNoteTitle() {
        return noteTitle;
    }
}
