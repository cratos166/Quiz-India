package com.nbird.multiplayerquiztrivia.BUZZER.BOT.DIALOG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.BUZZER.ACTIVTY.LobbyBuzzerActivity;
import com.nbird.multiplayerquiztrivia.BUZZER.DIALOG.BuzzerJoinWithPasswordDialog;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.FIREBASE.ConnectionStatus;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.FIREBASE.VS.RoomCodeGenerator;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.LobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.JoinWithPasswordDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerInfo;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Room;

public class BOTBuzzerDialogLocal {
    NativeAd NATIVE_ADS;
    AppData appData;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    int roomCodeInt;
    public void start(Context context, View view){

        appData=new AppData();


        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_model_2,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);


        LottieAnimationView imageIcon=(LottieAnimationView) view1.findViewById(R.id.imageIcon);
        TextView textTitle=(TextView) view1.findViewById(R.id.textTitle);
        Button createButton=(Button) view1 .findViewById(R.id.buttonYes);
        Button joinButton=(Button) view1.findViewById(R.id.buttonNo);


        imageIcon.setAnimation(R.raw.select_option);
        imageIcon.playAnimation();

        textTitle.setText("CREATE your own room, share the code and play with the team! Enjoy with friends and ace the quiz! \nAlready have a code? Click JOIN now!");

        joinButton.setText("JOIN");
        createButton.setText("CREATE");

        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)){
            MobileAds.initialize(context);
            AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);

                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = view1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                            NATIVE_ADS=nativeAd;
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }



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
               try{NATIVE_ADS.destroy();}catch (Exception e){}
               BuzzerJoinWithPasswordDialog joinWithPasswordDialog=new BuzzerJoinWithPasswordDialog(context,joinButton,NATIVE_ADS);
               joinWithPasswordDialog.start();
               alertDialog.dismiss();
           }
       });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{NATIVE_ADS.destroy();}catch (Exception e){}
                createRoom(context,alertDialog,NATIVE_ADS);
                try{ alertDialog.dismiss();}catch (Exception e){}
            }
        });





    }




    private void createRoom(Context context, AlertDialog alertDialog, NativeAd NATIVE_ADS){

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

        /*
        ACTIVE 1 : IN LOBBY
        ACTIVE 2 : IN QUIZ
        ACTIVE 3 : IN SCORE ACTIVITY
         */


        Room room =new Room(mAuth.getCurrentUser().getUid(),myName,1,myPicURL,String.valueOf(roomCodeInt),1,1,1,true,1,true);

        Dialog dialog = null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,context);
        supportAlertDialog.showLoadingDialog();


        ConnectionStatus connectionStatus=new ConnectionStatus();
//        connectionStatus.myStatusSetter();
        //  table_user.child("TOURNAMENT").child("ROOM").child(String.valueOf(roomCodeInt)).removeValue();
        table_user.child("BUZZER").child("ROOM").child(String.valueOf(roomCodeInt)).setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try{
                            LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);

                            PlayerInfo playerInfo = new PlayerInfo(leaderBoardHolder.getUsername(),leaderBoardHolder.getScore(),leaderBoardHolder.getTotalTime(),leaderBoardHolder.getCorrect(),leaderBoardHolder.getWrong(),leaderBoardHolder.getImageUrl(),leaderBoardHolder.getSumationScore(),true,1);


                            //        table_user.child("TOURNAMENT").child("PLAYERS").child(String.valueOf(roomCodeInt)).removeValue();
                            table_user.child("BUZZER").child("PLAYERS").child(String.valueOf(roomCodeInt)).child(mAuth.getCurrentUser().getUid()).setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    supportAlertDialog.dismissLoadingDialog();
                                    try{
                                        alertDialog.dismiss();
                                    }catch (Exception e){

                                    }

                                    try{NATIVE_ADS.destroy();}catch (Exception e){}

                                    connectionStatus.buzzerStatusSetter(String.valueOf(roomCodeInt));
                                    connectionStatus.buzzerMAINS_STATUS(String.valueOf(roomCodeInt));

                                    Intent intent=new Intent(context, LobbyBuzzerActivity.class);
                                    intent.putExtra("playerNum",1);
                                    intent.putExtra("roomCode",String.valueOf(roomCodeInt));
                                    intent.putExtra("hostName",myName);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }
                            });

                        }catch (Exception e){
                            String name=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context);
                            String imageURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context);
                            LeaderBoardHolder leaderBoardHolder=new LeaderBoardHolder(name,0,0,0,0,imageURL,0);

                            PlayerInfo playerInfo = new PlayerInfo(leaderBoardHolder.getUsername(),leaderBoardHolder.getScore(),leaderBoardHolder.getTotalTime(),leaderBoardHolder.getCorrect(),leaderBoardHolder.getWrong(),leaderBoardHolder.getImageUrl(),leaderBoardHolder.getSumationScore(),true,1);


                            table_user.child("BUZZER").child("PLAYERS").child(String.valueOf(roomCodeInt)).child(mAuth.getCurrentUser().getUid()).setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    supportAlertDialog.dismissLoadingDialog();
                                    try{
                                        alertDialog.dismiss();
                                    }catch (Exception e){

                                    }

                                    try{NATIVE_ADS.destroy();}catch (Exception e){}

                                    connectionStatus.buzzerStatusSetter(String.valueOf(roomCodeInt));
                                    connectionStatus.buzzerMAINS_STATUS(String.valueOf(roomCodeInt));

                                    Intent intent=new Intent(context, LobbyBuzzerActivity.class);
                                    intent.putExtra("playerNum",1);
                                    intent.putExtra("roomCode",String.valueOf(roomCodeInt));
                                    intent.putExtra("hostName",myName);
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
