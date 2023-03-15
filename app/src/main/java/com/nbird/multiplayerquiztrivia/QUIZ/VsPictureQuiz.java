package com.nbird.multiplayerquiztrivia.QUIZ;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.BOT.VsBOTPictureQuiz;
import com.nbird.multiplayerquiztrivia.Dialog.DialogModel_1;
import com.nbird.multiplayerquiztrivia.Dialog.QuizCancelDialog;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.Dialog.Vs_Response;
import com.nbird.multiplayerquiztrivia.Dialog.WaitingVSInGameDialog;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.FIREBASE.HighestScore;
import com.nbird.multiplayerquiztrivia.FIREBASE.AnswerUploaderAndReceiver;
import com.nbird.multiplayerquiztrivia.FIREBASE.TotalScore;
import com.nbird.multiplayerquiztrivia.FIREBASE.VS.DataExchange;
import com.nbird.multiplayerquiztrivia.GENERATORS.ScoreGenerator;
import com.nbird.multiplayerquiztrivia.LL.LLManupulator;
import com.nbird.multiplayerquiztrivia.LL.LifeLine;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.questionHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class VsPictureQuiz extends AppCompatActivity {

    TextView questionTextView,scoreBoard,timerText,oppoNameTextView,myNameTextView;
    Button option1,option2,option3,option4,nextButton;
    LinearLayout linearLayout,linearLayoutexpert,linearLayoutAudience,linearLayoutFiftyFifty,linearLayoutSwap;
    CardView audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL,clockCardView;
    LottieAnimationView anim11,anim12,anim13,anim14,anim15,anim16,anim17,anim18,anim19,anim20;
    LottieAnimationView anim21,anim22,anim23,anim24,anim25,anim26,anim27,anim28,anim29,anim30;
    ImageView myPic,picOppo,questionImage;
    Dialog loadingDialog;
    CountDownTimer countDownTimer,c;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    private List<questionHolder> list;
    ArrayList<LottieAnimationView> animationList;
    ArrayList<Boolean> animList;

    int fiftyfiftynum=0,audiencenum=0,swapnum=0,expertnum=0,lifelineSum=0,position=0,num=0,score=0,myPosition=-1,count,category;
    String myName,myPicURL;

    AppData appData;
    SongActivity songActivity;
    LLManupulator llManupulator;
    LifeLine lifeLine;
    SupportAlertDialog supportAlertDialog;
    TotalScore totalScore;
    HighestScore highestScore;
    AnswerUploaderAndReceiver answerUploaderAndReceiver;

    ArrayList<Integer> ansArray;

    int playerNum,mode;
    String oppoUID,oppoName,oppoImgStr;
    ValueEventListener isCompletedListener,vsRematchListener,lisnerForConnectionStatus,myConnectionLisner;
    DialogModel_1 dialogModel_1;
    int starter=1;



    int minutes=3;
    int second=0;
    String minutestext;
    String secondtext,timeTakenString;
    int timeTakenInt;

    LottieAnimationView party_popper;


    CardView timerCard;

    boolean rematchButtonEnable=true;
    DataExchange dataExchange;

    NativeAd NATIVE_ADS;


    private InterstitialAd mInterstitialAd;
    private void loadAds(){


        String key=AppString.INTERSTITIAL_ID;

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, key, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });


    }
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vs_picture_quiz);



        list=new ArrayList<>();
        appData=new AppData();
        animationList=new ArrayList<>();
        animList=new ArrayList<>();
        ansArray = new ArrayList<>();


        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, VsPictureQuiz.this)){
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);


            Random r=new Random();
            int num=r.nextInt(AppString.ADS_FREQUENCY_PICTURE);
            if(num==1){
                loadAds();
            }
        }

        answerUploaderAndReceiver=new AnswerUploaderAndReceiver();


        songStopperAndResumer();

         timerCard=(CardView) findViewById(R.id.timerCard);


        questionTextView=findViewById(R.id.question);
        scoreBoard=findViewById(R.id.questionNumber);
        option1=(Button) findViewById(R.id.button1);
        option2=(Button) findViewById(R.id.button2);
        option3=(Button) findViewById(R.id.button3);
        option4=(Button) findViewById(R.id.button4);
        nextButton=(Button) findViewById(R.id.nextbutton);
        linearLayout=(LinearLayout) findViewById(R.id.linearButtonlayout);
        timerText=(TextView) findViewById(R.id.timer);
        audienceLL=(CardView) findViewById(R.id.audience);
        expertAdviceLL=(CardView) findViewById(R.id.expert);
        fiftyfiftyLL=(CardView) findViewById(R.id.fiftyfifty);
        swapTheQuestionLL=(CardView) findViewById(R.id.swap);
        linearLayoutexpert=(LinearLayout) findViewById(R.id.linearLayoutexpert) ;
        linearLayoutAudience=(LinearLayout) findViewById(R.id.linearLayoutAudience) ;
        linearLayoutFiftyFifty=(LinearLayout) findViewById(R.id.linearLayoutfiftyfifty) ;
        linearLayoutSwap=(LinearLayout) findViewById(R.id.linearLayoutSwap) ;
        anim11=(LottieAnimationView) findViewById(R.id.anim11);anim12=(LottieAnimationView) findViewById(R.id.anim12);anim13=(LottieAnimationView) findViewById(R.id.anim13);
        anim14=(LottieAnimationView) findViewById(R.id.anim14);anim15=(LottieAnimationView) findViewById(R.id.anim15);anim16=(LottieAnimationView) findViewById(R.id.anim16);
        anim17=(LottieAnimationView) findViewById(R.id.anim17);anim18=(LottieAnimationView) findViewById(R.id.anim18);anim19=(LottieAnimationView) findViewById(R.id.anim19);
        anim20=(LottieAnimationView) findViewById(R.id.anim20);anim21=(LottieAnimationView) findViewById(R.id.anim21);anim22=(LottieAnimationView) findViewById(R.id.anim22);
        anim23=(LottieAnimationView) findViewById(R.id.anim23);anim24=(LottieAnimationView) findViewById(R.id.anim24);anim25=(LottieAnimationView) findViewById(R.id.anim25);
        anim26=(LottieAnimationView) findViewById(R.id.anim26);anim27=(LottieAnimationView) findViewById(R.id.anim27);anim28=(LottieAnimationView) findViewById(R.id.anim28);
        anim29=(LottieAnimationView) findViewById(R.id.anim29);anim30=(LottieAnimationView) findViewById(R.id.anim30);
        myPic=(ImageView) findViewById(R.id.myPic);
        clockCardView = (CardView) findViewById(R.id.cardView3);

        picOppo=(ImageView) findViewById(R.id.picOppo);
        oppoNameTextView=(TextView) findViewById(R.id.oppoNameTextView);
        myNameTextView=(TextView) findViewById(R.id.myNameTextView);

        questionImage=(ImageView) findViewById(R.id.questionImage);

        party_popper=(LottieAnimationView) findViewById(R.id.party_popper);


        playerNum=getIntent().getIntExtra("playerNum",1);
        oppoUID=getIntent().getStringExtra("oppoUID");
        oppoName=getIntent().getStringExtra("oppoName");
        oppoImgStr=getIntent().getStringExtra("oppoImgStr");
        mode=getIntent().getIntExtra("mode",1);




        ArrayList<LottieAnimationView> oppoAnimList=new ArrayList<>();
        oppoAnimList.add(anim21); oppoAnimList.add(anim22); oppoAnimList.add(anim23); oppoAnimList.add(anim24); oppoAnimList.add(anim25);
        oppoAnimList.add(anim26); oppoAnimList.add(anim27); oppoAnimList.add(anim28); oppoAnimList.add(anim29); oppoAnimList.add(anim30);

        answerUploaderAndReceiver.vsReceiver(oppoUID,oppoAnimList);


        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, VsPictureQuiz.this);
        myPicURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, VsPictureQuiz.this);

        oppoNameTextView.setText(oppoName);
        myNameTextView.setText(myName);


        Glide.with(getBaseContext()).load(myPicURL).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(myPic);

        Glide.with(getBaseContext()).load(oppoImgStr).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(picOppo);


        Log.i("MY PIC",myPicURL);
        Log.i("oppo PIC",oppoImgStr);

        llManupulator=new LLManupulator(audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL);

        animationList.add(anim11);animationList.add(anim12);animationList.add(anim13);animationList.add(anim14);animationList.add(anim15);
        animationList.add(anim16);animationList.add(anim17);animationList.add(anim18);animationList.add(anim19);animationList.add(anim20);

        supportAlertDialog=new SupportAlertDialog(loadingDialog,VsPictureQuiz.this);
        supportAlertDialog.showLoadingDialog();

        lifeLine();
        questionSelector();

        totalScore=new TotalScore();
        totalScore.getSingleModeScore();

        highestScore=new HighestScore();
        highestScore.start();


        c=new CountDownTimer(1000*15,1000) {
            @Override
            public void onTick(long l) {
                if(questionImage.getDrawable() != null){
                    try {
                        if(c!=null){
                            c.cancel();
                        }
                        mainTimer();
                        supportAlertDialog.dismissLoadingDialog();
                    }catch (Exception e){
                       e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFinish() {
                mainTimer();
                supportAlertDialog.dismissLoadingDialog();
            }
        }.start();

        myConnectionLisner=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    int value=snapshot.getValue(Integer.class);

                    if(value==0){

                        intentFunDeleter();

                        Intent i=new Intent(VsPictureQuiz.this, MainActivity.class);
                        startActivity(i);

                        finish();

                        //  myConnectionOff();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").addValueEventListener(myConnectionLisner);


        lisnerForConnectionStatus=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    int value=snapshot.getValue(Integer.class);

                    if(value==0){
                        dialogModel_1=new DialogModel_1(VsPictureQuiz.this,"Opponent is disconnected from the server.","Possible reasons may be Slow Internet or your opponent would have left the game.\nTry to find another opponent.",R.raw.userremoved,"Okay",questionTextView,isCompletedListener,vsRematchListener,lisnerForConnectionStatus,answerUploaderAndReceiver,oppoUID,NATIVE_ADS);
                        dialogModel_1.displayDialog();
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



    }
    private void intentFunDeleter(){
        try{table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").removeEventListener(myConnectionLisner);}catch (Exception e){e.printStackTrace();}
        try{table_user.child("VS_PLAY").child("IsDone").child(oppoUID).removeEventListener(isCompletedListener);}catch (Exception e){}

        try{table_user.child("VS_REQUEST").child(oppoUID).removeEventListener(vsRematchListener);}catch (Exception e){}

        try{table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);}catch (Exception e){e.printStackTrace();}

        try{ answerUploaderAndReceiver.removeAnimListener(oppoUID);}catch (Exception e){}



        try{ songActivity.songStop(); }catch (Exception e){ }
        if(countDownTimer!=null){ countDownTimer.cancel();}
    }


    public void lifeLine(){

        lifeLine=new LifeLine(linearLayoutFiftyFifty,linearLayoutAudience,linearLayoutexpert,position,list,option1,option2,option3,option4,myName,VsPictureQuiz.this);

        fiftyfiftyLL.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { if(fiftyfiftynum==0) { lifelineSum++;fiftyfiftynum = 1;lifeLine.setPosition(position);lifeLine.fiftyfiftyLL(); }else{ lifeLine.LLUsed("FIFTY-FIFTY"); } }});
        audienceLL.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { if(audiencenum==0) { lifelineSum++;audiencenum = 1;lifeLine.setPosition(position);lifeLine.audienceLL(); }else{ lifeLine.LLUsed("AUDIENCE"); } }});


        swapTheQuestionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swapnum==0) {
                    lifelineSum++;swapnum=1;
                    linearLayoutSwap.setBackgroundResource(R.drawable.usedicon);
                    nextButton.setEnabled(false);nextButton.setAlpha(0.7f);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { enableOption(true); }
                    position++;llManupulator.True();count = 0;
                    try{
                        playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                    }catch (Exception e){
                        playAnim(questionTextView, 0, list.get(1).getQuestionTextView());
                    }

                }else{ lifeLine.LLUsed("SWAP"); }
            }
        });

        expertAdviceLL.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { if(expertnum==0){ lifelineSum++;expertnum=1;lifeLine.setPosition(position);lifeLine.expertAdviceLL(); }else{ lifeLine.LLUsed("EXPERT ADVICE"); } }});
    }


    public void questionSelector() {


        ansArray=getIntent().getIntegerArrayListExtra("answerInt");


        try{

            if(ansArray.size()==11){
                for (int i = 0; i < 11; i++) {
                    fireBaseData(Long.parseLong(String.valueOf(ansArray.get(i))));
                }

                if(playerNum==2){
                    table_user.child("VS_PLAY").child(oppoUID).child("Answers").removeValue();
                }

            }else{
                table_user.child("VS_PLAY").child(oppoUID).child("Answers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            ansArray = (ArrayList<Integer>) snapshot.getValue();

                            for (int i = 0; i < 11; i++) {
                                fireBaseData(Long.parseLong(String.valueOf(ansArray.get(i))));
                            }

                            if(playerNum==2){
                                table_user.child("VS_PLAY").child(oppoUID).child("Answers").removeValue();
                            }

                        }catch (Exception e){

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        }catch (Exception e){
            e.printStackTrace();
            table_user.child("VS_PLAY").child(oppoUID).child("Answers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        ansArray = (ArrayList<Integer>) snapshot.getValue();

                        for (int i = 0; i < 11; i++) {
                            fireBaseData(Long.parseLong(String.valueOf(ansArray.get(i))));
                        }

                        if(playerNum==2){
                            table_user.child("VS_PLAY").child(oppoUID).child("Answers").removeValue();
                        }

                    }catch (Exception e){

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }







    public void fireBaseData (long setNumber){
        myRef.child("PictureQuizMain").child(String.valueOf(setNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.add(snapshot.getValue(questionHolder.class));

                try{
                    Glide.with(getBaseContext())
                            .load(list.get(num).getQuestionPicture()).error((Drawable) Glide.with(getBaseContext()).load(list.get(num).getQuestionPicture()).error((Drawable) Glide.with(getBaseContext()).load(list.get(num).getQuestionPicture()).error((Drawable) Glide.with(getBaseContext()).load(list.get(num).getQuestionPicture()).preload(20,10)).preload(20,10)).preload(20,10))
                            .preload(20, 10);
                }catch (Exception e){

                }

                mainManupulations();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VsPictureQuiz.this, error.getMessage(), Toast.LENGTH_SHORT).show();
              //  supportAlertDialog.dismissLoadingDialog();
                finish();
            }
        });
    }

    public void mainManupulations(){

        num++;
        if (num == 10) {



            if (list.size() > 0) {
                for (int i = 0; i < 4; i++) {
                    linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View view) {
                            try {
                                checkAnswer((Button) view);
                            } catch (Exception e) {
                                //        Toast.makeText(quizActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {

                        playMusic(R.raw.buttonmusic);
                        nextButton.setEnabled(false);
                        nextButton.setAlpha(0.7f);
                        enableOption(true);
                        position++;
                        llManupulator.True();

                        if (swapnum == 0) { if (position == 10) { adShow(0);return; } } else { if (position == 11) { adShow(0);return; } }
                        count = 0;
                        playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                    }
                });
            } else {
                finish();
                Toast.makeText(VsPictureQuiz.this, "No Questions", Toast.LENGTH_SHORT).show();
            }
        //    supportAlertDialog.dismissLoadingDialog();
        }
    }


    private void playAnim(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationStart(Animator animator) {
                if(value==0 && count<4){
                    String option="";
                    if(count==0){

                        String linkHolder=list.get(position).getQuestionPicture();
                        try{
                            Glide.with(getBaseContext())
                                    .load(linkHolder)
                                    .error(Glide.with(getBaseContext()).load(linkHolder).error(Glide.with(getBaseContext()).load(linkHolder).error(Glide.with(getBaseContext()).load(linkHolder))))
                                    .into(questionImage);
                        }catch (Exception e){

                        }


                        option=list.get(position).getOption1();
                        option1.setTextColor(Color.parseColor("#DEE7FF"));
                        linearLayout.getChildAt(0).setBackgroundResource(R.drawable.border_theme_2);

                    }else if(count==1){
                        option=list.get(position).getOption2();
                        option2.setTextColor(Color.parseColor("#DEE7FF"));
                        linearLayout.getChildAt(1).setBackgroundResource(R.drawable.border_theme_2);

                    }else if(count==2){
                        option=list.get(position).getOption3();
                        option3.setTextColor(Color.parseColor("#DEE7FF"));
                        linearLayout.getChildAt(2).setBackgroundResource(R.drawable.border_theme_2);

                    }else if(count==3){
                        option=list.get(position).getOption4();
                        option4.setTextColor(Color.parseColor("#DEE7FF"));
                        linearLayout.getChildAt(3).setBackgroundResource(R.drawable.border_theme_2);

                    }
                    playAnim(linearLayout.getChildAt(count),0,option);
                    count++;
                }
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                if (value == 0) {
                    try {
                        ((TextView) view).setText(data);
                        if(swapnum==0){
                            scoreBoard.setText((position+1)+"/10 ");
                        }else{
                            scoreBoard.setText((position)+"/10 ");
                        }
                    } catch (ClassCastException ex) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) { }
            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
    }



    public void playMusic(int id){
        MediaPlayer musicNav;
        musicNav = MediaPlayer.create(VsPictureQuiz.this,id);
        musicNav.start();
        musicNav.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                musicNav.reset();
                musicNav.release();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(Button selectedOption){
        enableOption(false);
        nextButton.setEnabled(true);
        nextButton.setAlpha(1);

        llManupulator.False();

        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){
            //correct

            answerUploaderAndReceiver.vsUpload(1);

            playMusic(R.raw.correctmusic);
            ANIM_MANU(R.raw.tickanim);
            animList.add(true);
            selectedOption.setBackgroundResource(R.drawable.option_right);
            //green color
            selectedOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            selectedOption.setShadowLayer(3,1,1,R.color.green);
            score++;
        }else {
            //incorrect

            answerUploaderAndReceiver.vsUpload(2);

            playMusic(R.raw.wrongansfinal);
            ANIM_MANU(R.raw.wronganim);
            animList.add(false);
            selectedOption.setBackgroundResource(R.drawable.option_wrong);     //red color
            selectedOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            selectedOption.setShadowLayer(3,1,1,R.color.green);
            Button correctOption = (Button) linearLayout.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.option_right);    //green color
            correctOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            correctOption.setShadowLayer(3,1,1,R.color.red);
        }
    }

    public void ANIM_MANU(int id){
        myPosition++;
        LottieAnimationView anim=animationList.get(myPosition);
        anim.setAnimation(id);
        anim.playAnimation();
        anim.loop(false);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable){
        for (int i=0;i<4;i++) {
            linearLayout.getChildAt(i).setEnabled(enable);
            if (enable) {
                linearLayout.getChildAt(i).setBackgroundResource(R.drawable.option_null);
            }
        }
    }


    public void mainTimer(){




        if(countDownTimer==null){

            countDownTimer=new CountDownTimer(1000*180, 1000) {


                @SuppressLint("ResourceAsColor")
                public void onTick(long millisUntilFinished) {


                    if(second==0){
                        minutes--;
                        minutestext="0"+String.valueOf(minutes);
                        second=59;
                        if(second<10){
                            secondtext="0"+String.valueOf(second);
                        }else{
                            secondtext=String.valueOf(second);
                        }
                        timerText.setText(minutestext+":"+secondtext+" ");

                    }else{
                        minutestext="0"+String.valueOf(minutes);
                        if(second<10){
                            secondtext="0"+String.valueOf(second);
                        }else{
                            secondtext=String.valueOf(second);
                        }
                        timerText.setText(minutestext+":"+secondtext+" ");
                        second--;
                    }

                    //Last 15 seconds end animation
                    if(minutes==0 && second<=15){

                        timerText.setTextColor(Color.parseColor("#FF5E5E"));

                        //Continuous zoomIn - zoomOut
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(clockCardView, "scaleX", 0.9f, 1f);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(clockCardView, "scaleY", 0.9f, 1f);

                        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
                        scaleX.setRepeatMode(ObjectAnimator.REVERSE);

                        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
                        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

                        AnimatorSet scaleAnim = new AnimatorSet();
                        scaleAnim.setDuration(500);
                        scaleAnim.play(scaleX).with(scaleY);

                        scaleAnim.start();
                    }

                }
                public void onFinish() {

                    timerText.setText("00:00");
                    minutes=0;
                    second=0;

                    Toast.makeText(VsPictureQuiz.this, "Time Over", Toast.LENGTH_SHORT).show();
                    adShow(1);


                }

            }.start();
        }

    }




    public void quizFinishDialog(int i){

         timerCard.setVisibility(View.INVISIBLE);

        if(i==0){
            try{
                countDownTimer.cancel();
            }catch (Exception e){
                e.printStackTrace();
            }






            if((60-second)>=10){
                if(second==0){
                    timeTakenString="0"+String.valueOf(2-minutes+1)+":00";
                }else{
                    timeTakenString="0"+String.valueOf(2-minutes)+":"+String.valueOf(60-minutes);
                }
            }else{
                timeTakenString="0"+String.valueOf(2-minutes)+":0"+String.valueOf(60-minutes);
            }

            timeTakenInt=((2-minutes)*60)+(60-second);

        }else if(i==1){

            minutes=0;
            second=0;
            timeTakenString="03:00";
            timeTakenInt=180;
        }



        ScoreGenerator scoreGenerator=new ScoreGenerator(minutes,second,lifelineSum,score);

        totalScore.setTotalScore(scoreGenerator.start()+totalScore.getTotalScore());
        totalScore.setSingleModeScore();

        if(highestScore.getHighestScore()<scoreGenerator.start()){
            highestScore.setHighestScore(scoreGenerator.start());
            highestScore.upLoadHighestScore(scoreGenerator.start());
        }

        HashMap<String,Integer> map=new HashMap<>();
        map.put("Expert",expertnum);
        map.put("Flip",swapnum);
        map.put("Audience",audiencenum);
        map.put("Fifty-Fifty",fiftyfiftynum);




        Dialog loadingDialog = null;
        SupportAlertDialog supportAlertDialog =new SupportAlertDialog(loadingDialog,VsPictureQuiz.this);
        supportAlertDialog.showLoadingDialog();




        vsRematchListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    boolean request=snapshot.getValue(Boolean.class);
                    if(request){
                        if(dataExchange.rematchButtonEnable){
                            table_user.child("VS_REQUEST").child(oppoUID).removeValue();
                            String tt=oppoName+" has send a rematch request to you.";
                            Vs_Response vs_response=new Vs_Response(VsPictureQuiz.this,countDownTimer,option1,songActivity,R.raw.rematch_request,mode,tt,oppoUID,oppoName,oppoImgStr,lisnerForConnectionStatus,vsRematchListener,isCompletedListener);
                            vs_response.start();
                            table_user.child("VS_REQUEST").child(oppoUID).removeEventListener(vsRematchListener);
                        }else{





                            table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").removeValue();


                            if(playerNum==1){

                                dataExchange.reMatch.setVisibility(View.GONE);
                                table_user.child("VS_REQUEST").child(mAuth.getCurrentUser().getUid()).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dataExchange.rematchButtonEnable=true;
                                        dataExchange.reMatch.setVisibility(View.VISIBLE);
                                        Toast.makeText(VsPictureQuiz.this, "PLEASE SEND REQUEST AGAIN", Toast.LENGTH_LONG).show();

                                    }
                                });



                            }else{
                                dataExchange.reMatch.setVisibility(View.GONE);
                                table_user.child("VS_REQUEST").child(mAuth.getCurrentUser().getUid()).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dataExchange.rematchButtonEnable=true;

                                        new CountDownTimer(15000,100){

                                            @Override
                                            public void onTick(long l) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                try{
                                                    dataExchange.reMatch.setVisibility(View.VISIBLE);
                                                }catch (Exception e){

                                                }
                                            }
                                        }.start();

                                        Toast.makeText(VsPictureQuiz.this, "Both the players have send the request for rematch at the same time. Try again to send the request again after 15 seconds. ", Toast.LENGTH_LONG).show();

                                    }
                                });


                            }





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
        table_user.child("VS_REQUEST").child(oppoUID).addValueEventListener(vsRematchListener);


        int finalTimeTakenInt = timeTakenInt;
        String finalTimeTakenString = timeTakenString;
        table_user.child("VS_PLAY").child("IsDone").child(mAuth.getCurrentUser().getUid()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                table_user.child("VS_PLAY").child("IsDone").child(oppoUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try{

                            Boolean isCompletedOppo=snapshot.getValue(Boolean.class);
                            supportAlertDialog.dismissLoadingDialog();
                            if(isCompletedOppo){
                                starter=0;
                                // dialogModel_1.removeLisnerForConnectionStatus(oppoUID);

                                try{
                                    table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                try{
                                    table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").removeEventListener(myConnectionLisner);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                 dataExchange=new DataExchange(VsPictureQuiz.this,map,animList,score, finalTimeTakenString,
                                        lifelineSum,totalScore.getTotalScore(),highestScore.getHighestScore(),scoreGenerator.start(),audienceLL,myName,myPicURL,
                                        category,1, finalTimeTakenInt,oppoUID,oppoName,oppoImgStr,animationList,mode,lisnerForConnectionStatus,answerUploaderAndReceiver,vsRematchListener,isCompletedListener,countDownTimer,party_popper,rematchButtonEnable);

                                dataExchange.start();
                            }else{

                                WaitingVSInGameDialog waitingVSInGameDialog =new WaitingVSInGameDialog(myPicURL,myName,String.valueOf(score), finalTimeTakenString,String.valueOf(score*10),String.valueOf(lifelineSum),VsPictureQuiz.this,questionTextView,NATIVE_ADS);
                                isCompletedListener=new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try{
                                            Boolean isCompleted=snapshot.getValue(Boolean.class);
                                            if(isCompleted){
                                                waitingVSInGameDialog.dismiss();

                                                starter=0;

                                                // dialogModel_1.removeLisnerForConnectionStatus(oppoUID);

                                                table_user.child("VS_PLAY").child("IsDone").child(oppoUID).removeEventListener(isCompletedListener);

                                                try{
                                                    table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                                try{
                                                    table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").removeEventListener(myConnectionLisner);
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                                 dataExchange=new DataExchange(VsPictureQuiz.this,map,animList,score, finalTimeTakenString,
                                                        lifelineSum,totalScore.getTotalScore(),highestScore.getHighestScore(),scoreGenerator.start(),audienceLL,myName,myPicURL,
                                                        category,1, finalTimeTakenInt,oppoUID,oppoName,oppoImgStr,animationList,mode,lisnerForConnectionStatus,answerUploaderAndReceiver,vsRematchListener,isCompletedListener,countDownTimer,party_popper,rematchButtonEnable);
                                                dataExchange.start();
                                            }else{
                                                if(starter==1){
                                                    waitingVSInGameDialog.start();
                                                }

                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                }; table_user.child("VS_PLAY").child("IsDone").child(oppoUID).addValueEventListener(isCompletedListener);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });







    }






    public void songStopperAndResumer(){
        CardView cardViewSpeaker=(CardView) findViewById(R.id.cardViewSpeaker);
        final ImageView speakerImage=(ImageView) findViewById(R.id.speakerImage);
        final LinearLayout Speaker=(LinearLayout) findViewById(R.id.Speaker);
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,VsPictureQuiz.this)){
            songActivity=new SongActivity(this);
            songActivity.startMusic();
        }else{
            Speaker.setBackgroundResource(R.drawable.usedicon);
            speakerImage.setBackgroundResource(R.drawable.music_off);
        }
        cardViewSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,VsPictureQuiz.this)){
                    songActivity.songStop();
                    Speaker.setBackgroundResource(R.drawable.usedicon);
                    speakerImage.setBackgroundResource(R.drawable.music_off);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,VsPictureQuiz.this,false);
                }else{
                    songActivity=new SongActivity(VsPictureQuiz.this);
                    songActivity.startMusic();
                    Speaker.setBackgroundResource(R.drawable.single_color_2);
                    speakerImage.setBackgroundResource(R.drawable.music_on);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,VsPictureQuiz.this,true);
                }
            }
        });
    }

    public void onBackPressed() {
        QuizCancelDialog quizCancelDialog=new QuizCancelDialog(VsPictureQuiz.this,countDownTimer,option1,songActivity,lisnerForConnectionStatus,oppoUID,vsRematchListener,isCompletedListener,NATIVE_ADS);
        quizCancelDialog.startVsMode();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{mInterstitialAd=null;}catch (Exception e){}
        try{ songActivity.songStop(); }catch (Exception e){ }
        if(countDownTimer!=null){ countDownTimer.cancel();}
        try{mAdView.destroy();}catch (Exception e){}

        try{table_user.child("VS_PLAY").child("IsDone").child(oppoUID).removeEventListener(isCompletedListener);}catch (Exception e){}

        try{table_user.child("VS_REQUEST").child(oppoUID).removeEventListener(vsRematchListener);}catch (Exception e){}

        try{table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);}catch (Exception e){e.printStackTrace();}

        answerUploaderAndReceiver.removeAnimListener(oppoUID);

        try{NATIVE_ADS.destroy();}catch (Exception e){}

        Runtime.getRuntime().gc();
    }

    public void adShow(int i){

        if(mInterstitialAd!=null) {
            // Step 1: Display the interstitial
            mInterstitialAd.show(VsPictureQuiz.this);
            // Step 2: Attach an AdListener
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    quizFinishDialog(i);

                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    quizFinishDialog(i);

                }
            });


        }else{
            quizFinishDialog(i);
        }
    }


}