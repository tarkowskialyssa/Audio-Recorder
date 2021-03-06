package com.zybooks.audiorecorder;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.Viewholder>{
    private Context context;

    //List of all audio records
    //private String fileName;
    private View.OnClickListener mOnPlayClickListener;
    private View.OnClickListener mOnDeleteClickListener;

    private File[] allFiles;
    //private onItemListClick onItemListClick;

    //Constructor
    public AudioAdapter(File[] allFiles, View.OnClickListener onPlayClickListener, View.OnClickListener onDeleteClickListener){
        this.allFiles = allFiles;
        mOnPlayClickListener = onPlayClickListener;
        mOnDeleteClickListener = onDeleteClickListener;

    }

    //Inflates the layout for each card in the recycler view
    @NonNull
    @Override
    public AudioAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_card, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioAdapter.Viewholder holder, int position) {
        //Sets text and image for each card
        holder.fileName.setText(allFiles[position].getName());

        SimpleDateFormat sim = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        holder.fileTime.setText("Created on " + sim.format(allFiles[position].lastModified()));

        holder.playImage.setImageResource(R.drawable.play);

        //Sets tag (file) and onClickListener for play button
        holder.playImage.setTag(allFiles[position]);
        holder.playImage.setOnClickListener(mOnPlayClickListener);

        //Sets tag (file) and onClickListener for delete button
        holder.deleteButton.setTag(position);
        holder.deleteButton.setOnClickListener(mOnDeleteClickListener);
    }

    @Override
    public int getItemCount() {
        //Returns number of cards/files in recycler view
        Log.d("getItemCount()", "Count: " + allFiles.length);
        return allFiles.length;
    }

    //View holder class for initializing of TextViews
    public class Viewholder extends RecyclerView.ViewHolder{
        private TextView fileName, fileTime;
        private ImageView playImage;
        private Button deleteButton;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.idFileName);
            fileTime = itemView.findViewById(R.id.idFileTime);
            playImage = itemView.findViewById(R.id.idPlayButton);
            deleteButton = itemView.findViewById(R.id.idDeleteButton);
        }
    }

    public void updateFiles(File[] updatedFiles){
        allFiles = updatedFiles;
    }



}
