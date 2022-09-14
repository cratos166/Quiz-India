package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.Record;
import com.nbird.multiplayerquiztrivia.GENERATORS.BatchGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.LevelGenerators;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalPictureQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalSingleQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalVideoQuiz;
import com.nbird.multiplayerquiztrivia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultHandling {

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
    ArrayList<LottieAnimationView> animationList;
    int category;
    int IntentInt,timeTakenInt;


    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    //Single Mode Normal Quiz : 1

    public ResultHandling(Context context, HashMap<String, Integer> llMap, ArrayList<Boolean> animList,
                          int correctAnsInt, String timeTakenString, int lifeLineUsedInt, long totalScoreInt,
                          int higestScoreInt, int scoreInt, CardView view, String myNameString,
                          String myPicURL, int category, int IntentInt, int timeTakenInt) {
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
        this.category=category;
        this.IntentInt=IntentInt;
        this.timeTakenInt=timeTakenInt;
    }


    public void start(){


        Record record =new Record(scoreInt,correctAnsInt,category,context,view,timeTakenInt,myNameString,myPicURL);
        record.startLineGraph();
        record.startBarGroup();
        record.startPieChart();
        record.setLeaderBoard();



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

        LottieAnimationView anim1=(LottieAnimationView) viewRemove1.findViewById(R.id.anim11);
        LottieAnimationView anim2=(LottieAnimationView) viewRemove1.findViewById(R.id.anim12);
        LottieAnimationView anim3=(LottieAnimationView) viewRemove1.findViewById(R.id.anim13);
        LottieAnimationView anim4=(LottieAnimationView) viewRemove1.findViewById(R.id.anim14);
        LottieAnimationView anim5=(LottieAnimationView) viewRemove1.findViewById(R.id.anim15);
        LottieAnimationView anim6=(LottieAnimationView) viewRemove1.findViewById(R.id.anim16);
        LottieAnimationView anim7=(LottieAnimationView) viewRemove1.findViewById(R.id.anim17);
        LottieAnimationView anim8=(LottieAnimationView) viewRemove1.findViewById(R.id.anim18);
        LottieAnimationView anim9=(LottieAnimationView) viewRemove1.findViewById(R.id.anim19);
        LottieAnimationView anim10=(LottieAnimationView) viewRemove1.findViewById(R.id.anim20);

        Button homeButton=(Button) viewRemove1.findViewById(R.id.homeButton);
        Button changeCategory=(Button) viewRemove1.findViewById(R.id.changeCategory);
        Button reMatch=(Button) viewRemove1.findViewById(R.id.reMatch);
        Button leaderBoard=(Button) viewRemove1.findViewById(R.id.leaderBoard);

        LineChart lineChart = (LineChart) viewRemove1.findViewById(R.id.lineChart);

        record.getLineGraph(lineChart);

        LinearLayout linearLayoutexpert=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutexpert);
        LinearLayout linearLayoutAudience=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutAudience);
        LinearLayout linearLayoutSwap=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutSwap);
        LinearLayout linearLayoutfiftyfifty=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutfiftyfifty);

        animationList=new ArrayList<>();

        animationList.add(anim1); animationList.add(anim2); animationList.add(anim3);
        animationList.add(anim4); animationList.add(anim5); animationList.add(anim6);
        animationList.add(anim7); animationList.add(anim8); animationList.add(anim9);
        animationList.add(anim10);



        correctAnswer.setText("Correct/Wrong : "+correctAnsInt+"/"+String.valueOf(10-correctAnsInt));
        timeTaken.setText("Time Taken : "+timeTakenString);
        lifeLineUsed.setText("Life-Line Used : "+lifeLineUsedInt);

        LevelGenerators levelGenerators=new LevelGenerators(totalScoreInt,levelText);
        levelGenerators.start();

        BatchGenerator batchGenerator=new BatchGenerator(totalScoreInt,batch);
        batchGenerator.start();

        highestScore.setText("Your Highest Score : "+higestScoreInt);
        totalScore.setText("Total Score : "+scoreInt);

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



        for(int i=0;i<animationList.size();i++){
            if(animList.get(i)){
                animationList.get(i).setAnimation(R.raw.tickanim);
                animationList.get(i).playAnimation();
                animationList.get(i).loop(false);
            }else{
                animationList.get(i).setAnimation(R.raw.wronganim);
                animationList.get(i).playAnimation();
                animationList.get(i).loop(false);
            }

        }


        final AlertDialog alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent i=new Intent(context,MainActivity.class);
                context.startActivity(i);
                ((Activity)context).finish();
            }
        });

        if(IntentInt==2||IntentInt==3||IntentInt==4){
            changeCategory.setVisibility(View.GONE);
        }
        changeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                DialogCategory dialogCategory=new DialogCategory(context,view);
                dialogCategory.start();
            }
        });

        reMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                switch (IntentInt){
                    case 1:
                        Intent intent = new Intent(context, NormalSingleQuiz.class);
                        intent.putExtra("category", category);
                        view.getContext().startActivity(intent);
                        ((Activity)view.getContext()).finish();
                        break;
                    case 2:
                        Intent intent1 = new Intent(context, NormalPictureQuiz.class);
                        view.getContext().startActivity(intent1);
                        ((Activity)view.getContext()).finish();
                        break;
                    case 3:
                        Intent intent2 = new Intent(context, NormalAudioQuiz.class);
                        view.getContext().startActivity(intent2);
                        ((Activity)view.getContext()).finish();
                        break;
                    case 4:
                        Intent intent3 = new Intent(context, NormalVideoQuiz.class);
                        view.getContext().startActivity(intent3);
                        ((Activity)view.getContext()).finish();
                        break;
                }

            }
        });

        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               record.getLeaderBoard();
            }
        });

    }





}
