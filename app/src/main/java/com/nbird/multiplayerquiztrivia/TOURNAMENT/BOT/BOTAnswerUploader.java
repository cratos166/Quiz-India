package com.nbird.multiplayerquiztrivia.TOURNAMENT.BOT;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbird.multiplayerquiztrivia.BUZZER.MODEL.PlayerDisplayBuzzerHolder;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerDisplayInQuizHolder;

public class BOTAnswerUploader {



    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    int position=0;
    String myName;
    String myURL;
    String myUID;



    public BOTAnswerUploader( String myName, String myURL, String myUID) {
        this.myName = myName;
        this.myURL = myURL;
        this.myUID=myUID;
    }


    public void start(){

        PlayerDisplayInQuizHolder playerDisplayInQuizHolder=new PlayerDisplayInQuizHolder(myName,myURL);

        table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).child(myUID).setValue(playerDisplayInQuizHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void startBuzzer(){

        PlayerDisplayBuzzerHolder playerDisplayBuzzerHolder=new PlayerDisplayBuzzerHolder(myName,myURL,0);

        table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).child(myUID).setValue(playerDisplayBuzzerHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void uploadBuzzerScore(int score){
        table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).child(myUID).child("score").setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void upload(int ans){

        table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).child(myUID).child("arrayList").child(String.valueOf(position)).setValue(ans).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        position++;
    }
}
