package com.nbird.multiplayerquiztrivia.BOT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.R;

public class BotRequestDialog {

    Context context;
    View v;
    int animInt;
    String title,oppoNameString,oppoPicURL;
    int mode;

    public BotRequestDialog(Context context, View v, int animInt, String title, int mode, String oppoNameString,String oppoPicURL) {
        this.context = context;
        this.v = v;
        this.animInt = animInt;
        this.title = title;
        this.mode = mode;
        this.oppoNameString=oppoNameString;
        this.oppoPicURL=oppoPicURL;

    }

    public void start(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.dialog_model_2,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);


        Button yesButton=(Button) viewRemove1.findViewById(R.id.buttonYes);
        Button noButton=(Button) viewRemove1.findViewById(R.id.buttonNo);

        TextView textTitle=(TextView) viewRemove1.findViewById(R.id.textTitle);
        textTitle.setText(title);

        LottieAnimationView anim=(LottieAnimationView)  viewRemove1.findViewById(R.id.imageIcon);
        anim.setAnimation(animInt);
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

                if(mode==1){
                    Toast.makeText(context, "Response send", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(context, VsBOTPictureQuiz.class);
                    i.putExtra("oppoName",oppoNameString);
                    i.putExtra("oppoImageURL",oppoPicURL);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }else if(mode==2){
                    Toast.makeText(context, "Response send", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(context, VsBOTNormalQuiz.class);
                    i.putExtra("oppoName",oppoNameString);
                    i.putExtra("oppoImageURL",oppoPicURL);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }else if(mode==3){

                }else{

                }



            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Response send", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });


    }


}
