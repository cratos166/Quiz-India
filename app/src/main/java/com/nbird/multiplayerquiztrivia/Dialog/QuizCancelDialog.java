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

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.EXTRA.SongActivity;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalPictureQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

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
    NativeAd NATIVE_ADS;

    public QuizCancelDialog(Context context, CountDownTimer countDownTimer, Button v, SongActivity songActivity, ValueEventListener lisnerForConnectionStatus,String oppoUID,ValueEventListener vsRematchListener,ValueEventListener isCompletedListener,NativeAd NATIVE_ADS) {
        this.context = context;
        this.countDownTimer = countDownTimer;
        this.v = v;
        this.songActivity = songActivity;
        this.lisnerForConnectionStatus=lisnerForConnectionStatus;
        this.oppoUID=oppoUID;
        this.vsRematchListener=vsRematchListener;
        this.isCompletedListener=isCompletedListener;
        this.NATIVE_ADS=NATIVE_ADS;
    }

    public QuizCancelDialog(Context context, CountDownTimer countDownTimer, Button v, SongActivity songActivity, ValueEventListener lisnerForConnectionStatus, String oppoUID, ValueEventListener vsRematchListener, ValueEventListener isCompletedListener, ValueEventListener myConnectionLisner,NativeAd NATIVE_ADS) {
        this.context = context;
        this.countDownTimer = countDownTimer;
        this.v = v;
        this.songActivity = songActivity;
        this.lisnerForConnectionStatus=lisnerForConnectionStatus;
        this.oppoUID=oppoUID;
        this.vsRematchListener=vsRematchListener;
        this.isCompletedListener=isCompletedListener;
        this.myConnectionLisner=myConnectionLisner;
        this.NATIVE_ADS=NATIVE_ADS;
    }

    public QuizCancelDialog(Context context, CountDownTimer countDownTimer, Button option1, SongActivity songActivity,NativeAd NATIVE_ADS) {
        this.context = context;
        this.countDownTimer = countDownTimer;
        this.v = option1;
        this.songActivity = songActivity;
        this.NATIVE_ADS=NATIVE_ADS;
    }


    public void startVsMode(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.dialog_model_2,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);
        Button yesButton=(Button) viewRemove1.findViewById(R.id.buttonYes);
        Button noButton=(Button) viewRemove1.findViewById(R.id.buttonNo);

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

                        try {NATIVE_ADS.destroy();}catch (Exception e){}

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
                try {NATIVE_ADS.destroy();}catch (Exception e){}
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

                try {NATIVE_ADS.destroy();}catch (Exception e){}

             Intent intent=new Intent(context,MainActivity.class);
             context.startActivity(intent);
             ((Activity) context).finish();

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {NATIVE_ADS.destroy();}catch (Exception e){}
                alertDialog.dismiss();
            }
        });

    }

}
