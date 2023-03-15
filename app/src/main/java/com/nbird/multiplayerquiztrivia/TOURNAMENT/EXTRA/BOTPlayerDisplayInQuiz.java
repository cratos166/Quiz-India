package com.nbird.multiplayerquiztrivia.TOURNAMENT.EXTRA;

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
import com.nbird.multiplayerquiztrivia.BUZZER.ADAPTER.PlayerDisplayInBuzzerAdapter;
import com.nbird.multiplayerquiztrivia.BUZZER.MODEL.PlayerDisplayBuzzerHolder;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerDisplayInQuizAadapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerDisplayInQuizHolder;

import java.util.ArrayList;

public class BOTPlayerDisplayInQuiz {

    RecyclerView recyclerView;
    Context context;
    private PlayerDisplayInQuizAadapter playerDisplayInQuizAadapter;
    private PlayerDisplayInBuzzerAdapter playerDisplayInBuzzerAadapter;
    ArrayList<PlayerDisplayInQuizHolder> arrayList;
    ArrayList<PlayerDisplayBuzzerHolder> arrayList1;


    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    ValueEventListener valueEventListener;

    int size;
    int gate;
    public BOTPlayerDisplayInQuiz(Context context, RecyclerView recyclerView, int size,ValueEventListener valueEventListener,int gate) {
        this.context = context;
        this.recyclerView=recyclerView;
        this.size=size;
        this.valueEventListener=valueEventListener;
        this.gate=gate;
    }

    public void start(){


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(recyclerView.HORIZONTAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList=new ArrayList<>();


        if(gate==1){
            arrayList=new ArrayList<>();
            playerDisplayInQuizAadapter = new PlayerDisplayInQuizAadapter(context,arrayList,size);
            recyclerView.setAdapter(playerDisplayInQuizAadapter);
            valueEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    arrayList.clear();

                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                        try{
                            PlayerDisplayInQuizHolder playerDisplayInQuizHolder=dataSnapshot.getValue(PlayerDisplayInQuizHolder.class);
                            arrayList.add(playerDisplayInQuizHolder);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    playerDisplayInQuizAadapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).addValueEventListener(valueEventListener);

        }else {
            arrayList1=new ArrayList<>();
            playerDisplayInBuzzerAadapter = new PlayerDisplayInBuzzerAdapter(context,arrayList1,size);
            recyclerView.setAdapter(playerDisplayInBuzzerAadapter);
            valueEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    arrayList1.clear();

                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                        try{
                            PlayerDisplayBuzzerHolder playerDisplayInQuizHolder=dataSnapshot.getValue(PlayerDisplayBuzzerHolder.class);
                            arrayList1.add(playerDisplayInQuizHolder);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    playerDisplayInBuzzerAadapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).addValueEventListener(valueEventListener);

        }





    }

}
