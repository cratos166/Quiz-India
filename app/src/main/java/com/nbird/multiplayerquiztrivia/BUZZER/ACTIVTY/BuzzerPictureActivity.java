package com.nbird.multiplayerquiztrivia.BUZZER.ACTIVTY;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.BUZZER.ADAPTER.PlayerDataDisplayBuzzer;
import com.nbird.multiplayerquiztrivia.BUZZER.EXTRA.BuzzerAnswerUploader;
import com.nbird.multiplayerquiztrivia.BUZZER.MODEL.BuzzerDataExchangeHolder;
import com.nbird.multiplayerquiztrivia.BUZZER.MODEL.BuzzerManupulationHolder;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.questionHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.Timers.PicLoader;

import java.util.ArrayList;
import java.util.List;

public class BuzzerPictureActivity extends AppCompatActivity {


    TextView questionTextView,scoreBoard,timerText;
    Button option1,option2,option3,option4;
    LinearLayout linearLayout,linearLayoutexpert,linearLayoutAudience,linearLayoutFiftyFifty,linearLayoutSwap;
    CardView clockCardView;

    Dialog loadingDialog;
    CountDownTimer countDownTimer;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");

    private List<questionHolder> list;
    private ArrayList<Integer> ansList;


    int position=0,num=0,score=0,myPosition=-1,count;
    String myName,myPicURL;

    AppData appData;
    SongActivity songActivity;


    SupportAlertDialog supportAlertDialog;

    ArrayList<Integer> listAns;
    String roomCode;
    int time,myPlayerNum;

    BuzzerAnswerUploader answerUploader;

    PlayerDataDisplayBuzzer playerDataDisplayBuzzer;

    ValueEventListener playerInfoGetterListener,BUZZERTrackerListener;
    RecyclerView recyclerView;

    int minutes=2;
    int second=59;
    String minutestext;
    String secondtext,hostName;
    int numberOfQuestions;

    int myScore=0;

    int currentQuestionStatus=0;
    int kk=15;

    //0->NO ONE HAS ANSWERED->3 or -1
    //1->1 PERSON HAS ANSWERED WRONG->2 or -1
    //2->2 PERSONS HAVE ANSWERED WRONG, CHANGE QUESTION->1 or -1
    //-1->CORRECT
    boolean isAnswered=false;

    ImageView questionImage;

