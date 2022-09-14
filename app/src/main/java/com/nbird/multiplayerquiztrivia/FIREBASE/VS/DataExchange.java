package com.nbird.multiplayerquiztrivia.FIREBASE.VS;

import android.app.Activity;
import android.app.Dialog;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.Dialog.DialogCategory;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.Record;
import com.nbird.multiplayerquiztrivia.GENERATORS.BatchGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.LevelGenerators;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.DataExchangeHolder;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalPictureQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalSingleQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalVideoQuiz;
import com.nbird.multiplayerquiztrivia.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DataExchange {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference myRef = database.getReference();

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
    int category;
    int IntentInt,timeTakenInt;
    Dialog loadingDialog;
    String oppoUID, oppoNameString, oppoImgStr;
    ArrayList<LottieAnimationView> animationList,animationListOppo;
    ValueEventListener valueEventListener;


    public DataExchange(Context context, HashMap<String, Integer> llMap, ArrayList<Boolean> animList, int correctAnsInt, String timeTakenString, int lifeLineUsedInt, long totalScoreInt, int higestScoreInt, int scoreInt, CardView view, String myNameString, String myPicURL, int category, int intentInt, int timeTakenInt, String oppoUID, String oppoName, String oppoImgStr,ArrayList<LottieAnimationView> animationList) {
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
        this.myNameString = myNameString;
        this.myPicURL = myPicURL;
        this.category = category;
        IntentInt = intentInt;
        this.timeTakenInt = timeTakenInt;
        this.oppoUID=oppoUID;
        this.oppoNameString=oppoName;
        this.oppoImgStr=oppoImgStr;
        this.animationList=animationList;
    }


    public void start(){
        mAuth = FirebaseAuth.getInstance();
        myDataDisplay();
    }

    private void myDataDisplay(){
        Record record =new Record(scoreInt,correctAnsInt,category,context,view,timeTakenInt,myNameString,myPicURL);
        record.startLineGraph();
        record.startBarGroup();
        record.setLeaderBoard();



        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.dialog_result_vs,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);




        TextView correctAnswer=(TextView) viewRemove1.findViewById(R.id.correctAnswer);
        TextView timeTaken=(TextView) viewRemove1.findViewById(R.id.timeTaken);
        TextView lifeLineUsed=(TextView) viewRemove1.findViewById(R.id.lifeLineUsed);
        TextView totalScore=(TextView) viewRemove1.findViewById(R.id.totalScore);
        TextView myName=(TextView) viewRemove1.findViewById(R.id.myName);


        TextView correctAnswerOppo=(TextView) viewRemove1.findViewById(R.id.correctAnswerOppo);
        TextView timeTakenOppo=(TextView) viewRemove1.findViewById(R.id.timeTakenOppo);
        TextView lifeLineUsedOppo=(TextView) viewRemove1.findViewById(R.id.lifeLineUsedOppo);
        TextView totalScoreOppo=(TextView) viewRemove1.findViewById(R.id.totalScoreOppo);
        TextView oppoName=(TextView) viewRemove1.findViewById(R.id.oppoName);

        ImageView myPic=(ImageView) viewRemove1.findViewById(R.id.myPic);

        ImageView oppoImage=(ImageView) viewRemove1.findViewById(R.id.oppoImage);

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


        LottieAnimationView anim11=(LottieAnimationView) viewRemove1.findViewById(R.id.anim21);
        LottieAnimationView anim12=(LottieAnimationView) viewRemove1.findViewById(R.id.anim22);
        LottieAnimationView anim13=(LottieAnimationView) viewRemove1.findViewById(R.id.anim23);
        LottieAnimationView anim14=(LottieAnimationView) viewRemove1.findViewById(R.id.anim24);
        LottieAnimationView anim15=(LottieAnimationView) viewRemove1.findViewById(R.id.anim25);
        LottieAnimationView anim16=(LottieAnimationView) viewRemove1.findViewById(R.id.anim26);
        LottieAnimationView anim17=(LottieAnimationView) viewRemove1.findViewById(R.id.anim27);
        LottieAnimationView anim18=(LottieAnimationView) viewRemove1.findViewById(R.id.anim28);
        LottieAnimationView anim19=(LottieAnimationView) viewRemove1.findViewById(R.id.anim29);
        LottieAnimationView anim20=(LottieAnimationView) viewRemove1.findViewById(R.id.anim30);

        Button homeButton=(Button) viewRemove1.findViewById(R.id.homeButton);
        Button changeCategory=(Button) viewRemove1.findViewById(R.id.changeCategory);
        Button reMatch=(Button) viewRemove1.findViewById(R.id.reMatch);




        LinearLayout linearLayoutexpert=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutexpert);
        LinearLayout linearLayoutAudience=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutAudience);
        LinearLayout linearLayoutSwap=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutSwap);
        LinearLayout linearLayoutfiftyfifty=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutfiftyfifty);

        LinearLayout linearLayoutexpertOppo=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutexpertOppo);
        LinearLayout linearLayoutAudienceOppo=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutAudienceOppo);
        LinearLayout linearLayoutSwapOppo=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutSwapOppo);
        LinearLayout linearLayoutfiftyfiftyOppo=(LinearLayout) viewRemove1.findViewById(R.id.linearLayoutfiftyfiftyOppo);


        animationList=new ArrayList<>();
        animationListOppo=new ArrayList<>();

        animationList.add(anim1); animationList.add(anim2); animationList.add(anim3);
        animationList.add(anim4); animationList.add(anim5); animationList.add(anim6);
        animationList.add(anim7); animationList.add(anim8); animationList.add(anim9);
        animationList.add(anim10);


        animationListOppo.add(anim11); animationListOppo.add(anim12); animationListOppo.add(anim13);
        animationListOppo.add(anim14); animationListOppo.add(anim15); animationListOppo.add(anim16);
        animationListOppo.add(anim17); animationListOppo.add(anim18); animationListOppo.add(anim19);
        animationListOppo.add(anim20);


        dataUpload(correctAnswerOppo,timeTakenOppo,lifeLineUsedOppo,totalScoreOppo,oppoName,oppoImage
        ,linearLayoutexpertOppo,linearLayoutAudienceOppo,linearLayoutSwapOppo,linearLayoutfiftyfiftyOppo,animationListOppo);


        correctAnswer.setText("Correct/Wrong : "+correctAnsInt+"/"+String.valueOf(10-correctAnsInt));
        timeTaken.setText("Time Taken : "+timeTakenString);
        lifeLineUsed.setText("Life-Line Used : "+lifeLineUsedInt);


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
                Intent i=new Intent(context, MainActivity.class);
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
//                switch (IntentInt){
//                    case 1:
//                        Intent intent = new Intent(context, NormalSingleQuiz.class);
//                        intent.putExtra("category", category);
//                        view.getContext().startActivity(intent);
//                        ((Activity)view.getContext()).finish();
//                        break;
//                    case 2:
//                        Intent intent1 = new Intent(context, NormalPictureQuiz.class);
//                        view.getContext().startActivity(intent1);
//                        ((Activity)view.getContext()).finish();
//                        break;
//                    case 3:
//                        Intent intent2 = new Intent(context, NormalAudioQuiz.class);
//                        view.getContext().startActivity(intent2);
//                        ((Activity)view.getContext()).finish();
//                        break;
//                    case 4:
//                        Intent intent3 = new Intent(context, NormalVideoQuiz.class);
//                        view.getContext().startActivity(intent3);
//                        ((Activity)view.getContext()).finish();
//                        break;
//                }

            }
        });
    }

    private void dataUpload(TextView correctAnswerOppo, TextView timeTakenOppo, TextView lifeLineUsedOppo, TextView totalScoreOppo, TextView oppoName, ImageView oppoImage, LinearLayout linearLayoutexpertOppo, LinearLayout linearLayoutAudienceOppo, LinearLayout linearLayoutSwapOppo, LinearLayout linearLayoutfiftyfiftyOppo, ArrayList<LottieAnimationView> animationListOppo){

        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(loadingDialog,context);
        supportAlertDialog.showLoadingDialog();


        DataExchangeHolder dataExchangeHolder=new DataExchangeHolder(llMap,animList,correctAnsInt,timeTakenString,lifeLineUsedInt,totalScoreInt,
                scoreInt,myNameString,myPicURL,timeTakenInt);

        table_user.child("VS_PLAY").child("DataExchange").child(mAuth.getCurrentUser().getUid()).setValue(dataExchangeHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                valueEventListener=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            DataExchangeHolder oppoHolder=snapshot.getValue(DataExchangeHolder.class);
                            correctAnswerOppo.setText("Correct/Wrong : "+oppoHolder.getCorrectAnsInt()+"/"+String.valueOf(10-oppoHolder.getCorrectAnsInt()));
                            timeTakenOppo.setText("Time Taken : "+oppoHolder.getTimeTakenString());
                            lifeLineUsedOppo.setText("Life-Line Used : "+oppoHolder.getLifeLineUsedInt());
                            totalScoreOppo.setText("Total Score : "+oppoHolder.getScoreInt());

                            oppoName.setText(oppoNameString);
                            Glide.with(context).load(oppoImgStr).apply(RequestOptions
                                            .bitmapTransform(new RoundedCorners(18)))
                                    .into(oppoImage);


                            if(oppoHolder.getLlMap().get("Expert")==1){
                                linearLayoutexpertOppo.setBackgroundResource(R.drawable.usedicon);
                            }

                            if(oppoHolder.getLlMap().get("Flip")==1){
                                linearLayoutSwapOppo.setBackgroundResource(R.drawable.usedicon);
                            }

                            if(oppoHolder.getLlMap().get("Audience")==1){
                                linearLayoutAudienceOppo.setBackgroundResource(R.drawable.usedicon);
                            }

                            if(oppoHolder.getLlMap().get("Fifty-Fifty")==1){
                                linearLayoutfiftyfiftyOppo.setBackgroundResource(R.drawable.usedicon);
                            }


                            for(int i=0;i<oppoHolder.getAnimList().size();i++){
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

                            table_user.child("VS_PLAY").child("DataExchange").child(oppoUID).removeEventListener(valueEventListener);
                            table_user.child("VS_PLAY").child("DataExchange").child(oppoUID).removeValue();

                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                table_user.child("VS_PLAY").child("DataExchange").child(oppoUID).addValueEventListener(valueEventListener);
            }
        });
    }

}
