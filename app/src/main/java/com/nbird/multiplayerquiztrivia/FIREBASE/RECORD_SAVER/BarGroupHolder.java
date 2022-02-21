package com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER;

public class BarGroupHolder {
    int correct;
    int wrong;

    public BarGroupHolder() {
    }

    public BarGroupHolder(int correct, int wrong) {
        this.correct = correct;
        this.wrong = wrong;
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
}
