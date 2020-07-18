package com.example.note_goal_diggers_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class IconAdapter extends BaseAdapter {

    Context context;
   
    public int position;
    String photopath;
    Bitmap photobitmap;

    List<CategoryModel> categoryModelList;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
