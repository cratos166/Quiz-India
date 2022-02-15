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

        if(scoreSum <500){
            if(scoreSum <100){
                levelText.setText(" Level : 1 ");
            }else{
                levelText.setText(" Level : 2 ");
            }
        }else{
            long holder;
            holder= scoreSum /500;
            levelText.setText(" Level "+holder+" ");
        }

    }

}
