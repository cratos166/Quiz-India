package com.nbird.multiplayerquiztrivia.FIREBASE;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.R;

import java.util.ArrayList;

public class AnswerUploaderAndReceiver {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    int positionMy=0;
    int positionOppo=0;
    ValueEventListener vsReceiverListener;

    public void vsUpload(int ans){
        table_user.child("VS_PLAY").child("PlayerCurrentAns").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(positionMy)).setValue(ans).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        positionMy++;
    }

    public void vsReceiver(String oppoUID, ArrayList<LottieAnimationView> oppoAnimList) {


        try {


        vsReceiverListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {


                    if (snapshot.getValue(Integer.class) == 1) {
                        oppoAnimList.get(positionOppo).setAnimation(R.raw.tickanim);
                        oppoAnimList.get(positionOppo).playAnimation();
                        table_user.child("VS_PLAY").child("PlayerCurrentAns").child(oppoUID).child(String.valueOf(positionOppo)).removeEventListener(vsReceiverListener);
                        positionOppo++;
                        table_user.child("VS_PLAY").child("PlayerCurrentAns").child(oppoUID).child(String.valueOf(positionOppo)).addValueEventListener(vsReceiverListener);
                    } else if (snapshot.getValue(Integer.class) == 2) {
                        oppoAnimList.get(positionOppo).setAnimation(R.raw.wronganim);
                        oppoAnimList.get(positionOppo).playAnimation();
                        table_user.child("VS_PLAY").child("PlayerCurrentAns").child(oppoUID).child(String.valueOf(positionOppo)).removeEventListener(vsReceiverListener);
                        positionOppo++;
                        table_user.child("VS_PLAY").child("PlayerCurrentAns").child(oppoUID).child(String.valueOf(positionOppo)).addValueEventListener(vsReceiverListener);
                    }






                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("VS_PLAY").child("PlayerCurrentAns").child(oppoUID).child(String.valueOf(positionOppo)).addValueEventListener(vsReceiverListener);

        }catch (Exception e){

        }

    }




}
