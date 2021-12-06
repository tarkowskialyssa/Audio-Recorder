package com.zybooks.audiorecorder;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "RecordActivity";
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ImageView recordButton;
    private boolean mStartRecording = true;
    private TextView fileText;
    private MediaRecorder recorder = null;
    private Chronometer timer;
    private String TAG = "RecordActivity";

    //Request permission to record audio
    private boolean permissionToRecordAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordButton = findViewById(R.id.idRecordImageView);
        fileText = findViewById(R.id.fileText);
        timer = findViewById(R.id.recordTimer);
    }

    private void onRecord(boolean start){
        //testText.setText("in onRecord()");
        if (start) {
            startRecording();
        } else {
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

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordPath + "/" + fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();
        fileText.setText("Recording: " + fileName);
    }

    //Stops recording
    private void stopRecording() {
        timer.stop();
        recorder.stop();
        recorder.release();
        recorder = null;
        fileText.setText("Press the button to record another file");
        Toast.makeText(this, "Recording Saved!", Toast.LENGTH_SHORT).show();
    }

    public void onRecordClick (View view) {
        havePermissions();
        if (permissionToRecordAccepted){
            onRecord(mStartRecording);

            mStartRecording = !mStartRecording;

            //Updates button image
            int imageResource = mStartRecording ? R.drawable.play : R.drawable.stop;
            recordButton.setImageDrawable(ContextCompat.getDrawable(RecordActivity.this, imageResource));

        } else {
            Log.d(TAG, "No permissions");
        }
    }

    private void havePermissions () {
        if (!checkPermission()) {
            Log.d(TAG, "No permissions");
            requestPermission();
        } else {
            Log.d(TAG, "Permissions granted");
            permissionToRecordAccepted = true;
        }
    }

    private boolean checkPermission() {
        int resultStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int resultRecord = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return (resultStorage == PackageManager.PERMISSION_GRANTED && resultRecord == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        Log.d(TAG, "Requesting permissions");
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
                        Log.d(TAG, "The user now has permissions");
                        permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    } else {
                        Log.d(TAG, "The user does NOT have permissions");
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