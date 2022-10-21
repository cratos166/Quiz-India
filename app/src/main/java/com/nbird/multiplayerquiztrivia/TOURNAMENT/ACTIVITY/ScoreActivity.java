package com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.Model.DataExchangeHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerDataAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.ResultAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.SERVER.DataSetter;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    String roomCode;
    int maxQuestions;
    ValueEventListener resultEventListener;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    ArrayList<DataExchangeHolder> playerDataArrayList;

    ResultAdapter resultAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


        roomCode=getIntent().getStringExtra("roomCode");
        maxQuestions=getIntent().getIntExtra("maxQuestions",10);






        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);

        playerDataArrayList=new ArrayList<>();

        resultAdapter=new ResultAdapter(ScoreActivity.this,playerDataArrayList,maxQuestions);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(resultAdapter);


        //   playerDataSetter();
       resultGetter();




    }


    private void resultGetter(){

        resultEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                playerDataArrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    DataExchangeHolder dataExchangeHolder=dataSnapshot.getValue(DataExchangeHolder.class);

                    playerDataArrayList.add(dataExchangeHolder);


                }


                resultAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        table_user.child("TOURNAMENT").child("RESULT").child(roomCode).addValueEventListener(resultEventListener);
    }

}