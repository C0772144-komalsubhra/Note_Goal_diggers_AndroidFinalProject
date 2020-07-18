package com.example.note_goal_diggers_android;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class ListViewAdaptor extends ArrayAdapter {


    Context mContext;
    int layoutRes;
    List<String> categories;



    public ListViewAdaptor(@NonNull Context context, int resource, List<String> category) {
        super(context, resource,category);
        this.mContext = context;
        this.layoutRes = resource;
        this.categories = category;
    }
}
