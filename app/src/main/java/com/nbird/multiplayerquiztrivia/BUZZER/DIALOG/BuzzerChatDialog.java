package com.nbird.multiplayerquiztrivia.BUZZER.DIALOG;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.ChatAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.ChatHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class BuzzerChatDialog {

    Context context;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    String roomCode;
    RecyclerView recyclerview;

    String myName;
    ValueEventListener eventListenerChat;

    ArrayList<ChatHolder> chatHolderArrayList;
    ChatAdapter chatAdapter;


    public BuzzerChatDialog(Context context, String roomCode, String myName, ValueEventListener eventListenerChat) {
        this.context = context;
        this.roomCode=roomCode;
        this.myName=myName;
        this.eventListenerChat=eventListenerChat;
    }

    public void start(View v){
        AlertDialog.Builder builderFact=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.dialog_chat,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(false);

        final AlertDialog alertDialog=builderFact.create();

        recyclerview = (RecyclerView) viewFact.findViewById(R.id.recyclerview);
        Button cancelButton = (Button) viewFact.findViewById(R.id.cancelButton);

        EditText editText=(EditText) viewFact.findViewById(R.id.editText);
        Button sendButton=(Button) viewFact.findViewById(R.id.sendButton);


        chatHolderArrayList=new ArrayList<>();


        receiveMessage();

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        try{
            alertDialog.show();
        }catch (Exception e){

        }

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(recyclerview.VERTICAL);
        linearLayoutManager1.setStackFromEnd(true);
        //   linearLayoutManager1.setReverseLayout(true);

        recyclerview.setLayoutManager(linearLayoutManager1);
        chatAdapter = new ChatAdapter(context, chatHolderArrayList);
        recyclerview.setAdapter(chatAdapter);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    alertDialog.dismiss();
                }catch (Exception e){

                }
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=editText.getText().toString();
                if(message.equals("")){
                    Toast.makeText(context, "Null text cannot be send!", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(message);
                    editText.setText("");
                }
            }
        });


    }

    private void receiveMessage(){





        eventListenerChat=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatHolderArrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatHolder chatHolder=dataSnapshot.getValue(ChatHolder.class);
                    chatHolderArrayList.add(chatHolder);
                }
                recyclerview.smoothScrollToPosition(recyclerview.getAdapter().getItemCount());
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("BUZZER").child("CHAT").child(roomCode).addValueEventListener(eventListenerChat);

    }


    private void sendMessage(String message){
        String randomString = UUID.randomUUID().toString();
        Date date = new Date();
        long timeMilli = date.getTime();
        ChatHolder chatHolder = new ChatHolder(myName, message, timeMilli);
        table_user.child("BUZZER").child("CHAT").child(roomCode).child(String.valueOf(timeMilli)).setValue(chatHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });


    }



}
