package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.Model.OnlineDetailHolder;
import com.nbird.multiplayerquiztrivia.Model.PlayHolder;
import com.nbird.multiplayerquiztrivia.R;

public class DialogJoinVS {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    public void start(Context context, View view, int quizMode){
        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_join_layout,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);


        LottieAnimationView imageIcon=(LottieAnimationView) view1.findViewById(R.id.imageIcon);
        TextView textTitle=(TextView) view1.findViewById(R.id.textTitle);
        Button joinButton=(Button) view1 .findViewById(R.id.joinButton1);


        TextInputEditText passWordET=(TextInputEditText) view1.findViewById(R.id.username);



        imageIcon.setAnimation(R.raw.join_anim);
        imageIcon.playAnimation();





        final AlertDialog alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passWordET.getText().toString().equals("")){
                    passWordET.setError("Fields Cannot Be Empty");
                }else if(passWordET.getText().toString().length()>=6){
                    passWordET.setError("Field Length Should Be Less Than 6 Characters");
                }else{
                    joiner(context,passWordET.getText().toString(),passWordET);
                }
            }
        });


    }


    public void joiner(Context context, String password,TextInputEditText passWordET){
        Dialog dialog = null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,context);
        supportAlertDialog.showLoadingDialog();

        table_user.child("VS_ARENA").orderByChild("roomCode").equalTo(Integer.parseInt(password)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                OnlineDetailHolder onlineDetailHolder = null;
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    onlineDetailHolder=dataSnapshot1.getValue(OnlineDetailHolder.class);
                }

                try{

                    String oppoUID=onlineDetailHolder.getUID();
                    int mode=onlineDetailHolder.getMode();
                    int roomCode=onlineDetailHolder.getRoomCode();

                    table_user.child("VS_ARENA").child(oppoUID).removeValue();

                    PlayHolder playHolder=new PlayHolder(oppoUID,mAuth.getCurrentUser().getUid());
                    table_user.child("VS_PLAY").child(oppoUID).child("Personal").setValue(playHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    //TODO CODE MATCHED REMOVE THE OPPONENT FROM VS_ARENA

                }catch (Exception e){
                    passWordET.setError("No Room Present With This Password");
                    supportAlertDialog.dismissLoadingDialog();
                }
                

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}
