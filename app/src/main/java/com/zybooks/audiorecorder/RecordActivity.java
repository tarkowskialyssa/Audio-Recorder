package com.zybooks.audiorecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class RecordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "RecordActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private ImageView recordButton;
    private boolean mStartRecording = true;

    private MediaRecorder recorder = null;

    //Request permission to record audio
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                Log.d(LOG_TAG, "in switch statement;");
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) {
            Log.d(LOG_TAG, "finish();");
            finish();
        }
    }

    private void onRecord(boolean start){
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    //Starts recording with mic
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    //Stops recording
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public void onRecordClick (View view) {
        onRecord(mStartRecording);
        if (mStartRecording) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordButton = findViewById(R.id.idRecordImageView);
        recordButton.setOnClickListener(v -> {
            //Starts or stops recording
            onRecord(mStartRecording);
            //Updates button image
            int imageResource = mStartRecording ? R.drawable.play : R.drawable.stop;
            mStartRecording = !mStartRecording;
            Drawable drawable = ContextCompat.getDrawable(RecordActivity.this, imageResource);
            recordButton.setImageDrawable(drawable);
        });
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