    CountDownTimer cc;



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzzer_picture);

        loadAds();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        listAns=getIntent().getIntegerArrayListExtra("answerInt");
        roomCode=getIntent().getStringExtra("roomCode");
        time=getIntent().getIntExtra("time",180);
        myPlayerNum=getIntent().getIntExtra("playerNum",1);
        hostName= getIntent().getStringExtra("hostName");
        numberOfQuestions = getIntent().getIntExtra("numberOfQuestions",10);

        list=new ArrayList<>();
        appData=new AppData();
        ansList=new ArrayList<>();


        if(myPlayerNum==1){
            table_user.child("BUZZER").child("RESULT").child(roomCode).removeValue();
        }

        songStopperAndResumer();

        questionTextView=findViewById(R.id.question);
        scoreBoard=findViewById(R.id.questionNumber);
        option1=(Button) findViewById(R.id.button1);
        option2=(Button) findViewById(R.id.button2);
        option3=(Button) findViewById(R.id.button3);
        option4=(Button) findViewById(R.id.button4);
        linearLayout=(LinearLayout) findViewById(R.id.linearButtonlayout);
        timerText=(TextView) findViewById(R.id.timer);
        linearLayoutexpert=(LinearLayout) findViewById(R.id.linearLayoutexpert) ;
        linearLayoutAudience=(LinearLayout) findViewById(R.id.linearLayoutAudience) ;
        linearLayoutFiftyFifty=(LinearLayout) findViewById(R.id.linearLayoutfiftyfifty) ;
        linearLayoutSwap=(LinearLayout) findViewById(R.id.linearLayoutSwap) ;
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView) ;
        questionImage=(ImageView) findViewById(R.id.questionImage);



        clockCardView = (CardView) findViewById(R.id.cardView3);

        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, BuzzerPictureActivity.this);
        myPicURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, BuzzerPictureActivity.this);

        // timer=new NormalBUZZERTimer(countDownTimer,time*1000,1000,BuzzerPictureActivity.this,timerText,clockCardView);

        supportAlertDialog=new SupportAlertDialog(loadingDialog,BuzzerPictureActivity.this);
        supportAlertDialog.showLoadingDialog();

        questionSelector();

        answerUploader=new BuzzerAnswerUploader(roomCode,myName,myPicURL);
        answerUploader.start();

        playerDataDisplayBuzzer=new PlayerDataDisplayBuzzer(BuzzerPictureActivity.this,playerInfoGetterListener,roomCode,recyclerView,currentQuestionStatus);
        playerDataDisplayBuzzer.start();


        Toast.makeText(this, "Game starts in 15 seconds.Please wait!", Toast.LENGTH_SHORT).show();
        cc=new CountDownTimer(1000*15,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                supportAlertDialog.dismissLoadingDialog();
                setCountDownTimer();
            }
        }.start();

        
        
    }

    public void questionSelector() {
        for (int i = 0; i < listAns.size(); i++) {
            int setNumber=listAns.get(i);
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
                Toast.makeText(BuzzerPictureActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                supportAlertDialog.dismissLoadingDialog();
                finish();
            }
        });
    }

    public void mainManupulations(){

        num++;
        if (num == listAns.size()) {

            if (list.size() > 0) {
                for (int i = 0; i < 4; i++) {
                    int finalI = i;
                    linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View view) {
                            try {
                                checkAnswer((Button) view, finalI);
                            } catch (Exception e) {
                                //        Toast.makeText(quizActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
            } else {
                finish();
                Toast.makeText(BuzzerPictureActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
            }

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

                        scoreBoard.setText((position+1)+"/"+(listAns.size()-1));

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
        musicNav = MediaPlayer.create(BuzzerPictureActivity.this,id);
        musicNav.start();
        musicNav.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                musicNav.reset();
                musicNav.release();
            }
        });
    }

    private void BUZZERStatusSetter(int value){

        myRef.child("BUZZER").child("BUZZER_TRACKER").child(roomCode).child(String.valueOf(position)).child("currentQuestionStatus").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });



    }


    private void BUZZEROptionsSetter(String button,int value){
        myRef.child("BUZZER").child("BUZZER_TRACKER").child(roomCode).child(String.valueOf(position)).child(button).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(Button selectedOption,int i){




        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){

            isAnswered=true;


            ansList.add(1);

            if(currentQuestionStatus==0){
                myScore=myScore+3;
                BUZZERStatusSetter(3);
            }else if(currentQuestionStatus==1){
                myScore=myScore+2;
                BUZZERStatusSetter(3);
            }else if (currentQuestionStatus==2){
                myScore=myScore+1;
                BUZZERStatusSetter(3);
            }

            if(i==0){
                BUZZEROptionsSetter("option1",1);
            }else if(i==1){
                BUZZEROptionsSetter("option2",1);
            }else if(i==2){
                BUZZEROptionsSetter("option3",1);
            }else if(i==3){
                BUZZEROptionsSetter("option4",1);
            }


            scoreUploader();
            answerUploader.upload(1);
            //correct
            playMusic(R.raw.correctmusic);
//            ANIM_MANU(R.raw.tickanim);

            selectedOption.setBackgroundResource(R.drawable.option_right);
            //green color
            selectedOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            selectedOption.setShadowLayer(3,1,1,R.color.green);
            score++;


            enableOption(false);
        }else {

            isAnswered=true;

            ansList.add(2);

            if(currentQuestionStatus==0){
                myScore=myScore-1;
                BUZZERStatusSetter(1);
            }else if(currentQuestionStatus==1){
                myScore=myScore-1;
                BUZZERStatusSetter(2);
            }else if (currentQuestionStatus==2){
                myScore=myScore-1;
                BUZZERStatusSetter(3);
            }


            if(i==0){
                BUZZEROptionsSetter("option1",2);
            }else if(i==1){
                BUZZEROptionsSetter("option2",2);
            }else if(i==2){
                BUZZEROptionsSetter("option3",2);
            }else if(i==3){
                BUZZEROptionsSetter("option4",2);
            }

            scoreUploader();
            answerUploader.upload(2);
            //incorrect
            playMusic(R.raw.wrongansfinal);
//            ANIM_MANU(R.raw.wronganim);

            selectedOption.setBackgroundResource(R.drawable.option_wrong);     //red color
            selectedOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            selectedOption.setShadowLayer(3,1,1,R.color.green);
            Button correctOption = (Button) linearLayout.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.option_right);    //green color
            correctOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            correctOption.setShadowLayer(3,1,1,R.color.red);
            enableOption(false);
        }
    }


    private void scoreUploader(){
        table_user.child("BUZZER").child("ANSWERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("score").setValue(myScore).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
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

        Dialog dialog=null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog, BuzzerPictureActivity.this);
        supportAlertDialog.showLoadingDialog();



        BuzzerDataExchangeHolder dataExchangeHolder=new BuzzerDataExchangeHolder(ansList,myScore,score,0,myName,myPicURL);

        table_user.child("BUZZER").child("RESULT").child(roomCode).child(mAuth.getCurrentUser().getUid()).setValue(dataExchangeHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("activityNumber").setValue(3).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        supportAlertDialog.dismissLoadingDialog();

                        try{ table_user.child("BUZZER").child("ANSWERS").child(roomCode).removeEventListener(playerInfoGetterListener);}catch (Exception e){}
                        try{ myRef.child("BUZZER").child("BUZZER_TRACKER").child(roomCode).child(String.valueOf(position)).removeEventListener(BUZZERTrackerListener);}catch (Exception e){}




                        if(mInterstitialAd!=null) {
                            // Step 1: Display the interstitial
                            mInterstitialAd.show(BuzzerPictureActivity.this);
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
        Intent intent=new Intent(BuzzerPictureActivity.this,BuzzerScoreActivity.class);
        intent.putExtra("roomCode",roomCode);
        intent.putExtra("maxQuestions",list.size()-1);
        intent.putExtra("playerNum",myPlayerNum);
        intent.putExtra("hostName",hostName);
        startActivity(intent);
        finish();
    }



    public void songStopperAndResumer(){
        CardView cardViewSpeaker=(CardView) findViewById(R.id.cardViewSpeaker);
        final ImageView speakerImage=(ImageView) findViewById(R.id.speakerImage);
        final LinearLayout Speaker=(LinearLayout) findViewById(R.id.Speaker);
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,BuzzerPictureActivity.this)){
            songActivity=new SongActivity(this);
            songActivity.startMusic();
        }else{
            Speaker.setBackgroundResource(R.drawable.usedicon);
            speakerImage.setBackgroundResource(R.drawable.music_off);
        }
        cardViewSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,BuzzerPictureActivity.this)){
                    songActivity.songStop();
                    Speaker.setBackgroundResource(R.drawable.usedicon);
                    speakerImage.setBackgroundResource(R.drawable.music_off);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,BuzzerPictureActivity.this,false);
                }else{
                    songActivity=new SongActivity(BuzzerPictureActivity.this);
                    songActivity.startMusic();
                    Speaker.setBackgroundResource(R.drawable.single_color_2);
                    speakerImage.setBackgroundResource(R.drawable.music_on);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,BuzzerPictureActivity.this,true);
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

        try{ songActivity.songStop(); }catch (Exception e){ }
        if(countDownTimer!=null){ countDownTimer.cancel();}
        try{ if(cc!=null){cc.cancel();}}catch (Exception e){}


        Runtime.getRuntime().gc();
    }

    public void quitScoreActivityActivity(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(BuzzerPictureActivity.this, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(BuzzerPictureActivity.this).inflate(R.layout.dialog_model_2,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
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
                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,BuzzerPictureActivity.this);
                supportAlertDialog.showLoadingDialog();


                if(myPlayerNum==1){
                    table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        try{table_user.child("BUZZER").child("ANSWERS").child(roomCode).removeEventListener(playerInfoGetterListener);}catch (Exception e){}

        try{countDownTimer.cancel();}catch (Exception e){}


        Intent intent=new Intent(BuzzerPictureActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    private void setCountDownTimer(){


        listnereSetter();
        countDownTimer=new CountDownTimer(15*numberOfQuestions*1000, 1000) {

            @SuppressLint("ResourceAsColor")
            public void onTick(long millisUntilFinished) {

                if(kk==0){
                    currentQuestionStatus=0;
                    timerText.setText(String.valueOf(kk));
                    if(!isAnswered){
                        answerUploader.upload(3);
                        ansList.add(3);
                    }

                    isAnswered=false;

                    kk=15;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        enableOption(true);
                    }
                    listenerRemover();
                    position++;
                    listnereSetter();
                    if (position == listAns.size()-1) { }
                    count = 0;
                    playAnim(questionTextView, 0, list.get(position).getQuestionTextView());

                }else{
                    timerText.setText(String.valueOf(kk));
                    kk--;
                }

            }
            public void onFinish() {

                quizFinishDialog();


            }

        }.start();
    }


    private void listenerRemover(){
        myRef.child("BUZZER").child("BUZZER_TRACKER").child(roomCode).child(String.valueOf(position)).removeEventListener(BUZZERTrackerListener);
    }


    private void listnereSetter(){
        BUZZERTrackerListener=new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try{


                    BuzzerManupulationHolder BUZZERManupulationHolder=snapshot.getValue(BuzzerManupulationHolder.class);
                    int option1Ans=BUZZERManupulationHolder.getOption1();
                    int option2Ans=BUZZERManupulationHolder.getOption2();
                    int option3Ans=BUZZERManupulationHolder.getOption3();
                    int option4Ans=BUZZERManupulationHolder.getOption4();
                    currentQuestionStatus=BUZZERManupulationHolder.getCurrentQuestionStatus();
                    if(option1Ans==1){
                        option1.setBackgroundResource(R.drawable.option_right);
                        //green color
                        option1.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                        option1.setShadowLayer(3,1,1,R.color.green);
                        enableOption(false);
                    }else if(option2Ans==1){
                        option2.setBackgroundResource(R.drawable.option_right);
                        //green color
                        option2.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                        option2.setShadowLayer(3,1,1,R.color.green);
                        enableOption(false);
                    }else if(option3Ans==1){
                        option3.setBackgroundResource(R.drawable.option_right);
                        //green color
                        option3.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                        option3.setShadowLayer(3,1,1,R.color.green);
                        enableOption(false);
                    }else if(option4Ans==1){
                        option4.setBackgroundResource(R.drawable.option_right);
                        //green color
                        option4.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                        option4.setShadowLayer(3,1,1,R.color.green);
                        enableOption(false);
                    }

                    if(option1Ans==2){
                        option1.setBackgroundResource(R.drawable.option_wrong);     //red color
                        option1.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                        option1.setShadowLayer(3,1,1,R.color.green);
                    }

                    if(option2Ans==2){
                        option2.setBackgroundResource(R.drawable.option_wrong);     //red color
                        option2.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                        option2.setShadowLayer(3,1,1,R.color.green);
                    }

                    if(option3Ans==2){
                        option3.setBackgroundResource(R.drawable.option_wrong);     //red color
                        option3.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                        option3.setShadowLayer(3,1,1,R.color.green);
                    }

                    if(option4Ans==2){
                        option4.setBackgroundResource(R.drawable.option_wrong);     //red color
                        option4.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                        option4.setShadowLayer(3,1,1,R.color.green);
                    }


                    if(currentQuestionStatus==3){

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            enableOption(false);
                        }

                    }

                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRef.child("BUZZER").child("BUZZER_TRACKER").child(roomCode).child(String.valueOf(position)).addValueEventListener(BUZZERTrackerListener);
    }



}