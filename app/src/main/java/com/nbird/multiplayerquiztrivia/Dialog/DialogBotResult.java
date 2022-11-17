package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nbird.multiplayerquiztrivia.BOT.BotRequestDialog;
import com.nbird.multiplayerquiztrivia.BOT.VsBOTAudioQuiz;
import com.nbird.multiplayerquiztrivia.BOT.VsBOTNormalQuiz;
import com.nbird.multiplayerquiztrivia.BOT.VsBOTPictureQuiz;
import com.nbird.multiplayerquiztrivia.BOT.VsBOTVideoQuiz;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.Record;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class DialogBotResult {


    int scoreInt,correctAnsInt,category,timeTakenInt,lifeLineUsedInt;
    int oppoScoreInt,oppoCorrectAnsInt,oppoTimeTakenInt,oppoLifeLineUsedInt;
    CardView view;
    Context context;

    String myNameString,myPicURL,timeTakenString;
    String oppoNameString,oppoPicURL,oppoTimeTakenString;


    ArrayList<LottieAnimationView> animationList;
    ArrayList<LottieAnimationView> animationListOppo;

    Map<String,Integer> llMap,oppoLLMap;
    ArrayList<Boolean> animList,oppoAnimList;
    View vv;

    CountDownTimer countDownTimerRematch,countDownTimerBot,countDownTimerBotOffline;
    boolean isRequestSend=false;
    int mode;


    boolean isBotOnline=true;
    public DialogBotResult(int scoreInt, int correctAnsInt, int category, int timeTakenInt, int lifeLineUsedInt, int oppoScoreInt, int oppoCorrectAnsInt, int oppoTimeTakenInt, int oppoLifeLineUsedInt, CardView view, Context context, String myNameString, String myPicURL, String timeTakenString, String oppoNameString, String oppoPicURL, String oppoTimeTakenString, Map<String, Integer> llMap, Map<String, Integer> oppoLLMap, ArrayList<Boolean> animList, ArrayList<Boolean> oppoAnimList,View vv,int mode) {
        this.scoreInt = scoreInt;
        this.correctAnsInt = correctAnsInt;
        this.category = category;
        this.timeTakenInt = timeTakenInt;
        this.lifeLineUsedInt = lifeLineUsedInt;
        this.oppoScoreInt = oppoScoreInt;
        this.oppoCorrectAnsInt = oppoCorrectAnsInt;
        this.oppoTimeTakenInt = oppoTimeTakenInt;
        this.oppoLifeLineUsedInt = oppoLifeLineUsedInt;
        this.view = view;
        this.context = context;
        this.myNameString = myNameString;
        this.myPicURL = myPicURL;
        this.timeTakenString = timeTakenString;
        this.oppoNameString = oppoNameString;
        this.oppoPicURL = oppoPicURL;
        this.oppoTimeTakenString = oppoTimeTakenString;
        this.llMap = llMap;
        this.oppoLLMap = oppoLLMap;
        this.animList = animList;
        this.oppoAnimList = oppoAnimList;
        this.vv=vv;
        this.mode=mode;
    }

    public void start(){



        Record record =new Record(scoreInt,correctAnsInt,category,context,view,timeTakenInt,myNameString,myPicURL);
        record.startLineGraph();
        record.startBarGroup();
        record.setLeaderBoard();



        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.dialog_result_vs,(ConstraintLayout) vv.findViewById(R.id.layoutDialogContainer),false);
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

        ImageView oppoImage=(ImageView) viewRemove1.findViewById(R.id.oppoPic);

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
        Button reMatch=(Button) viewRemove1.findViewById(R.id.reMatch);

        LottieAnimationView partyPopper=(LottieAnimationView) viewRemove1.findViewById(R.id.party_popper);


        TextView result=(TextView) viewRemove1.findViewById(R.id.result);



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



        correctAnswer.setText("Correct/Wrong : "+correctAnsInt+"/"+String.valueOf(10-correctAnsInt));
        timeTaken.setText("Time Taken : "+timeTakenString);
        lifeLineUsed.setText("Life-Line Used : "+lifeLineUsedInt);


        correctAnswerOppo.setText("Correct/Wrong : "+oppoCorrectAnsInt+"/"+String.valueOf(10-oppoCorrectAnsInt));
        timeTakenOppo.setText("Time Taken : "+oppoTimeTakenString);
        lifeLineUsedOppo.setText("Life-Line Used : "+oppoLifeLineUsedInt);



        totalScore.setText("Total Score : "+scoreInt);

        totalScoreOppo.setText("Total Score : "+oppoScoreInt);


        if(scoreInt>oppoScoreInt){
            result.setText(myNameString+" Won");
            partyPopper.setAnimation(R.raw.party_popper);
            partyPopper.playAnimation();
            partyPopper.loop(false);
        }else if(scoreInt<oppoScoreInt){
            result.setText(oppoNameString+" Won");
        }else{
            result.setText(myNameString+" Won");
            partyPopper.setAnimation(R.raw.party_popper);
            partyPopper.playAnimation();
            partyPopper.loop(false);
        }

        myName.setText(myNameString);
        Glide.with(context).load(myPicURL).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(myPic);


        oppoName.setText(oppoNameString);
        Glide.with(context).load(oppoPicURL).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(oppoImage);


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


        if(oppoLLMap.get("Expert")==1){
            linearLayoutexpertOppo.setBackgroundResource(R.drawable.usedicon);
        }

        if(oppoLLMap.get("Flip")==1){
            linearLayoutSwapOppo.setBackgroundResource(R.drawable.usedicon);
        }

        if(oppoLLMap.get("Audience")==1){
            linearLayoutAudienceOppo.setBackgroundResource(R.drawable.usedicon);
        }

        if(oppoLLMap.get("Fifty-Fifty")==1){
            linearLayoutfiftyfiftyOppo.setBackgroundResource(R.drawable.usedicon);
        }






        for(int i=0;i<animationList.size();i++){
            try{
                if(animList.get(i)){
                    animationList.get(i).setAnimation(R.raw.tickanim);
                    animationList.get(i).playAnimation();
                    animationList.get(i).loop(false);
                }else{
                    animationList.get(i).setAnimation(R.raw.wronganim);
                    animationList.get(i).playAnimation();
                    animationList.get(i).loop(false);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        for(int i=0;i<animationListOppo.size();i++){
            try{
                if(oppoAnimList.get(i)){
                    animationListOppo.get(i).setAnimation(R.raw.tickanim);
                    animationListOppo.get(i).playAnimation();
                    animationListOppo.get(i).loop(false);
                }else{
                    animationListOppo.get(i).setAnimation(R.raw.wronganim);
                    animationListOppo.get(i).playAnimation();
                    animationListOppo.get(i).loop(false);
                }
            }catch (Exception e){
                e.printStackTrace();
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

                try{
                    countDownTimerRematch.cancel();
                }catch (Exception e){

                }

                try{
                    countDownTimerBot.cancel();
                }catch (Exception e){

                }

                Intent i=new Intent(context, MainActivity.class);
                context.startActivity(i);
                ((Activity)context).finish();

            }
        });


        countDownTimerBotOffline=new CountDownTimer(1000*60,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                isBotOnline=false;
            }
        }.start();


        reMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reMatch.setVisibility(View.GONE);

                isRequestSend=true;

                if(isBotOnline){
                    Toast.makeText(context, "Rematch request send to the opponent", Toast.LENGTH_SHORT).show();
                    Random random=new Random();
                    int rr=random.nextInt(15);
                    int kk=random.nextInt(5)+2;



                    countDownTimerRematch=new CountDownTimer(kk*1000,1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {

                            isRequestSend=false;

                            if(rr<12){

                                try{
                                    countDownTimerRematch.cancel();
                                }catch (Exception e){

                                }

                                try{
                                    countDownTimerBot.cancel();
                                }catch (Exception e){

                                }
                                try{
                                    countDownTimerBotOffline.cancel();
                                }catch (Exception e){

                                }

                                Toast.makeText(context, "The request was accepted by the opponent.", Toast.LENGTH_LONG).show();
                                if(mode==1){
                                    Intent i=new Intent(context, VsBOTPictureQuiz.class);
                                    i.putExtra("oppoName",oppoNameString);
                                    i.putExtra("oppoImageURL",oppoPicURL);
                                    context.startActivity(i);
                                    ((Activity)context).finish();
                                }else if(mode==2){
                                    Intent i=new Intent(context, VsBOTNormalQuiz.class);
                                    i.putExtra("oppoName",oppoNameString);
                                    i.putExtra("oppoImageURL",oppoPicURL);
                                    context.startActivity(i);
                                    ((Activity)context).finish();
                                }else if(mode==3){
                                    Intent i=new Intent(context, VsBOTAudioQuiz.class);
                                    i.putExtra("oppoName",oppoNameString);
                                    i.putExtra("oppoImageURL",oppoPicURL);
                                    context.startActivity(i);
                                    ((Activity)context).finish();
                                }else{
                                    Intent i=new Intent(context, VsBOTVideoQuiz.class);
                                    i.putExtra("oppoName",oppoNameString);
                                    i.putExtra("oppoImageURL",oppoPicURL);
                                    context.startActivity(i);
                                    ((Activity)context).finish();
                                }

                            }else if(rr<14){
                                Toast.makeText(context, "The request was decline by the opponent.", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, "Opponent has left the game.Please try to find some other opponent.", Toast.LENGTH_LONG).show();
                            }

                        }
                    }.start();
                }else{
                    Toast.makeText(context, "Opponent has left the room.Please find some other opponent!", Toast.LENGTH_LONG).show();
                }


            }
        });

        botRequestSend();

    }


    private void botRequestSend(){
        Random random=new Random();
        int rr=random.nextInt(15)+8;
        countDownTimerBot=new CountDownTimer(rr*1000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                Random random1=new Random();
                int bb=random1.nextInt(10);


                if(bb<8){
                    if(!isRequestSend){

                            String title=oppoNameString+" has send you request for a rematch.";
                            BotRequestDialog botRequestDialog=new BotRequestDialog(context,vv,R.raw.rematch_request,title,mode,oppoNameString,oppoPicURL);
                            botRequestDialog.start();


                    }
                }

            }
        }.start();
    }





}
