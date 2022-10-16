package com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.nbird.multiplayerquiztrivia.FIREBASE.ConnectionStatus;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.LobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Room;

public class JoinWithPasswordDialog {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    Context context;
    View v;
    AppData appData;

    public JoinWithPasswordDialog(Context context, View v) {
        this.context = context;
        this.v = v;
    }

    public void start(){

        AlertDialog.Builder builderFact=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.dialog_join_layout,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(true);

        final AlertDialog alertDialog=builderFact.create();

        appData=new AppData();

        Button done=(Button) viewFact.findViewById(R.id.joinButton1);
        EditText codeEditText=(EditText) viewFact.findViewById(R.id.username);

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        try{
            alertDialog.show();
        }catch (Exception e){

        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String roomCode=codeEditText.getText().toString();

                if(roomCode.equals("")){
                    codeEditText.setError("Fields Cannot Be Empty");
                }else{
                    joiner(roomCode,codeEditText);
                }


            }
        });


    }


    private void joiner(String roomCode,EditText codeEditText){ 
        Dialog loadingDialog = null;
        SupportAlertDialog supportAlertDialog = new SupportAlertDialog(loadingDialog,context);
        supportAlertDialog.showLoadingDialog();


        ConnectionStatus connectionStatus=new ConnectionStatus();
        connectionStatus.myStatusSetter();








        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    Room room=snapshot.getValue(Room.class);


                    String hostUID=room.getHostUID();
                    Log.i("Host UID",hostUID);



                    if(room.getNumberOfPlayers()<AppString.TOURNAMENT_MAX_PLAYERS){
                        int numberOfPlayers=room.getNumberOfPlayers()+1;


                        table_user.child("TOURNAMENT").child("ROOM").child(roomCode).child("numberOfPlayers").setValue(numberOfPlayers).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {




                                table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        try{
                                            LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);

                                            table_user.child("TOURNAMENT").child("PLAYERS").child(String.valueOf(room.getRoomCode())).child(mAuth.getCurrentUser().getUid()).setValue(leaderBoardHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    supportAlertDialog.dismissLoadingDialog();
                                                    try{
                                                        supportAlertDialog.dismissLoadingDialog();
                                                    }catch (Exception e){

                                                    }

                                                    Intent intent=new Intent(context, LobbyActivity.class);
                                                    intent.putExtra("playerNum",2);
                                                    intent.putExtra("roomCode",String.valueOf(room.getRoomCode()));
                                                    intent.putExtra("hostName",room.getHostName());
                                                    context.startActivity(intent);
                                                    ((Activity) context).finish();
                                                }
                                            });

                                        }catch (Exception e){
                                            String name=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context);
                                            String imageURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context);
                                            LeaderBoardHolder leaderBoardHolder=new LeaderBoardHolder(name,0,0,0,0,imageURL,0);

                                            table_user.child("TOURNAMENT").child("PLAYERS").child(String.valueOf(room.getRoomCode())).child(mAuth.getCurrentUser().getUid()).setValue(leaderBoardHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    supportAlertDialog.dismissLoadingDialog();
                                                    try{
                                                        supportAlertDialog.dismissLoadingDialog();
                                                    }catch (Exception e){

                                                    }

                                                    Intent intent=new Intent(context, LobbyActivity.class);
                                                    intent.putExtra("playerNum",2);
                                                    intent.putExtra("roomCode",String.valueOf(room.getRoomCode()));
                                                    intent.putExtra("hostName",room.getHostName());
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






                }catch (Exception e){
                    supportAlertDialog.dismissLoadingDialog();
                    Log.i("error","error");
                    codeEditText.setError("No room available with this room code.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }






}
