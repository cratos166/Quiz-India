package com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL;

public class Room {

    private String hostUID;
    private String hostName;
    private int gameMode;
    private String hostImageURL;
    private int roomCode,numberOfPlayers,time,numberOfQuestions;
    private boolean privacy;


    public Room(String hostUID, String hostName, int gameMode, String hostImageURL, int roomCode, int numberOfPlayers, int time, int numberOfQuestions, boolean privacy) {
        this.hostUID = hostUID;
        this.hostName = hostName;
        this.gameMode = gameMode;
        this.hostImageURL = hostImageURL;
        this.roomCode = roomCode;
        this.numberOfPlayers = numberOfPlayers;
        this.time = time;
        this.numberOfQuestions = numberOfQuestions;
        this.privacy = privacy;
    }


    public Room() {
    }

    public String getHostUID() {
        return hostUID;
    }

    public void setHostUID(String hostUID) {
        this.hostUID = hostUID;
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

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
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

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }
}
