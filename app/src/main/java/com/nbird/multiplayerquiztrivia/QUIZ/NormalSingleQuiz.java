package com.nbird.multiplayerquiztrivia.QUIZ;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.Dialog.QuizCancelDialog;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.LL.LLManupulator;
import com.nbird.multiplayerquiztrivia.LL.LifeLine;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.questionHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.Timers.QuizTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class NormalSingleQuiz extends AppCompatActivity {

    TextView questionTextView,scoreBoard;
    Button option1,option2,option3,option4,nextButton;
    LinearLayout linearLayout;

    CardView audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL;
    LinearLayout linearLayoutexpert,linearLayoutAudience,linearLayoutFiftyFifty,linearLayoutSwap;

    TextView timerText;

    LottieAnimationView anim11,anim12,anim13,anim14,anim15,anim16,anim17,anim18,anim19,anim20;

    ImageView myPic;
    Dialog loadingDialog;
    SupportAlertDialog supportAlertDialog;

    int category;
    AppData appData;
    CardView clockCardView;


    private List<questionHolder> list;

    int fiftyfiftynum=0,audiencenum=0,swapnum=0,expertnum=0;

    int lifelineSum=0;

    LifeLine lifeLine;
    String myName;

    int position=0;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    int num=0,score=0;
    SongActivity songActivity;
    int count;

    LLManupulator llManupulator;

    ArrayList<LottieAnimationView> animationList;
    int myPosition=-1;

    QuizTimer timer;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_single_quiz);

        category=getIntent().getIntExtra("category",1);

        list=new ArrayList<>();
        appData=new AppData();
        animationList=new ArrayList<>();


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
        anim11=(LottieAnimationView) findViewById(R.id.anim11);
        anim12=(LottieAnimationView) findViewById(R.id.anim12);
        anim13=(LottieAnimationView) findViewById(R.id.anim13);
        anim14=(LottieAnimationView) findViewById(R.id.anim14);
        anim15=(LottieAnimationView) findViewById(R.id.anim15);
        anim16=(LottieAnimationView) findViewById(R.id.anim16);
        anim17=(LottieAnimationView) findViewById(R.id.anim17);
        anim18=(LottieAnimationView) findViewById(R.id.anim18);
        anim19=(LottieAnimationView) findViewById(R.id.anim19);
        anim20=(LottieAnimationView) findViewById(R.id.anim20);
        myPic=(ImageView) findViewById(R.id.myPic);
        clockCardView = (CardView) findViewById(R.id.cardView3);

        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, NormalSingleQuiz.this);
        Glide.with(getBaseContext()).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, NormalSingleQuiz.this)).apply(RequestOptions
                .bitmapTransform(new RoundedCorners(18)))
                .into(myPic);

        llManupulator=new LLManupulator(audienceLL,expertAdviceLL,fiftyfiftyLL,swapTheQuestionLL);

        animationList.add(anim11);animationList.add(anim12);animationList.add(anim13);animationList.add(anim14);animationList.add(anim15);
        animationList.add(anim16);animationList.add(anim17);animationList.add(anim18);animationList.add(anim19);animationList.add(anim20);



        supportAlertDialog=new SupportAlertDialog(loadingDialog,NormalSingleQuiz.this);
        supportAlertDialog.showLoadingDialog();

        lifeLine();

        questionSelector();

    }


    public void lifeLine(){

        lifeLine=new LifeLine(linearLayoutFiftyFifty,linearLayoutAudience,linearLayoutexpert,position,list,option1,option2,
                option3,option4,myName,NormalSingleQuiz.this);


        fiftyfiftyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fiftyfiftynum==0) {
                    lifelineSum++;
                    fiftyfiftynum = 1;

                    lifeLine.fiftyfiftyLL();

                }else{
                    lifeLine.LLUsed();

                }


            }
        });


        audienceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(audiencenum==0) {

                    lifelineSum++;
                    audiencenum = 1;

                    lifeLine.audienceLL();

                }else{
                    lifeLine.LLUsed();
                }

            }
        });


        swapTheQuestionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swapnum==0) {

                    lifelineSum++;
                    swapnum=1;
                    linearLayoutSwap.setBackgroundResource(R.drawable.usedicon);
                    nextButton.setEnabled(false);
                    nextButton.setAlpha(0.7f);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        enableOption(true);
                    }
                    position++;
                    llManupulator.True();


                    count = 0;
                    playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                }else{
                    lifeLine.LLUsed();
                }
            }
        });


        expertAdviceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expertnum==0){


                    lifelineSum++;
                    expertnum=1;


                    lifeLine.expertAdviceLL();
                }else{
                    lifeLine.LLUsed();
                }
            }
        });



    }


    public void questionSelector() {
        for (int i = 0; i < 11; i++) {
            Random rand = new Random();
            int setNumber;
            switch (category) {
                case 1: case 3: case 4: case 5: case 6: case 9: case 10: case 11: case 12: case 17: setNumber = rand.nextInt(299) + 1;fireBaseData(setNumber);break;
                case 2: case 14: setNumber = rand.nextInt(499) + 1;fireBaseData(setNumber);break;
                case 7: setNumber = rand.nextInt(401) + 1;fireBaseData(setNumber);break;
                case 8: case 18: setNumber = rand.nextInt(339) + 1;fireBaseData(setNumber);break;
                case 13: case 15: case 16: setNumber = rand.nextInt(249) + 1;fireBaseData(setNumber);break;
                case 19: setNumber = rand.nextInt(399) + 1;fireBaseData(setNumber);break;
                default: setNumber = rand.nextInt(6326) + 1;fireBaseData2(setNumber);break;
            }
            //NEED TO CHANGE HERE
            //NEED TO CHANGE HERE
        }
    }

            public void fireBaseData ( int setNumber){
                myRef.child("SETS").child(String.valueOf(category)).child("questions").child(String.valueOf(setNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.add(snapshot.getValue(questionHolder.class));
                        mainManupulations();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(NormalSingleQuiz.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        supportAlertDialog.dismissLoadingDialog();
                        finish();
                    }
                });
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
                Toast.makeText(NormalSingleQuiz.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                supportAlertDialog.dismissLoadingDialog();
                finish();
            }
        });
    }

            public void mainManupulations(){

                num++;
                if (num == 10) {
                    timer=new QuizTimer(countDownTimer,60000*3,1000,NormalSingleQuiz.this,timerText,clockCardView);
                    timer.start();
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

                                if (swapnum == 0) {
                                    if (position == 10) {
                                        quizFinishDialog();
                                        return;
                                    }
                                } else {
                                    if (position == 11) {
                                        quizFinishDialog();
                                        return;
                                    }
                                }
                                count = 0;
                                playAnim(questionTextView, 0, list.get(position).getQuestionTextView());
                            }
                        });
                    } else {
                        finish();
                        Toast.makeText(NormalSingleQuiz.this, "No Questions", Toast.LENGTH_SHORT).show();
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
                        option1.setTextColor(Color.parseColor("#ffffff"));
                        linearLayout.getChildAt(0).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                    }else if(count==1){
                        option=list.get(position).getOption2();
                        option2.setTextColor(Color.parseColor("#ffffff"));
                        linearLayout.getChildAt(1).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                    }else if(count==2){
                        option=list.get(position).getOption3();
                        option3.setTextColor(Color.parseColor("#ffffff"));
                        linearLayout.getChildAt(2).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                    }else if(count==3){
                        option=list.get(position).getOption4();
                        option4.setTextColor(Color.parseColor("#ffffff"));
                        linearLayout.getChildAt(3).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
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
                            scoreBoard.setText(" Question "+(position+1)+"/10 ");
                        }else{
                            scoreBoard.setText(" Question "+(position)+"/10 ");
                        }

                    } catch (ClassCastException ex) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }



    public void playMusic(int id){
         MediaPlayer musicNav;
            musicNav = MediaPlayer.create(NormalSingleQuiz.this,id);
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

            playMusic(R.raw.correctmusic);

            ANIM_MANU(R.raw.tickanim);
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B1FF88")));   //green color
            selectedOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            selectedOption.setShadowLayer(3,1,1,R.color.lightgreen);
            score++;
        }else {
            //incorrect
            playMusic(R.raw.wrongansfinal);
            ANIM_MANU(R.raw.wronganim);
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF8888")));     //red color
            selectedOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            selectedOption.setShadowLayer(3,1,1,R.color.lightgreen);
            Button correctOption = (Button) linearLayout.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B1FF88")));     //green color
            correctOption.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            correctOption.setShadowLayer(3,1,1,R.color.lightred);
        }
    }

    public void ANIM_MANU(int id){
        myPosition++;
        LottieAnimationView anim=animationList.get(myPosition);
        anim.setAnimation(R.raw.tickanim);
        anim.playAnimation();
        anim.loop(false);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable){
        for (int i=0;i<4;i++) {
            linearLayout.getChildAt(i).setEnabled(enable);
            if (enable) {
                linearLayout.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E4E4E4")));
            }
        }
    }


            public void quizFinishDialog(){

//                try {
//                    songActivity.songStop();
//                } catch (Exception e) {
//
//                }
//                Intent scoreIntent = new Intent(quizActivity.this, scoreActivity.class);
//                scoreIntent.putExtra("score", score);
//                scoreIntent.putExtra("lifeline", lifelineSum);
//                scoreIntent.putExtra("minutes", minutes);
//                scoreIntent.putExtra("seconds", second);
//                scoreIntent.putExtra("minutestext", minutestext);
//                scoreIntent.putExtra("secondtext", secondtext);
//                scoreIntent.putExtra("category", category);
//                scoreIntent.putExtra("milliholder", milliHolder);
//                scoreIntent.putExtra("imageurl", imageurl);
//                startActivity(scoreIntent);
//                if (countDownTimer != null) {
//                    countDownTimer.cancel();
//                }
//                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
//                finish();
            }






    public void songStopperAndResumer(){
        CardView cardViewSpeaker=(CardView) findViewById(R.id.cardViewSpeaker);
        final ImageView speakerImage=(ImageView) findViewById(R.id.speakerImage);
        final LinearLayout Speaker=(LinearLayout) findViewById(R.id.Speaker);
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,NormalSingleQuiz.this)){
            songActivity=new SongActivity(this);
            songActivity.startMusic();
        }else{
            Speaker.setBackgroundResource(R.drawable.usedicon);
            speakerImage.setBackgroundResource(R.drawable.music_off);
        }
        cardViewSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,NormalSingleQuiz.this)){
                    songActivity.songStop();
                    Speaker.setBackgroundResource(R.drawable.usedicon);
                    speakerImage.setBackgroundResource(R.drawable.music_off);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,NormalSingleQuiz.this,false);
                }else{
                    songActivity=new SongActivity(NormalSingleQuiz.this);
                    songActivity.startMusic();
                    Speaker.setBackgroundResource(R.drawable.border_theme_2);
                    speakerImage.setBackgroundResource(R.drawable.music_on);
                    appData.setSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_SONG,NormalSingleQuiz.this,true);
                }
            }
        });
    }

    public void onBackPressed() {
        QuizCancelDialog quizCancelDialog=new QuizCancelDialog(NormalSingleQuiz.this,timer.getCountDownTimer(),option1,songActivity);
        quizCancelDialog.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try{ songActivity.songStop(); }catch (Exception e){ }
        if(countDownTimer!=null){ countDownTimer.cancel();}

        Runtime.getRuntime().gc();
    }


}






