package com.nbird.multiplayerquiztrivia.GENERATORS;

import android.widget.ImageView;

import com.nbird.multiplayerquiztrivia.R;

public class BatchGenerator {

    long scoreSum;
    ImageView imageView;

    public BatchGenerator(long scoreSum,ImageView imageView) {
        this.scoreSum = scoreSum;
        this.imageView=imageView;
    }

    public void start(){


        if(scoreSum <500){

            imageView.setBackgroundResource(R.drawable.blackiron);
        }else if(scoreSum <2000){

            imageView.setBackgroundResource(R.drawable.bronze);
        }else if(scoreSum <8000){


            imageView.setBackgroundResource(R.drawable.silver);
        }else if(scoreSum <18000){


            imageView.setBackgroundResource(R.drawable.gold);
        }else if(scoreSum <30000){


            imageView.setBackgroundResource(R.drawable.platinum);
        }else if(scoreSum <40000){


            imageView.setBackgroundResource(R.drawable.diamond);
        }else if(scoreSum <80000){


            imageView.setBackgroundResource(R.drawable.amethyst);
        }else if(scoreSum <120000){


            imageView.setBackgroundResource(R.drawable.master);
        }else{


            imageView.setBackgroundResource(R.drawable.king);
        }
    }

}
