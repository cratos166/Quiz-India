package com.nbird.multiplayerquiztrivia.GENERATORS;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class QuestionGenerator {


    int quizMode,numberOfQuestion;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference myRef = database.getReference();


    public QuestionGenerator(int quizMode, int numberOfQuestion) {
        this.quizMode = quizMode;
        this.numberOfQuestion = numberOfQuestion;
    }

    public void start(){
        ArrayList<Integer> listAns = new ArrayList<>();

        switch (quizMode) {
            case 1:
                pictureQuizNumberUploader(listAns);
                break;
            case 2:
                normalQuizNumberUploader(listAns);
                break;
            case 3:
                audioQuizNumberUploader(listAns);
                break;
            case 4:
                vidioQuizNUmberUploader(listAns);
                break;
        }
    }



    public void normalQuizNumberUploader(ArrayList<Integer> listAns){
        Random random=new Random();
        for(int i=0;i<numberOfQuestion;i++){
            listAns.add(random.nextInt(6326)+1);
        }

        table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void pictureQuizNumberUploader(ArrayList<Integer> listAns){
        Random random=new Random();

        for(int i=0;i<numberOfQuestion;i++){
            int setNumber = random.nextInt(4999)+1;
            if(setNumber>1210&&setNumber<2000){
                setNumber=setNumber-1000;
            }
            listAns.add(setNumber);
        }
        table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void audioQuizNumberUploader(ArrayList<Integer> listAns){
        Random random=new Random();
        myRef.child("QUIZNUMBERS").child("AudioQuestionQuantity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num;
                try{
                    num=snapshot.getValue(Integer.class);
                }catch (Exception e){
                    num=156;
                }
                for(int i=0;i<numberOfQuestion;i++){
                    int setNumber = random.nextInt(num)+1;
                    listAns.add(setNumber);
                }

                table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void vidioQuizNUmberUploader(ArrayList<Integer> listAns){
        Random random=new Random();
        myRef.child("QUIZNUMBERS").child("VideoQuestionQuantity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num;
                try{
                    num=snapshot.getValue(Integer.class);
                }catch (Exception e){
                    num=118;
                }
                for(int i=0;i<numberOfQuestion;i++){
                    int setNumber = random.nextInt(num)+1;
                    listAns.add(setNumber);
                }

                table_user.child("VS_PLAY").child(mAuth.getCurrentUser().getUid()).child("Answers").setValue(listAns).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
