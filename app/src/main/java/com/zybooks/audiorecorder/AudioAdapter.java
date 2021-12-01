package com.zybooks.audiorecorder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.Viewholder>{
    private Context context;
    //List of all audio records
    private ArrayList<AudioModel> audioModelArrayList;

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
        //Sets text for each card
        AudioModel model = audioModelArrayList.get(position);
        holder.fileName.setText(model.getFileName());
        holder.fileTime.setText(model.getFileTime());
    }

    @Override
    public int getItemCount() {
        //Returns number of cards in recycler view
        return audioModelArrayList.size();
    }

    //View holder class for initializing of TextViews
    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView fileName, fileTime;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.idFileName);
            fileTime = itemView.findViewById(R.id.idFileTime);
        }
    }
}
