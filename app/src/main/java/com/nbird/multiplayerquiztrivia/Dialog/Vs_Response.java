package com.nbird.multiplayerquiztrivia.Dialog;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.QUIZ.VsAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsNormalQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsPictureQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsVideoQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.util.ArrayList;
import java.util.EventListener;

public class Vs_Response {

    Context context;
    CountDownTimer countDownTimer;
    Button v;
    SongActivity songActivity;
    int animInt,mode;
    String title;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference myRef = database.getReference();

    String oppoUID, oppoNameString, oppoImgStr;

    ValueEventListener questionGetterListner,lisnerForConnectionStatus,vsRematchListener,isCompletedListener;

    NativeAd NATIVE_ADS;

    public Vs_Response(Context context, CountDownTimer countDownTimer, Button v, SongActivity songActivity, int animInt, int mode, String title, String oppoUID, String oppoNameString, String oppoImgStr,ValueEventListener lisnerForConnectionStatus,ValueEventListener vsRematchListener,ValueEventListener isCompletedListener) {
        this.context = context;
        this.countDownTimer = countDownTimer;
        this.v = v;
        this.songActivity = songActivity;
        this.animInt = animInt;
        this.mode = mode;
        this.title = title;
        this.oppoUID = oppoUID;
        this.oppoNameString = oppoNameString;
        this.oppoImgStr = oppoImgStr;
        this.lisnerForConnectionStatus=lisnerForConnectionStatus;
        this.vsRematchListener=vsRematchListener;
        this.isCompletedListener=isCompletedListener;
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

        yesButton.setText("Accept");
        noButton.setText("Reject");


        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)){

            MobileAds.initialize(context);
            AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);
                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = viewRemove1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                            NATIVE_ADS=nativeAd;
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }




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



                noButton.setEnabled(false);
                yesButton.setEnabled(false);

                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }
                alertDialog.cancel();
                try{
                    songActivity.songStop();
                }catch (Exception e){

                }

                table_user.child("VS_PLAY").child("IsDone").child(mAuth.getCurrentUser().getUid()).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        table_user.child("VS_RESPONSE").child(mAuth.getCurrentUser().getUid()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                noButton.setEnabled(true);
                                yesButton.setEnabled(true);

                                Toast.makeText(context,"Response Send",Toast.LENGTH_SHORT).show();

                                questionGetterListner=new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try{
                                            ArrayList<Integer> arrayListAnswers = (ArrayList<Integer>) snapshot.getValue();
                                            table_user.child("VS_PLAY").child(oppoUID).child("Answers").removeEventListener(questionGetterListner);
                                            table_user.child("VS_PLAY").child(oppoUID).child("Answers").removeValue();

                                            try{
                                                table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            table_user.child("VS_PLAY").child("PlayerCurrentAns").child(mAuth.getCurrentUser().getUid()).removeValue();

                                            try{table_user.child("VS_REQUEST").child(oppoUID).removeEventListener(vsRematchListener);}catch (Exception e){}

                                            try{table_user.child("VS_PLAY").child("IsDone").child(oppoUID).removeEventListener(isCompletedListener);}catch (Exception e){}

                                     //       table_user.child("VS_PLAY").child("DataExchange").child(mAuth.getCurrentUser().getUid()).removeValue();

                                            if(countDownTimer!=null){ countDownTimer.cancel();}

                                            try {NATIVE_ADS.destroy();}catch (Exception e){}

                                            switch (mode) {
                                                case 2:
                                                    Intent intent = new Intent(context, VsNormalQuiz.class);
                                                    intent.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) arrayListAnswers);
                                                    intent.putExtra("playerNum",2);
                                                    intent.putExtra("oppoImgStr",oppoImgStr);
                                                    intent.putExtra("oppoName",oppoNameString);
                                                    intent.putExtra("oppoUID",oppoUID);
                                                    intent.putExtra("mode",mode);
                                                    context.startActivity(intent);
                                                    ((Activity) context).finish();
                                                    break;
                                                case 1:
                                                    Intent intent1 = new Intent(context, VsPictureQuiz.class);
                                                    intent1.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) arrayListAnswers);
                                                    intent1.putExtra("playerNum",2);
                                                    intent1.putExtra("oppoImgStr",oppoImgStr);
                                                    intent1.putExtra("oppoName",oppoNameString);
                                                    intent1.putExtra("oppoUID",oppoUID);
                                                    intent1.putExtra("mode",mode);
                                                    context.startActivity(intent1);
                                                    ((Activity) context).finish();
                                                    break;
                                                case 3:
                                                    Intent intent2 = new Intent(context, VsAudioQuiz.class);
                                                    intent2.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) arrayListAnswers);
                                                    intent2.putExtra("playerNum",2);
                                                    intent2.putExtra("oppoImgStr",oppoImgStr);
                                                    intent2.putExtra("oppoName",oppoNameString);
                                                    intent2.putExtra("oppoUID",oppoUID);
                                                    intent2.putExtra("mode",mode);
                                                    context.startActivity(intent2);
                                                    ((Activity) context).finish();
                                                    break;
                                                case 4:
                                                    Intent intent3 = new Intent(context, VsVideoQuiz.class);
                                                    intent3.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) arrayListAnswers);
                                                    intent3.putExtra("playerNum",2);
                                                    intent3.putExtra("oppoImgStr",oppoImgStr);
                                                    intent3.putExtra("oppoName",oppoNameString);
                                                    intent3.putExtra("oppoUID",oppoUID);
                                                    intent3.putExtra("mode",mode);
                                                    context.startActivity(intent3);
                                                    ((Activity) context).finish();
                                                    break;
                                            }


                                        }catch (Exception e){

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };

                                table_user.child("VS_PLAY").child(oppoUID).child("Answers").addValueEventListener(questionGetterListner);




                            }
                        });
                    }
                });



            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                noButton.setEnabled(false);
                yesButton.setEnabled(false);

                table_user.child("VS_RESPONSE").child(mAuth.getCurrentUser().getUid()).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Response Send",Toast.LENGTH_SHORT).show();
                        try {NATIVE_ADS.destroy();}catch (Exception e){}
                        noButton.setEnabled(true);
                        yesButton.setEnabled(true);
                        try{
                            alertDialog.cancel();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

            }
        });


    }

}
