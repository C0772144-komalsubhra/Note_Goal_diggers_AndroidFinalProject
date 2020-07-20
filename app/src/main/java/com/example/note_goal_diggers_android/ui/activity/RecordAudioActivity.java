package com.example.note_goal_diggers_android.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.database.NotesDB;

import java.io.File;
import java.io.IOException;

public class RecordAudioActivity extends AppCompatActivity {
    private NavController navController;
    private ImageButton playPauseBtn;
    private Boolean isRecording = false;

    private String recordPermission = Manifest.permission.RECORD_AUDIO;

    private int PERMISSION_CODE = 21;

    private MediaRecorder mediaRecorder;
    private Chronometer timer;

    private NotesDB noteDB;
    private File file;
    private Integer selectedNoteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        playPauseBtn = findViewById(R.id.playPauseButton);
        timer = findViewById(R.id.timer);
        noteDB = new NotesDB(this);

        selectedNoteId = getIntent().getIntExtra("selectedNoteId", 0);

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording){
                    stopRecording();
                    playPauseBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_media_play, null));
                    isRecording = false;
                }else {
                    if (checkPermission()) {
                        startRecording();
                        playPauseBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_media_pause, null));
                        isRecording = true;
                    }
                }
            }
        });
    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        String fileName = System.currentTimeMillis() + ".3gp";

        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getDir("recordings", Context.MODE_PRIVATE);
        file = new File(directory, fileName);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(file.toString());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        noteDB.addNoteRecordingAttachment(selectedNoteId, file.toString());
        finish();
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, recordPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            ActivityCompat.requestPermissions(this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
        }
    }
}

