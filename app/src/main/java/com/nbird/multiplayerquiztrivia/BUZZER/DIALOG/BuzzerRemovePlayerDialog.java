package com.nbird.multiplayerquiztrivia.BUZZER.DIALOG;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerRemoveAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;

import java.util.ArrayList;

public class BuzzerRemovePlayerDialog {

    Context context;


    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    String roomCode;
    RecyclerView recyclerView;

    PlayerRemoveAdapter myAdapter;



    public BuzzerRemovePlayerDialog(Context context, String roomCode) {
        this.context = context;
        this.roomCode=roomCode;
    }

    public void start(View v,ArrayList<Details> playerDataArrayList){
        AlertDialog.Builder builderFact=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.tournament_remove_player_layout,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(false);

        final AlertDialog alertDialog=builderFact.create();


        recyclerView = (RecyclerView) viewFact.findViewById(R.id.recyclerViewRemovePlayer);
        Button done=(Button) viewFact.findViewById(R.id.doneButton);

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        try{
            alertDialog.show();
        }catch (Exception e){

        }



        int[] arr=new int[playerDataArrayList.size()];



        myAdapter=new PlayerRemoveAdapter(context,playerDataArrayList,arr);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setAdapter(myAdapter);



        Button cancelButton=viewFact.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{alertDialog.dismiss();}catch (Exception e){}


            }
        });



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{alertDialog.dismiss();}catch (Exception e){}

           //     int tracker=0;

                for(int i=0;i<arr.length;i++){
                    if(arr[i]==1){
                   //    tracker++;
                        table_user.child("BUZZER").child("PLAYERS").child(roomCode).child(playerDataArrayList.get(i).getUid()).removeValue();
                    }

                }


            }
        });

    }




}
