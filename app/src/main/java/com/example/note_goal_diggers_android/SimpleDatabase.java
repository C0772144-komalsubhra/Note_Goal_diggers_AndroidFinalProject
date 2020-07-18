package com.example.note_goal_diggers_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleDatabase extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "NotesDatabase";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CAT = "category";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LONG = "longitude";
    public static final String COLUMN_AUDIO = "audio";
    public static final String COLUMN_IMAGE = "image";

    public SimpleDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //  new table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table  " + TABLE_NAME + "(" +
                COLUMN_ID + " integer not null constraint note_pk primary key autoincrement," +
                COLUMN_CAT + " varchar(200) not null, " +
                COLUMN_TITLE + " varchar(200) not null , " +
                COLUMN_DESC + " varchar(200) not null , " +
                COLUMN_DATE + " varchar(200) not null , " +
                COLUMN_LAT + " double not null , " +
                COLUMN_LONG + " double not null , " +
                COLUMN_AUDIO + " varchar(200)  , " +
                COLUMN_IMAGE + " varchar(200) );";
        ;
        db.execSQL(sql);
    }


    //for existing table(drop the current table and create new one )
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql  = "drop table if exists " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }
    boolean addNote( String category, String title, String desc, String date ,double latitude, double longitude,String audio,String image){


        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CAT,category);
        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_DESC,desc);
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_LAT,latitude);
        cv.put(COLUMN_LONG,longitude);
        cv.put(COLUMN_AUDIO,audio);
        cv.put(COLUMN_IMAGE,image);



        return sqLiteDatabase.insert(TABLE_NAME,null,cv) != -1;

    }

    Cursor getAllNotes(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        return sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_CAT + " =?",new String[]{MainActivity.categoryName.get(MainActivity.catPosition)} );
    }


    Cursor getAllSortedNotes(String col){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        return sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_CAT + " =?" + " ORDER BY " + col, new String[]{MainActivity.categoryName.get(MainActivity.catPosition)} );
    }




    boolean updateNote(int id, String title, String desc,String audio,String image){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_DESC,desc);
        cv.put(COLUMN_AUDIO,audio);
        cv.put(COLUMN_IMAGE,image);

        // this method returns the number of rows affected

        return sqLiteDatabase.update(TABLE_NAME,cv,COLUMN_ID + "=?",new String[]{String.valueOf(id)}) > 0;
    }

    boolean deletenote(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        //the delete method returns the number of rows affected

        return sqLiteDatabase.delete(TABLE_NAME,COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }



}
