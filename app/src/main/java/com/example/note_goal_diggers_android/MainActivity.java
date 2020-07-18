package com.example.note_goal_diggers_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,categoryName);
        listView.setAdapter(adapter);




        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

                View view = inflater.inflate(R.layout.category_dialog,null);

                builder.setView(view);

                final EditText editTextCategory = view.findViewById(R.id.edit_text_cat);


                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        String cat = editTextCategory.getText().toString();

                        categoryName.add(cat);

                        try {
                            sharedPreferences.edit().putString("cname",ObjectSerializer.serialize(categoryName)).apply();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                            if(dataBaseHelper.addNote(cat," hb"," hvbh"," hbvh")){
//                                Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(MainActivity.this, "not added", Toast.LENGTH_SHORT).show();
//                            }



                        listView.setAdapter(adapter);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                catPosition = position;
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                intent.putExtra("id", position);
                startActivity(intent);

            }
        });



    }


}
