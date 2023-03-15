package com.nbird.multiplayerquiztrivia.BUZZER.MODEL;

import java.util.ArrayList;

public class PlayerDisplayBuzzerHolder {
    private String name;
    private String imageURL;
    private ArrayList<Integer> arrayList;
    private int score;


    public PlayerDisplayBuzzerHolder() {
    }

    public PlayerDisplayBuzzerHolder(String name, String imageURL, ArrayList<Integer> arrayList, int score) {
        this.name = name;
        this.imageURL = imageURL;
        this.arrayList = arrayList;
        this.score = score;
    }

    public PlayerDisplayBuzzerHolder(String name, String imageURL, int score) {
        this.name = name;
        this.imageURL = imageURL;
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArrayList<Integer> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Integer> arrayList) {
        this.arrayList = arrayList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
