package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;

public class QuizCancelDialog {

    Context context;
    CountDownTimer countDownTimer;
    Button v;
    SongActivity songActivity;


    public QuizCancelDialog(Context context, CountDownTimer countDownTimer, Button v, SongActivity songActivity) {
        this.context = context;
        this.countDownTimer = countDownTimer;
        this.v = v;
        this.songActivity = songActivity;
    }

    public void start(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.quiz_asker_layout,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);
        Button yesButton=(Button) viewRemove1.findViewById(R.id.buttonYes);
        Button noButton=(Button) viewRemove1.findViewById(R.id.buttonNo);





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

                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }
                alertDialog.cancel();
                Intent i=new Intent(context, MainActivity.class);
                v.getContext().startActivity(i);
                try{
                    songActivity.songStop();
                }catch (Exception e){

                }
                ((Activity)context).finish();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


    }
}
