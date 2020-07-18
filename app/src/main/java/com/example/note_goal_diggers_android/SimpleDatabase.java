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
    }
