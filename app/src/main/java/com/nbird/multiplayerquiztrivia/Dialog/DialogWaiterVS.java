package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Debug;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.nbird.multiplayerquiztrivia.FIREBASE.VS.RoomCodeGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.BatchGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.LevelGenerators;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.FirstTime;
import com.nbird.multiplayerquiztrivia.Model.OnlineDetailHolder;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalPictureQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalSingleQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalVideoQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsAudioQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsNormalQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsPictureQuiz;
import com.nbird.multiplayerquiztrivia.QUIZ.VsVideoQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class DialogWaiterVS {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    ShimmerFrameLayout shimmerOppo,shimerFact;
    slideAdapterMainMenuHorizontalSlide sliderAdapter;
    DatabaseReference myRef = database.getReference();

    ViewPager slideViewPager;
    LinearLayout dotLayout;
    List<mainMenuFactsHolder> list;
    int num=0;
    TextView[] mDots;
    ActionBarDrawerToggle mToggle;

    Context context;
    private int currentPage;

    AppData appData;
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

    ValueEventListener listenerOppoConnected;
    CountDownTimer countDownTimerIntent;

    int incrementer=0;
    int roomCodeInt;

    LeaderBoardHolder leaderBoardHolder;

    public void start(Context context, View view,int quizMode){

        this.context=context;

        appData = new AppData();

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

        CardView share=(CardView) view1.findViewById(R.id.share);


        String myNameStr=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plane");
                String shareBody=myNameStr+" Has Created A Room To Play With You.\n"+"Here's Your Room Code : "+roomCodeInt+".";
                String sharesub="Multiplayer Quiz Trivia";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                context.startActivity(Intent.createChooser(shareIntent,"Room Code"));
            }
        });



        myName.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context));
        Glide.with(context).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context)).apply(RequestOptions
                .bitmapTransform(new RoundedCorners(18)))
                .into(myImage);


        myBatchSetter(myBatch,myLevelText);

        for(int i=1;i<=3;i++){
            dataForHorizontalSlide(context);
        }

        roomCode(view1,quizMode);




        final AlertDialog alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{ table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Personal").child("player2UID").removeEventListener(listenerOppoConnected); }catch (Exception e){ }
                try{ countDownTimerIntent.cancel(); }catch (Exception e){ }
                try{ table_user.child("VS_ARENA").child(mAuth.getCurrentUser().getUid()).removeValue(); }catch (Exception e){ }
                try { alertDialog.dismiss(); }catch (Exception e){ }
            }
        });



    }


    public void roomCode(View view1,int quizMode){


        TextView roomCode=(TextView) view1.findViewById(R.id.roomCode);

        RoomCodeGenerator roomCodeGenerator=new RoomCodeGenerator();
        roomCodeInt=roomCodeGenerator.start();

        roomCode.setText("Code : "+String.valueOf(roomCodeInt));

        OnlineDetailHolder onlineDetailHolder=new OnlineDetailHolder(mAuth.getCurrentUser().getUid(),quizMode,roomCodeInt);


        table_user.child("VS_ARENA").child(mAuth.getCurrentUser().getUid()).setValue(onlineDetailHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                listenerOppoConnected=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            try {

                                String Player2UID = snapshot.getValue(String.class);

                                Log.i("Player 2 UID",Player2UID);
                                table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Personal").child("player2UID").removeEventListener(listenerOppoConnected);
                                table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).removeValue();




                                    table_user.child("LeaderBoard").child(Player2UID).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                                            try {
                                                leaderBoardHolder = snapshot.getValue(LeaderBoardHolder.class);



                                            OneVSOneOpponentDataSetter oneVSOneOpponentDataSetter = new OneVSOneOpponentDataSetter(leaderBoardHolder, oppoImage, oppoName, highestScore, totalTime, oppoRatio, oppoAccu, shimmerOppo, context, mainlinearLayout, oppoBatchCardView, oppoBatch, oppoLevelText);
                                            oneVSOneOpponentDataSetter.start();


                                            } catch (Exception e) {
                                                table_user.child("User").child(Player2UID).child("personal").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        FirstTime firstTime = snapshot.getValue(FirstTime.class);
                                                        leaderBoardHolder = new LeaderBoardHolder(firstTime.getUserName(), 0, 0, 0, 0, firstTime.getImageURL(), 0);
                                                        OneVSOneOpponentDataSetter oneVSOneOpponentDataSetter = new OneVSOneOpponentDataSetter(leaderBoardHolder, oppoImage, oppoName, highestScore, totalTime, oppoRatio, oppoAccu, shimmerOppo, context, mainlinearLayout, oppoBatchCardView, oppoBatch, oppoLevelText);
                                                        oneVSOneOpponentDataSetter.start();



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


                                ConnectionStatus connectionStatus=new ConnectionStatus();
                                connectionStatus.myStatusSetter();



                                ArrayList<Integer> listAns = new ArrayList<>();

                                switch (quizMode) {
                                    case 1:
                                        pictureQuizNumberUploader(listAns);
                                        break;
                                    case 2:
                                        normalQuizNumberUploader(listAns);
                                        break;
                                    case 3:
                                        audioQuizNumberUploader(listAns);
                                        break;
                                    case 4:
                                        vidioQuizNUmberUploader(listAns);
                                        break;
                                }

                                cancelButton.setTextSize(10f);


                                table_user.child("VS_PLAY").child("IsDone").child(mAuth.getCurrentUser().getUid()).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                                cancelButton.setEnabled(false);

                                table_user.child("VS_ARENA").child(mAuth.getCurrentUser().getUid()).removeValue();

                                countDownTimerIntent = new CountDownTimer(1000 * 10, 1000) {
                                    @Override
                                    public void onTick(long l) {
                                        cancelButton.setText("Game Starts In " + l / 1000);
                                    }

                                    @Override
                                    public void onFinish() {
                                        switch (quizMode) {
                                            case 2:
                                                Intent intent = new Intent(context, VsNormalQuiz.class);
                                                intent.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) listAns);
                                                intent.putExtra("playerNum",1);
                                                intent.putExtra("oppoImgStr",leaderBoardHolder.getImageUrl());
                                                intent.putExtra("oppoName",leaderBoardHolder.getUsername());
                                                intent.putExtra("oppoUID",Player2UID);
                                                intent.putExtra("mode",quizMode);
                                                context.startActivity(intent);
                                                ((Activity) context).finish();
                                                break;
                                            case 1:
                                                Intent intent1 = new Intent(context, VsPictureQuiz.class);
                                                intent1.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) listAns);
                                                intent1.putExtra("playerNum",1);
                                                intent1.putExtra("oppoImgStr",leaderBoardHolder.getImageUrl());
                                                intent1.putExtra("oppoName",leaderBoardHolder.getUsername());
                                                intent1.putExtra("oppoUID",Player2UID);
                                                intent1.putExtra("mode",quizMode);
                                                context.startActivity(intent1);
                                                ((Activity) context).finish();
                                                break;
                                            case 3:
                                                Intent intent2 = new Intent(context, VsAudioQuiz.class);
                                                intent2.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) listAns);
                                                intent2.putExtra("playerNum",1);
                                                intent2.putExtra("oppoImgStr",leaderBoardHolder.getImageUrl());
                                                intent2.putExtra("oppoName",leaderBoardHolder.getUsername());
                                                intent2.putExtra("oppoUID",Player2UID);
                                                intent2.putExtra("mode",quizMode);
                                                context.startActivity(intent2);
                                                ((Activity) context).finish();
                                                break;
                                            case 4:
                                                Intent intent3 = new Intent(context, VsVideoQuiz.class);
                                                intent3.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) listAns);
                                                intent3.putExtra("playerNum",1);
                                                intent3.putExtra("oppoImgStr",leaderBoardHolder.getImageUrl());
                                                intent3.putExtra("oppoName",leaderBoardHolder.getUsername());
                                                intent3.putExtra("oppoUID",Player2UID);
                                                intent3.putExtra("mode",quizMode);
                                                context.startActivity(intent3);
                                                ((Activity) context).finish();
                                                break;
                                        }

                                    }
                                }.start();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Personal").child("player2UID").addValueEventListener(listenerOppoConnected);

            }
        });
    }





    public void normalQuizNumberUploader(ArrayList<Integer> listAns){
        Random random=new Random();
        for(int i=0;i<11;i++){
            listAns.add(random.nextInt(6326)+1);
        }

        table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void pictureQuizNumberUploader(ArrayList<Integer> listAns){
        Random random=new Random();

        for(int i=0;i<11;i++){
            int setNumber = random.nextInt(4999)+1;
            if(setNumber>1210&&setNumber<2000){
                setNumber=setNumber-1000;
            }
            listAns.add(setNumber);
        }
        table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void audioQuizNumberUploader(ArrayList<Integer> listAns){
        Random random=new Random();
        myRef.child("QUIZNUMBERS").child("AudioQuestionQuantity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num;
                try{
                    num=snapshot.getValue(Integer.class);
                }catch (Exception e){
                    num=3464;
                }
                for(int i=0;i<11;i++){
                    int setNumber = random.nextInt(num)+1;
                    listAns.add(setNumber);
                }

                table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void vidioQuizNUmberUploader(ArrayList<Integer> listAns){
        Random random=new Random();
        myRef.child("QUIZNUMBERS").child("VideoQuestionQuantity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num;
                try{
                    num=snapshot.getValue(Integer.class);
                }catch (Exception e){
                    num=118;
                }
                for(int i=0;i<11;i++){
                    int setNumber = random.nextInt(num)+1;
                    listAns.add(setNumber);
                }

                table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

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
