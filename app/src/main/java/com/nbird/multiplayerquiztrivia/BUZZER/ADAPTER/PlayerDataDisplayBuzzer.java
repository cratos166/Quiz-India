package com.nbird.multiplayerquiztrivia.BUZZER.ADAPTER;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.BUZZER.MODEL.PlayerDisplayBuzzerHolder;

import java.util.ArrayList;

public class PlayerDataDisplayBuzzer {

    RecyclerView recyclerView;
    Context context;
    private PlayerDisplayInBuzzerAdapter playerDisplayInBuzzerAdapter;
    ArrayList<PlayerDisplayBuzzerHolder> arrayList;

    ValueEventListener valueEventListener;
    String roomCode;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    int currentQuestionStatus;


    public PlayerDataDisplayBuzzer(Context context, ValueEventListener valueEventListener, String roomCode, RecyclerView recyclerView, int currentQuestionStatus) {
        this.context = context;
        this.valueEventListener = valueEventListener;
        this.roomCode = roomCode;
        this.recyclerView=recyclerView;
        this.currentQuestionStatus=currentQuestionStatus;

    }

    public void start(){


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(recyclerView.HORIZONTAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList=new ArrayList<>();


        playerDisplayInBuzzerAdapter = new PlayerDisplayInBuzzerAdapter(context,arrayList,currentQuestionStatus);
        recyclerView.setAdapter(playerDisplayInBuzzerAdapter);



        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    PlayerDisplayBuzzerHolder playerDisplayBuzzerHolder=dataSnapshot.getValue(PlayerDisplayBuzzerHolder.class);
                    arrayList.add(playerDisplayBuzzerHolder);

                }

                playerDisplayInBuzzerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("BUZZER").child("ANSWERS").child(roomCode).addValueEventListener(valueEventListener);

    }


}