package com.nbird.multiplayerquiztrivia.TOURNAMENT.EXTRA;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerDisplayInQuizAadapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.RoomListAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerDisplayInQuizHolder;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerInfo;

import java.util.ArrayList;

public class PlayerDisplayInQuiz {

    RecyclerView recyclerView;
    Context context;
    private PlayerDisplayInQuizAadapter playerDisplayInQuizAadapter;
    ArrayList<PlayerDisplayInQuizHolder> arrayList;

    ValueEventListener valueEventListener;
    String roomCode;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    public PlayerDisplayInQuiz(Context context, ValueEventListener valueEventListener, String roomCode,RecyclerView recyclerView) {
        this.context = context;
        this.valueEventListener = valueEventListener;
        this.roomCode = roomCode;
        this.recyclerView=recyclerView;
    }

    public void start(){


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(recyclerView.HORIZONTAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList=new ArrayList<>();


        playerDisplayInQuizAadapter = new PlayerDisplayInQuizAadapter(context,arrayList);
        recyclerView.setAdapter(playerDisplayInQuizAadapter);



        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                        PlayerDisplayInQuizHolder playerDisplayInQuizHolder=dataSnapshot.getValue(PlayerDisplayInQuizHolder.class);
                        arrayList.add(playerDisplayInQuizHolder);

                }

                playerDisplayInQuizAadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("TOURNAMENT").child("ANSWERS").child(roomCode).addValueEventListener(valueEventListener);

    }


}
