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
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.Glide;
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
import com.nbird.multiplayerquiztrivia.BOT.VsBOTPictureQuiz;
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
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.BOT.BOTAnswerUploader;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.BOT.BOTScoreActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.EXTRA.BOTPlayerDisplayInQuiz;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BOTTournamentPictureActivity extends AppCompatActivity {
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

    int fiftyfiftynum=0,audiencenum=0,swapnum=0,expertnum=0,lifelineSum=0,position=0,num=0,score=0,myPosition=-1,count,numberOfQuestions;
    String myName,myPicURL;

    AppData appData;
    SongActivity songActivity;
    LLManupulator llManupulator;

    LifeLine lifeLine;
    SupportAlertDialog supportAlertDialog;
    TotalScore totalScore;
    HighestScore highestScore;

    String roomCode;
    int time,myPlayerNum;

    BOTAnswerUploader answerUploader;

    BOTPlayerDisplayInQuiz botplayerDisplayInQuiz;

    ValueEventListener playerInfoGetterListener;
    RecyclerView recyclerView;

    int minutes=3,minutesBot=3;
    int second=0,secondBot=0;
    String minutestext;
    String secondtext,hostName;

    ImageView questionImage;

    public static final int VARIABLE_BOT_TIME=3;
    public static final int FIXED_BOT_TIME=4;


    private InterstitialAd mInterstitialAd;
    private void loadAds(){


        String key= AppString.INTERSTITIAL_ID;

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

    CountDownTimer botAnswerUploader,c;
    int numberOfPlayers;


    ArrayList<Details> botData;
    int gameMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottournament_picture);

        time=getIntent().getIntExtra("time",180);
        myPlayerNum=getIntent().getIntExtra("playerNum",1);
        numberOfQuestions=getIntent().getIntExtra("numberOfQuestions",10);
        numberOfPlayers=getIntent().getIntExtra("numberOfPlayers",10);
        gameMode=getIntent().getIntExtra("gameMode",1);
        hostName=getIntent().getStringExtra("hostName");

        list=new ArrayList<>();
        appData=new AppData();
        animationList=new ArrayList<>();
        animList=new ArrayList<>();
        botData=new ArrayList<>();


        if(time==270){
            minutes=4;
            second=30;
            minutesBot=4;
            secondBot=30;
        }else if(time==360){
            minutes=6;
            second=0;
            minutesBot=6;
            secondBot=0;
        }

        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, BOTTournamentPictureActivity.this)){
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

        table_user.child("BOT_TOURNAMENT").child("RESULT").child(mAuth.getCurrentUser().getUid()).removeValue();
        table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).removeValue();


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
        questionImage=(ImageView) findViewById(R.id.questionImage) ;


        clockCardView = (CardView) findViewById(R.id.cardView3);

        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, BOTTournamentPictureActivity.this);
        myPicURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, BOTTournamentPictureActivity.this);


        llManupulator=new LLManupulator(audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL);

        // timer=new NormalTournamentTimer(countDownTimer,time*1000,1000,BOTTournamentPictureActivity.this,timerText,clockCardView);



        supportAlertDialog=new SupportAlertDialog(loadingDialog,BOTTournamentPictureActivity.this);
        supportAlertDialog.showLoadingDialog();

        lifeLine();
        questionSelector();

        totalScore=new TotalScore();
        totalScore.getSingleModeScore();

        highestScore=new HighestScore();
        highestScore.start();

        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(loadingDialog, BOTTournamentPictureActivity.this);
        supportAlertDialog.showLoadingDialog();

        c=new CountDownTimer(1000*15,1000) {
            @Override
            public void onTick(long l) {
                if(questionImage.getDrawable() != null){
                    try {
                        if(c!=null){
                            c.cancel();
                        }
                        setCountDownTimer();
                        botAnsUploader();
                        supportAlertDialog.dismissLoadingDialog();
                    }catch (Exception e){

                    }

                }
            }

            @Override
            public void onFinish() {
                setCountDownTimer();
                botAnsUploader();
                supportAlertDialog.dismissLoadingDialog();
            }
        }.start();



        answerUploader=new BOTAnswerUploader(myName,myPicURL,mAuth.getCurrentUser().getUid());
        answerUploader.start();

        botplayerDisplayInQuiz=new BOTPlayerDisplayInQuiz(BOTTournamentPictureActivity.this,recyclerView, numberOfQuestions,playerInfoGetterListener,1);
        botplayerDisplayInQuiz.start();






    }


    public void botAnsUploader(){


        table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    try{

                        Details playerDisplayInQuizHolder=dataSnapshot.getValue(Details.class);

                        if(playerDisplayInQuizHolder.getUid().equals(mAuth.getCurrentUser().getUid())){

                        }else{
                            botData.add(playerDisplayInQuizHolder);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                Random random=new Random();
                ArrayList<Boolean> makerBool=new ArrayList();
                ArrayList<Integer> jkInt=new ArrayList();

                ArrayList<Integer> totalAnswered=new ArrayList();
                ArrayList<BOTAnswerUploader> answerUploaderArrayList=new ArrayList<>();
                ArrayList<Integer> numberOfCorrenctAnswer=new ArrayList<>();
                ArrayList<ArrayList<Boolean>> animListAnswer=new ArrayList<>();
                for(int i=0;i<botData.size();i++){
                    ArrayList<Boolean> arr=new ArrayList<>();
                    animListAnswer.add(arr);
                    makerBool.add(false);
                    jkInt.add(random.nextInt(VARIABLE_BOT_TIME)+FIXED_BOT_TIME);
                    totalAnswered.add(0);
                    numberOfCorrenctAnswer.add(0);

                    answerUploaderArrayList.add(new BOTAnswerUploader(botData.get(i).getName(),botData.get(i).getImageURL(),botData.get(i).getUid()));
                    answerUploaderArrayList.get(i).start();

                }


                final int[] numberOfBotsCompleted = {0};


                botAnswerUploader=new CountDownTimer(1000*time,1000) {
                    @Override
                    public void onTick(long l) {

                        if(secondBot==0){
                            minutesBot--;
                            secondBot=59;
                        }else{
                            secondBot--;
                        }



                        for(int i=0;i<botData.size();i++){
                            if(makerBool.get(i)){

                                if(totalAnswered.get(i)!=numberOfQuestions-1){
                                    makerBool.set(i,false);

                                    jkInt.set(i,random.nextInt(VARIABLE_BOT_TIME)+FIXED_BOT_TIME);

                                    totalAnswered.set(i,totalAnswered.get(i)+1);

                                    Boolean b=random.nextBoolean();

                                    if(b){
                                        numberOfCorrenctAnswer.set(i,numberOfCorrenctAnswer.get(i)+1);
                                        answerUploaderArrayList.get(i).upload(1);
                                        ArrayList<Boolean> arrBool=animListAnswer.get(i);
                                        arrBool.add(true);
                                    }else{
                                        answerUploaderArrayList.get(i).upload(2);
                                        ArrayList<Boolean> arrBool=animListAnswer.get(i);
                                        arrBool.add(false);
                                    }

                                    if(totalAnswered.get(i)==numberOfQuestions-1){
                                        numberOfBotsCompleted[0]++;
                                        botAnswerDataUploader(numberOfCorrenctAnswer.get(i),animListAnswer.get(i),botData.get(i).getName(),botData.get(i).getImageURL(),botData.get(i).getUid());
                                    }

                                    if(numberOfBotsCompleted[0]==botData.size()){
                                        try {
                                            botAnswerUploader.cancel();
                                        }catch (Exception e){

                                        }

                                    }

                                }

                            }else{
                                int kl=jkInt.get(i);
                                kl--;
                                jkInt.set(i,kl);
                                if(kl==0){
                                    makerBool.set(i,true);
                                }
                            }
                        }


                    }

                    @Override
                    public void onFinish() {

                        if(numberOfBotsCompleted[0]!=botData.size()){
                            for(int i=0;i<botData.size();i++){
                                if(totalAnswered.get(i)!=numberOfQuestions-1){
                                    botAnswerDataUploader(numberOfCorrenctAnswer.get(i),animListAnswer.get(i),botData.get(i).getName(),botData.get(i).getImageURL(),botData.get(i).getUid());
                                }
                            }

                        }





                    }
                }.start();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void lifeLine(){

        lifeLine=new LifeLine(linearLayoutFiftyFifty,linearLayoutAudience,linearLayoutexpert,position,list,option1,option2,option3,option4,myName,BOTTournamentPictureActivity.this);

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
        for(int i=0;i<numberOfQuestions;i++){
            Random rand = new Random();
            int setNumber = rand.nextInt(4999)+1;
            if(setNumber>1210&&setNumber<2000){
                setNumber=setNumber-1000;
            }
            fireBaseData2(setNumber);
        }
    }



    public void fireBaseData2 ( int setNumber){
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
                Toast.makeText(BOTTournamentPictureActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                supportAlertDialog.dismissLoadingDialog();
                finish();
            }
        });
    }

    public void mainManupulations(){

        num++;
        if (num == numberOfQuestions) {

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

                        if (swapnum == 0) { if (position == numberOfQuestions-1) { quizFinishDialog();return; } } else { if (position == numberOfQuestions) { quizFinishDialog();return; } }
                        count = 0;
                        playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                    }
                });
            } else {
                finish();
                Toast.makeText(BOTTournamentPictureActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
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
                            scoreBoard.setText((position+1)+"/"+(numberOfQuestions-1));
                        }else{
                            scoreBoard.setText((position)+"/"+(numberOfQuestions-1));
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
        musicNav = MediaPlayer.create(BOTTournamentPictureActivity.this,id);
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


    public void botAnswerDataUploader(Integer numberOfCorrectAnswer, ArrayList<Boolean> booleans, String name, String imageURL,String UID){
        int minutesLeft=minutesBot;
        int secondsLeft=secondBot;

        String timeTakenString;




        Random random=new Random();
        int oppoLifelineSum=random.nextInt(5);

        if(oppoLifelineSum==4||oppoLifelineSum==3){
            oppoLifelineSum=random.nextInt(5);
        }

        Log.i("oppoLifelineSum" , String.valueOf(oppoLifelineSum));


        HashMap<String,Integer> oppoMap=new HashMap<>();
        oppoMap.put("Expert",0);
        oppoMap.put("Audience",0);
        oppoMap.put("Fifty-Fifty",0);
        oppoMap.put("Flip",0);
        for(int i=0;i<oppoLifelineSum;){
            if(i<oppoLifelineSum){
                if(oppoMap.get("Expert")==0){
                    boolean isUse=random.nextBoolean();
                    if(isUse){
                        oppoMap.put("Expert",1);
                        i++;
                        Log.i("Expert","true");
                    }
                }
            }



            if(i<oppoLifelineSum){
                if(oppoMap.get("Audience")==0){
                    boolean isUse=random.nextBoolean();
                    if(isUse){
                        oppoMap.put("Audience",1);
                        i++;
                        Log.i("Audience","true");
                    }
                }
            }




            if(i<oppoLifelineSum){
                if(oppoMap.get("Fifty-Fifty")==0){
                    boolean isUse=random.nextBoolean();
                    if(isUse){
                        oppoMap.put("Fifty-Fifty",1);
                        i++;
                        Log.i("Fifty-Fifty","true");
                    }
                }
            }




            if(i<oppoLifelineSum){
                if(oppoMap.get("Flip")==0){
                    boolean isUse=random.nextBoolean();
                    if(isUse){
                        oppoMap.put("Flip",1);
                        i++;
                        Log.i("Flip","true");
                    }
                }
            }



        }


//        if((60-secondsLeft)>=10){
//            timeTakenString="0"+String.valueOf(2-minutesLeft)+":"+String.valueOf(60-secondsLeft);
//        }else{
//            timeTakenString="0"+String.valueOf(2-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
//        }
        int timeTakenInt;
        if(time==180){
            timeTakenInt=((2-minutesLeft)*60)+(60-secondsLeft);

            if((60-secondsLeft)>=10){
                if(secondsLeft==0){
                    timeTakenString="0"+String.valueOf(2-minutesLeft+1)+":00";
                }else{
                    timeTakenString="0"+String.valueOf(2-minutesLeft)+":"+String.valueOf(60-secondsLeft);
                }
            }else{
                timeTakenString="0"+String.valueOf(2-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
            }


        }else if(time==270){
            if(4-minutesLeft==0){
                timeTakenInt=30-secondsLeft;
            }else{
                timeTakenInt=((4-minutesLeft)*60)+(60-secondsLeft);

            }



            if((60-secondsLeft)>=10){
                if(secondsLeft==0){
                    timeTakenString="0"+String.valueOf(4-minutesLeft+1)+":00";
                }else{
                    if(4-minutesLeft==0){
                        timeTakenString="0"+String.valueOf(4-minutesLeft)+":"+String.valueOf(30-secondsLeft);
                    }else{
                        timeTakenString="0"+String.valueOf(4-minutesLeft)+":"+String.valueOf(60-secondsLeft);
                    }

                }
            }else{
                if(4-minutesLeft==0){
                    timeTakenString="0"+String.valueOf(4-minutesLeft)+":0"+String.valueOf(30-secondsLeft);
                }else{
                    timeTakenString="0"+String.valueOf(4-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
                }

            }


        }else{
            timeTakenInt=((5-minutesLeft)*60)+(60-secondsLeft);


            if((60-secondsLeft)>=10){
                if(secondsLeft==0){
                    timeTakenString="0"+String.valueOf(5-minutesLeft+1)+":00";
                }else{
                    timeTakenString="0"+String.valueOf(5-minutesLeft)+":"+String.valueOf(60-secondsLeft);
                }
            }else{
                timeTakenString="0"+String.valueOf(5-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
            }

        }


        ScoreGenerator scoreGenerator=new ScoreGenerator(minutesLeft,secondsLeft,oppoLifelineSum,numberOfCorrectAnswer);


        DataExchangeHolder dataExchangeHolder=new DataExchangeHolder(oppoMap,booleans,numberOfCorrectAnswer,timeTakenString,oppoLifelineSum,0,scoreGenerator.start(),name,imageURL,timeTakenInt);
        table_user.child("BOT_TOURNAMENT").child("RESULT").child(mAuth.getCurrentUser().getUid()).child(UID).setValue(dataExchangeHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {






            }
        });



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






//        if((60-secondsLeft)>=10){
//            timeTakenString="0"+String.valueOf(2-minutesLeft)+":"+String.valueOf(60-secondsLeft);
//        }else{
//            timeTakenString="0"+String.valueOf(2-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
//        }

        int timeTakenInt;
        if(time==180){
            timeTakenInt=((2-minutesLeft)*60)+(60-secondsLeft);

            if((60-secondsLeft)>=10){
                if(secondsLeft==0){
                    timeTakenString="0"+String.valueOf(2-minutesLeft+1)+":00";
                }else{
                    timeTakenString="0"+String.valueOf(2-minutesLeft)+":"+String.valueOf(60-secondsLeft);
                }
            }else{
                timeTakenString="0"+String.valueOf(2-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
            }


        }else if(time==270){
            if(4-minutesLeft==0){
                timeTakenInt=30-secondsLeft;
            }else{
                timeTakenInt=((4-minutesLeft)*60)+(60-secondsLeft);

            }

            if((60-secondsLeft)>=10){
                if(secondsLeft==0){
                    timeTakenString="0"+String.valueOf(4-minutesLeft+1)+":00";
                }else{
                    if(4-minutesLeft==0){
                        timeTakenString="0"+String.valueOf(4-minutesLeft)+":"+String.valueOf(30-secondsLeft);
                    }else{
                        timeTakenString="0"+String.valueOf(4-minutesLeft)+":"+String.valueOf(60-secondsLeft);
                    }

                }
            }else{
                if(4-minutesLeft==0){
                    timeTakenString="0"+String.valueOf(4-minutesLeft)+":0"+String.valueOf(30-secondsLeft);
                }else{
                    timeTakenString="0"+String.valueOf(4-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
                }

            }


        }else{
            timeTakenInt=((5-minutesLeft)*60)+(60-secondsLeft);


            if((60-secondsLeft)>=10){
                if(secondsLeft==0){
                    timeTakenString="0"+String.valueOf(5-minutesLeft+1)+":00";
                }else{
                    timeTakenString="0"+String.valueOf(5-minutesLeft)+":"+String.valueOf(60-secondsLeft);
                }
            }else{
                timeTakenString="0"+String.valueOf(5-minutesLeft)+":0"+String.valueOf(60-secondsLeft);
            }

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

        DataExchangeHolder dataExchangeHolder=new DataExchangeHolder(map,animList,score,timeTakenString,lifelineSum,0,scoreGenerator.start(),myName,myPicURL,timeTakenInt);

        Dialog dialog=null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,BOTTournamentPictureActivity.this);
        supportAlertDialog.showLoadingDialog();

        Record record =new Record(scoreGenerator.start(),score,1,BOTTournamentPictureActivity.this,audienceLL,timeTakenInt,myName,myPicURL);
        record.startLineGraph();
        record.startBarGroup();
        record.setLeaderBoard();

        table_user.child("BOT_TOURNAMENT").child("RESULT").child(mAuth.getCurrentUser().getUid()).child(mAuth.getCurrentUser().getUid()).setValue(dataExchangeHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                supportAlertDialog.dismissLoadingDialog();



                if(mInterstitialAd!=null) {
                    // Step 1: Display the interstitial
                    mInterstitialAd.show(BOTTournamentPictureActivity.this);
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


    private void intentFun(){

        try{ table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).removeEventListener(playerInfoGetterListener);}catch (Exception e){}


        Intent intent=new Intent(BOTTournamentPictureActivity.this, BOTScoreActivity.class);
        intent.putExtra("maxQuestions",numberOfQuestions-1);
        intent.putExtra("playerNum",myPlayerNum);
        intent.putExtra("hostName",hostName);
        intent.putExtra("numberOfActivePlayer",botData.size()+1);

        if(numberOfQuestions==11){
            intent.putExtra("numberOfQuestions",1);

        }else if(numberOfQuestions==16){
            intent.putExtra("numberOfQuestions",2);
        }else if(numberOfQuestions==21){
            intent.putExtra("numberOfQuestions",3);
        }

        if(time==180){
            intent.putExtra("time",1);
        }else if(time==270){
            intent.putExtra("time",2);
        }else if(time==360){
            intent.putExtra("time",3);
        }


        intent.putExtra("gameMode",gameMode);





        startActivity(intent);
        finish();
    }




    public void songStopperAndResumer(){
        CardView cardViewSpeaker=(CardView) findViewById(R.id.cardViewSpeaker);
        final ImageView speakerImage=(ImageView) findViewById(R.id.speakerImage);
        final LinearLayout Speaker=(LinearLayout) findViewById(R.id.Speaker);
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,BOTTournamentPictureActivity.this)){
            songActivity=new SongActivity(this);
            songActivity.startMusic();
        }else{
            Speaker.setBackgroundResource(R.drawable.usedicon);
            speakerImage.setBackgroundResource(R.drawable.music_off);
        }
        cardViewSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,BOTTournamentPictureActivity.this)){
                    songActivity.songStop();
                    Speaker.setBackgroundResource(R.drawable.usedicon);
                    speakerImage.setBackgroundResource(R.drawable.music_off);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,BOTTournamentPictureActivity.this,false);
                }else{
                    songActivity=new SongActivity(BOTTournamentPictureActivity.this);
                    songActivity.startMusic();
                    Speaker.setBackgroundResource(R.drawable.single_color_2);
                    speakerImage.setBackgroundResource(R.drawable.music_on);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,BOTTournamentPictureActivity.this,true);
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

        try{c.cancel();}catch (Exception e){}
        try{mInterstitialAd=null;}catch (Exception e){}
        try{mAdView.destroy();}catch (Exception e){}
        try{ songActivity.songStop(); }catch (Exception e){ }
        if(countDownTimer!=null){ countDownTimer.cancel();}

        Runtime.getRuntime().gc();
    }

    public void quitScoreActivityActivity(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(BOTTournamentPictureActivity.this, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(BOTTournamentPictureActivity.this).inflate(R.layout.dialog_model_2,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
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
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, BOTTournamentPictureActivity.this)){
            MobileAds.initialize(BOTTournamentPictureActivity.this);
            AdLoader adLoader = new AdLoader.Builder(BOTTournamentPictureActivity.this, AppString.NATIVE_ID)
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
                try{ table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).removeEventListener(playerInfoGetterListener);}catch (Exception e){}
                //TODO INTENT FUNCTION TO BE DONE

                try{
                    countDownTimer.cancel();
                }catch (Exception e){

                }

                Intent intent=new Intent(BOTTournamentPictureActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

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

                Toast.makeText(BOTTournamentPictureActivity.this, "Time Over", Toast.LENGTH_SHORT).show();
                quizFinishDialog();


            }

        }.start();
    }
    
    
    
}