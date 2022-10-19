package com.nbird.multiplayerquiztrivia.TOURNAMENT.SERVER;

import android.widget.Adapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerDataAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerInfo;

import java.util.ArrayList;

public class DataSetter {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    public DataSetter() {
    }

    public void getPlayerData(String roomCode, ArrayList<Details> playerDataArrayList, PlayerDataAdapter myAdapter, ValueEventListener valueEventListener){


        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                playerDataArrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){


                    try{



                        PlayerInfo playerInfo=dataSnapshot.getValue(PlayerInfo.class);


                        if(playerInfo.isActive()){
                            float acc=((playerInfo.getCorrect()*100)/playerInfo.getWrong());

                            int min=playerInfo.getTotalTime()/60;
                            int sec=playerInfo.getTotalTime()%60;

                            String totalTime=min+" min "+sec+" sec";
                            String accStr=acc+"%";
                            String highestScore=String.valueOf(playerInfo.getScore());
                            playerDataArrayList.add(new Details(playerInfo.getImageUrl(),playerInfo.getUsername(),totalTime,accStr,highestScore,dataSnapshot.getKey()));

                        }else{
                            table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(dataSnapshot.getKey()).removeValue();
                        }



                    }catch (Exception e){

                    }

                }


                table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("numberOfPlayers").setValue(playerDataArrayList.size()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });


                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).addValueEventListener(valueEventListener);



    }

}
