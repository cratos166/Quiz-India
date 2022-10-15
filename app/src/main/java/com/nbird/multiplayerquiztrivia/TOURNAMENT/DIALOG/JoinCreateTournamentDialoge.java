package com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.FIREBASE.VS.RoomCodeGenerator;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.LobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Room;

public class JoinCreateTournamentDialoge {


    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    ShimmerFrameLayout shimmer;
    AppData appData;


    public void start(Context context, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_tournament_join_create, (ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);


        Button createButton = (Button) view1.findViewById(R.id.createButton);
        Button joinButton = (Button) view1.findViewById(R.id.joinPrivateRoom);

        shimmer=view1.findViewById(R.id.shimmer);

        shimmer.startShimmerAnimation();

        appData=new AppData();



        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }




        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinWithPasswordDialog joinWithPasswordDialog=new JoinWithPasswordDialog(context,joinButton);
                joinWithPasswordDialog.start();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 createRoom(context,alertDialog);
            }
        });

    }






    private void createRoom(Context context, AlertDialog alertDialog){

        String myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context);
        String myPicURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC, context);


        /*
        MODE 1 : NORMAL QUIZ
        MODE 2 : PICTURE QUIZ
        MODE 3 : NORMAL BUZZER
        MODE 4 : PICTURE BUZZER
         */




        RoomCodeGenerator roomCodeGenerator=new RoomCodeGenerator();
        int roomCodeInt=roomCodeGenerator.start();


        /*
        TIME 1 : 120 SEC
        TIME 2 : 180 SEC
        TIME 3 : 240 SEC
        TIME 4 : 300 SEC
         */


        /*
        NO. OF QUESTION 1 : 10 SEC
        NO. OF QUESTION 2 : 15 SEC
        NO. OF QUESTION 3 : 20 SEC
         */


        /*
        PRIVACY TRUE : PUBLIC
        PRIVACY FALSE : PRIVATE
         */


        Room room =new Room(mAuth.getCurrentUser().getUid(),myName,1,myPicURL,String.valueOf(roomCodeInt),1,1,1,true);

        Dialog dialog = null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,context);
        supportAlertDialog.showLoadingDialog();


        table_user.child("TOURNAMENT").child("ROOM").child(String.valueOf(roomCodeInt)).setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try{
                            LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);

                            table_user.child("TOURNAMENT").child("PLAYERS").child(String.valueOf(roomCodeInt)).child(mAuth.getCurrentUser().getUid()).setValue(leaderBoardHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    supportAlertDialog.dismissLoadingDialog();
                                    try{
                                        alertDialog.dismiss();
                                    }catch (Exception e){

                                    }

                                    Intent intent=new Intent(context, LobbyActivity.class);
                                    intent.putExtra("playerNum",1);
                                    intent.putExtra("roomCode",String.valueOf(roomCodeInt));
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }
                            });

                        }catch (Exception e){
                            String name=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context);
                            String imageURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context);
                            LeaderBoardHolder leaderBoardHolder=new LeaderBoardHolder(name,0,0,0,0,imageURL,0);

                            table_user.child("TOURNAMENT").child("PLAYERS").child(String.valueOf(roomCodeInt)).child(mAuth.getCurrentUser().getUid()).setValue(leaderBoardHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    supportAlertDialog.dismissLoadingDialog();
                                    try{
                                        alertDialog.dismiss();
                                    }catch (Exception e){

                                    }

                                    Intent intent=new Intent(context, LobbyActivity.class);
                                    intent.putExtra("playerNum",1);
                                    intent.putExtra("roomCode",String.valueOf(roomCodeInt));
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }
                            });

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });









            }
        });



    }



}
