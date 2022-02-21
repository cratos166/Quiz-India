package com.nbird.multiplayerquiztrivia.FIREBASE;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HighestScore {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    int highestScore=0;

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void start(){
        mAuth = FirebaseAuth.getInstance();
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("highestScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    highestScore= snapshot.getValue(Integer.class);
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HIGHEST SCORE ACTIVITY",error.getMessage());
                Log.e("HIGHEST SCORE ACTIVITY",error.getDetails());
            }
        });
    }


    public void upLoadHighestScore(int currentScore){
        mAuth = FirebaseAuth.getInstance();

            table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("highestScore").setValue(currentScore).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("HighestScore","HIGHEST SCORE UPLOADED");
                }
            });

    }

}
