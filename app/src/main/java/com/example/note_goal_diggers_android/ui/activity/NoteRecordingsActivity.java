package com.example.note_goal_diggers_android.ui.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_goal_diggers_android.R;
import com.example.note_goal_diggers_android.adapters.AudioListAdaptor;
import com.example.note_goal_diggers_android.database.NotesDB;
import com.example.note_goal_diggers_android.models.NoteAttachment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

public class NoteRecordingsActivity extends AppCompatActivity implements AudioListAdaptor.onItemListClick  {
    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView audioList;
    private AudioListAdaptor audioListAdaptor;

    private File[] allFiles;

    private MediaPlayer mediaPlayer = null;
    private Boolean isPlaying = false;
    private File fileToPlay = null;

    private ImageButton playButton;
    private TextView playerHeader;
    private TextView playerFileName;
    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;

    private Integer selectedNoteId;
    private ArrayList<NoteAttachment> attachments;
    private NotesDB notesDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_recordings);
        notesDB = new NotesDB(this);
        selectedNoteId = getIntent().getIntExtra("selectedNoteId", 0);
        attachments = notesDB.getNoteRecordingsByNoteId(selectedNoteId);

        playerSheet = findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        audioList = findViewById(R.id.audio_list_view);

        playButton = findViewById(R.id.imageView3);
        playerHeader = findViewById(R.id.player_header_title);
        playerFileName = findViewById(R.id.player_filename);

        playerSeekbar = findViewById(R.id.player_seekbar);


        ContextWrapper cw = new ContextWrapper(NoteRecordingsActivity.this);
        File directory = cw.getDir("recordings", Context.MODE_PRIVATE);
        allFiles = directory.listFiles(new FileFilter() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean accept(final File pathname) {
                if (attachments != null) {
                    return attachments.removeIf(new Predicate<NoteAttachment>() {
                        @Override
                        public boolean test(NoteAttachment noteAttachment) {
                            return noteAttachment.getFilePath().compareToIgnoreCase(pathname.toString()) == 0;
                        }
                    });
                }
                else {
                    return false;
                }
            }
        });

        audioListAdaptor = new AudioListAdaptor(allFiles, this);
        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(NoteRecordingsActivity.this));
        audioList.setAdapter(audioListAdaptor);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying){
                    pauseAudio();
                }else {
                    if (fileToPlay == null){
                        resumeAudio();
                    }
                }
            }
        });

        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                resumeAudio();
            }
        });
    }

    @Override
    public void onClickListener(File file, int position) {
        fileToPlay = file;
        if (isPlaying){
            stopAudio();
        }
        playAudio(fileToPlay);
    }

    private void pauseAudio(){
        mediaPlayer.pause();
        playButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_media_play, null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void resumeAudio(){
        mediaPlayer.start();
        playButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_media_pause, null));
        isPlaying = true;
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar,0);
    }

    private void stopAudio() {
        isPlaying = false;
        playButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_media_play, null));
        playerHeader.setText("Stopped");
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void playAudio(File fileToPlay) {
        mediaPlayer = new MediaPlayer();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException e){
            e.printStackTrace();
        }

        playButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_media_pause, null));
        playerFileName.setText(fileToPlay.getName());
        playerHeader.setText("Playing");
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopAudio();
                playerHeader.setText("Finished");
            }
        });

        playerSeekbar.setMax(mediaPlayer.getDuration());
        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar,0);
    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isPlaying){
            stopAudio();
        }
    }

}
