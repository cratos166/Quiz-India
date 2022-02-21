package com.nbird.multiplayerquiztrivia.GENERATORS;

import android.widget.TextView;

public class LevelGenerators {

    long scoreSum;
    TextView levelText;


    public LevelGenerators(long scoreSum, TextView levelText) {
        this.scoreSum = scoreSum;
        this.levelText = levelText;
    }


    public void start(){


        levelText.setText("Lv. "+String.valueOf((scoreSum/500)+1));


    }

}
