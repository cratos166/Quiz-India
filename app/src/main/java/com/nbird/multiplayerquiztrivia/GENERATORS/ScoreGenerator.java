package com.nbird.multiplayerquiztrivia.GENERATORS;

public class ScoreGenerator {

    int minutes;
    int seconds;
    int numberOfLL;
    int correctAns;

    public ScoreGenerator(int minutes, int seconds, int numberOfLL, int correctAns) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.numberOfLL = numberOfLL;
        this.correctAns = correctAns;
    }

    public int start(){
        int totalSecondsLeft=seconds+(minutes*60);
        return (totalSecondsLeft/6)+(correctAns*10)+(4-numberOfLL)*5;

    }

}
