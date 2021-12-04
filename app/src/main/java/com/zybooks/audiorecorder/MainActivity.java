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
    private ArrayList<AudioModel> audioModelArrayList;

    private File[] allFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set recycler view
        audioRV = findViewById(R.id.idAudioRV);

        //Add data to new ArrayList
        //DUMMY DATA FOR NOW
        audioModelArrayList = new ArrayList<>();
        audioModelArrayList.add(new AudioModel("file1.mpg", "15:00", R.drawable.play));
        audioModelArrayList.add(new AudioModel("file2.mpg", "12:44", R.drawable.play));
        audioModelArrayList.add(new AudioModel("verylongnameoffile.mpg", "00:01", R.drawable.play));
        audioModelArrayList.add(new AudioModel("alyssasfile.mpg", "00:17", R.drawable.play));


        //NOT DUMMY DATA
        //String path = getExternalFilesDir("/").getAbsolutePath();
        //File directory = new File(path);
        //allFiles = directory.listFiles();

        //Would need to change req in adapter to non-array list
        //AudioAdapter audioAdapter = new AudioAdapter(this, allFiles);

        //Initialize adapter class and pass arraylist to it
        AudioAdapter audioAdapter = new AudioAdapter(this, audioModelArrayList);

        //Set a layout manager for recycler view--vertical list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Set layout manager and adapter to recycler view
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