package com.zybooks.audiorecorder;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.Viewholder>{
    private Context context;
    //List of all audio records
    private ArrayList<AudioModel> audioModelArrayList;
    //private String fileName;

    private File[] allFiles;

    //Constructor
    public AudioAdapter(Context context, ArrayList<AudioModel> audioModelArrayList){
        this.context = context;
        this.audioModelArrayList = audioModelArrayList;
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
        //AudioModel model = audioModelArrayList.get(position);
        //holder.fileName.setText(model.getFileName());
        //holder.fileTime.setText(model.getFileTime());
        //holder.playImage.setImageResource(model.getPlayImage());

        //fileName = model.getFileName();

        holder.fileName.setText(allFiles[position].getName());
        holder.fileTime.setText(allFiles[position].lastModified() + "");
    }

    @Override
    public int getItemCount() {
        //Returns number of cards in recycler view
        return audioModelArrayList.size();
    }

    //View holder class for initializing of TextViews
    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView fileName, fileTime;
        private ImageView playImage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.idFileName);
            fileTime = itemView.findViewById(R.id.idFileTime);
            playImage = itemView.findViewById(R.id.idPlayButton);
        }
    }


}
