package com.example.note_goal_diggers_android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.adapters.NotesAdapter;
import com.example.note_goal_diggers_android.database.NotesDB;
import com.example.note_goal_diggers_android.models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class NotesActivity extends AppCompatActivity implements Serializable, FloatingActionButton.OnClickListener {
    private RecyclerView notesrecyclerView;
    private NotesDB notesDB;
    private Integer selectedSubjectId;
    private NotesAdapter notesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        FloatingActionButton notesFab = findViewById(R.id.notesFab);
        notesrecyclerView = findViewById(R.id.notesRecyclerView);
        notesDB = new NotesDB(this);
        selectedSubjectId = getIntent().getIntExtra("selectedSubjectId",-1);
        populateNotes(null);
        notesFab.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateNotes(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateNotes(null);
    }

    private void populateNotes(ArrayList<Notes> notes){
        if ( notes == null) {
            notesAdapter = new NotesAdapter(notesDB.getNoteBySubjectId(selectedSubjectId), selectedSubjectId);
        }else{
            notesAdapter = new NotesAdapter(notes, selectedSubjectId);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        notesrecyclerView.setLayoutManager(linearLayoutManager);
        notesrecyclerView.setAdapter(notesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes, menu);

        MenuItem search = menu.findItem(R.id.noteSearch);
        SearchView searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.length() == 0){
                    populateNotes(null);
                }
                else {
                    notesAdapter.searchNoteFilter.filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ArrayList<Notes> notes = notesDB.getNoteBySubjectId(selectedSubjectId);
        switch (id){
            case R.id.menu_title:
                Collections.sort(notes, Notes.getTitleComparator());
                populateNotes(notes);
                Toast.makeText(getApplicationContext(),"Sorted By Title",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_date:
                Collections.sort(notes, Notes.getDateComparator());
                populateNotes(notes);
                Toast.makeText(getApplicationContext(),"Latest Notes First",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(NotesActivity.this, NewNoteActivity.class);
        intent.putExtra("selectedSubjectId", selectedSubjectId);
        startActivity(intent);
    }

}
