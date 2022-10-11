package com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL;

public class Details {
    public String imageURL,name,totalTime,accuracy,highestScore;

    public Details(String imageURL, String name, String totalTime, String accuracy, String highestScore) {
        this.imageURL = imageURL;
        this.name = name;
        this.totalTime = totalTime;
        this.accuracy = accuracy;
        this.highestScore = highestScore;
    }

    public Details() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(String highestScore) {
        this.highestScore = highestScore;
    }
}
