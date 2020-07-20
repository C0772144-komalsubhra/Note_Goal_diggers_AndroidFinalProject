package com.example.note_goal_diggers_android.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.models.Subject;
import com.example.note_goal_diggers_android.ui.activity.NotesActivity;

import java.util.ArrayList;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder> {
    private ArrayList<Subject> subjectArrayList;

    public SubjectsAdapter(ArrayList<Subject> subjectArrayList){
        this.subjectArrayList = subjectArrayList;
    }
    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        SubjectViewHolder subjectViewHolder = new SubjectViewHolder(view);
        return subjectViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, final int position) {
        final Subject subject = this.subjectArrayList.get(position);
        holder.txtSubject.setText(subject.getSubjectName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject selectedSubject = subjectArrayList.get(position);
                Intent sIntent = new Intent(v.getContext(), NotesActivity.class);
                sIntent.putExtra("selectedSubjectId",selectedSubject.getSubjectId());
                v.getContext().startActivity(sIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.subjectArrayList.size();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {

        TextView txtSubject;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubject = itemView.findViewById(R.id.txtSubjectName);
        }
    }
}

