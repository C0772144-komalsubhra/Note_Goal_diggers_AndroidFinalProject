package com.example.note_goal_diggers_android.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.models.Notes;
import com.example.note_goal_diggers_android.ui.activity.NewNoteActivity;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private ArrayList<Notes> notesArrayList;
    private Integer selectedSubjectId;

    public NotesAdapter(ArrayList<Notes> notesArrayList, Integer selectedSubjectId){
        this.notesArrayList = notesArrayList;
        this.selectedSubjectId = selectedSubjectId;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder, final int position) {
        final Notes note = this.notesArrayList.get(position);

        holder.txtNote.setText(note.getNoteTitle().isEmpty() ? note.getNoteData() : note.getNoteTitle());
        holder.txtTimestamp.setText(note.getTimestamp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewNoteActivity.class);
                intent.putExtra("selectedNoteId", note.getNoteId());
                intent.putExtra("selectedSubjectId",selectedSubjectId);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.notesArrayList.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {

        TextView txtNote;
        TextView txtTimestamp;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNote = itemView.findViewById(R.id.txtNotes);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);
        }
    }

    public final Filter searchNoteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Notes> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(notesArrayList);
            }
            else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (Notes note : notesArrayList) {
                    if (note.getNoteTitle().toLowerCase().contains(pattern) || note.getNoteData().toLowerCase().contains(pattern)){
                        filteredList.add(note);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notesArrayList.clear();
            notesArrayList.addAll((ArrayList<Notes>)results.values);
            notifyDataSetChanged();
        }
    };
}

