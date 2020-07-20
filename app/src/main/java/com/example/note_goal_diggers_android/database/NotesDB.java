package com.example.note_goal_diggers_android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.note_goal_diggers_android.models.NoteAttachment;
import com.example.note_goal_diggers_android.models.NoteLocation;
import com.example.note_goal_diggers_android.models.Notes;
import com.example.note_goal_diggers_android.models.Subject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class NotesDB extends SQLiteOpenHelper {
    private static final String TBL_SUBJECT = "tblSubject";
    private static final String NOTE_SUBJECT_ID = "SID";
    private static final String SUBJECT_ID = NOTE_SUBJECT_ID;
    private static final String SUBJECT_NAME = "SUBJECT_NAME";

    private static final String TBL_NOTES = "tblNotes";
    private static final String NOTE_ID = "NID";
    private static final String NOTE_TITLE = "NOTE_TITLE";
    private static final String NOTE_DATA = "NOTE_DATA";
    public static final String NOTE_TIMESTAMP = "TIMESTAMP";

    public static final String TBL_NOTE_ATTACHMENTS = "tblNoteAttachments";
    public static final String NOTE_ATTACHMENT_ID = "NOTE_ATTACHMENT_ID";
    public static final String FILE_PATH = "FILE_PATH";
    public static final String ATTACHMENT_TYPE = "ATTACHMENT_TYPE";
    public static final String ATTACHMENT_TIMESTAMP = "ATTACHMENT_TIMESTAMP";
    public static final String TBL_NOTE_LOCATIONS = "tblNoteLocations";
    public static final String NOTE_LOCATOIN_ID = "NOTE_LOCATOIN_ID";
    public static final String NOTE_LAT = "LAT";
    public static final String NOTE_LONG = "LONG";


    public NotesDB(@Nullable Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TBL_SUBJECT + " (" + SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT_NAME + " TEXT)";
        db.execSQL(query) ;

        query = "CREATE TABLE " + TBL_NOTES + " (" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_SUBJECT_ID +" INTEGER REFERENCES "+ TBL_SUBJECT +"("+ SUBJECT_ID +"), "+ NOTE_TITLE + " TEXT, " + NOTE_DATA + " TEXT, " + NOTE_TIMESTAMP + " default CURRENT_TIMESTAMP)";
        db.execSQL(query);

        query = "CREATE TABLE " + TBL_NOTE_ATTACHMENTS + "(" + NOTE_ATTACHMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+NOTE_ID+" INETGER REFERENCES "+TBL_NOTES+"("+NOTE_ID+ "), " + FILE_PATH + " TEXT, " + ATTACHMENT_TYPE + " TEXT, " + ATTACHMENT_TIMESTAMP + " default CURRENT_TIMESTAMP)";
        db.execSQL(query);

        query = "CREATE TABLE " + TBL_NOTE_LOCATIONS+ "(" + NOTE_LOCATOIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+NOTE_ID+" INETGER REFERENCES "+TBL_NOTES+"("+NOTE_ID+")"+ ", " + NOTE_LAT + " DECIMAL, " + NOTE_LONG + " DECIMAL)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private SQLiteDatabase readableDB(){
        return this.getReadableDatabase();
    }


    public boolean addSubject(Subject newSubject){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues subject = new ContentValues();

        subject.put(SUBJECT_NAME, newSubject.getSubjectName());

        try{
            long insert = db.insert(TBL_SUBJECT, null, subject);
            if (insert < 0)
            {
                return false;
            }
            else {
                return true;
            }
        }
        catch (SQLiteException e){
            Log.d("error", e.getMessage());
        }
        return false;
    }

    public ArrayList<Subject> getAllSubjects(){
        ArrayList<Subject> allSubjects = new ArrayList<>();

        String query = "SELECT " + SUBJECT_ID + ", " + SUBJECT_NAME + " FROM "+ TBL_SUBJECT;

        SQLiteDatabase db = readableDB();

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do{
                Integer subjectId = cursor.getInt(0);
                String subjectName = cursor.getString(1);
                Subject temp = new Subject(subjectId,subjectName);
                allSubjects.add(temp);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return allSubjects;
    }


    public ArrayList<Notes> getNoteBySubjectId(Integer sid){
        ArrayList<Notes> allNotes = new ArrayList<>();

        String query = "SELECT * FROM "+TBL_NOTES+" WHERE "+NOTE_SUBJECT_ID+"="+sid;

        SQLiteDatabase db = readableDB();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Integer noteId = cursor.getInt(0);
                Integer noteSubjectId = cursor.getInt(1);
                String noteTitle = cursor.getString(2);
                String noteData = cursor.getString(3);
                Timestamp timestamp = Timestamp.valueOf(cursor.getString(4));

                Notes temp = new Notes(noteId, noteSubjectId, noteTitle, noteData, timestamp);
                allNotes.add(temp);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  allNotes;
    }

    public Notes getNoteByNoteId(Integer nid){
        Notes note;

        String query = "SELECT * FROM "+TBL_NOTES+" WHERE "+NOTE_ID+"="+nid;

        SQLiteDatabase db = readableDB();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            Integer noteId = cursor.getInt(0);
            Integer noteSubjectId = cursor.getInt(1);
            String noteTitle = cursor.getString(2);
            String noteData = cursor.getString(3);
            Timestamp timestamp = Timestamp.valueOf(cursor.getString(4));
            Notes temp = new Notes(noteId, noteSubjectId, noteTitle, noteData, timestamp);

            cursor.close();
            db.close();
            return temp;
        }
        cursor.close();
        db.close();
        return null;
    }


    public long addNote(Notes newNote){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues note = new ContentValues();

        note.put(NOTE_SUBJECT_ID, newNote.getNoteSubjectId());
        note.put(NOTE_TITLE, newNote.getNoteTitle());
        note.put(NOTE_DATA, newNote.getNoteData());

        return db.insert(TBL_NOTES, null, note);
    }

    public long addNoteImageAttachment(long noteId, String imagePath){
        SQLiteDatabase db = getWritableDatabase();
        final ContentValues note = new ContentValues();

        note.put(NOTE_ID,noteId);
        note.put(FILE_PATH, imagePath);
        note.put(ATTACHMENT_TYPE,"image");
        return db.insert(TBL_NOTE_ATTACHMENTS, null, note);
    }

    public long addNoteRecordingAttachment(long noteId, String filePath){
        SQLiteDatabase db = getWritableDatabase();
        final ContentValues note = new ContentValues();

        note.put(NOTE_ID,noteId);
        note.put(FILE_PATH, filePath);
        note.put(ATTACHMENT_TYPE, "recording");
        return db.insert(TBL_NOTE_ATTACHMENTS, null, note);
    }

    public boolean updateNote(Notes notes){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues note = new ContentValues();

        note.put(NOTE_SUBJECT_ID, notes.getNoteSubjectId());
        note.put(NOTE_TITLE, notes.getNoteTitle());
        note.put(NOTE_DATA, notes.getNoteData());

        int update = db.update(TBL_NOTES, note, NOTE_ID+"="+ notes.getNoteId(), null);

        if (update < 0)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<NoteAttachment> getNoteImagesByNoteId(Integer noteId){
        ArrayList<NoteAttachment> images = new ArrayList<>();
        String query = "SELECT * FROM "+ TBL_NOTE_ATTACHMENTS +" WHERE "+NOTE_ID+" = "+noteId +" AND "+ATTACHMENT_TYPE+"='image'";

        SQLiteDatabase db = readableDB();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Integer noteImageId = cursor.getInt(0);
                Integer nid = cursor.getInt(1);
                String imagePath = cursor.getString(2);
                String type = cursor.getString(3);
                Timestamp timestamp = Timestamp.valueOf(cursor.getString(4));
                NoteAttachment temp = new NoteAttachment(noteImageId, nid, imagePath, type,timestamp);
                images.add(temp);
            }while (cursor.moveToNext());
            return images;
        }
        cursor.close();
        db.close();
        return null;
    }

    public ArrayList<NoteAttachment> getNoteRecordingsByNoteId(Integer noteId){
        ArrayList<NoteAttachment> images = new ArrayList<>();
        String query = "SELECT * FROM "+ TBL_NOTE_ATTACHMENTS +" WHERE "+NOTE_ID+" = "+noteId +" AND "+ATTACHMENT_TYPE+"='recording'";

        SQLiteDatabase db = readableDB();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Integer noteImageId = cursor.getInt(0);
                Integer nid = cursor.getInt(1);
                String imagePath = cursor.getString(2);
                String type = cursor.getString(3);
                Timestamp timestamp = Timestamp.valueOf(cursor.getString(4));
                NoteAttachment temp = new NoteAttachment(noteImageId, nid, imagePath, type,timestamp);
                images.add(temp);
            }while (cursor.moveToNext());
            return images;
        }
        cursor.close();
        db.close();
        return null;
    }

    public long addNoteLocation(long nid, Location location){
        SQLiteDatabase db = getWritableDatabase();
        final ContentValues noteLocation = new ContentValues();

        noteLocation.put(NOTE_ID,nid);
        noteLocation.put(NOTE_LAT, location.getLatitude());
        noteLocation.put(NOTE_LONG, location.getLongitude());

        return db.insert(TBL_NOTE_LOCATIONS, null, noteLocation);
    }

    public ArrayList<ArrayList<NoteLocation>> getNoteLocations(){
        ArrayList<ArrayList<NoteLocation>> noteLocations = new ArrayList<>();
        ArrayList<NoteLocation> temp = new ArrayList<>();

        SQLiteDatabase db = readableDB();
        String query = "SELECT "+NOTE_ID+","+NOTE_TITLE+" FROM "+TBL_NOTES;

        final Cursor noteIds = db.rawQuery(query, null);

        if (noteIds.moveToFirst()){
            do {
                query = "SELECT * FROM "+TBL_NOTE_LOCATIONS+" WHERE "+NOTE_ID+"="+noteIds.getInt(0);
                final Cursor noteLocation = db.rawQuery(query, null);
                if (noteLocation.moveToFirst()){
                    temp.clear();
                    do {
                        String noteTitle = noteIds.getString(1);

                        Integer noteLocationId = noteLocation.getInt(0);
                        Integer noteId = noteLocation.getInt(1);
                        Double lat = noteLocation.getDouble(2);
                        Double Long = noteLocation.getDouble(3);

                        final Location location = new Location("");
                        location.setLatitude(lat);
                        location.setLongitude(Long);
                        NoteLocation noteLocation1 = new NoteLocation(noteLocationId, noteId,  location, noteTitle);
                        temp.add(noteLocation1);
                    }while (noteLocation.moveToNext());
                    noteLocations.add(temp);
                }

            }while (noteIds.moveToNext());
        }
        return noteLocations;
    }
}
