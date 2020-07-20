package com.example.note_goal_diggers_android.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.adapters.AdapterGridBasic;
import com.example.note_goal_diggers_android.database.NotesDB;
import com.example.note_goal_diggers_android.models.NoteAttachment;
import com.example.note_goal_diggers_android.widget.SpacingItemDecoration;

import java.util.ArrayList;

public class NoteAttachmentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterGridBasic mAdapter;
    private Bundle values;
    private ArrayList<NoteAttachment> images;
    private NotesDB notesDB;

    private Integer EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_attachment);
        ActivityCompat.requestPermissions(NoteAttachmentActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        notesDB = new NotesDB(this);
        values = getIntent().getExtras();
    }

    private int dpToPx(int dp) {
        Resources r = this.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, dpToPx(2), true));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new AdapterGridBasic(this, images);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                images = notesDB.getNoteImagesByNoteId(values.getInt("selectedNoteId"));
                if (images!=null){
                    initComponent();
                }
            }
            else {
                finish();
            }
        }
    }
}
