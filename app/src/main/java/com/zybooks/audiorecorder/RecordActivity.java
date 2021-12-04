package com.zybooks.audiorecorder;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "RecordActivity";
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ImageView recordButton;
    private boolean mStartRecording = true;
    private TextView testText;
    private MediaRecorder recorder = null;
    private Chronometer timer;

    //Request permission to record audio
    private boolean permissionToRecordAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordButton = findViewById(R.id.idRecordImageView);
        testText = findViewById(R.id.testText);
        timer = findViewById(R.id.recordTimer);
    }

    private void onRecord(boolean start){
        //testText.setText("in onRecord()");
        if (start) {
            testText.setText("onRecord(), start");
            startRecording();
        } else {
            //testText.setText("onRecord(), stop");
            stopRecording();
        }
    }

    //Starts recording with mic
    private void startRecording() {
        //Resets timer to zero
        timer.setBase(SystemClock.elapsedRealtime());
        //Starts timer
        timer.start();
        String recordPath = getExternalFilesDir("/").getAbsolutePath();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "audio_" + timeStamp + ".3gp";

        testText.setText("Start recording");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordPath + "/" + fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            //testText.setText("StartRecording(), prepared()");
        } catch (IOException e) {
            testText.setText("StartRecording() aint working");
            Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();
        testText.setText(recordPath + "/" + fileName);
    }

    //Stops recording
    private void stopRecording() {
        timer.stop();
        recorder.stop();
        recorder.release();
        recorder = null;
        Toast.makeText(this, "Recording Saved!", Toast.LENGTH_LONG);
    }

    public void onRecordClick (View view) {
        havePermissions();
        if (permissionToRecordAccepted){
            // testText.setText("beginRecord()" + mStartRecording);
            onRecord(mStartRecording);

            mStartRecording = !mStartRecording;

            //Updates button image
            //testText.setText("beginRecord()" + mStartRecording);
            int imageResource = mStartRecording ? R.drawable.play : R.drawable.stop;
            recordButton.setImageDrawable(ContextCompat.getDrawable(RecordActivity.this, imageResource));

        } else {
            testText.setText("Don't have permission to record");
        }
    }

    private void havePermissions () {
        if (!checkPermission()) {
            Toast.makeText(this, "No permissions", Toast.LENGTH_LONG).show();
            testText.setText("No permissions");
            requestPermission();
        } else {
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_LONG).show();
            testText.setText("Permissions granted");
            permissionToRecordAccepted = true;
        }
    }

    private boolean checkPermission() {
        int resultStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int resultRecord = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return (resultStorage == PackageManager.PERMISSION_GRANTED && resultRecord == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        testText.setText("Requesting permissions");
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    boolean recordAccepted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);

                    if (storageAccepted && recordAccepted) {
                        Toast.makeText(this, "You now have permissions!", Toast.LENGTH_LONG).show();
                        testText.setText("You now have permissions");
                        permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    } else {
                        Toast.makeText(this, "You DON'T have permissions", Toast.LENGTH_LONG).show();
                        testText.setText("You do NOT have permissions");
                        //finish();
                    }
                }
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }
}