package com.nbird.multiplayerquiztrivia.Model;

public class OnlineDetailHolder {
    String UID;
    int mode,roomCode;

    public OnlineDetailHolder() {
    }

    public OnlineDetailHolder(String UID, int mode, int roomCode) {
        this.UID = UID;
        this.mode = mode;
        this.roomCode = roomCode;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
