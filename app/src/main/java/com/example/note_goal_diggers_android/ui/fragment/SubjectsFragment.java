package com.example.note_goal_diggers_android.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.adapters.SubjectsAdapter;
import com.example.note_goal_diggers_android.database.NotesDB;
import com.example.note_goal_diggers_android.models.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class SubjectsFragment extends Fragment implements FloatingActionButton.OnClickListener {
    private SubjectsAdapter subjectsAdapter;
    private RecyclerView subjectrecyclerView;
    private NotesDB notesDB;

    public SubjectsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subjects, container, false);
    }

    private void refreshData(){
        subjectsAdapter = new SubjectsAdapter(notesDB.getAllSubjects());

        StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        subjectrecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        subjectrecyclerView.setAdapter(subjectsAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final FloatingActionButton fab = view.findViewById(R.id.fab);
        subjectrecyclerView = view.findViewById(R.id.subjectsrecyclerView);
        notesDB = new NotesDB(this.getContext());
        refreshData();
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("New Subject");
        builder.setView(R.layout.new_subjet_textfield);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputEditText inputEditText = ((AlertDialog) dialog).findViewById(R.id.txtSubject);
                Subject newSubject = new Subject(inputEditText.getText().toString());
                NotesDB notesDB = new NotesDB(getActivity());
                if (notesDB.addSubject(newSubject)){
                    refreshData();
                    Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                }
            }
        });


        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}

