package com.zybooks.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView audioRV;
    // ArrayList for storing data
    private AudioAdapter audioAdapter;

    private File[] allFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set recycler view
        audioRV = findViewById(R.id.idAudioRV);

        //Set all files
        String path = getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFiles = directory.listFiles();

        //Initialize adapter class and pass files to it
        audioAdapter = new AudioAdapter(allFiles); //May need to add context first

        //Set a layout manager for recycler view--vertical list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Set layout manager and adapter to recycler view
        audioRV.setHasFixedSize(true);
        //OTHER WAY: audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioRV.setLayoutManager(linearLayoutManager);
        audioRV.setAdapter(audioAdapter);
    }

    public void onNewClick (View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    public void onPlayClick (View view) {
        int itemPosition = audioRV.getChildLayoutPosition(view);
        //String item = audioModelArrayList.get(itemPosition);
        //Toast.makeText(this, item, Toast.LENGTH_LONG).show();
    }
}