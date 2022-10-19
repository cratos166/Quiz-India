package com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class PlayerDisplayInQuizHolder {

    private String name;
    private String imageURL;
    private ArrayList<Integer> arrayList;



    public PlayerDisplayInQuizHolder() {
    }

    public PlayerDisplayInQuizHolder(String name, String imageURL, ArrayList<Integer> arrayList) {
        this.name = name;
        this.imageURL = imageURL;
        this.arrayList = arrayList;
    }

    public PlayerDisplayInQuizHolder(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
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
}
