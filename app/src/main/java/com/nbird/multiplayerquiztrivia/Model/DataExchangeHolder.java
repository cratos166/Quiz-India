package com.nbird.multiplayerquiztrivia.Model;

import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.HashMap;

public class DataExchangeHolder {
    HashMap<String, Integer> llMap;
    ArrayList<Boolean> animList;
    int correctAnsInt;
    String timeTakenString;
    int lifeLineUsedInt;
    long totalScoreInt;
    int scoreInt;
    String myNameString,myPicURL;
    int timeTakenInt;


    public DataExchangeHolder() {
    }

    public DataExchangeHolder(HashMap<String, Integer> llMap, ArrayList<Boolean> animList, int correctAnsInt, String timeTakenString, int lifeLineUsedInt, long totalScoreInt, int scoreInt, String myNameString, String myPicURL, int timeTakenInt) {
        this.llMap = llMap;
        this.animList = animList;
        this.correctAnsInt = correctAnsInt;
        this.timeTakenString = timeTakenString;
        this.lifeLineUsedInt = lifeLineUsedInt;
        this.totalScoreInt = totalScoreInt;
        this.scoreInt = scoreInt;
        this.myNameString = myNameString;
        this.myPicURL = myPicURL;
        this.timeTakenInt = timeTakenInt;
    }

    public HashMap<String, Integer> getLlMap() {
        return llMap;
    }

    public void setLlMap(HashMap<String, Integer> llMap) {
        this.llMap = llMap;
    }

    public ArrayList<Boolean> getAnimList() {
        return animList;
    }

    public void setAnimList(ArrayList<Boolean> animList) {
        this.animList = animList;
    }

    public int getCorrectAnsInt() {
        return correctAnsInt;
    }

    public void setCorrectAnsInt(int correctAnsInt) {
        this.correctAnsInt = correctAnsInt;
    }

    public String getTimeTakenString() {
        return timeTakenString;
    }

    public void setTimeTakenString(String timeTakenString) {
        this.timeTakenString = timeTakenString;
    }

    public int getLifeLineUsedInt() {
        return lifeLineUsedInt;
    }

    public void setLifeLineUsedInt(int lifeLineUsedInt) {
        this.lifeLineUsedInt = lifeLineUsedInt;
    }

    public long getTotalScoreInt() {
        return totalScoreInt;
    }

    public void setTotalScoreInt(long totalScoreInt) {
        this.totalScoreInt = totalScoreInt;
    }

    public int getScoreInt() {
        return scoreInt;
    }

    public void setScoreInt(int scoreInt) {
        this.scoreInt = scoreInt;
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



    public int getTimeTakenInt() {
        return timeTakenInt;
    }

    public void setTimeTakenInt(int timeTakenInt) {
        this.timeTakenInt = timeTakenInt;
    }
}
