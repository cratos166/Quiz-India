package com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
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
import com.nbird.multiplayerquiztrivia.FACTS.mainMenuFactsHolder;
import com.nbird.multiplayerquiztrivia.FACTS.slideAdapterMainMenuHorizontalSlide;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FactsDialog {

    public ViewPager slideViewPager;
    private LinearLayout dotLayout;
    private TextView[] mDots;
    private List<mainMenuFactsHolder> factList;
    private ShimmerFrameLayout shimmerFact;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("NEW_APP");
    DatabaseReference myRef = database.getReference();

    int num=0;
    private int currentPage;

    slideAdapterMainMenuHorizontalSlide sliderAdapter;
    ActionBarDrawerToggle mToggle;
    Context context;
    NativeAd NATIVE_ADS;

    public FactsDialog(Context context,NativeAd NATIVE_ADS) {
        this.context = context;
        this.NATIVE_ADS=NATIVE_ADS;
    }

    public void start(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_facts, (ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);

        factList=new ArrayList<>();

        slideViewPager=(ViewPager) view1.findViewById(R.id.slideViewPager);
        dotLayout=(LinearLayout) view1.findViewById(R.id.dotLayout);
        shimmerFact=(ShimmerFrameLayout) view1.findViewById(R.id.shimmerFact);


        shimmerFact.startShimmerAnimation();

        ImageView cancel=(ImageView) view1.findViewById(R.id.cancel);

        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }


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


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{NATIVE_ADS.destroy();}catch (Exception e){}

                try {
                    alertDialog.dismiss();
                } catch (Exception e) {

                }
            }
        });

        for(int i=0;i<3;i++){
            dataForHorizontalSlide(context);
        }

    }



    public void dataForHorizontalSlide(Context context){

        // create instance of Random class
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        int setRandomNumber;
        final int categoryRandomNumber = rand.nextInt(7)+1;
        if(categoryRandomNumber<=5||categoryRandomNumber==7){
            setRandomNumber = rand.nextInt(49)+1;
        }else{
            setRandomNumber = rand.nextInt(199)+1;
        }

        factList=new ArrayList<>();

        myRef.child("Facts").child(String.valueOf(categoryRandomNumber)).child(String.valueOf(setRandomNumber)).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange( DataSnapshot snapshot) {

                factList.add(snapshot.getValue(mainMenuFactsHolder.class));
                num++;


                if(num==3){
                    shimmerFact.stopShimmerAnimation();
                    shimmerFact.setVisibility(View.GONE);
                    AdapterManupulation(context);
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context,"Facts Data Can't be Loaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void AdapterManupulation(Context context){
        sliderAdapter=new slideAdapterMainMenuHorizontalSlide(context,factList);
        slideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0,context);
        slideViewPager.addOnPageChangeListener(viewListner);
        sliderAdapter.notifyDataSetChanged();
    }



    public void addDotsIndicator(int position,Context context){
        mDots=new TextView[3];
        dotLayout.removeAllViews();
        for(int i=0;i<mDots.length;i++){
            mDots[i]=new TextView(context);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(Color.parseColor("#555B68"));
            dotLayout.addView(mDots[i]);

        }
        if(mDots.length>0){
            mDots[position].setTextColor(Color.parseColor("#BFD1FF"));
        }
    }
    ViewPager.OnPageChangeListener viewListner=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position,context);
            currentPage=position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



}
