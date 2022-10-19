package com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;

public class BasicDialog {

    Context context;
    View v;
    String title,dis,buttonString;
    int animInt;


    public BasicDialog(Context context, View v, String title, String dis, String buttonString, int animInt) {
        this.context = context;
        this.v = v;
        this.title = title;
        this.dis = dis;
        this.buttonString = buttonString;
        this.animInt = animInt;
    }

    public void start(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.dialog_model_1,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);
        Button button=(Button) viewRemove1.findViewById(R.id.button);

        TextView textTitle=(TextView) viewRemove1.findViewById(R.id.textTitle);
        textTitle.setText(title);


        TextView textDis=(TextView) viewRemove1.findViewById(R.id.textDis);
        textDis.setText(dis);

        LottieAnimationView anim=(LottieAnimationView)  viewRemove1.findViewById(R.id.anim);
        anim.setAnimation(animInt);
        anim.playAnimation();
        anim.loop(true);



        button.setText(buttonString);




        final AlertDialog alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               try{
                   alertDialog.dismiss();

               }catch (Exception e){

               }

            }
        });
    }

}
