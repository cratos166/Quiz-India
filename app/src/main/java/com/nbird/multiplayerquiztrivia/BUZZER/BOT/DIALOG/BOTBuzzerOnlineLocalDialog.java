package com.nbird.multiplayerquiztrivia.BUZZER.BOT.DIALOG;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.BOTDialogJoinOrCreate;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.BOTJoinCreateTournamentDialog;

public class BOTBuzzerOnlineLocalDialog {
    NativeAd NATIVE_ADS;
    public void start(Context context, View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_online_local,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);

        CardView onlineCard=(CardView) view1.findViewById(R.id.onlineCard);
        CardView localCard=(CardView) view1.findViewById(R.id.localCard);

        ImageView cancel=(ImageView) view1.findViewById(R.id.cancel);

        TextView onlineMainText=(TextView) view1.findViewById(R.id.onlineMainText);
        onlineMainText.setText("1vs10 With Random Players");

        TextView localMainText=(TextView) view1.findViewById(R.id.localMainText);
        localMainText.setText("Play With Upto 10 Friends Of Yours");


        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)){
            MobileAds.initialize(context);
            AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                        NativeAd N_D=NATIVE_ADS;
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);

                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = view1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                            N_D=nativeAd;
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


        onlineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{NATIVE_ADS.destroy();}catch (Exception e){}
                BOTBuzzerJoinCreateTournamentDialog botBuzzerJoinCreateTournamentDialog=new BOTBuzzerJoinCreateTournamentDialog();
                botBuzzerJoinCreateTournamentDialog.start(context,onlineCard);
                alertDialog.dismiss();
            }
        });

        localCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{NATIVE_ADS.destroy();}catch (Exception e){}

                BOTBuzzerDialogLocal botDialogJoinOrCreate=new BOTBuzzerDialogLocal();
                botDialogJoinOrCreate.start(context,localCard);
                alertDialog.dismiss();
//                BOTDialogJoinOrCreate botDialogJoinOrCreate=new BOTDialogJoinOrCreate();
//                botDialogJoinOrCreate.start(context,localCard);
//                DialogJoinOrCreate dialogJoinOrCreate=new DialogJoinOrCreate();
//                dialogJoinOrCreate.start(context,view,quizMode);
//                alertDialog.dismiss();
            }
        });




    }

}
