package com.nbird.multiplayerquiztrivia.BUZZER.ACTIVTY;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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
import com.nbird.multiplayerquiztrivia.BUZZER.DIALOG.BuzzerPrivacyDialog;
import com.nbird.multiplayerquiztrivia.BUZZER.DIALOG.BuzzerRemovePlayerDialog;
import com.nbird.multiplayerquiztrivia.BUZZER.DIALOG.BuzzerSettingDialog;
import com.nbird.multiplayerquiztrivia.BUZZER.SERVER.BuzzerDataSetter;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.LobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerDataAdapter;
import com.nbird.multiplayerquiztrivia.BUZZER.DIALOG.BuzzerChatDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.FactsDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.TroubleShootDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;

import java.util.ArrayList;
import java.util.Random;

public class LobbyBuzzerActivity extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference myRef=database.getReference();
    ValueEventListener privacyListener,numberOfQuestionListener,totalTimeListener,modeListener;


    int myPlayerNum=1;

    RecyclerView recyclerview;
    ArrayList<Details> playerDataArrayList;
    PlayerDataAdapter myAdapter;

    AppData appData;


    CardView factButton,chatButton,cancelButton,settingButton,removePlayerButton,troubleshoot,privacyButton;


    String roomCode,hostNameStr;

    TextView privacyTextView,questionTextView,timeTextView,modeTextView,hostName,roomCodeTextView;

    int numberofQuestion=1,gameMode=1,timeInt=1;
    Boolean privacy=true;


    ValueEventListener valueEventListener,chatEventListener,hostEventListener,myEventListener,questionGetterListener;
    Button startButton;

    String myName,myPicStr;


    CountDownTimer countDownTimer;

    ArrayList<Integer> listAns;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);



        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerview=(RecyclerView) findViewById(R.id.recyclerview);

        appData=new AppData();

        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, LobbyBuzzerActivity.this);
        myPicStr=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, LobbyBuzzerActivity.this);


        privacyTextView=(TextView) findViewById(R.id.privacyTextView);
        questionTextView=(TextView) findViewById(R.id.questionTextView);
        timeTextView=(TextView) findViewById(R.id.timeTextView);
        modeTextView=(TextView) findViewById(R.id.modeTextView);
        hostName=(TextView) findViewById(R.id.hostName);


        factButton=(CardView) findViewById(R.id.card1fact);
        chatButton=(CardView) findViewById(R.id.card1chat);
        settingButton=(CardView) findViewById(R.id.cardcancel);
        cancelButton=(CardView) findViewById(R.id.card1cancel);

        removePlayerButton=(CardView) findViewById(R.id.removePlayer);
        troubleshoot=(CardView) findViewById(R.id.troubleshoot);
        privacyButton=(CardView) findViewById(R.id.cardchat) ;
        startButton=(Button) findViewById(R.id.startButton);


        roomCodeTextView=(TextView) findViewById(R.id.roomCodeTextView);

        listAns=new ArrayList<>();



        myPlayerNum=getIntent().getIntExtra("playerNum",1);
        roomCode=getIntent().getStringExtra("roomCode");
        hostNameStr=getIntent().getStringExtra("hostName");


        if(myPlayerNum==1){
            table_user.child("BUZZER").child("QUESTIONS").child(roomCode).removeValue();
            table_user.child("BUZZER").child("ANSWERS").child(roomCode).removeValue();
        }

        roomCodeTextView.setText("Room Code : "+roomCode);

        playerDataArrayList=new ArrayList<>();

        myAdapter=new PlayerDataAdapter(this,playerDataArrayList);
        recyclerview.setLayoutManager(new GridLayoutManager(this,2));
        recyclerview.setAdapter(myAdapter);


     //   playerDataSetter();
        BuzzerDataSetter dataSetter=new BuzzerDataSetter();
        dataSetter.getPlayerData(roomCode,playerDataArrayList,myAdapter,valueEventListener);



        BuzzerSettingDialog settingDialog=new BuzzerSettingDialog(LobbyBuzzerActivity.this,roomCode);


        hostName.setText("Host : "+hostNameStr);

        if(myPlayerNum!=1){
            startButton.setTextSize(8.0f);
            startButton.setText("Waiting for Host to start the game");
            startButton.setEnabled(false);
            startButton.setAlpha(0.7f);


            privacyButton.setVisibility(View.GONE);
            settingButton.setVisibility(View.GONE);
            removePlayerButton.setVisibility(View.GONE);


            hostTracker();

            questionDownloader();

            myEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        Boolean bb=snapshot.getValue(Boolean.class);
                        if(bb==true){
                         //   Toast.makeText(LobbyActivity.this, "true", Toast.LENGTH_SHORT).show();
                        }else{
                            try{ table_user.child("BUZZER").child("PLAYERS").child(roomCode).removeEventListener(valueEventListener);}catch (Exception e){}
                            try{  table_user.child("BUZZER").child("CHAT").child(roomCode).removeEventListener(chatEventListener);}catch (Exception e){}
                            try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").removeEventListener(hostEventListener);}catch (Exception e){}
                            try{ table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").removeEventListener(myEventListener);}catch (Exception e1){}


                            try{  table_user.child("BUZZER").child("ROOM").child(roomCode).child("privacy").removeEventListener(privacyListener);}catch (Exception e){}
                            try{table_user.child("BUZZER").child("ROOM").child(roomCode).child("numberOfQuestions").removeEventListener(numberOfQuestionListener);}catch (Exception e){}
                            try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("time").removeEventListener(totalTimeListener);}catch (Exception e){}
                            try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("gameMode").removeEventListener(modeListener);}catch (Exception e){}
                            try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("active").removeEventListener(questionGetterListener);}catch (Exception e){}

                            try{countDownTimer.cancel();}catch (Exception e){}

                            Intent intent=new Intent(LobbyBuzzerActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }catch (Exception e){
                        intentFunctionMainActivity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").addValueEventListener(myEventListener);



        }else{


            table_user.child("BUZZER").child("RESULT").child(roomCode).removeValue();
            table_user.child("BUZZER").child("QUESTIONS").child(roomCode).removeValue();
            table_user.child("BUZZER").child("ANSWERS").child(roomCode).removeValue();
            table_user.child("BUZZER").child("CHAT").child(roomCode).removeValue();


        }


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                quitLobbyActivity();




            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BuzzerChatDialog chatDialog=new BuzzerChatDialog(LobbyBuzzerActivity.this,roomCode,myName,chatEventListener);
                chatDialog.start(chatButton);
            }
        });


        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuzzerPrivacyDialog privacyDialog=new BuzzerPrivacyDialog(LobbyBuzzerActivity.this,roomCode,privacy);
                privacyDialog.start(privacyButton);
            }
        });


        factButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FactsDialog factsDialog=new FactsDialog(LobbyBuzzerActivity.this);
                factsDialog.start(factButton);



            }
        });

        troubleshoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TroubleShootDialog troubleShootDialog=new TroubleShootDialog(LobbyBuzzerActivity.this);
                troubleShootDialog.start(troubleshoot);
            }
        });



        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingDialog.start(settingButton);
            }
        });



        removePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Details> playerToBeRemoved=new ArrayList<>();


                for(int i=0;i<playerDataArrayList.size();i++){
                    if(!mAuth.getCurrentUser().getUid().equals(playerDataArrayList.get(i).getUid())){
                        playerToBeRemoved.add(playerDataArrayList.get(i));
                    }
                }


                BuzzerRemovePlayerDialog removePlayerDialog=new BuzzerRemovePlayerDialog(LobbyBuzzerActivity.this,roomCode);
                removePlayerDialog.start(removePlayerButton,playerToBeRemoved);

            }
        });



        settingsEventListner();



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                questionUploader();

                startButton.setTextSize(10.0f);

                startButton.setEnabled(false);
                startButton.setAlpha(0.7f);


                privacyButton.setVisibility(View.GONE);
                settingButton.setVisibility(View.GONE);
                removePlayerButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.INVISIBLE);




            }
        });


    }



    private void questionDownloader(){



        questionGetterListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    int bb=snapshot.getValue(Integer.class);
                    if(bb==2){

                        cancelButton.setVisibility(View.INVISIBLE);

                        table_user.child("BUZZER").child("QUESTIONS").child(roomCode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try{

                                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                        int holder=dataSnapshot.getValue(Integer.class);
                                        listAns.add(holder);
                                    }

                                    timerStarter();
                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("BUZZER").child("ROOM").child(roomCode).child("active").addValueEventListener(questionGetterListener);




    }


    private void intentFunctionMainActivity(){





        try{ table_user.child("BUZZER").child("PLAYERS").child(roomCode).removeEventListener(valueEventListener);}catch (Exception e1){}
        try{  table_user.child("BUZZER").child("CHAT").child(roomCode).removeEventListener(chatEventListener);}catch (Exception e1){}
        try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").removeEventListener(hostEventListener);}catch (Exception e1){}
        try{ table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").removeEventListener(myEventListener);}catch (Exception e1){}


        try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("active").removeEventListener(questionGetterListener);}catch (Exception e1){}
        try{  table_user.child("BUZZER").child("ROOM").child(roomCode).child("privacy").removeEventListener(privacyListener);}catch (Exception e1){}
        try{table_user.child("BUZZER").child("ROOM").child(roomCode).child("numberOfQuestions").removeEventListener(numberOfQuestionListener);}catch (Exception e1){}
        try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("time").removeEventListener(totalTimeListener);}catch (Exception e1){}
        try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("gameMode").removeEventListener(modeListener);}catch (Exception e1){}

        try{countDownTimer.cancel();}catch (Exception e1){}


        Intent intent=new Intent(LobbyBuzzerActivity.this,MainActivity.class);
        intent.putExtra("notificationOfHost",2);
        startActivity(intent);
        finish();
    }


    private void intentFunction(){

        Dialog dialog=null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog, LobbyBuzzerActivity.this);
        supportAlertDialog.showLoadingDialog();


        table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("activityNumber").setValue(2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                supportAlertDialog.dismissLoadingDialog();

                try{ table_user.child("BUZZER").child("PLAYERS").child(roomCode).removeEventListener(valueEventListener);}catch (Exception e){}
                try{  table_user.child("BUZZER").child("CHAT").child(roomCode).removeEventListener(chatEventListener);}catch (Exception e){}
                try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").removeEventListener(hostEventListener);}catch (Exception e){}
                try{ table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").removeEventListener(myEventListener);}catch (Exception e1){}

                try{  table_user.child("BUZZER").child("ROOM").child(roomCode).child("active").removeEventListener(questionGetterListener);}catch (Exception e){}
                try{  table_user.child("BUZZER").child("ROOM").child(roomCode).child("privacy").removeEventListener(privacyListener);}catch (Exception e){}
                try{table_user.child("BUZZER").child("ROOM").child(roomCode).child("numberOfQuestions").removeEventListener(numberOfQuestionListener);}catch (Exception e){}
                try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("time").removeEventListener(totalTimeListener);}catch (Exception e){}
                try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("gameMode").removeEventListener(modeListener);}catch (Exception e){}


                try{countDownTimer.cancel();}catch (Exception e){}

                Intent intent = null;
                if(gameMode==1){
                    intent=new Intent(LobbyBuzzerActivity.this, BuzzerNormalActivity.class);
                }else if(gameMode==2){
                    intent=new Intent(LobbyBuzzerActivity.this, BuzzerPictureActivity.class);
                }

                intent.putIntegerArrayListExtra("answerInt", (ArrayList<Integer>) listAns);
                intent.putExtra("roomCode",roomCode);
                intent.putExtra("playerNum",myPlayerNum);
                intent.putExtra("hostName",hostNameStr);

                if(numberofQuestion==1){
                    intent.putExtra("numberOfQuestions",10);
                }else if(numberofQuestion==2){
                    intent.putExtra("numberOfQuestions",15);
                }else{
                    intent.putExtra("numberOfQuestions",20);
                }

                if(timeInt==1){
                    intent.putExtra("time",180);
                }else if(timeInt==2){
                    intent.putExtra("time",270);
                }else{
                    intent.putExtra("time",360);
                }


                startActivity(intent);
                finish();


            }
        });




    }




    private void questionUploader(){

        int number;
        if(numberofQuestion==1){
            number=10;
        }else if(numberofQuestion==2){
            number=15;
        }else{
            number=20;
        }


        if(gameMode==1){
            normalQuizNumberUploader(listAns,number);
        }else if(gameMode==2){
            pictureQuizNumberUploader(listAns,number);
        }

    }


    private void timerStarter(){
        countDownTimer=new CountDownTimer(1000*10,1000) {
            @Override
            public void onTick(long l) {
                startButton.setText("Game starts in "+l/1000);
            }

            @Override
            public void onFinish() {
                intentFunction();
            }
        }.start();
    }


    private void roomActivator(){

        table_user.child("BUZZER").child("ROOM").child(roomCode).child("active").setValue(2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                timerStarter();
            }
        });


    }


    public void normalQuizNumberUploader(ArrayList<Integer> listAns, int number){
        Random random=new Random();
        for(int i=0;i<=number;i++){
            listAns.add(random.nextInt(6326)+1);
        }

        table_user.child("BUZZER").child("QUESTIONS").child(roomCode).setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                roomActivator();
            }
        });
    }

    public void pictureQuizNumberUploader(ArrayList<Integer> listAns, int number){
        Random random=new Random();

        for(int i=0;i<=number;i++){
            int setNumber = random.nextInt(4999)+1;
            if(setNumber>1210&&setNumber<2000){
                setNumber=setNumber-1000;
            }
            listAns.add(setNumber);
        }
        table_user.child("BUZZER").child("QUESTIONS").child(roomCode).setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                roomActivator();
            }
        });
    }



    private void settingsEventListner(){
        privacyListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                try{
                    privacy=snapshot.getValue(Boolean.class);
                    if(privacy){
                        privacyTextView.setText("PUBLIC");
                    }else{
                        privacyTextView.setText("PRIVATE");
                    }

                }catch (Exception e){

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("BUZZER").child("ROOM").child(roomCode).child("privacy").addValueEventListener(privacyListener);


        numberOfQuestionListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    numberofQuestion=snapshot.getValue(Integer.class);
                    if(numberofQuestion==1){
                        questionTextView.setText("10");
                    }else if(numberofQuestion==2){
                        questionTextView.setText("15");
                    }else{
                        questionTextView.setText("20");
                    }
                }catch (Exception e){

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("BUZZER").child("ROOM").child(roomCode).child("numberOfQuestions").addValueEventListener(numberOfQuestionListener);


        totalTimeListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    timeInt=snapshot.getValue(Integer.class);
                    if(timeInt==1){
                        timeTextView.setText("3 Mins");
                    }else if(timeInt==2){
                        timeTextView.setText("4.5 Mins");
                    }else{
                        timeTextView.setText("6 Mins");
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("BUZZER").child("ROOM").child(roomCode).child("time").addValueEventListener(totalTimeListener);

        modeListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                try{
                    gameMode=snapshot.getValue(Integer.class);
                    if(gameMode==1){
                        modeTextView.setText("Normal Buzzer");
                    }else if(gameMode==2){
                        modeTextView.setText("Picture Buzzer");
                    }
                }catch (Exception e){

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("BUZZER").child("ROOM").child(roomCode).child("gameMode").addValueEventListener(modeListener);

    }



    public void removerFunction(){

        if(myPlayerNum==1){
            table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    table_user.child("BUZZER").child("PLAYERS").child(roomCode).removeValue();
                    table_user.child("BUZZER").child("CHAT").child(roomCode).removeValue();
                    table_user.child("BUZZER").child("ROOM").child(roomCode).removeValue();



                    try{ table_user.child("BUZZER").child("PLAYERS").child(roomCode).removeEventListener(valueEventListener);}catch (Exception e){}
                    try{  table_user.child("BUZZER").child("CHAT").child(roomCode).removeEventListener(chatEventListener);}catch (Exception e){}
                    try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").removeEventListener(hostEventListener);}catch (Exception e){}

                    try{  table_user.child("BUZZER").child("ROOM").child(roomCode).child("privacy").removeEventListener(privacyListener);}catch (Exception e){}
                    try{table_user.child("BUZZER").child("ROOM").child(roomCode).child("numberOfQuestions").removeEventListener(numberOfQuestionListener);}catch (Exception e){}
                    try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("time").removeEventListener(totalTimeListener);}catch (Exception e){}
                    try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("gameMode").removeEventListener(modeListener);}catch (Exception e){}
                    try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("active").removeEventListener(questionGetterListener);}catch (Exception e){}

                    try{countDownTimer.cancel();}catch (Exception e){}


                    Intent intent=new Intent(LobbyBuzzerActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();


                }
            });
        }else {

            table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });



        }


    }


    public void quitLobbyActivity(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(LobbyBuzzerActivity.this, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(LobbyBuzzerActivity.this).inflate(R.layout.dialog_model_2,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);


        Button yesButton=(Button) viewRemove1.findViewById(R.id.buttonYes);
        Button noButton=(Button) viewRemove1.findViewById(R.id.buttonNo);

        TextView textTitle=(TextView) viewRemove1.findViewById(R.id.textTitle);

        if(myPlayerNum==1){
            textTitle.setText("You are the host. If you left the room, the whole room will be dissolved.\n\n You really want to quit ?");

        }else{
            textTitle.setText("You really want to quit ?");

        }

        LottieAnimationView anim=(LottieAnimationView)  viewRemove1.findViewById(R.id.imageIcon);
        anim.setAnimation(R.raw.exit_lobby);
        anim.playAnimation();
        anim.loop(true);





        final AlertDialog alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                removerFunction();



                try{
                    alertDialog.dismiss();
                }catch (Exception e){

                }
//                ((Activity)context).finish();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


    public void hostTracker(){
        hostEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    boolean isActive=snapshot.getValue(Boolean.class);

                    if(!isActive){
                        Toast.makeText(LobbyBuzzerActivity.this, "HOST LEFT", Toast.LENGTH_LONG).show();

                        try{ table_user.child("BUZZER").child("PLAYERS").child(roomCode).removeEventListener(valueEventListener);}catch (Exception e1){}
                        try{  table_user.child("BUZZER").child("CHAT").child(roomCode).removeEventListener(chatEventListener);}catch (Exception e1){}
                        try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").removeEventListener(hostEventListener);}catch (Exception e1){}


                        try{  table_user.child("BUZZER").child("ROOM").child(roomCode).child("privacy").removeEventListener(privacyListener);}catch (Exception e){}
                        try{table_user.child("BUZZER").child("ROOM").child(roomCode).child("numberOfQuestions").removeEventListener(numberOfQuestionListener);}catch (Exception e){}
                        try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("time").removeEventListener(totalTimeListener);}catch (Exception e){}
                        try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("gameMode").removeEventListener(modeListener);}catch (Exception e){}
                        try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("active").removeEventListener(questionGetterListener);}catch (Exception e){}

                        try{countDownTimer.cancel();}catch (Exception e){}



                        Intent intent=new Intent(LobbyBuzzerActivity.this, MainActivity.class);
                        intent.putExtra("notificationOfHost",1);
                        LobbyBuzzerActivity.this.startActivity(intent);
                        ((Activity) LobbyBuzzerActivity.this).finish();



                    }

                }catch (Exception e){
                    Toast.makeText(LobbyBuzzerActivity.this, "HOST LEFT", Toast.LENGTH_LONG).show();

                    try{ table_user.child("BUZZER").child("PLAYERS").child(roomCode).removeEventListener(valueEventListener);}catch (Exception e1){}
                    try{  table_user.child("BUZZER").child("CHAT").child(roomCode).removeEventListener(chatEventListener);}catch (Exception e1){}
                    try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").removeEventListener(hostEventListener);}catch (Exception e1){}


                    try{  table_user.child("BUZZER").child("ROOM").child(roomCode).child("privacy").removeEventListener(privacyListener);}catch (Exception e1){}
                    try{table_user.child("BUZZER").child("ROOM").child(roomCode).child("numberOfQuestions").removeEventListener(numberOfQuestionListener);}catch (Exception e1){}
                    try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("time").removeEventListener(totalTimeListener);}catch (Exception e1){}
                    try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("gameMode").removeEventListener(modeListener);}catch (Exception e1){}
                    try{ table_user.child("BUZZER").child("ROOM").child(roomCode).child("active").removeEventListener(questionGetterListener);}catch (Exception e1){}

                    try{countDownTimer.cancel();}catch (Exception e1){}

                    Intent intent=new Intent(LobbyBuzzerActivity.this,MainActivity.class);
                    intent.putExtra("notificationOfHost",1);
                    LobbyBuzzerActivity.this.startActivity(intent);
                    ((Activity) LobbyBuzzerActivity.this).finish();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").addValueEventListener(hostEventListener);

    }

    public void onBackPressed() {
        quitLobbyActivity();
    }


}