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
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.FIREBASE.HighestScore;
import com.nbird.multiplayerquiztrivia.FIREBASE.TotalScore;
import com.nbird.multiplayerquiztrivia.GENERATORS.ScoreGenerator;
import com.nbird.multiplayerquiztrivia.LL.LLManupulator;
import com.nbird.multiplayerquiztrivia.LL.LifeLine;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.DataExchangeHolder;
import com.nbird.multiplayerquiztrivia.Model.questionHolder;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalPictureQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.EXTRA.AnswerUploader;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.EXTRA.PlayerDisplayInQuiz;
import com.nbird.multiplayerquiztrivia.Timers.PicLoader;
import com.nbird.multiplayerquiztrivia.Timers.QuizTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TournamentPictureActivity extends AppCompatActivity {

    TextView questionTextView,scoreBoard,timerText;
    Button option1,option2,option3,option4,nextButton;
    LinearLayout linearLayout,linearLayoutexpert,linearLayoutAudience,linearLayoutFiftyFifty,linearLayoutSwap;
    CardView audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL,clockCardView;

    Dialog loadingDialog;
    CountDownTimer countDownTimer,c,c1;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");

    private List<questionHolder> list;
    ArrayList<LottieAnimationView> animationList;
    ArrayList<Boolean> animList;

    int fiftyfiftynum=0,audiencenum=0,swapnum=0,expertnum=0,lifelineSum=0,position=0,num=0,score=0,myPosition=-1,count,statusFinder=1;
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

    int minutes=2;
    int second=59;
    String minutestext;
    String secondtext,hostName;
    ImageView questionImage;


    CardView playOrPauseButton;
    LottieAnimationView backwardanim,forwardanim;
    SeekBar seekBar;
    LinearLayout linearFun1;
    MediaPlayer music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_picture);

        listAns=getIntent().getIntegerArrayListExtra("answerInt");
        roomCode=getIntent().getStringExtra("roomCode");
        time=getIntent().getIntExtra("time",180);
        myPlayerNum=getIntent().getIntExtra("playerNum",1);
        hostName= getIntent().getStringExtra("hostName");

        list=new ArrayList<>();
        appData=new AppData();
        animationList=new ArrayList<>();
        animList=new ArrayList<>();


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
        questionImage=(ImageView) findViewById(R.id.questionImage);

        linearFun1=(LinearLayout) findViewById(R.id.linearFun1);
        playOrPauseButton=(CardView) findViewById(R.id.mainButton);
        seekBar=(SeekBar) findViewById(R.id.determinateBar);
        backwardanim=(LottieAnimationView) findViewById(R.id.backwardanim);
        forwardanim=(LottieAnimationView) findViewById(R.id.forwardanim);


        clockCardView = (CardView) findViewById(R.id.cardView3);

        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, TournamentPictureActivity.this);
        myPicURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, TournamentPictureActivity.this);


        llManupulator=new LLManupulator(audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL);

       // timer=new QuizTimer(countDownTimer,60000*3,1000, TournamentPictureActivity.this,timerText,clockCardView);

        animationListner();
        seekerManupulator();


        c1=new CountDownTimer(3*60*1000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    seekBar.setProgress(music.getCurrentPosition());
                }catch (Exception e){

                }
            }
            @Override
            public void onFinish() {

            }
        }.start();


        supportAlertDialog=new SupportAlertDialog(loadingDialog,TournamentPictureActivity.this);
        supportAlertDialog.showLoadingDialog();

        lifeLine();
        questionSelector();


        c=new CountDownTimer(1000*15,1000) {
            @Override
            public void onTick(long l) {
                if(questionImage.getDrawable() != null){
                    try {
                        if(c!=null){
                            c.cancel();
                        }
                        setCountDownTimer();
                        supportAlertDialog.dismissLoadingDialog();
                    }catch (Exception e){

                    }

                }
            }

            @Override
            public void onFinish() {
                setCountDownTimer();
                supportAlertDialog.dismissLoadingDialog();
            }
        }.start();

        totalScore=new TotalScore();
        totalScore.getSingleModeScore();

        highestScore=new HighestScore();
        highestScore.start();




        answerUploader=new AnswerUploader(roomCode,myName,myPicURL);
        answerUploader.start();

        playerDisplayInQuiz=new PlayerDisplayInQuiz(TournamentPictureActivity.this,playerInfoGetterListener,roomCode,recyclerView);
        playerDisplayInQuiz.start();
        
        
    }


    public void lifeLine(){

        lifeLine=new LifeLine(linearLayoutFiftyFifty,linearLayoutAudience,linearLayoutexpert,position,list,option1,option2,option3,option4,myName,TournamentPictureActivity.this);

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
        myRef.child("SongQuizJson").child(String.valueOf(setNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
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

            }
        });
    }

    public void mainManupulations(){

        num++;
        if (num == listAns.size()) {

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
                Toast.makeText(TournamentPictureActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
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

                        String linkHolder=list.get(position).getImageURL();


                        clearMediaPlayer();
                        songURLDownload(list.get(position).getSongURL());


                        Glide.with(getBaseContext()).load(linkHolder).apply(RequestOptions
                                        .bitmapTransform(new RoundedCorners(14)))
                                .into(questionImage);

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
        musicNav = MediaPlayer.create(TournamentPictureActivity.this,id);
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

        table_user.child("TOURNAMENT").child("RESULT").child(roomCode).child(mAuth.getCurrentUser().getUid()).setValue(dataExchangeHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try{table_user.child("TOURNAMENT").child("ANSWERS").child(roomCode).removeEventListener(playerInfoGetterListener);}catch (Exception e){}

                Intent intent=new Intent(TournamentPictureActivity.this,ScoreActivity.class);
                intent.putExtra("roomCode",roomCode);
                intent.putExtra("maxQuestions",list.size()-1);
                intent.putExtra("playerNum",myPlayerNum);
                intent.putExtra("hostName",hostName);
                startActivity(intent);
                finish();

            }
        });


    }






    public void songStopperAndResumer(){
        CardView cardViewSpeaker=(CardView) findViewById(R.id.cardViewSpeaker);
        final ImageView speakerImage=(ImageView) findViewById(R.id.speakerImage);
        final LinearLayout Speaker=(LinearLayout) findViewById(R.id.Speaker);
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,TournamentPictureActivity.this)){
            songActivity=new SongActivity(this);
            songActivity.startMusic();
        }else{
            Speaker.setBackgroundResource(R.drawable.usedicon);
            speakerImage.setBackgroundResource(R.drawable.music_off);
        }
        cardViewSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,TournamentPictureActivity.this)){
                    songActivity.songStop();
                    Speaker.setBackgroundResource(R.drawable.usedicon);
                    speakerImage.setBackgroundResource(R.drawable.music_off);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,TournamentPictureActivity.this,false);
                }else{
                    songActivity=new SongActivity(TournamentPictureActivity.this);
                    songActivity.startMusic();
                    Speaker.setBackgroundResource(R.drawable.single_color_2);
                    speakerImage.setBackgroundResource(R.drawable.music_on);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,TournamentPictureActivity.this,true);
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
        if(c!=null){ c.cancel();}
        if(c1!=null){ c1.cancel();}
        Runtime.getRuntime().gc();
    }

    public void quitScoreActivityActivity(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(TournamentPictureActivity.this, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(TournamentPictureActivity.this).inflate(R.layout.dialog_model_2,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
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
                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,TournamentPictureActivity.this);
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


        Intent intent=new Intent(TournamentPictureActivity.this, MainActivity.class);
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

                    timerText.setTextColor(R.color.red);

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




                minutes=0;
                second=0;

                Toast.makeText(TournamentPictureActivity.this, "Time Over", Toast.LENGTH_SHORT).show();
                quizFinishDialog();


            }

        }.start();
    }

    public void animationListner(){
        playOrPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusFinder==1){
                    statusFinder=0;
                    linearFun1.setBackgroundResource(R.drawable.play_button);
                    pauseMusic();
                }else{
                    statusFinder=1;
                    linearFun1.setBackgroundResource(R.drawable.pause_button);
                    startMusic();
                }
            }
        });

        backwardanim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=music.getCurrentPosition();
                if(i<=5000){
                    music.seekTo(0);
                    seekBar.setProgress(0);

                }else{
                    int p=i-5000;
                    music.seekTo(p);
                    seekBar.setProgress(p);
                }
                backwardanim.setAnimation(R.raw.rewind_anim);
                backwardanim.playAnimation();
                backwardanim.loop(false);

            }
        });

        forwardanim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=music.getCurrentPosition();
                try{
                    int p=i+5000;
                    music.seekTo(p);
                    seekBar.setProgress(p);
                }catch (Exception e){
                    music.seekTo(music.getDuration());
                    seekBar.setProgress(music.getDuration());
                    music.pause();
                }
                forwardanim.setAnimation(R.raw.forward_anim);
                forwardanim.playAnimation();
                forwardanim.loop(false);
            }
        });
    }


    public void seekerManupulator(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                // seekBarHint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                // seekBarHint.setVisibility(View.VISIBLE);



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (music != null && music.isPlaying()) {
                    music.seekTo(seekBar.getProgress());
                    music.start();
                }
            }
        });
    }
    private void clearMediaPlayer() {
        try {
            music.stop();
            music.release();
            music = null;
        }catch (Exception e){

        }

    }

    public void pauseMusic(){
        try{
            music.pause();
        }catch (Exception e){

        }

    }

    public void startMusic(){

        try{
            music.start();
        }catch (Exception e){

        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        Boolean isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
        if (isInBackground) {
            music.pause();
        } else {
            try{
                if (!music.isPlaying()) {
                    music.start();
                    music.setVolume(0.4f,0.4f);
                }
            }catch (Exception e){

            }

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            music.pause();
        }catch (Exception e){

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        Boolean isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
        if (isInBackground) {

            try{
                music.pause();
            }catch (Exception e){

            }
        } else {
            try{
                if (!music.isPlaying()) {
                    music.start();
                    music.setVolume(0.4f,0.4f);
                }
            }catch (Exception e){

            }

        }
    }


    private  class DownloadData extends AsyncTask<String,Void,String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            supportAlertDialog.dismissLoadingDialog();

            try{
                DownloadData.this.cancel(true);
            }catch (Exception e){

            }


            return;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                music.setDataSource(strings[0]);
            } catch (IOException e) {

            }
            music.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    music.start();
                    seekBar.setMax(music.getDuration());
                    music.setLooping(true);

                }
            });
            music.prepareAsync();
            return null;
        }
    }

    public void songURLDownload(String musicUrl){

        music = new MediaPlayer();

        try{
            DownloadData downloadData=new DownloadData();
            downloadData.execute(musicUrl);
        }catch (Exception e) {

        }

    }


}