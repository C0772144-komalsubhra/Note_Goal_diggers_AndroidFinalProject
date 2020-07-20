package com.example.note_goal_diggers_android.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.database.NotesDB;
import com.example.note_goal_diggers_android.models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity implements LocationListener {
    private TextInputEditText title;
    private TextInputEditText data;
    private FloatingActionButton cameraFab;
    private FloatingActionButton audioFab;
    private NotesDB noteDB;

    private Integer selectedSubjectId;
    private Integer selectedNoteId;

    private final Integer IMAGE_CAPTURE_REQUEST_CODE = 101;
    private final Integer IMAGE_PICK_REQUEST_CODE = 102;
    private final Integer RECORD_AUDIO_REQUEST_CODE = 103;
    private final Integer USE_LOCATION_REQUEST_CODE = 104;

    private Boolean location_permission_granted = false;
    private LocationManager locationManager;
    private Location lastLocation;

    private Notes selectedNote;
    private ArrayList<String> selectedImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        this.setTitle("New Note");
        title = findViewById(R.id.titleTextInputEditText);
        data = findViewById(R.id.noteTextInputEditText);
        cameraFab = findViewById(R.id.camerafab);
        audioFab = findViewById(R.id.audiofab);
        noteDB = new NotesDB(this);

        selectedSubjectId = getIntent().getIntExtra("selectedSubjectId", 0);
        selectedNoteId = getIntent().getIntExtra("selectedNoteId", 0);

        if (selectedNoteId > 0) {
            selectedNote = noteDB.getNoteByNoteId(selectedNoteId);
        }

        if (selectedNote != null) {
            if (!selectedNote.getNoteTitle().isEmpty()) {
                title.setText(selectedNote.getNoteTitle());
            }

            if (!selectedNote.getNoteData().isEmpty()) {
                data.setText(selectedNote.getNoteData());
            }
        }

        cameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] options = {"Image Library", "Camera"};

                AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
                builder.setTitle("Image options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, IMAGE_CAPTURE_REQUEST_CODE);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        audioFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNote != null) {
                    Intent intent = new Intent(NewNoteActivity.this, RecordAudioActivity.class);
                    intent.putExtra("selectedNoteId", selectedNote.getNoteId());
                    startActivityForResult(intent, RECORD_AUDIO_REQUEST_CODE);
                } else {
                    Toast.makeText(NewNoteActivity.this, "Please first save the note to record audio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},USE_LOCATION_REQUEST_CODE);
        }
        else {
            location_permission_granted = true;
            requestLocationPermission();
        }
    }

    @SuppressLint("MissingPermission")
    protected void requestLocationPermission(){
        if (location_permission_granted){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            File file = new File(directory, System.currentTimeMillis() + ".jpeg");
            if (!file.exists()) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    selectedImages.add(file.toString());
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Log.d("imagePath", selectedImage.toString());
            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    selectedImages.add(picturePath);
                    cursor.close();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addCurrentLocationToNote(final long nid) {
        if (location_permission_granted) {
            if (lastLocation != null) {
                noteDB.addNoteLocation(nid, lastLocation);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.noteSave:
                if (!title.getText().toString().isEmpty() || !data.getText().toString().isEmpty() || !selectedImages.isEmpty()){
                    if (selectedNote == null){
                        //Create New Note
                        Notes newNote = new Notes(selectedSubjectId, title.getText().toString(), data.getText().toString());
                        long noteId = noteDB.addNote(newNote);
                        if (!selectedImages.isEmpty()){
                            for (String path: selectedImages){
                                noteDB.addNoteImageAttachment(noteId, path);
                            }
                        }
                        addCurrentLocationToNote(noteId);
                    }else {
                        //Update the note
                        selectedNote.setNoteTitle(title.getText().toString());
                        selectedNote.setNoteData(data.getText().toString());
                        noteDB.updateNote(selectedNote);

                        if(!selectedImages.isEmpty()){
                            for (String path: selectedImages){
                                noteDB.addNoteImageAttachment(selectedNote.getNoteId(), path);
                            }
                        }
                        addCurrentLocationToNote(selectedNote.getNoteId());
                    }
                }
                finish();
                break;

            case R.id.noteAttachments:
                if (selectedNoteId > 0){
                    Intent intent = new Intent(this, NoteAttachmentActivity.class);
                    Bundle values = new Bundle();
                    values.putInt("selectedNoteId", selectedNoteId);
                    intent.putExtras(values);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Please save the note to view attachments", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.noteRecordings:
                if (selectedNote != null){
                    Intent intent = new Intent(NewNoteActivity.this, NoteRecordingsActivity.class);
                    intent.putExtra("selectedNoteId", selectedNote.getNoteId());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Please save the note to view recordings", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.lastLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == USE_LOCATION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                location_permission_granted = true;
                requestLocationPermission();
            }
        }
    }
}
