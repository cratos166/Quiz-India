package com.nbird.multiplayerquiztrivia.BUZZER.DIALOG;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbird.multiplayerquiztrivia.R;

public class BuzzerPrivacyDialog {

    Context context;
    RadioGroup radioGroup;
    Boolean privacy;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    String roomCode;



    public BuzzerPrivacyDialog(Context context, String roomCode, Boolean privacy) {
        this.context = context;
        this.roomCode=roomCode;
        this.privacy=privacy;
    }

    public void start(View v){
        AlertDialog.Builder builderFact=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.dialog_privacy_setting,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(false);

        final AlertDialog alertDialog=builderFact.create();

        radioGroup = (RadioGroup) viewFact.findViewById(R.id.groupradio);
        Button done=(Button) viewFact.findViewById(R.id.buttonYes);

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        try{
            alertDialog.show();
        }catch (Exception e){

        }

        RadioButton radioButton1 = (RadioButton) radioGroup.findViewById(R.id.radio_public);
        RadioButton radioButton2 = (RadioButton) radioGroup.findViewById(R.id.radio_private);
        // Get the selected Radio Button
        if(privacy){
            radioButton1.setChecked(true);
            radioButton2.setChecked(false);
        }else{
            radioButton2.setChecked(true);
            radioButton1.setChecked(false);
        }

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId)
                    {

                    }
                });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When submit button is clicked,
                // Ge the Radio Button which is set
                // If no Radio Button is set, -1 will be returned
                final MediaPlayer musicNav;
                musicNav = MediaPlayer.create(context, R.raw.finalbuttonmusic);
                musicNav.start();
                musicNav.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        musicNav.reset();
                        musicNav.release();
                    }
                });
                int selectedId = radioGroup.getCheckedRadioButtonId();


                RadioButton radioButton = (RadioButton)radioGroup.findViewById(selectedId);

                if (selectedId == -1) {
                    Toast.makeText(context, "Select Any One Option", Toast.LENGTH_LONG);
                }else if(radioButton.getText().equals("Public")){
                    privacy=true;
                    privacyDataUploaderHost(true);
                    Toast.makeText(context, "Public", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else{
                    privacy=false;
                    privacyDataUploaderHost(false);
                    Toast.makeText(context, "Private", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }



            }
        });

    }


    private void privacyDataUploaderHost(Boolean action){
        table_user.child("BUZZER").child("ROOM").child(roomCode).child("privacy").setValue(action).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }


}
