package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.nbird.multiplayerquiztrivia.QUIZ.NormalAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalPictureQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalVideoQuiz;
import com.nbird.multiplayerquiztrivia.R;

public class DialogWaiterVS {
    public void start(Context context, View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_waiter_onevsone,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);




        final AlertDialog alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }


    }



}
