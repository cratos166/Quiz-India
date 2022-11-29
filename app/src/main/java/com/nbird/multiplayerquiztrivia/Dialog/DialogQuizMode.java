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

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.common.SignInButton;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalPictureQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalSingleQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalVideoQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

public class DialogQuizMode {
    NativeAd NATIVE_ADS;
    public void start(Context context,View view,int gate){
        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_select_mode,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);

        CardView pictureMode=(CardView) view1.findViewById(R.id.pictureMode);
        CardView normalMode=(CardView) view1.findViewById(R.id.normalMode);
        CardView audioMode=(CardView) view1.findViewById(R.id.audioMode);
        CardView videoMode=(CardView) view1.findViewById(R.id.videoMode);

        ImageView cancel=(ImageView) view1.findViewById(R.id.cancel);



        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)){

            MobileAds.initialize(context);
            AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);

                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = view1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                            NATIVE_ADS=nativeAd;
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }




        final AlertDialog alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{NATIVE_ADS.destroy();}catch (Exception e){}
                alertDialog.dismiss();
            }
        });


        pictureMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gate==1){
                    try{NATIVE_ADS.destroy();}catch (Exception e){}
                    Intent intent = new Intent(context, NormalPictureQuiz.class);
                    alertDialog.dismiss();
                    view.getContext().startActivity(intent);
                    ((Activity)view.getContext()).finish();
                }else{
                    try{NATIVE_ADS.destroy();}catch (Exception e){}
                    DialogOnlineLocal dialogOnlineLocal=new DialogOnlineLocal();
                    dialogOnlineLocal.start(context,view,1,NATIVE_ADS);
                    alertDialog.dismiss();
                }

            }
        });

        normalMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gate==1){
                    try{NATIVE_ADS.destroy();}catch (Exception e){}
                    DialogCategory dialogCategory=new DialogCategory(context,view);
                    dialogCategory.start();
                    alertDialog.dismiss();
                }else{
                    DialogOnlineLocal dialogOnlineLocal=new DialogOnlineLocal();
                    dialogOnlineLocal.start(context,view,2,NATIVE_ADS);
                    alertDialog.dismiss();
                }

            }
        });

        audioMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gate==1){
                    try{NATIVE_ADS.destroy();}catch (Exception e){}
                    Intent intent = new Intent(context, NormalAudioQuiz.class);
                    alertDialog.dismiss();
                    view.getContext().startActivity(intent);
                    ((Activity)view.getContext()).finish();
                }else {
                    DialogOnlineLocal dialogOnlineLocal=new DialogOnlineLocal();
                    dialogOnlineLocal.start(context,view,3,NATIVE_ADS);
                    alertDialog.dismiss();
                }

            }
        });

        videoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gate==1){
                    try{NATIVE_ADS.destroy();}catch (Exception e){}
                    Intent intent = new Intent(context, NormalVideoQuiz.class);
                    alertDialog.dismiss();
                    view.getContext().startActivity(intent);
                    ((Activity)view.getContext()).finish();
                }else {
                    DialogOnlineLocal dialogOnlineLocal=new DialogOnlineLocal();
                    dialogOnlineLocal.start(context,view,4,NATIVE_ADS);
                    alertDialog.dismiss();
                }

            }
        });




    }







}
