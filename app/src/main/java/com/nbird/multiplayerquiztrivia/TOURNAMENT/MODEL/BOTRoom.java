package com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL;

public class BOTRoom {

    private String hostName;
    private int gameMode;
    private String hostImageURL;
    private int numberOfPlayers,time,numberOfQuestions;


    public BOTRoom() {
    }

    public BOTRoom(String hostName, int gameMode, String hostImageURL, int numberOfPlayers, int time, int numberOfQuestions) {
        this.hostName = hostName;
        this.gameMode = gameMode;
        this.hostImageURL = hostImageURL;
        this.numberOfPlayers = numberOfPlayers;
        this.time = time;
        this.numberOfQuestions = numberOfQuestions;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public String getHostImageURL() {
        return hostImageURL;
    }

    public void setHostImageURL(String hostImageURL) {
        this.hostImageURL = hostImageURL;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
