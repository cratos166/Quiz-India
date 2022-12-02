package com.nbird.multiplayerquiztrivia.EXTRA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.GENERATORS.BatchGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.LevelGenerators;

public class OneVSOneOpponentDataSetter {

    LeaderBoardHolder leaderBoardHolder;
    ImageView oppoImage;
    TextView oppoName,highestScore,totalTime,oppoRatio,oppoAccu;
    ShimmerFrameLayout shimmerFrameLayout;
    Context mContext;
    LinearLayout mainlinearLayout;
    CardView oppoBatchCardView;
    ImageView oppoBatch;
    TextView oppoLevelText;

    public OneVSOneOpponentDataSetter(LeaderBoardHolder leaderBoardHolder, ImageView oppoImage, TextView oppoName, TextView highestScore, TextView totalTime, TextView oppoRatio, TextView oppoAccu, ShimmerFrameLayout shimmerFrameLayout, Context mContext, LinearLayout mainlinearLayout, CardView oppoBatchCardView, ImageView oppoBatch, TextView oppoLevelText) {
        this.leaderBoardHolder = leaderBoardHolder;
        this.oppoImage = oppoImage;
        this.oppoName = oppoName;
        this.highestScore = highestScore;
        this.totalTime = totalTime;
        this.oppoRatio = oppoRatio;
        this.oppoAccu = oppoAccu;
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.mContext = mContext;
        this.mainlinearLayout = mainlinearLayout;
        this.oppoBatchCardView = oppoBatchCardView;
        this.oppoBatch = oppoBatch;
        this.oppoLevelText = oppoLevelText;
    }

    public void start(){


        Glide.with(mContext).load(leaderBoardHolder.getImageUrl()).into(oppoImage);
        oppoName.setText(leaderBoardHolder.getUsername());
        highestScore.setText("Highest Score : "+leaderBoardHolder.getScore());

        String timeStr=leaderBoardHolder.getTotalTime()/60+" Min "+leaderBoardHolder.getTotalTime()%60+" Sec ";
        totalTime.setText("Total Time : "+timeStr);

        oppoRatio.setText("Correct/Wrong : "+leaderBoardHolder.getCorrect()+"/"+leaderBoardHolder.getWrong());

        try{
            int accuStr=(leaderBoardHolder.getCorrect()*100)/(leaderBoardHolder.getCorrect()+leaderBoardHolder.getWrong());
            oppoAccu.setText("Accuracy : "+String.valueOf(accuStr)+"%");
        }catch (Exception e){
            oppoAccu.setText("Accuracy : "+"0%");
        }


        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);

        mainlinearLayout.setVisibility(View.VISIBLE);

        BatchGenerator batchGenerator=new BatchGenerator(leaderBoardHolder.getSumationScore(),oppoBatch);
        batchGenerator.start();
        LevelGenerators levelGenerators=new LevelGenerators(leaderBoardHolder.getSumationScore(),oppoLevelText);
        levelGenerators.start();

        oppoBatchCardView.setVisibility(View.VISIBLE);

    }

}
