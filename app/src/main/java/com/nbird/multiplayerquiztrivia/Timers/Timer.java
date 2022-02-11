package com.nbird.multiplayerquiztrivia.Timers;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.nbird.multiplayerquiztrivia.R;

public class Timer {

    CountDownTimer countDownTimer;
    long totalTime;
    long interval;
    Context context;

    int minutes=0;
    int second=0;
    String minutestext;
    String secondtext;

    TextView clockTextView;
    CardView cardViewClock;

    public Timer(CountDownTimer countDownTimer, long totalTime, long interval, Context context,TextView clockTextView,CardView cardViewClock) {
        this.countDownTimer = countDownTimer;
        this.totalTime = totalTime;
        this.interval = interval;
        this.context = context;
        this.clockTextView=clockTextView;
        this.cardViewClock=cardViewClock;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    public void start(){
        countDownTimer=new CountDownTimer(totalTime, interval) {


            @SuppressLint("ResourceAsColor")
            public void onTick(long millisUntilFinished) {
                if(second==60){
                    second=0;
                    minutes++;

                    minutestext="0"+String.valueOf(minutes);

                    if(second<10){
                        secondtext="0"+String.valueOf(second);
                    }else{
                        secondtext=String.valueOf(second);
                    }
                    clockTextView.setText(" Timer "+minutestext+":"+secondtext+" ");
                    second++;
                }else{
                    minutestext="0"+String.valueOf(minutes);
                    if(second<10){
                        secondtext="0"+String.valueOf(second);
                    }else{
                        secondtext=String.valueOf(second);
                    }
                    clockTextView.setText(" Timer "+minutestext+":"+secondtext+" ");
                    second++;
                }

                //Last 15 seconds end animation
                if(minutes==0 && second>45){

                    clockTextView.setTextColor(R.color.red);


                    //Continuous zoomIn - zoomOut
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(cardViewClock, "scaleX", 0.9f, 1f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(cardViewClock, "scaleY", 0.9f, 1f);

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

                Toast.makeText(context, "Time Over", Toast.LENGTH_SHORT).show();


//                Intent scoreIntent = new Intent(context, scoreActivity.class);
//                try{
//                    songActivity.songStop();
//                }catch (Exception e){
//
//                }
//                scoreIntent.putExtra("score", score);
//                scoreIntent.putExtra("lifeline",lifelineSum);
//                scoreIntent.putExtra("minutes",minutes);
//                scoreIntent.putExtra("seconds",second);
//                scoreIntent.putExtra("minutestext",minutestext);
//                scoreIntent.putExtra("secondtext",secondtext);
//                scoreIntent.putExtra("milliholder",milliHolder);
//                scoreIntent.putExtra("category",category);
//                scoreIntent.putExtra("imageurl",imageurl);
//                startActivity(scoreIntent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                if(countDownTimer!=null){
//                    countDownTimer.cancel();}
//                finish();
            }

        }.start();
    }

}
