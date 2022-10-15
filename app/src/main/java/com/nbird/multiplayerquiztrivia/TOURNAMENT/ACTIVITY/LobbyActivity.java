package com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.FACTS.mainMenuFactsHolder;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.MAIN.RecyclerViewAdapter;
import com.nbird.multiplayerquiztrivia.QUIZ.NormalSingleQuiz;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerDataAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.FactsDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.PrivacyDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.SettingDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.TroubleShootDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    ValueEventListener privacyListener,numberOfQuestionListener,totalTimeListener,modeListener;


    int myPlayerNum=1;

    RecyclerView recyclerview;
    ArrayList<Details> playerDataArrayList;
    PlayerDataAdapter myAdapter;

    AppData appData;


    CardView factButton,chatButton,cancelButton,settingButton,removePlayerButton,troubleshoot,privacyButton;


    String roomCode;

    TextView privacyTextView,questionTextView,timeTextView,modeTextView;

    int numberofQuestion=1,gameMode=1,timeInt=1;
    Boolean privacy=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        recyclerview=(RecyclerView) findViewById(R.id.recyclerview);

        appData=new AppData();


        privacyTextView=(TextView) findViewById(R.id.privacyTextView);
        questionTextView=(TextView) findViewById(R.id.questionTextView);
        timeTextView=(TextView) findViewById(R.id.timeTextView);
        modeTextView=(TextView) findViewById(R.id.modeTextView);


        factButton=(CardView) findViewById(R.id.card1fact);
        chatButton=(CardView) findViewById(R.id.card1chat);
        settingButton=(CardView) findViewById(R.id.cardcancel);

        removePlayerButton=(CardView) findViewById(R.id.removePlayer);
        troubleshoot=(CardView) findViewById(R.id.troubleshoot);
        privacyButton=(CardView) findViewById(R.id.cardchat) ;



        myPlayerNum=getIntent().getIntExtra("playerNum",1);
        roomCode=getIntent().getStringExtra("roomCode");


        playerDataArrayList=new ArrayList<>();

        myAdapter=new PlayerDataAdapter(this,playerDataArrayList);
        recyclerview.setLayoutManager(new GridLayoutManager(this,2));
        recyclerview.setAdapter(myAdapter);


        myDataSetter();


        SettingDialog settingDialog=new SettingDialog(LobbyActivity.this,roomCode);

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrivacyDialog privacyDialog=new PrivacyDialog(LobbyActivity.this,roomCode);
                privacyDialog.start(privacyButton);
            }
        });


        factButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FactsDialog factsDialog=new FactsDialog(LobbyActivity.this);
                factsDialog.start(factButton);



            }
        });

        troubleshoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TroubleShootDialog troubleShootDialog=new TroubleShootDialog(LobbyActivity.this);
                troubleShootDialog.start(troubleshoot);
            }
        });



        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingDialog.start(settingButton);
            }
        });



        settingsEventListner();



    }

    private void settingsEventListner(){
        privacyListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                privacy=snapshot.getValue(Boolean.class);
                if(privacy){
                    privacyTextView.setText("PUBLIC");
                }else{
                    privacyTextView.setText("PRIVATE");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("privacy").addValueEventListener(privacyListener);


        numberOfQuestionListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numberofQuestion=snapshot.getValue(Integer.class);
                if(numberofQuestion==1){
                    questionTextView.setText("10");
                }else if(numberofQuestion==2){
                    questionTextView.setText("15");
                }else{
                    questionTextView.setText("20");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("numberOfQuestions").addValueEventListener(numberOfQuestionListener);


        totalTimeListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeInt=snapshot.getValue(Integer.class);
                if(timeInt==1){
                    timeTextView.setText("3 Mins");
                }else if(timeInt==2){
                    timeTextView.setText("4.5 Mins");
                }else{
                    timeTextView.setText("6 Mins");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("time").addValueEventListener(totalTimeListener);

        modeListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameMode=snapshot.getValue(Integer.class);
                if(gameMode==1){
                    modeTextView.setText("Normal");
                }else if(gameMode==2){
                    modeTextView.setText("Picture");
                }else if(gameMode==3){
                    modeTextView.setText("Buzzer Normal");
                }else{
                    modeTextView.setText("Buzzer Picture");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("gameMode").addValueEventListener(modeListener);

    }




    private void myDataSetter(){

        table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);

                    float acc=((leaderBoardHolder.getCorrect()*100)/leaderBoardHolder.getWrong());

                    int min=leaderBoardHolder.getTotalTime()/60;
                    int sec=leaderBoardHolder.getTotalTime()%60;

                    String totalTime=min+" min "+sec+" sec";
                    String accStr=acc+"%";
                    String highestScore=String.valueOf(leaderBoardHolder.getScore());
                    playerDataArrayList.add(new Details(leaderBoardHolder.getImageUrl(),leaderBoardHolder.getUsername(),totalTime,accStr,highestScore));

                    myAdapter.notifyDataSetChanged();

                }catch (Exception e){
                    String myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, LobbyActivity.this);
                    String myPicURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, LobbyActivity.this);
                    String totalTime="0 min 0 sec";
                    String accStr="0%";
                    String highestScore="0";
                    playerDataArrayList.add(new Details(myPicURL,myName,totalTime,accStr,highestScore));
                    myAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




}