package com.example.note_goal_diggers_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    public static ArrayList<String> categoryName;
    public static int catPosition;
    Button addCategory;
    SimpleDatabase dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Categories");


        listView = findViewById(R.id.category_list_view);
        addCategory = findViewById(R.id.btn_add_category);
        categoryName = new ArrayList<>();
        dataBaseHelper = new  SimpleDatabase(this);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.note_goal_diggers_android", Context.MODE_PRIVATE);

        try {
            categoryName = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("cname",ObjectSerializer.serialize(new ArrayList<>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}