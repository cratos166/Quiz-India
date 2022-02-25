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

    public PicLoader(QuizTimer gameCountDownTimer, ImageView questionImage, SupportAlertDialog loadingDialog) {
        this.gameCountDownTimer = gameCountDownTimer;
        this.questionImage = questionImage;
        this.supportAlertDialog = loadingDialog;
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
