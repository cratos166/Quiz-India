package com.nbird.multiplayerquiztrivia.FIREBASE.VS;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.FACTS.slideAdapterMainMenuHorizontalSlide;

public class FirstDoneVs {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference myRef = database.getReference();


    String hostUID;
    String oppoUID;
    ValueEventListener valueEventListener;
    Context context;
    View v;
    WaitingDialog waitingDialog;
    NativeAd NATIVE_ADS;

    public FirstDoneVs(String hostUID, String oppoUID, ValueEventListener valueEventListener,Context context,View v,NativeAd NATIVE_ADS) {
        this.hostUID = hostUID;
        this.oppoUID = oppoUID;
        this.valueEventListener = valueEventListener;
        this.context=context;
        this.v=v;
        this.NATIVE_ADS=NATIVE_ADS;
    }

    public void start(){

        try{
            valueEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     try{
                         Boolean oppoIsCompleted=snapshot.getValue(Boolean.class);


                         if(oppoIsCompleted){
                             waitingDialog.stop();
                             table_user.child("VS_PLAY").child(hostUID).child("Status").child(oppoUID).child("isComplete").removeEventListener(valueEventListener);
                         }else{
                             waitingDialog=new WaitingDialog(context,v,NATIVE_ADS);
                             waitingDialog.start();
                         }
                     }catch (Exception e){
                         waitingDialog=new WaitingDialog(context,v,NATIVE_ADS);
                         waitingDialog.start();
                     }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            table_user.child("VS_PLAY").child(hostUID).child("Status").child(oppoUID).child("isComplete").addValueEventListener(valueEventListener);

        }catch (Exception e){

        }
    }

}
