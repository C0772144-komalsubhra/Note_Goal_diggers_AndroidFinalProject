package com.example.note_goal_diggers_android.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class NoteAttachment {
    private Integer noteImageId;
    private Integer noteId;
    private String filePath;
    private String type;
    private Timestamp timestamp;

    public NoteAttachment(Integer noteImageId, Integer noteId, String filePath, String type, Timestamp timestamp) {
        this.noteImageId = noteImageId;
        this.noteId = noteId;
        this.filePath = filePath;
        this.type = type;
        this.timestamp = timestamp;
    }

    public Integer getNoteImageId() {
        return noteImageId;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        return format.format(timestamp);
    }

    public Timestamp getTimestampObject() {
        return this.timestamp;
    }

    public String getType() {
        return type;
    }
}


