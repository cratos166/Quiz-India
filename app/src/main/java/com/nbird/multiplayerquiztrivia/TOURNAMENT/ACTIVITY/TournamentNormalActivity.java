package com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
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
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.FIREBASE.HighestScore;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.Record;
import com.nbird.multiplayerquiztrivia.FIREBASE.TotalScore;
import com.nbird.multiplayerquiztrivia.GENERATORS.ScoreGenerator;
import com.nbird.multiplayerquiztrivia.LL.LLManupulator;
import com.nbird.multiplayerquiztrivia.LL.LifeLine;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.DataExchangeHolder;
import com.nbird.multiplayerquiztrivia.Model.questionHolder;

import com.nbird.multiplayerquiztrivia.QUIZ.VsNormalQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.EXTRA.AnswerUploader;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.EXTRA.PlayerDisplayInQuiz;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TournamentNormalActivity extends AppCompatActivity {


    TextView questionTextView,scoreBoard,timerText;
    Button option1,option2,option3,option4,nextButton;
    LinearLayout linearLayout,linearLayoutexpert,linearLayoutAudience,linearLayoutFiftyFifty,linearLayoutSwap;
    CardView audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL,clockCardView;

    Dialog loadingDialog;
    CountDownTimer countDownTimer;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");

    private List<questionHolder> list;
    ArrayList<LottieAnimationView> animationList;
    ArrayList<Boolean> animList;

    int fiftyfiftynum=0,audiencenum=0,swapnum=0,expertnum=0,lifelineSum=0,position=0,num=0,score=0,myPosition=-1,count;
    String myName,myPicURL;

    AppData appData;
    SongActivity songActivity;
    LLManupulator llManupulator;

    LifeLine lifeLine;
    SupportAlertDialog supportAlertDialog;
    TotalScore totalScore;
    HighestScore highestScore;

    ArrayList<Integer> listAns;
    String roomCode;
    int time,myPlayerNum;

    AnswerUploader answerUploader;

    PlayerDisplayInQuiz playerDisplayInQuiz;

    ValueEventListener playerInfoGetterListener;
    RecyclerView recyclerView;

    int minutes=3;
    int second=0;
    String minutestext;
    String secondtext,hostName;


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
        setContentView(R.layout.activity_tournament_normal);



        listAns=getIntent().getIntegerArrayListExtra("answerInt");
        roomCode=getIntent().getStringExtra("roomCode");
        time=getIntent().getIntExtra("time",180);
        myPlayerNum=getIntent().getIntExtra("playerNum",1);
        hostName= getIntent().getStringExtra("hostName");

        list=new ArrayList<>();
        appData=new AppData();
        animationList=new ArrayList<>();
        animList=new ArrayList<>();


        if(time==270){
            minutes=4;
            second=30;
        }else if(time==360){
            minutes=6;
            second=0;
        }

        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, TournamentNormalActivity.this)){
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);


            Random r=new Random();
            int num=r.nextInt(AppString.ADS_FREQUENCY_NORMAL);
            if(num==1){
                loadAds();
            }
        }

        if(myPlayerNum==1){
            table_user.child("TOURNAMENT").child("RESULT").child(roomCode).removeValue();
        }

        songStopperAndResumer();

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
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView) ;


        clockCardView = (CardView) findViewById(R.id.cardView3);

        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, TournamentNormalActivity.this);
        myPicURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, TournamentNormalActivity.this);


        llManupulator=new LLManupulator(audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL);

       // timer=new NormalTournamentTimer(countDownTimer,time*1000,1000,TournamentNormalActivity.this,timerText,clockCardView);



        supportAlertDialog=new SupportAlertDialog(loadingDialog,TournamentNormalActivity.this);
        supportAlertDialog.showLoadingDialog();

        lifeLine();
        questionSelector();

        totalScore=new TotalScore();
        totalScore.getSingleModeScore();

        highestScore=new HighestScore();
        highestScore.start();




        answerUploader=new AnswerUploader(roomCode,myName,myPicURL);
        answerUploader.start();

        playerDisplayInQuiz=new PlayerDisplayInQuiz(TournamentNormalActivity.this,playerInfoGetterListener,roomCode,recyclerView, listAns.size());
        playerDisplayInQuiz.start();
        
    }


    public void lifeLine(){

        lifeLine=new LifeLine(linearLayoutFiftyFifty,linearLayoutAudience,linearLayoutexpert,position,list,option1,option2,option3,option4,myName,TournamentNormalActivity.this);

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
                    playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                }else{ lifeLine.LLUsed("SWAP"); }
            }
        });

        expertAdviceLL.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { if(expertnum==0){ lifelineSum++;expertnum=1;lifeLine.setPosition(position);lifeLine.expertAdviceLL(); }else{ lifeLine.LLUsed("EXPERT ADVICE"); } }});
    }


    public void questionSelector() {
        for (int i = 0; i < listAns.size(); i++) {
            int setNumber=listAns.get(i);
            fireBaseData2(setNumber);
        }
    }



    public void fireBaseData2 ( int setNumber){
        myRef.child("NormalQuizBIGJSON").child(String.valueOf(setNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.add(snapshot.getValue(questionHolder.class));
                mainManupulations();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TournamentNormalActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                supportAlertDialog.dismissLoadingDialog();
                finish();
            }
        });
    }

    public void mainManupulations(){

        num++;
        if (num == listAns.size()) {
            setCountDownTimer();
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

                        if (swapnum == 0) { if (position == listAns.size()-1) { quizFinishDialog();return; } } else { if (position == listAns.size()) { quizFinishDialog();return; } }
                        count = 0;
                        playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                    }
                });
            } else {
                finish();
                Toast.makeText(TournamentNormalActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
            }
            supportAlertDialog.dismissLoadingDialog();
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
                            scoreBoard.setText((position+1)+"/"+(listAns.size()-1));
                        }else{
                            scoreBoard.setText((position)+"/"+(listAns.size()-1));
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
        musicNav = MediaPlayer.create(TournamentNormalActivity.this,id);
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

            answerUploader.upload(1);
            //correct
            playMusic(R.raw.correctmusic);
//            ANIM_MANU(R.raw.tickanim);
            animList.add(true);
            selectedOption.setBackgroundResource(R.drawable.option_right);
            //green color
            selectedOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            selectedOption.setShadowLayer(3,1,1,R.color.green);
            score++;


        }else {

            answerUploader.upload(2);
            //incorrect
            playMusic(R.raw.wrongansfinal);
//            ANIM_MANU(R.raw.wronganim);
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





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable){
        for (int i=0;i<4;i++) {
            linearLayout.getChildAt(i).setEnabled(enable);
            if (enable) {
                linearLayout.getChildAt(i).setBackgroundResource(R.drawable.option_null);
            }
        }
    }


    public void quizFinishDialog(){

        try{
            countDownTimer.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }

        int minutesLeft=minutes;
        int secondsLeft=second;

        String timeTakenString;


        if((60-secondsLeft)>=10){
            if(secondsLeft==0){
                timeTakenString="0"+String.valueOf(2-minutesLeft+1)+":00";
            }else{
                timeTakenString="0"+String.valueOf(2-minutesLeft)+":"+String.valueOf(60-secondsLeft);
            }
        }else{
            timeTakenString="0"+String.valueOf(2-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
        }




//        if((60-secondsLeft)>=10){
//            timeTakenString="0"+String.valueOf(2-minutesLeft)+":"+String.valueOf(60-secondsLeft);
//        }else{
//            timeTakenString="0"+String.valueOf(2-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
//        }

        int timeTakenInt=((2-minutesLeft)*60)+(60-secondsLeft);

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

        DataExchangeHolder dataExchangeHolder=new DataExchangeHolder(map,animList,score,timeTakenString,lifelineSum,0,scoreGenerator.start(),myName,myPicURL,timeTakenInt);


        Dialog dialog=null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,TournamentNormalActivity.this);
        supportAlertDialog.showLoadingDialog();

        Record record =new Record(scoreGenerator.start(),score,1,TournamentNormalActivity.this,audienceLL,timeTakenInt,myName,myPicURL);
        record.startLineGraph();
        record.startBarGroup();
        record.setLeaderBoard();

        table_user.child("TOURNAMENT").child("RESULT").child(roomCode).child(mAuth.getCurrentUser().getUid()).setValue(dataExchangeHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("activityNumber").setValue(3).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        supportAlertDialog.dismissLoadingDialog();

                        try{table_user.child("TOURNAMENT").child("ANSWERS").child(roomCode).removeEventListener(playerInfoGetterListener);}catch (Exception e){}




                        if(mInterstitialAd!=null) {
                            // Step 1: Display the interstitial
                            mInterstitialAd.show(TournamentNormalActivity.this);
                            // Step 2: Attach an AdListener
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);

                                    intentFun();

                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();

                                    intentFun();
                                }
                            });


                        }else{

                            intentFun();
                        }



                    }
                });


            }
        });


    }


    private void intentFun(){
        Intent intent=new Intent(TournamentNormalActivity.this,ScoreActivity.class);
        intent.putExtra("roomCode",roomCode);
        intent.putExtra("maxQuestions",list.size()-1);
        intent.putExtra("playerNum",myPlayerNum);
        intent.putExtra("hostName",hostName);
        if(time==180){
            intent.putExtra("time",1);
        }else if(time==270){
            intent.putExtra("time",2);
        }else if(time==360){
            intent.putExtra("time",3);
        }

        startActivity(intent);
        finish();
    }




    public void songStopperAndResumer(){
        CardView cardViewSpeaker=(CardView) findViewById(R.id.cardViewSpeaker);
        final ImageView speakerImage=(ImageView) findViewById(R.id.speakerImage);
        final LinearLayout Speaker=(LinearLayout) findViewById(R.id.Speaker);
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,TournamentNormalActivity.this)){
            songActivity=new SongActivity(this);
            songActivity.startMusic();
        }else{
            Speaker.setBackgroundResource(R.drawable.usedicon);
            speakerImage.setBackgroundResource(R.drawable.music_off);
        }
        cardViewSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,TournamentNormalActivity.this)){
                    songActivity.songStop();
                    Speaker.setBackgroundResource(R.drawable.usedicon);
                    speakerImage.setBackgroundResource(R.drawable.music_off);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,TournamentNormalActivity.this,false);
                }else{
                    songActivity=new SongActivity(TournamentNormalActivity.this);
                    songActivity.startMusic();
                    Speaker.setBackgroundResource(R.drawable.single_color_2);
                    speakerImage.setBackgroundResource(R.drawable.music_on);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,TournamentNormalActivity.this,true);
                }
            }
        });
    }

    public void onBackPressed() {
        quitScoreActivityActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try{mInterstitialAd=null;}catch (Exception e){}
        try{mAdView.destroy();}catch (Exception e){}
        try{ songActivity.songStop(); }catch (Exception e){ }
        if(countDownTimer!=null){ countDownTimer.cancel();}

        Runtime.getRuntime().gc();
    }

    public void quitScoreActivityActivity(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(TournamentNormalActivity.this, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(TournamentNormalActivity.this).inflate(R.layout.dialog_model_2,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);


        Button yesButton=(Button) viewRemove1.findViewById(R.id.buttonYes);
        Button noButton=(Button) viewRemove1.findViewById(R.id.buttonNo);

        TextView textTitle=(TextView) viewRemove1.findViewById(R.id.textTitle);


        if(myPlayerNum==1){
            textTitle.setText("You are the host. If you left the room, the whole room will be dissolved.\n\n You really want to quit ?");
        }else {
            textTitle.setText("You really want to quit ?");
        }

        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, TournamentNormalActivity.this)){
            MobileAds.initialize(TournamentNormalActivity.this);
            AdLoader adLoader = new AdLoader.Builder(TournamentNormalActivity.this, AppString.NATIVE_ID)
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




        LottieAnimationView anim=(LottieAnimationView)  viewRemove1.findViewById(R.id.imageIcon);
        anim.setAnimation(R.raw.exit_lobby);
        anim.playAnimation();
        anim.loop(true);





        final AlertDialog alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog=null;
                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,TournamentNormalActivity.this);
                supportAlertDialog.showLoadingDialog();


                if(myPlayerNum==1){
                    table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    try{
                                        supportAlertDialog.dismissLoadingDialog();
                                    }catch (Exception e1){

                                    }

                                    try{
                                        alertDialog.dismiss();
                                    }catch (Exception e){

                                    }

                                    intentMain();
                                }
                            });

                        }
                    });
                }else{
                    table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            supportAlertDialog.dismissLoadingDialog();

                            try{
                                alertDialog.dismiss();
                            }catch (Exception e){

                            }

                            intentMain();



                        }
                    });
                }


            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


    private void intentMain(){
        try{table_user.child("TOURNAMENT").child("ANSWERS").child(roomCode).removeEventListener(playerInfoGetterListener);}catch (Exception e){}

        try{countDownTimer.cancel();}catch (Exception e){}


        Intent intent=new Intent(TournamentNormalActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }


    private void setCountDownTimer(){
        countDownTimer=new CountDownTimer(time*1000, 1000) {


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

                Toast.makeText(TournamentNormalActivity.this, "Time Over", Toast.LENGTH_SHORT).show();
                quizFinishDialog();


            }

        }.start();
    }


}