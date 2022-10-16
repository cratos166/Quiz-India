package com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL;

public class PlayerInfo {

    String username;
    int score;
    int totalTime;
    int correct;
    int wrong;
    String imageUrl;
    int sumationScore;
    boolean active;


    public PlayerInfo() {
    }


    public PlayerInfo(String username, int score, int totalTime, int correct, int wrong, String imageUrl, int sumationScore, boolean active) {
        this.username = username;
        this.score = score;
        this.totalTime = totalTime;
        this.correct = correct;
        this.wrong = wrong;
        this.imageUrl = imageUrl;
        this.sumationScore = sumationScore;
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSumationScore() {
        return sumationScore;
    }

    public void setSumationScore(int sumationScore) {
        this.sumationScore = sumationScore;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}


