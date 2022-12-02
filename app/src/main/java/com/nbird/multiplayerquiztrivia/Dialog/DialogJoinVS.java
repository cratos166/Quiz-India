package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.EXTRA.OneVSOneOpponentDataSetter;
import com.nbird.multiplayerquiztrivia.FACTS.mainMenuFactsHolder;
import com.nbird.multiplayerquiztrivia.FACTS.slideAdapterMainMenuHorizontalSlide;
import com.nbird.multiplayerquiztrivia.FIREBASE.ConnectionStatus;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.GENERATORS.BatchGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.LevelGenerators;
import com.nbird.multiplayerquiztrivia.Model.FirstTime;
import com.nbird.multiplayerquiztrivia.Model.OnlineDetailHolder;
import com.nbird.multiplayerquiztrivia.Model.PlayHolder;
import com.nbird.multiplayerquiztrivia.QUIZ.VsAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsNormalQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsPictureQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsVideoQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DialogJoinVS {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    ImageView oppoImage;
    TextView oppoName;
    TextView highestScore;
    TextView totalTime;
    TextView oppoRatio;
    TextView oppoAccu;
    LinearLayout mainlinearLayout;
    CardView oppoBatchCardView;
    ImageView oppoBatch;
    TextView oppoLevelText;
    Button cancelButton;
    ShimmerFrameLayout shimmerOppo,shimerFact;
    slideAdapterMainMenuHorizontalSlide sliderAdapter;
    ViewPager slideViewPager;
    LinearLayout dotLayout;
    List<mainMenuFactsHolder> list;
    int num=0;
    TextView[] mDots;
    DatabaseReference myRef = database.getReference();
    CountDownTimer countDownTimerIntent;
    Context context;
    private int currentPage;
    int incrementer=0;
    ValueEventListener questionGetterListner;
    String oppoImgStr;
    NativeAd NATIVE_ADS;

    public void start(Context context, View view, int quizMode){
        this.context=context;

        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_join_layout,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);


        LottieAnimationView imageIcon=(LottieAnimationView) view1.findViewById(R.id.imageIcon);
        TextView textTitle=(TextView) view1.findViewById(R.id.textTitle);
        Button joinButton=(Button) view1 .findViewById(R.id.joinButton1);
        ImageView cancel=(ImageView) view1.findViewById(R.id.cancel);


        TextInputEditText passWordET=(TextInputEditText) view1.findViewById(R.id.username);



        imageIcon.setAnimation(R.raw.join_anim);
        imageIcon.playAnimation();

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
                try{alertDialog.dismiss();}catch (Exception e){}
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passWordET.getText().toString().equals("")){
                    passWordET.setError("Fields Cannot Be Empty");
                }else if(passWordET.getText().toString().length()>=10){
                    passWordET.setError("Field Length Should Be Less Than 10 Characters");
                }else{

                    joiner(context,passWordET.getText().toString(),passWordET,view);
                }
            }
        });


    }


    public void joiner(Context context, String password, TextInputEditText passWordET, View view){
        Dialog dialog = null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,context);
        supportAlertDialog.showLoadingDialog();

        table_user.child("VS_ARENA").orderByChild("roomCode").equalTo(Integer.parseInt(password)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                OnlineDetailHolder onlineDetailHolder = null;
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    onlineDetailHolder=dataSnapshot1.getValue(OnlineDetailHolder.class);
                }

                try{

                    String oppoUID=onlineDetailHolder.getUID();

                    table_user.child("VS_ARENA").child(oppoUID).removeValue();

                    PlayHolder playHolder=new PlayHolder(oppoUID,mAuth.getCurrentUser().getUid());
                    OnlineDetailHolder finalOnlineDetailHolder = onlineDetailHolder;
                    OnlineDetailHolder finalOnlineDetailHolder1 = onlineDetailHolder;
                    table_user.child("VS_PLAY").child(oppoUID).child("Personal").setValue(playHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            waiterAlertDialog(context,view, finalOnlineDetailHolder);


                            questionGetterListner=new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {



                                        try {

                                            ArrayList<Integer> arrayListAnswers = (ArrayList<Integer>) snapshot.getValue();
                                            table_user.child("VS_PLAY").child(oppoUID).child("Answers").removeEventListener(questionGetterListner);
                                            cancelButton.setTextSize(10f);





                                            // CONNECTION SETTER FOR ONLINE
                                            ConnectionStatus connectionStatus=new ConnectionStatus();
                                            connectionStatus.myStatusSetter();

                                            table_user.child("VS_PLAY").child("IsDone").child(mAuth.getCurrentUser().getUid()).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });


                                            countDownTimerIntent = new CountDownTimer(1000 * 10, 1000) {
                                                @Override
                                                public void onTick(long l) {
                                                    cancelButton.setText("Game Starts In " + l / 1000);
                                                }

                                                @Override
                                                public void onFinish() {

                                                    try{NATIVE_ADS.destroy();}catch (Exception e){}



                                                    switch (finalOnlineDetailHolder1.getMode()) {
                                                        case 2:
                                                            Intent intent = new Intent(context, VsNormalQuiz.class);
                                                            intent.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) arrayListAnswers);
                                                            intent.putExtra("playerNum",2);
                                                            intent.putExtra("oppoUID",oppoUID);
                                                            intent.putExtra("oppoName", oppoName.getText().toString());
                                                            intent.putExtra("oppoImgStr",oppoImgStr);
                                                            intent.putExtra("mode",2);
                                                            context.startActivity(intent);
                                                            ((Activity) context).finish();
                                                            break;
                                                        case 1:
                                                            Intent intent1 = new Intent(context, VsPictureQuiz.class);
                                                            intent1.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) arrayListAnswers);
                                                            intent1.putExtra("playerNum",2);
                                                            intent1.putExtra("oppoUID",oppoUID);
                                                            intent1.putExtra("oppoName", oppoName.getText().toString());
                                                            intent1.putExtra("oppoImgStr",oppoImgStr);
                                                            intent1.putExtra("mode",1);
                                                            context.startActivity(intent1);
                                                            ((Activity) context).finish();
                                                            break;
                                                        case 3:
                                                            Intent intent2 = new Intent(context, VsAudioQuiz.class);
                                                            intent2.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) arrayListAnswers);
                                                            intent2.putExtra("playerNum",2);
                                                            intent2.putExtra("oppoUID",oppoUID);
                                                            intent2.putExtra("oppoName", oppoName.getText().toString());
                                                            intent2.putExtra("oppoImgStr",oppoImgStr);
                                                            intent2.putExtra("mode",3);
                                                            context.startActivity(intent2);
                                                            ((Activity) context).finish();
                                                            break;
                                                        case 4:
                                                            Intent intent3 = new Intent(context, VsVideoQuiz.class);
                                                            intent3.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) arrayListAnswers);
                                                            intent3.putExtra("playerNum",2);
                                                            intent3.putExtra("oppoUID",oppoUID);
                                                            intent3.putExtra("oppoName", oppoName.getText().toString());
                                                            intent3.putExtra("oppoImgStr",oppoImgStr);
                                                            intent3.putExtra("mode",4);
                                                            context.startActivity(intent3);
                                                            ((Activity) context).finish();
                                                            break;
                                                    }
                                                }
                                            }.start();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            };table_user.child("VS_PLAY").child(oppoUID).child("Answers").addValueEventListener(questionGetterListner);


                           // intent.putIntegerArrayListExtra("arrList12345", (ArrayList<Integer>) arrlist);


                        }
                    });





                }catch (Exception e){
                    passWordET.setError("No Room Present With This Password");
                    supportAlertDialog.dismissLoadingDialog();
                }
                

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void waiterAlertDialog(Context context, View view, OnlineDetailHolder onlineDetailHolder){

        AppData appData = new AppData();

        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_waiter_onevsone,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);

        shimmerOppo=view1.findViewById(R.id.shimmerOppo);
        shimerFact=view1.findViewById(R.id.shimmerFact);

        shimerFact.startShimmerAnimation();
        shimmerOppo.startShimmerAnimation();

        slideViewPager=(ViewPager) view1.findViewById(R.id.slideViewPager);
        dotLayout=(LinearLayout) view1.findViewById(R.id.dotLayout);

        TextView myName=(TextView) view1.findViewById(R.id.myName);
        ImageView myImage=(ImageView) view1.findViewById(R.id.myImage);
        ImageView myBatch=(ImageView) view1.findViewById(R.id.myBatch);
        TextView myLevelText=(TextView) view1.findViewById(R.id.myLevelText);

        oppoImage=(ImageView) view1.findViewById(R.id.oppoImage);
        oppoName=(TextView) view1.findViewById(R.id.oppoName);

        highestScore=(TextView) view1.findViewById(R.id.highestScore);
        totalTime=(TextView) view1.findViewById(R.id.totalTime);
        oppoRatio=(TextView) view1.findViewById(R.id.oppoRatio);
        oppoAccu=(TextView) view1.findViewById(R.id.oppoAccu);

        mainlinearLayout=(LinearLayout) view1.findViewById(R.id.mainlinearLayout);

        oppoBatchCardView=(CardView) view1.findViewById(R.id.oppoBatchCardView);
        oppoBatch=(ImageView) view1.findViewById(R.id.oppoBatch);
        oppoLevelText=(TextView) view1.findViewById(R.id.oppoLevelText);

        cancelButton=(Button) view1.findViewById(R.id.cancelButton);

        LinearLayout roomCodeLinear=(LinearLayout) view1.findViewById(R.id.roomCodeLinear);
        roomCodeLinear.setVisibility(View.GONE);



        myName.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context));
        Glide.with(context).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context)).apply(RequestOptions
                .bitmapTransform(new RoundedCorners(18)))
                .into(myImage);


        myBatchSetter(myBatch,myLevelText);

        for(int i=1;i<=3;i++){
            dataForHorizontalSlide(context);
        }


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



        final AlertDialog alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }



        table_user.child("LeaderBoard").child(onlineDetailHolder.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);
                    OneVSOneOpponentDataSetter oneVSOneOpponentDataSetter=new OneVSOneOpponentDataSetter(leaderBoardHolder,oppoImage,oppoName,highestScore,totalTime,oppoRatio,oppoAccu,shimmerOppo,context,mainlinearLayout,oppoBatchCardView,oppoBatch,oppoLevelText);
                    oneVSOneOpponentDataSetter.start();
                    oppoImgStr=leaderBoardHolder.getImageUrl();
                }catch (Exception e){
                    table_user.child("User").child(onlineDetailHolder.getUID()).child("personal").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            FirstTime firstTime=snapshot.getValue(FirstTime.class);
                            LeaderBoardHolder leaderBoardHolder=new LeaderBoardHolder(firstTime.getUserName(),0,0,0,0,firstTime.getImageURL(),0);
                            OneVSOneOpponentDataSetter oneVSOneOpponentDataSetter=new OneVSOneOpponentDataSetter(leaderBoardHolder,oppoImage,oppoName,highestScore,totalTime,oppoRatio,oppoAccu,shimmerOppo,context,mainlinearLayout,oppoBatchCardView,oppoBatch,oppoLevelText);
                            oneVSOneOpponentDataSetter.start();
                            oppoImgStr=leaderBoardHolder.getImageUrl();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    public void myBatchSetter(ImageView myBatch, TextView myLevelText){
        table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).child("sumationScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    int summationScore=snapshot.getValue(Integer.class);
                    BatchGenerator batchGenerator=new BatchGenerator(summationScore,myBatch);
                    batchGenerator.start();
                    LevelGenerators levelGenerators=new LevelGenerators(summationScore,myLevelText);
                    levelGenerators.start();
                }catch (Exception e){
                    BatchGenerator batchGenerator=new BatchGenerator(0,myBatch);
                    batchGenerator.start();
                    LevelGenerators levelGenerators=new LevelGenerators(0,myLevelText);
                    levelGenerators.start();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
