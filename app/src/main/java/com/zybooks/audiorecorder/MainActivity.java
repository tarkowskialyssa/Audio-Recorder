package com.zybooks.audiorecorder;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private RecyclerView audioRV;
    // ArrayList for storing data
    private AudioAdapter audioAdapter;
    private MediaPlayer player = null;
    private String filePath;

    private File[] allFiles;
    private boolean startPlaying = true;

    //UI Elements
    private ImageView playButton;

    private static final int PERMISSION_REQUEST_CODE = 200;

    //Request permission to access audio
    private boolean permissionToAccessAccepted = false;

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

        havePermissions();

        // Click listener for the RecyclerView
        View.OnClickListener onPlayClickListener = itemView -> {
            playButton = itemView.findViewById(R.id.idPlayButton);

            if (permissionToAccessAccepted){
                // Get the file that was clicked
                String selectedFile = (String) itemView.getTag();
                String recordPath = getExternalFilesDir("/").getAbsolutePath();
                filePath = recordPath + "/" + selectedFile;

                Log.d("ON PLAY", "File Clicked: " + selectedFile);

                onPlay(startPlaying);
                startPlaying = !startPlaying;

                int imageResource = startPlaying ? R.drawable.play : R.drawable.stop;
                playButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, imageResource));

            } else {
                Log.d("MainActivity", "No Permissions");
            }

        };

        View.OnClickListener onDeleteClickListener = itemView -> {
            // Get the file that was clicked
            int position = (int) itemView.getTag();

            Log.d("clickedDelete", "Position: " + position);

            String selectedFile = allFiles[position].getName();
            String recordPath = getExternalFilesDir("/").getAbsolutePath();

            //Delete the file
            onDelete(position, recordPath + "/" + selectedFile);

            Log.d("ON DELETE", "File Clicked: " + selectedFile);
        };

        //Initialize adapter class and pass files to it
        audioAdapter = new AudioAdapter(allFiles, onPlayClickListener, onDeleteClickListener); //May need to add context first

        //Set a layout manager for recycler view--vertical list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Set layout manager and adapter to recycler view
        audioRV.setHasFixedSize(true);
        audioRV.setLayoutManager(linearLayoutManager);
        audioRV.setAdapter(audioAdapter);
    }

    public void onNewClick (View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying(){
        if (player != null) {
            player.release();
            player = null;
        }

        player = new MediaPlayer();
        File file = new File(filePath);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.fromFile(file));
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
        //try{
            //player.setDataSource(filePath);
            //player.prepare();
            //player.start();
            Log.d("Playing" ,filePath);
        //} catch (IOException e){
        //    Log.e("startPlaying()" ,"prepare() failed");
        //}
    }

    private void stopPlaying(){
        player.release();
        player = null;
        Log.d("Stopped: " ,filePath);
    }

    private void onDelete(int position, String filePath){
        File file = new File(filePath);
        file.delete();

        //Update allFiles
        String path = getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFiles = directory.listFiles();

        //Send updated file list to adapter
        audioAdapter.updateFiles(allFiles);

        Log.d("onDelete", "File length: " + allFiles.length);
        //Remove file from adapter
        audioAdapter.notifyItemRemoved(position);
        audioAdapter.notifyItemRangeChanged(position, allFiles.length);

        Toast.makeText(this, file + " deleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop(){
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void havePermissions () {
        if (!checkPermission()) {
            Log.d("havePermissions()", "No Permissions");
            requestPermission();
        } else {
            Log.d("havePermissions()", "Permissions granted");
            permissionToAccessAccepted = true;
        }
    }

    private boolean checkPermission() {
        int resultStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        Log.d("checkPermissions()", "Checking permissions");
        return (resultStorage == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        Log.d("requestPermission()", "Requesting permissions");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);

                    if (storageAccepted) {
                        Log.d("onRequest()", "The user now has permissions");
                        permissionToAccessAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    } else {
                        Log.d("onRequest()", "The user does NOT have permissions");
                    }
                }
        }
    }


}