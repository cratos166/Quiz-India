package com.nbird.multiplayerquiztrivia.TOURNAMENT.SERVER;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.LobbyActivity;

import java.util.EventListener;

public class HostTracker {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    ValueEventListener hostEventListener;
    Context context;
    String roomCode;

    public HostTracker(ValueEventListener hostEventListener, Context context, String roomCode) {
        this.hostEventListener = hostEventListener;
        this.context = context;
        this.roomCode = roomCode;
    }

    public void start(){
        hostEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    boolean isActive=snapshot.getValue(Boolean.class);

                    if(!isActive){
                        Toast.makeText(context, "HOST LEFT 1", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(context, "HOST LEFT 2", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").addValueEventListener(hostEventListener);

    }

}
