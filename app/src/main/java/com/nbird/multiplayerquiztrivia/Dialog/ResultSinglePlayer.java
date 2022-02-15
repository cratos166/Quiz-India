package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.GENERATORS.BatchGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.LevelGenerators;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalSingleQuiz;
import com.nbird.multiplayerquiztrivia.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultSinglePlayer {

    Context context;
    HashMap<String, Integer> llMap;
    ArrayList<Boolean> animList;
    int correctAnsInt;
    String timeTakenString;
    int lifeLineUsedInt;
    long totalScoreInt;
    int higestScoreInt;
    int scoreInt;
    CardView view;
    String myNameString,myPicURL;

    public ResultSinglePlayer(Context context, HashMap<String, Integer> llMap, ArrayList<Boolean> animList, int correctAnsInt, String timeTakenString, int lifeLineUsedInt, long totalScoreInt, int higestScoreInt, int scoreInt, CardView view, String myNameString, String myPicURL) {
        this.context = context;
        this.llMap = llMap;
        this.animList = animList;
        this.correctAnsInt = correctAnsInt;
        this.timeTakenString = timeTakenString;
        this.lifeLineUsedInt = lifeLineUsedInt;
        this.totalScoreInt = totalScoreInt;
        this.higestScoreInt = higestScoreInt;
        this.scoreInt = scoreInt;
        this.view = view;
        this.myNameString=myNameString;
        this.myPicURL=myPicURL;
    }



    public void start(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.dialog_result_single_player,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);


        TextView correctAnswer=(TextView) viewRemove1.findViewById(R.id.correctAnswer);
        TextView timeTaken=(TextView) viewRemove1.findViewById(R.id.timeTaken);
        TextView lifeLineUsed=(TextView) viewRemove1.findViewById(R.id.lifeLineUsed);
        TextView levelText=(TextView) viewRemove1.findViewById(R.id.levelText);
        TextView highestScore=(TextView) viewRemove1.findViewById(R.id.highestScore);
        TextView totalScore=(TextView) viewRemove1.findViewById(R.id.totalScore);
        TextView myName=(TextView) viewRemove1.findViewById(R.id.myName);

        ImageView myPic=(ImageView) viewRemove1.findViewById(R.id.myPic);
        ImageView batch=(ImageView) viewRemove1.findViewById(R.id.batch);

        Button homeButton=(Button) viewRemove1.findViewById(R.id.homeButton);
        Button changeCategory=(Button) viewRemove1.findViewById(R.id.changeCategory);
        Button reMatch=(Button) viewRemove1.findViewById(R.id.reMatch);
        Button leaderBoard=(Button) viewRemove1.findViewById(R.id.leaderBoard);


        LinearLayout linearLayoutexpert=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutexpert);
        LinearLayout linearLayoutAudience=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutAudience);
        LinearLayout linearLayoutSwap=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutSwap);
        LinearLayout linearLayoutfiftyfifty=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutfiftyfifty);


        correctAnswer.setText("Correct/Wrong : "+scoreInt+"/"+String.valueOf(10-scoreInt));
        timeTaken.setText("Time Taken : "+timeTakenString);
        lifeLineUsed.setText("Life-Line Used : "+lifeLineUsedInt);

        LevelGenerators levelGenerators=new LevelGenerators(totalScoreInt,levelText);
        levelGenerators.start();

        BatchGenerator batchGenerator=new BatchGenerator(totalScoreInt,batch);
        batchGenerator.start();

        highestScore.setText("Your Highest Score : "+higestScoreInt);
        totalScore.setText("Total Score : "+totalScore);

        myName.setText(myNameString);
        Glide.with(context).load(myPicURL).apply(RequestOptions
                .bitmapTransform(new RoundedCorners(18)))
                .into(myPic);


        if(llMap.get("Expert")==1){
            linearLayoutexpert.setBackgroundResource(R.drawable.usedicon);
        }

        if(llMap.get("Flip")==1){
            linearLayoutSwap.setBackgroundResource(R.drawable.usedicon);
        }

        if(llMap.get("Audience")==1){
            linearLayoutAudience.setBackgroundResource(R.drawable.usedicon);
        }

        if(llMap.get("Fifty-Fifty")==1){
            linearLayoutfiftyfifty.setBackgroundResource(R.drawable.usedicon);
        }









        final AlertDialog alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }


    }




}
