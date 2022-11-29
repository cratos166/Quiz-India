package com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.StartingPackage.SliderAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.TroubleShootAdapter;

import java.util.ArrayList;

public class TroubleShootDialog {
    private ViewPager slideViewPager;
    private LinearLayout dotLayout;
    private TroubleShootAdapter troubleShootAdapter;
    private TextView[] mDots;
    private CardView nextbutton, backbutton;
    private int currentPage;
    ImageView next_done_button;

    Context context;
    NativeAd NATIVE_ADS;

    public TroubleShootDialog(Context context,NativeAd NATIVE_ADS) {
        this.context = context;
        this.NATIVE_ADS=NATIVE_ADS;
    }

    public void start(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_trouble_shoot, (ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);


        slideViewPager = (ViewPager) view1.findViewById(R.id.slideViewPager);
        dotLayout = (LinearLayout) view1.findViewById(R.id.dotLayout);
        nextbutton = (CardView) view1.findViewById(R.id.nextButton);
        backbutton = (CardView) view1.findViewById(R.id.previousButton);

        next_done_button = (ImageView) view1.findViewById(R.id.next_done_button);


        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }


        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)) {

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






        troubleShootAdapter = new TroubleShootAdapter(context);
        slideViewPager.setAdapter(troubleShootAdapter);


        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(viewListner);

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentPage == 2) {
                    try{
                        alertDialog.dismiss();
                    }catch (Exception e){

                    }

                    try{NATIVE_ADS.destroy();}catch (Exception e){}

                }
                slideViewPager.setCurrentItem(currentPage + 1);

            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideViewPager.setCurrentItem(currentPage - 1);
            }
        });




    }




    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        dotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(context);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(60);
            mDots[i].setTextColor(Color.parseColor("#98A8D0"));
            dotLayout.addView(mDots[i]);

        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(Color.parseColor("#6A7592"));
        }
    }


    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;
            if (currentPage == 0) {

                nextbutton.setEnabled(true);
                backbutton.setEnabled(false);
                backbutton.setVisibility(View.INVISIBLE);
                nextbutton.setScaleX(1);
                nextbutton.setScaleY(1);
                next_done_button.setBackgroundResource(R.drawable.arrow_forward);
            } else if (position == mDots.length - 1) {
                nextbutton.setEnabled(true);
                backbutton.setEnabled(true);
                backbutton.setVisibility(View.VISIBLE);
                next_done_button.setBackgroundResource(R.drawable.done_button);
            } else {
                nextbutton.setEnabled(true);
                backbutton.setEnabled(true);
                backbutton.setVisibility(View.VISIBLE);
                nextbutton.setScaleX(1);
                next_done_button.setBackgroundResource(R.drawable.arrow_forward);
                nextbutton.setScaleY(1);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
