package com.example.note_goal_diggers_android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.database.NotesDB;
import com.example.note_goal_diggers_android.models.NoteLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment<onViewCreate> extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap map;
    NotesDB notesDB;
    public MapsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        notesDB = new NotesDB(getContext());
        mapView.getMapAsync(this);
    }



    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mapView.getMapAsync(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        ArrayList<ArrayList<NoteLocation>> noteLocations = notesDB.getNoteLocations();

        for (int i = 0; i < noteLocations.size(); i++) {
            ArrayList<NoteLocation> notes = noteLocations.get(i);

            for (int j = 0; j < notes.size(); j++) {
                LatLng latLng = new LatLng(notes.get(j).getLocation().getLatitude(), notes.get(j).getLocation().getLongitude());
                map.addMarker(new MarkerOptions().position(latLng).title(notes.get(j).getNoteTitle()));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
            }
        }
    }
}

