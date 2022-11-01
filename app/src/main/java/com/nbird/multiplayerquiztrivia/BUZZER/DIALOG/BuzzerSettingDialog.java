package com.nbird.multiplayerquiztrivia.BUZZER.DIALOG;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbird.multiplayerquiztrivia.R;

public class BuzzerSettingDialog {

    Context context;

    CardView time3Button,time45Button,time6Button,num10Button,num15Button,num20Button,buzzerNormalModeButton,buzzerPictureModeButton;

    int numTime=0;
    int numQuestion=0;
    int numMode=0;

    String roomCode;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();


    public BuzzerSettingDialog(Context context, String  roomCode) {
        this.context = context;
        this.roomCode = roomCode;
    }

    public void start(View v){
        AlertDialog.Builder builderFact=new AlertDialog.Builder(context,R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.dialog_buzzer_setting,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(false);

        final AlertDialog alertDialog=builderFact.create();

        time3Button=(CardView) viewFact.findViewById(R.id.card4);
        time45Button=(CardView) viewFact.findViewById(R.id.card5);
        time6Button=(CardView) viewFact.findViewById(R.id.card6);
        num10Button=(CardView) viewFact.findViewById(R.id.card10);
        num15Button=(CardView) viewFact.findViewById(R.id.card15);
        num20Button=(CardView) viewFact.findViewById(R.id.card20);
        buzzerNormalModeButton=(CardView) viewFact.findViewById(R.id.card7);
        buzzerPictureModeButton=(CardView) viewFact.findViewById(R.id.card8);
        LinearLayout linearTime3 =(LinearLayout) viewFact.findViewById(R.id.linear4);
        LinearLayout linearTime45 =(LinearLayout) viewFact.findViewById(R.id.linear5);
        LinearLayout linearTime6 =(LinearLayout) viewFact.findViewById(R.id.linear6);
        LinearLayout linearnum10 =(LinearLayout) viewFact.findViewById(R.id.linear1);
        LinearLayout linearnum15 =(LinearLayout) viewFact.findViewById(R.id.linear2);
        LinearLayout linearnum20 =(LinearLayout) viewFact.findViewById(R.id.linear3);
        LinearLayout buzzerlinearmodeNormal =(LinearLayout) viewFact.findViewById(R.id.linear7);
        LinearLayout buzzerlinearmodePicture =(LinearLayout) viewFact.findViewById(R.id.linear8);


        Button done=(Button) viewFact.findViewById(R.id.doneButton);


        settingButtonsManupulation(linearTime3,linearTime6,linearTime45,linearnum10,linearnum15,linearnum20,buzzerlinearmodeNormal,buzzerlinearmodePicture,time3Button,time6Button,time45Button,num10Button,num15Button,num20Button,buzzerNormalModeButton,buzzerPictureModeButton);

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        try{
            alertDialog.show();
        }catch (Exception e){

        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer musicNav;
                musicNav = MediaPlayer.create(context, R.raw.finalbuttonmusic);
                musicNav.start();
                musicNav.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        musicNav.reset();
                        musicNav.release();
                    }
                });
                switch (numQuestion){
                    case 0:
                        dataSettingHostQuestion(1);break;
                    case 1:
                        dataSettingHostQuestion(2);break;
                    case 2:
                        dataSettingHostQuestion(3);break;
                }

                switch (numTime){
                    case 0:
                        dataSettingHostTime(1);break;
                    case 1:
                        dataSettingHostTime(2);break;
                    case 2:
                        dataSettingHostTime(3);break;
                }

                switch (numMode){
                    case 0:
                        dataSettingHostMode(1);break;
                    case 1:
                        dataSettingHostMode(2);break;
                }

                alertDialog.dismiss();
            }
        });
    }



    public void dataSettingHostQuestion(int question){

//        int can;
//        if(privacyFinder==0){
//            can=1;
//        }else{
//            can=0;
//        }

        table_user.child("BUZZER").child("ROOM").child(roomCode).child("numberOfQuestions").setValue(question).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

    public void dataSettingHostMode(int mode){
//        int can;
//        if(privacyFinder==0){
//            can=1;
//        }else{
//            can=0;
//        }

        table_user.child("BUZZER").child("ROOM").child(roomCode).child("gameMode").setValue(mode).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

    public void dataSettingHostTime(int time){
//        int can;
//        if(privacyFinder==0){
//            can=1;
//        }else{
//            can=0;
//        }

        table_user.child("BUZZER").child("ROOM").child(roomCode).child("time").setValue(time).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }





    public void settingButtonsManupulation(final LinearLayout linearTime3, final LinearLayout linearTime6, final LinearLayout linearTime45, final LinearLayout linearnum10, final LinearLayout linearnum15, final LinearLayout linearnum20, final LinearLayout linearmodeNormal, final LinearLayout linearmodePicture, CardView time3Button, CardView time6Button, CardView time45Button, CardView num10Button, CardView num15Button, CardView num20Button, CardView normalModeButton, CardView pictureModeButton){
        if(numQuestion==0){
            linearnum10.setAlpha(0.5f);
            linearnum15.setAlpha(1);
            linearnum20.setAlpha(1);
        }else if(numQuestion==1){
            linearnum10.setAlpha(1);
            linearnum15.setAlpha(0.5f);
            linearnum20.setAlpha(1);
        }else{
            linearnum10.setAlpha(1);
            linearnum15.setAlpha(1);
            linearnum20.setAlpha(0.5f);
        }

        if(numTime==0){
            linearTime3.setAlpha(0.5f);
            linearTime45.setAlpha(1);
            linearTime6.setAlpha(1);
        }else if(numTime==1){
            linearTime3.setAlpha(1);
            linearTime45.setAlpha(0.5f);
            linearTime6.setAlpha(1);
        }else{
            linearTime3.setAlpha(1);
            linearTime45.setAlpha(1);
            linearTime6.setAlpha(0.5f);
        }

        if(numMode==0){
            linearmodeNormal.setAlpha(0.5f);
            linearmodePicture.setAlpha(1);

        }else if(numMode==1){
            linearmodeNormal.setAlpha(1);
            linearmodePicture.setAlpha(0.5f);
        }

        time3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numTime=0;
                linearTime3.setAlpha(0.5f);
                linearTime45.setAlpha(1);
                linearTime6.setAlpha(1);
            }
        });

        time45Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numTime=1;
                linearTime3.setAlpha(1);
                linearTime45.setAlpha(0.5f);
                linearTime6.setAlpha(1);
            }
        });

        time6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numTime=2;
                linearTime3.setAlpha(1);
                linearTime45.setAlpha(1);
                linearTime6.setAlpha(0.5f);
            }
        });

        num10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numQuestion=0;
                linearnum10.setAlpha(0.5f);
                linearnum15.setAlpha(1);
                linearnum20.setAlpha(1);
            }
        });

        num15Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numQuestion=1;
                linearnum10.setAlpha(1);
                linearnum15.setAlpha(0.5f);
                linearnum20.setAlpha(1);
            }
        });

        num20Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numQuestion=2;
                linearnum10.setAlpha(1);
                linearnum15.setAlpha(1);
                linearnum20.setAlpha(0.5f);
            }
        });

        normalModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numMode=0;
                linearmodeNormal.setAlpha(0.5f);
                linearmodePicture.setAlpha(1);
            }
        });

        pictureModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numMode=1;
                linearmodeNormal.setAlpha(1);
                linearmodePicture.setAlpha(0.5f);
            }
        });






    }

}
