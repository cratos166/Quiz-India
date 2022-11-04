package com.nbird.multiplayerquiztrivia.BUZZER.MODEL;

import java.util.ArrayList;
import java.util.HashMap;

public class BuzzerDataExchangeHolder {
    ArrayList<Integer> animList;
    int totalScore;
    int correctAns;
    int position;
    String myNameString,myPicURL;

    public BuzzerDataExchangeHolder() {
    }

    public BuzzerDataExchangeHolder(ArrayList<Integer> animList, int totalScore, int correctAns, int position, String myNameString, String myPicURL) {
        this.animList = animList;
        this.totalScore = totalScore;
        this.correctAns = correctAns;
        this.position = position;
        this.myNameString = myNameString;
        this.myPicURL = myPicURL;
    }

    public ArrayList<Integer> getAnimList() {
        return animList;
    }

    public void setAnimList(ArrayList<Integer> animList) {
        this.animList = animList;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMyNameString() {
        return myNameString;
    }

    public void setMyNameString(String myNameString) {
        this.myNameString = myNameString;
    }

    public String getMyPicURL() {
        return myPicURL;
    }

    public void setMyPicURL(String myPicURL) {
        this.myPicURL = myPicURL;
    }
}
