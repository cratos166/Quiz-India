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
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();


    int myPlayerNum=1;

    RecyclerView recyclerview;
    ArrayList<Details> playerDataArrayList;
    PlayerDataAdapter myAdapter;

    AppData appData;


    CardView factButton,chatButton,cancelButton,settingButton,removePlayerButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        recyclerview=(RecyclerView) findViewById(R.id.recyclerview);

        appData=new AppData();


        factButton=(CardView) findViewById(R.id.card1fact);
        chatButton=(CardView) findViewById(R.id.card1chat);
        cancelButton=(CardView) findViewById(R.id.cardcancel);
        settingButton=(CardView) findViewById(R.id.cardchat);
        removePlayerButton=(CardView) findViewById(R.id.removePlayer);



        myPlayerNum=getIntent().getIntExtra("playerNum",1);


        playerDataArrayList=new ArrayList<>();

        myAdapter=new PlayerDataAdapter(this,playerDataArrayList);
        recyclerview.setLayoutManager(new GridLayoutManager(this,2));
        recyclerview.setAdapter(myAdapter);


        myDataSetter();


        factButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FactsDialog factsDialog=new FactsDialog(LobbyActivity.this);
                factsDialog.start(factButton);



            }
        });



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