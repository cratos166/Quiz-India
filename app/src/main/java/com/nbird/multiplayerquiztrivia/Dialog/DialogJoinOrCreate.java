package com.nbird.multiplayerquiztrivia.Dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.BUZZER.ACTIVTY.LobbyBuzzerActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

public class DialogJoinOrCreate {
    NativeAd NATIVE_ADS;
    public void start(Context context, View view, int quizMode){



        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_model_2,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);


        LottieAnimationView imageIcon=(LottieAnimationView) view1.findViewById(R.id.imageIcon);
        TextView textTitle=(TextView) view1.findViewById(R.id.textTitle);
        Button createButton=(Button) view1 .findViewById(R.id.buttonYes);
        Button joinButton=(Button) view1.findViewById(R.id.buttonNo);


        imageIcon.setAnimation(R.raw.select_option);
        imageIcon.playAnimation();

        textTitle.setText("CREATE your own room, share the code and play with the team! Enjoy with friends and ace the quiz! \nAlready have a code? Click JOIN now!");

        joinButton.setText("JOIN");
        createButton.setText("CREATE");

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

       joinButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try{NATIVE_ADS.destroy();}catch (Exception e){}
               DialogJoinVS dialogJoinVS=new DialogJoinVS();
               dialogJoinVS.start(context,view,quizMode);
               alertDialog.dismiss();
           }
       });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{NATIVE_ADS.destroy();}catch (Exception e){}
                DialogWaiterVS dialogWaiterVS=new DialogWaiterVS();
                dialogWaiterVS.start(context,view,quizMode);
                alertDialog.dismiss();
            }
        });





    }

}
