package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaitingVSInGameDialog {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference myRef = database.getReference();

    String myPicStr;
    String username;
    String scoreStr;
    String timeTakenStr;
    String accuracyStr;
    String totalLifeLineUsed;
    Context context;
    TextView v;

    AlertDialog alertDialog;

    ViewPager slideViewPager;
    LinearLayout dotLayout;
    List<mainMenuFactsHolder> list;
    int num=0;
    TextView[] mDots;
    ActionBarDrawerToggle mToggle;

    ShimmerFrameLayout shimerFact;
    slideAdapterMainMenuHorizontalSlide sliderAdapter;

    private int currentPage;

    public WaitingVSInGameDialog(String myPicStr,String username, String score, String timeTaken, String accuracy, String totalLifeLineUsed, Context context, TextView v) {
        this.myPicStr = myPicStr;
        this.username = username;
        this.scoreStr = score;
        this.timeTakenStr = timeTaken;
        this.accuracyStr = accuracy;
        this.totalLifeLineUsed = totalLifeLineUsed;
        this.context=context;
        this.v=v;
    }

    public void start(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(context).inflate(R.layout.waiting_vs_dialog,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);

        ImageView imageView=(ImageView) viewRemove1.findViewById(R.id.myPic);
        TextView userName=(TextView) viewRemove1.findViewById(R.id.userName);
        TextView score=(TextView) viewRemove1.findViewById(R.id.score);
        TextView timeTaken=(TextView) viewRemove1.findViewById(R.id.timeTaken);
        TextView accuracy=(TextView) viewRemove1.findViewById(R.id.accuracy);
        TextView lifeLines=(TextView) viewRemove1.findViewById(R.id.lifeLines);


        shimerFact=viewRemove1.findViewById(R.id.shimmer_view_container);

        shimerFact.startShimmerAnimation();


        slideViewPager=(ViewPager) viewRemove1.findViewById(R.id.slideViewPager);
        dotLayout=(LinearLayout) viewRemove1.findViewById(R.id.dotLayout);


        Glide.with(context).load(myPicStr).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(imageView);

        userName.setText(username);
        score.setText("Correct/Wrong : "+scoreStr);
        timeTaken.setText("Time Taken : "+timeTakenStr);
        accuracy.setText("Accuracy : "+accuracyStr+"%");
        lifeLines.setText("Total Life-Lines : "+totalLifeLineUsed+"/4");


        for(int i=1;i<=3;i++){
            dataForHorizontalSlide(context);
        }



        alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

    }

    public void dismiss(){
        try{
            alertDialog.cancel();
        }catch (Exception e){
           e.printStackTrace();
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

        list=new ArrayList<>();

        myRef.child("Facts").child(String.valueOf(categoryRandomNumber)).child(String.valueOf(setRandomNumber)).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange( DataSnapshot snapshot) {

                list.add(snapshot.getValue(mainMenuFactsHolder.class));
                num++;


                if(num==3){
                    shimerFact.stopShimmerAnimation();
                    shimerFact.setVisibility(View.GONE);
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
        sliderAdapter=new slideAdapterMainMenuHorizontalSlide(context,list);
        slideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(viewListner);
        sliderAdapter.notifyDataSetChanged();
    }



    public void addDotsIndicator(int position){
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
            addDotsIndicator(position);
            currentPage=position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



}
