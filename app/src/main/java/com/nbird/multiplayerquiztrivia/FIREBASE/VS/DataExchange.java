package com.nbird.multiplayerquiztrivia.FIREBASE.VS;

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
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.EXTRA.MarksSetter;
import com.nbird.multiplayerquiztrivia.FIREBASE.AnswerUploaderAndReceiver;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.Record;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.DataExchangeHolder;
import com.nbird.multiplayerquiztrivia.QUIZ.VsAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsNormalQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsPictureQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsVideoQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DataExchange {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference myRef = database.getReference();

    Context context;
    HashMap<String, Integer> llMap;
    ArrayList<Boolean> animList;
    int correctAnsInt,mode;
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
    ValueEventListener valueEventListener, requestEventListener,lisnerForConnectionStatus,vsRematchListener,isCompletedListener;
    ArrayList<Integer> listAns;
    int value=1;
    AnswerUploaderAndReceiver answerUploaderAndReceiver;
    CountDownTimer countDownTimer;
    LottieAnimationView party_popper;


    public boolean rematchButtonEnable;

    public Button reMatch;

    TextView marksCorrectAnswerOppo;
    TextView marksTimeTakenOppo;
    TextView marksLifeLineUsedOppo;


    public DataExchange(Context context, HashMap<String, Integer> llMap, ArrayList<Boolean> animList, int correctAnsInt, String timeTakenString, int lifeLineUsedInt, long totalScoreInt, int higestScoreInt, int scoreInt, CardView view, String myNameString, String myPicURL, int category, int intentInt, int timeTakenInt, String oppoUID, String oppoName, String oppoImgStr, ArrayList<LottieAnimationView> animationList, int mode, ValueEventListener lisnerForConnectionStatus, AnswerUploaderAndReceiver answerUploaderAndReceiver,ValueEventListener vsRematchListener,ValueEventListener isCompletedListener,CountDownTimer countDownTimer,LottieAnimationView party_popper,boolean rematchButtonEnable) {
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
        this.mode=mode;
        this.lisnerForConnectionStatus=lisnerForConnectionStatus;
        this.answerUploaderAndReceiver=answerUploaderAndReceiver;
        this.vsRematchListener=vsRematchListener;
        this.isCompletedListener=isCompletedListener;
        this.countDownTimer=countDownTimer;
        this.party_popper=party_popper;
        this.rematchButtonEnable=rematchButtonEnable;
    }


    public void start(){




        listAns=new ArrayList<>();




        mAuth = FirebaseAuth.getInstance();



        lisnerForConnectionStatus=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    value=snapshot.getValue(Integer.class);

                    if(value==0){
                        try{
                            table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").addValueEventListener(lisnerForConnectionStatus);




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


        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)){
            MobileAds.initialize(context);
            AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);

                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = viewRemove1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }



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
         reMatch=(Button) viewRemove1.findViewById(R.id.reMatch);

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


        dataUpload(correctAnswerOppo,timeTakenOppo,lifeLineUsedOppo,totalScoreOppo,oppoName,oppoImage
        ,linearLayoutexpertOppo,linearLayoutAudienceOppo,linearLayoutSwapOppo,linearLayoutfiftyfiftyOppo,animationListOppo,result,party_popper);


        correctAnswer.setText("Correct/Wrong : "+correctAnsInt+"/"+String.valueOf(10-correctAnsInt));
        timeTaken.setText("Time Taken : "+timeTakenString);
        lifeLineUsed.setText("Life-Line Used : "+lifeLineUsedInt);


        totalScore.setText("Total Score : "+scoreInt);


        TextView marksCorrectAnswer=(TextView) viewRemove1.findViewById(R.id.marksCorrectAnswer);
        TextView marksTimeTaken=(TextView) viewRemove1.findViewById(R.id.marksTimeTaken);
        TextView marksLifeLineUsed=(TextView) viewRemove1.findViewById(R.id.marksLifeLineUsed);

        MarksSetter marksSetter=new MarksSetter(marksCorrectAnswer,marksTimeTaken,marksLifeLineUsed,correctAnsInt,timeTakenInt,lifeLineUsedInt);
        marksSetter.start();


         marksCorrectAnswerOppo=(TextView) viewRemove1.findViewById(R.id.marksCorrectAnswerOppo);
         marksTimeTakenOppo=(TextView) viewRemove1.findViewById(R.id.marksTimeTakenOppo);
         marksLifeLineUsedOppo=(TextView) viewRemove1.findViewById(R.id.marksLifeLineUsedOppo);


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

                Dialog loadingDialog = null;
                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(loadingDialog,context);
                supportAlertDialog.showLoadingDialog();


                table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        try{
                            table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        try {
                            table_user.child("VS_RESPONSE").child(oppoUID).removeEventListener(requestEventListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        table_user.child("VS_RESPONSE").child(oppoUID).removeValue();

                        table_user.child("VS_PLAY").child("PlayerCurrentAns").child(mAuth.getCurrentUser().getUid()).removeValue();

                        answerUploaderAndReceiver.removeAnimListener(oppoUID);

                        try{table_user.child("VS_PLAY").child("IsDone").child(oppoUID).removeEventListener(isCompletedListener);}catch (Exception e){}

                        supportAlertDialog.dismissLoadingDialog();






                            try{table_user.child("VS_REQUEST").child(oppoUID).removeEventListener(vsRematchListener);}catch (Exception e){}


                        alertDialog.dismiss();
                        Intent i=new Intent(context, MainActivity.class);
                        context.startActivity(i);
                        ((Activity)context).finish();
                    }
                });

            }
        });



        reMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               rematchButtonEnable=false;




                if (value == 0) {
                    Toast.makeText(context, "Opponent has left the room", Toast.LENGTH_SHORT).show();
                    Log.i("VALUE ","1");
                } else {
                    Log.i("VALUE ","2");

                Toast.makeText(context, "Rematch request send to the opponent", Toast.LENGTH_SHORT).show();
              //  reMatch.setEnabled(false);
              //  reMatch.setAlpha(0.7f);
                reMatch.setVisibility(View.GONE);

                table_user.child("VS_PLAY").child("IsDone").child(mAuth.getCurrentUser().getUid()).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        QuestionGenerator();



                    }
                });
            }
            }
        });
    }

    public void QuestionGenerator() {


        switch (mode) {
            case 1:
                pictureQuizNumberUploader(listAns);
                break;
            case 2:
                normalQuizNumberUploader(listAns);
                break;
            case 3:
                audioQuizNumberUploader(listAns);
                break;
            case 4:
                vidioQuizNUmberUploader(listAns);
                break;
        }
    }


    public void rematchMethod(){
        table_user.child("VS_REQUEST").child(mAuth.getCurrentUser().getUid()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                requestEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            Boolean requestResponse = snapshot.getValue(Boolean.class);
                            if (requestResponse) {

                                Toast.makeText(context, "The request was accepted by the opponent", Toast.LENGTH_SHORT).show();

                                try {
                                    table_user.child("VS_RESPONSE").child(oppoUID).removeEventListener(requestEventListener);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }



                                table_user.child("VS_RESPONSE").child(oppoUID).removeValue();

                                try{
                                    table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                table_user.child("VS_PLAY").child("PlayerCurrentAns").child(mAuth.getCurrentUser().getUid()).removeValue();

                                answerUploaderAndReceiver.removeAnimListener(oppoUID);

                                try{table_user.child("VS_REQUEST").child(oppoUID).removeEventListener(vsRematchListener);}catch (Exception e){}

                                try{table_user.child("VS_PLAY").child("IsDone").child(oppoUID).removeEventListener(isCompletedListener);}catch (Exception e){}

                                //  table_user.child("VS_PLAY").child("DataExchange").child(mAuth.getCurrentUser().getUid()).removeValue();

                                if(countDownTimer!=null){ countDownTimer.cancel();}

                                switch (mode) {
                                    case 2:
                                        Intent intent = new Intent(context, VsNormalQuiz.class);
                                        intent.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) listAns);
                                        intent.putExtra("playerNum", 1);
                                        intent.putExtra("oppoImgStr", oppoImgStr);
                                        intent.putExtra("oppoName", oppoNameString);
                                        intent.putExtra("oppoUID", oppoUID);
                                        intent.putExtra("mode", mode);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                        break;
                                    case 1:
                                        Intent intent1 = new Intent(context, VsPictureQuiz.class);
                                        intent1.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) listAns);
                                        intent1.putExtra("playerNum", 1);
                                        intent1.putExtra("oppoImgStr", oppoImgStr);
                                        intent1.putExtra("oppoName", oppoNameString);
                                        intent1.putExtra("oppoUID", oppoUID);
                                        intent1.putExtra("mode", mode);
                                        context.startActivity(intent1);
                                        ((Activity) context).finish();
                                        break;
                                    case 3:
                                        Intent intent2 = new Intent(context, VsAudioQuiz.class);
                                        intent2.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) listAns);
                                        intent2.putExtra("playerNum", 1);
                                        intent2.putExtra("oppoImgStr", oppoImgStr);
                                        intent2.putExtra("oppoName", oppoNameString);
                                        intent2.putExtra("oppoUID", oppoUID);
                                        intent2.putExtra("mode", mode);
                                        context.startActivity(intent2);
                                        ((Activity) context).finish();
                                        break;
                                    case 4:
                                        Intent intent3 = new Intent(context, VsVideoQuiz.class);
                                        intent3.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) listAns);
                                        intent3.putExtra("playerNum", 1);
                                        intent3.putExtra("oppoImgStr", oppoImgStr);
                                        intent3.putExtra("oppoName", oppoNameString);
                                        intent3.putExtra("oppoUID", oppoUID);
                                        intent3.putExtra("mode", mode);
                                        context.startActivity(intent3);
                                        ((Activity) context).finish();
                                        break;
                                }

                            } else {

                                rematchButtonEnable=true;


                                Toast.makeText(context, "The request was decline by the opponent", Toast.LENGTH_LONG).show();
                                try {
                                    table_user.child("VS_RESPONSE").child(oppoUID).addValueEventListener(requestEventListener);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                table_user.child("VS_RESPONSE").child(oppoUID).removeValue();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                table_user.child("VS_RESPONSE").child(oppoUID).addValueEventListener(requestEventListener);

            }
        });
    }



    public void normalQuizNumberUploader(ArrayList<Integer> listAns){
        listAns.clear();
        Random random=new Random();
        for(int i=0;i<11;i++){
            listAns.add(random.nextInt(6326)+1);
        }

        table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                rematchMethod();

            }
        });
    }

    public void pictureQuizNumberUploader(ArrayList<Integer> listAns){
        listAns.clear();
        Random random=new Random();

        for(int i=0;i<11;i++){
            int setNumber = random.nextInt(4999)+1;
            if(setNumber>1210&&setNumber<2000){
                setNumber=setNumber-1000;
            }
            listAns.add(setNumber);
        }
        table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                rematchMethod();
            }
        });
    }

    public void audioQuizNumberUploader(ArrayList<Integer> listAns){
        listAns.clear();
        Random random=new Random();
        myRef.child("QUIZNUMBERS").child("AudioQuestionQuantity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num;
                try{
                    num=snapshot.getValue(Integer.class);
                }catch (Exception e){
                    num=3464;
                }
                for(int i=0;i<11;i++){
                    int setNumber = random.nextInt(num)+1;
                    listAns.add(setNumber);
                }

                table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        rematchMethod();
                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void vidioQuizNUmberUploader(ArrayList<Integer> listAns){
        listAns.clear();
        Random random=new Random();
        myRef.child("QUIZNUMBERS").child("VideoQuestionQuantity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num;
                try{
                    num=snapshot.getValue(Integer.class);
                }catch (Exception e){
                    num=2719;
                }
                for(int i=0;i<11;i++){
                    int setNumber = random.nextInt(num)+1;
                    listAns.add(setNumber);
                }

                table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        rematchMethod();
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







    private void dataUpload(TextView correctAnswerOppo, TextView timeTakenOppo, TextView lifeLineUsedOppo, TextView totalScoreOppo, TextView oppoName, ImageView oppoImage, LinearLayout linearLayoutexpertOppo, LinearLayout linearLayoutAudienceOppo, LinearLayout linearLayoutSwapOppo, LinearLayout linearLayoutfiftyfiftyOppo, ArrayList<LottieAnimationView> animationListOppo,TextView result,LottieAnimationView party_popper){



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


                            MarksSetter marksSetterOppo=new MarksSetter(marksCorrectAnswerOppo,marksTimeTakenOppo,marksLifeLineUsedOppo,oppoHolder.getCorrectAnsInt(),oppoHolder.getTimeTakenInt(),oppoHolder.getLifeLineUsedInt());
                            marksSetterOppo.start();



                            table_user.child("VS_PLAY").child("DataExchange").child(oppoUID).removeEventListener(valueEventListener);
                     //       table_user.child("VS_PLAY").child("DataExchange").child(oppoUID).removeValue();
                            if(scoreInt>oppoHolder.getScoreInt()){
                               // party_popper.setAnimation(R.raw.tickanim);
                                party_popper.setVisibility(View.VISIBLE);
                                party_popper.playAnimation();
                                party_popper.loop(false);
                                result.setText(myNameString+" Won");
                            }else if(scoreInt<oppoHolder.getScoreInt()){
                                result.setText(oppoHolder.getMyNameString()+" Won");
                            }else{
                                result.setText("Draw");
                            }

                            oppoName.setText(oppoHolder.getMyNameString());
                            Log.i("name",oppoHolder.getMyNameString());
                            Glide.with(context).load(oppoHolder.getMyPicURL()).apply(RequestOptions
                                            .bitmapTransform(new RoundedCorners(18)))
                                    .into(oppoImage);

                            HashMap<String,Integer> map=oppoHolder.getLlMap();
                            Log.i("Map",map.toString());
                            if(map.get("Expert")==1){
                                linearLayoutexpertOppo.setBackgroundResource(R.drawable.usedicon);
                            }

                            if(map.get("Flip")==1){
                                linearLayoutSwapOppo.setBackgroundResource(R.drawable.usedicon);
                            }

                            if(map.get("Audience")==1){
                                linearLayoutAudienceOppo.setBackgroundResource(R.drawable.usedicon);
                            }

                            if(map.get("Fifty-Fifty")==1){
                                linearLayoutfiftyfiftyOppo.setBackgroundResource(R.drawable.usedicon);
                            }

                            ArrayList<Boolean> animList=oppoHolder.getAnimList();
                            for(int i=0;i<animList.size();i++){
                                if(animList.get(i)){
                                    animationListOppo.get(i).setAnimation(R.raw.tickanim);
                                    animationListOppo.get(i).playAnimation();
                                    animationListOppo.get(i).loop(false);
                                }else{
                                    animationListOppo.get(i).setAnimation(R.raw.wronganim);
                                    animationListOppo.get(i).playAnimation();
                                    animationListOppo.get(i).loop(false);
                                }
                            }

                        }catch (Exception e){
                           e.printStackTrace();
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
