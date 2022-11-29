package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.FIREBASE.AnswerUploaderAndReceiver;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.QUIZ.VsNormalQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.TournamentVideoActivity;

public class DialogModel_1 {

    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("NEW_APP");

    Context context;
    String title;
    String dis,oppoUID;
    int animInt;
    String buttonString;
    TextView v;

    ValueEventListener lisnerForConnectionStatus,isCompletedListener,vsRematchListener;
    AnswerUploaderAndReceiver answerUploaderAndReceiver;


    NativeAd NATIVE_ADS;

    public DialogModel_1(Context context, String title, String dis, int anim, String buttonString, TextView v, ValueEventListener isCompletedListener, ValueEventListener vsRematchListener, ValueEventListener lisnerForConnectionStatus, AnswerUploaderAndReceiver answerUploaderAndReceiver, String oppoUID,NativeAd NATIVE_ADS) {
        this.context = context;
        this.title = title;
        this.dis = dis;
        this.animInt = anim;
        this.buttonString = buttonString;
        this.v = v;
        this.isCompletedListener=isCompletedListener;
        this.vsRematchListener=vsRematchListener;
        this.lisnerForConnectionStatus=lisnerForConnectionStatus;
        this.answerUploaderAndReceiver=answerUploaderAndReceiver;
        this.oppoUID=oppoUID;
        this.NATIVE_ADS=NATIVE_ADS;
    }


    public void removeLisnerForConnectionStatus(String oppoUID){

        try{
            table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);
        }catch (Exception e){
            e.printStackTrace();
        }

        table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").removeValue();
        table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeValue();
    }

    public void displayDialog(){
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


        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)){
            MobileAds.initialize(context);
            AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        NativeAd N_A=NATIVE_ADS;
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);

                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = viewRemove1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                            N_A=nativeAd;
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



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{table_user.child("VS_CONNECTION").child(oppoUID).child("myStatus").removeEventListener(lisnerForConnectionStatus);}catch (Exception e){e.printStackTrace();}

                try{table_user.child("VS_PLAY").child("IsDone").child(oppoUID).removeEventListener(isCompletedListener);}catch (Exception e){}

                try{table_user.child("VS_REQUEST").child(oppoUID).removeEventListener(vsRematchListener);}catch (Exception e){}

                try{ answerUploaderAndReceiver.removeAnimListener(oppoUID);}catch (Exception e){}


                try{NATIVE_ADS.destroy();}catch (Exception e){}

                alertDialog.dismiss();
                Intent intent=new Intent(context,MainActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

}
