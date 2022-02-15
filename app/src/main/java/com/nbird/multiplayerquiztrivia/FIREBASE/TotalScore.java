package com.nbird.multiplayerquiztrivia.FIREBASE;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TotalScore {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    long totalScore=0;

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public void getSingleModeScore(){
        mAuth = FirebaseAuth.getInstance();
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("Single_Mode").child("totalScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    totalScore= snapshot.getValue(Long.class);
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TOTAL SCORE ACTIVITY",error.getMessage());
                Log.e("TOTAL SCORE ACTIVITY",error.getDetails());
            }
        });
    }

    public void setSingleModeScore(){
        mAuth = FirebaseAuth.getInstance();
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("Single_Mode").child("totalScore").setValue(totalScore).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


}
