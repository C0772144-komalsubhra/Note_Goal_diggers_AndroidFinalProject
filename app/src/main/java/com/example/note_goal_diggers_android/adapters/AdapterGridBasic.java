package com.example.note_goal_diggers_android.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.models.NoteAttachment;

import java.util.ArrayList;

public class AdapterGridBasic extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private ArrayList<NoteAttachment> items;
    private Context context;

    public AdapterGridBasic(Context context, ArrayList<NoteAttachment> images) {
        this.items = images;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.image.setImageBitmap(BitmapFactory.decodeFile(items.get(position).getFilePath()));
            view.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View dialogView =LayoutInflater.from(context).inflate(R.layout.dialog_image, null,false);
                    ImageView imageView = dialogView.findViewById(R.id.dialogImage);
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(dialogView);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(items.get(position).getFilePath()));
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
