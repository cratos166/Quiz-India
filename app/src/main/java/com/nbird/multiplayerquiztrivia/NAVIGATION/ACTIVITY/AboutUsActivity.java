package com.nbird.multiplayerquiztrivia.NAVIGATION.ACTIVITY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.BUZZER.ACTIVTY.BuzzerPictureActivity;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.util.Random;

public class AboutUsActivity extends AppCompatActivity {

    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        AppData appData=new AppData();
        boolean ads=appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, AboutUsActivity.this);
        if(ads){
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        TextView emailKartik=(TextView) findViewById(R.id.emailKartik);
        TextView instaKartik=(TextView) findViewById(R.id.instaKartik);

        TextView emailAakash=(TextView) findViewById(R.id.emailAakash);


        TextView emailZimmy=(TextView) findViewById(R.id.emailZimmy);
        TextView instaZimmy=(TextView) findViewById(R.id.instaZimmy);





        emailKartik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"sachinkartik166@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
            }
        });

        instaKartik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/kartik_sachu/"));
                startActivity(intent);
                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
            }
        });


        emailAakash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"aakashtyagi232@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
            }
        });


        emailZimmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"Zimmy.9724@yahoo.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
            }
        });

        instaZimmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/zimmy9537/"));
                startActivity(intent);
                overridePendingTransition(R.anim.fadeinmain, R.anim.fadeoutmain);
            }
        });


    }

    public void onBackPressed() {
        Intent intent=new Intent(AboutUsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        try{mAdView.destroy();}catch (Exception e){}
        Runtime.getRuntime().gc();

    }

}