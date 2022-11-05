package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalPictureQuiz;
import com.nbird.multiplayerquiztrivia.R;

public class QuizCancelDialog {

    Context context;
    CountDownTimer countDownTimer;
    Button v;
    SongActivity songActivity;
    String oppoUID;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference myRef = database.getReference();
    ValueEventListener lisnerForConnectionStatus,vsRematchListener,isCompletedListener,myConnectionLisner;

    public QuizCancelDialog(Context context, CountDownTimer countDownTimer, Button v, SongActivity songActivity, ValueEventListener lisnerForConnectionStatus,String oppoUID,ValueEventListener vsRematchListener,ValueEventListener isCompletedListener) {
        this.context = context;
        this.countDownTimer = countDownTimer;
        this.v = v;
        this.songActivity = songActivity;
        this.lisnerForConnectionStatus=lisnerForConnectionStatus;
        this.oppoUID=oppoUID;
        this.vsRematchListener=vsRematchListener;
        this.isCompletedListener=isCompletedListener;
    }

    public QuizCancelDialog(Context context, CountDownTimer countDownTimer, Button v, SongActivity songActivity, ValueEventListener lisnerForConnectionStatus, String oppoUID, ValueEventListener vsRematchListener, ValueEventListener isCompletedListener, ValueEventListener myConnectionLisner) {
        this.context = context;
        this.countDownTimer = countDownTimer;
        this.v = v;
        this.songActivity = songActivity;
        this.lisnerForConnectionStatus=lisnerForConnectionStatus;
        this.oppoUID=oppoUID;
        this.vsRematchListener=vsRematchListener;
        this.isCompletedListener=isCompletedListener;
        this.myConnectionLisner=myConnectionLisner;
    }

    public QuizCancelDialog(Context context, CountDownTimer countDownTimer, Button option1, SongActivity songActivity) {
        this.context = context;
        this.countDownTimer = countDownTimer;
        this.v = option1;
        this.songActivity = songActivity;
    }


    public void startVsMode(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.dialog_model_2,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
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



                Dialog loadingDialog = null;
                SupportAlertDialog supportAlertDialog=new SupportAlertDialog(loadingDialog,context);
                supportAlertDialog.showLoadingDialog();


                table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        try{table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);}catch (Exception e){e.printStackTrace();}

                        try{table_user.child("VS_PLAY").child("IsDone").child(oppoUID).removeEventListener(isCompletedListener);}catch (Exception e){}

                        try{table_user.child("VS_REQUEST").child(oppoUID).removeEventListener(vsRematchListener);}catch (Exception e){}


                        try{table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").removeEventListener(myConnectionLisner);}catch (Exception e){}

                        supportAlertDialog.dismissLoadingDialog();
//
                        if(countDownTimer!=null){
                            countDownTimer.cancel();
                        }

                        alertDialog.cancel();

                        Intent intent=new Intent(context,MainActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }
                });


            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


    public void startForSinglePlayer(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.dialog_model_2,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
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

             Intent intent=new Intent(context,MainActivity.class);
             context.startActivity(intent);
             ((Activity) context).finish();

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
