package com.nbird.multiplayerquiztrivia.Model;

public class PlayHolder {
    String player1UID;
    String player2UID;


    public PlayHolder(String player1UID, String player2UID) {
        this.player1UID = player1UID;
        this.player2UID = player2UID;
    }

    public String getPlayer1UID() {
        return player1UID;
    }

    public void setPlayer1UID(String player1UID) {
        this.player1UID = player1UID;
    }

    public String getPlayer2UID() {
        return player2UID;
    }

    public void setPlayer2UID(String player2UID) {
        this.player2UID = player2UID;
    }
}
