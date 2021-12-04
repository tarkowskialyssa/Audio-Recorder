package com.zybooks.audiorecorder;

//Stores the audio data that will be displayed in the Recycler View
public class AudioModel {
    private String fileName;
    private String fileTime;
    private int playImage;

    //Constructor
    public AudioModel(String fileName, String fileTime, int playImage){
        this.fileName = fileName;
        this.fileTime = fileTime;
        this.playImage = playImage;
    }

    //Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime){
        this.fileTime = fileTime;
    }

    public int getPlayImage() {
        return playImage;
    }

    public void setPlayImage(String playImage){
        this.fileTime = playImage;
    }

}
