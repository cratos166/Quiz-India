package com.nbird.multiplayerquiztrivia.TOURNAMENT.EXTRA;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerDisplayInQuizHolder;

import java.util.ArrayList;

public class AnswerUploader {


    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    int position=0;
    String roomCode;
    String myName;
    String myURL;



    public AnswerUploader(String roomCode, String myName, String myURL) {
        this.roomCode = roomCode;
        this.myName = myName;
        this.myURL = myURL;
    }


    public void start(){

        PlayerDisplayInQuizHolder playerDisplayInQuizHolder=new PlayerDisplayInQuizHolder(myName,myURL);

        table_user.child("TOURNAMENT").child("ANSWERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).setValue(playerDisplayInQuizHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void upload(int ans){
        table_user.child("TOURNAMENT").child("ANSWERS").child(roomCode).child(mAuth.getCurrentUser().getUid()).child("arrayList").child(String.valueOf(position)).setValue(ans).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        position++;
    }


}
