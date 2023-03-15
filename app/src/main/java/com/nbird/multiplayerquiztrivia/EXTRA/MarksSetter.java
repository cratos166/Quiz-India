package com.nbird.multiplayerquiztrivia.EXTRA;

import android.widget.TextView;

public class MarksSetter {

    TextView marksCorrectAnswer,marksTimeTaken,marksLifeLineUsed;

    int correctAnswer,timeTaken,lifeLineUsed;

    public MarksSetter() {
    }

    public MarksSetter(TextView marksCorrectAnswer, TextView marksTimeTaken, TextView marksLifeLineUsed, int correctAnswer, int timeTaken, int lifeLineUsed) {
        this.marksCorrectAnswer = marksCorrectAnswer;
        this.marksTimeTaken = marksTimeTaken;
        this.marksLifeLineUsed = marksLifeLineUsed;
        this.correctAnswer = correctAnswer;
        this.timeTaken = timeTaken;
        this.lifeLineUsed = lifeLineUsed;
    }

    public void start(){

        int timeRemaining=180-timeTaken;
        marksTimeTaken.setText("+"+timeRemaining/6);
        marksCorrectAnswer.setText("+"+correctAnswer*10);
        marksLifeLineUsed.setText("+"+(4-lifeLineUsed)*5);

    }

    public void startForTournament(int totalTime){

        int timeRemaining;

        timeRemaining=timeTaken-(correctAnswer*10)-((4-lifeLineUsed)*5);



        marksTimeTaken.setText("+"+timeRemaining);
        marksCorrectAnswer.setText("+"+correctAnswer*10);
        marksLifeLineUsed.setText("+"+(4-lifeLineUsed)*5);

    }

}
