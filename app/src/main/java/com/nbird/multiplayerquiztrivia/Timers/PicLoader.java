package com.nbird.multiplayerquiztrivia.Timers;

import android.app.Dialog;
import android.os.CountDownTimer;
import android.widget.ImageView;

import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;

public class PicLoader {
    CountDownTimer c;
    QuizTimer gameCountDownTimer;
    ImageView questionImage;
    SupportAlertDialog supportAlertDialog;
    NormalAudQuizTimer normalAudioDownTimer;
    NormalVidQuizTimer normalVidQuizTimer;

    public PicLoader(QuizTimer gameCountDownTimer, ImageView questionImage, SupportAlertDialog loadingDialog) {
        this.gameCountDownTimer = gameCountDownTimer;
        this.questionImage = questionImage;
        this.supportAlertDialog = loadingDialog;
    }

    public PicLoader(NormalAudQuizTimer normalAudioDownTimer, ImageView questionImage, SupportAlertDialog loadingDialog) {
        this.normalAudioDownTimer = normalAudioDownTimer;
        this.questionImage = questionImage;
        this.supportAlertDialog = loadingDialog;
    }




    public void startAudio(){
        c=new CountDownTimer(1000*15,1000) {
            @Override
            public void onTick(long l) {
                if(questionImage.getDrawable() != null){
                    try {
                        if(c!=null){
                            c.cancel();
                        }
                        normalAudioDownTimer.start();
                        supportAlertDialog.dismissLoadingDialog();
                    }catch (Exception e){

                    }

                }
            }

            @Override
            public void onFinish() {
                normalAudioDownTimer.start();
                supportAlertDialog.dismissLoadingDialog();
            }
        }.start();
    }



    public void start(){
        c=new CountDownTimer(1000*15,1000) {
            @Override
            public void onTick(long l) {
                if(questionImage.getDrawable() != null){
                    try {
                        if(c!=null){
                            c.cancel();
                        }
                        gameCountDownTimer.start();
                        supportAlertDialog.dismissLoadingDialog();
                    }catch (Exception e){

                    }

                }
            }

            @Override
            public void onFinish() {
                gameCountDownTimer.start();
                supportAlertDialog.dismissLoadingDialog();
            }
        }.start();
    }



}
