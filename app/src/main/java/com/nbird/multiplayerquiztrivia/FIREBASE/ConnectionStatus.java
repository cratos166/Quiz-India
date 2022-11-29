package com.nbird.multiplayerquiztrivia.FIREBASE;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnectionStatus {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    ValueEventListener valueEventListener = null;
    ValueEventListener valueEventListenerHost = null;
    boolean oneTime=true;
    boolean oneTimeHost=true;


    ValueEventListener valueEventListenerBUZZER = null;
    ValueEventListener valueEventListenerHostBUZZER = null;
    boolean oneTimeBUZZER=true;
    boolean oneTimeHostBUZZER=true;

    public void myStatusSetter(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").onDisconnect().setValue(0);


                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void cancelConnectionStatus(){
        table_user.child("VS_CONNECTION").child(mAuth.getCurrentUser().getUid()).child("myStatus").onDisconnect().cancel();
    }


    public void buzzerStatusSetter(String roomCode){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");

        valueEventListenerBUZZER=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                    if(oneTimeBUZZER){
                        oneTimeBUZZER=false;
                        table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").onDisconnect().setValue(false);
                    }
                }
                else {
                    connectedRef.removeEventListener(valueEventListenerBUZZER);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        connectedRef.addValueEventListener(valueEventListenerBUZZER);

    }


    public void buzzerMAINS_STATUS(String roomCode){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");


        valueEventListenerHostBUZZER=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                    if(oneTimeHostBUZZER){
                        oneTimeHostBUZZER=false;
                        table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        table_user.child("BUZZER").child("ROOM").child(roomCode).child("hostActive").onDisconnect().setValue(false);
                    }


                }
                else {
                    connectedRef.removeEventListener(valueEventListenerHostBUZZER);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        connectedRef.addValueEventListener(valueEventListenerHostBUZZER);
    }


    public void tournamentStatusSetter(String roomCode){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");




        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    if(oneTime){
                        oneTime =false;
                        table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").onDisconnect().setValue(false);
                    }

                }
                else {
                    connectedRef.removeEventListener(valueEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        connectedRef.addValueEventListener(valueEventListener);


//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                if (connected) {
//                    table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                        }
//                    });
//                    table_user.child("TOURNAMENT").child("PLAYERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("active").onDisconnect().setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            connectedRef.removeEventListener();
//                        }
//                    });
//                }
//                else {
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }



    public void tournamentMAINS_STATUS(String roomCode){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");


        valueEventListenerHost=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    if(oneTimeHost){
                        oneTimeHost=false;
                        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("hostActive").onDisconnect().setValue(false);
                    }

                }
                else {
                    connectedRef.removeEventListener(valueEventListenerHost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        connectedRef.addValueEventListener(valueEventListenerHost);
    }


}
