package com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
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
import com.nbird.multiplayerquiztrivia.BUZZER.ACTIVTY.BuzzerScoreActivity;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.FIREBASE.ConnectionStatus;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.RecyclerViewLeaderBoardAdapter;
import com.nbird.multiplayerquiztrivia.FIREBASE.VS.RoomCodeGenerator;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.LobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.RoomListAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.PlayerInfo;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Room;

import java.util.ArrayList;

public class JoinCreateTournamentDialoge {


    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    ShimmerFrameLayout shimmer;
    AppData appData;
    RecyclerView recyclerView;
    ArrayList<Room> list;
    RoomListAdapter categoryAdapter;
    TextView recyclerText;

    int roomCodeInt;

    public void start(Context context, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_tournament_join_create, (ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);


        Button createButton = (Button) view1.findViewById(R.id.createButton);
        Button joinButton = (Button) view1.findViewById(R.id.joinPrivateRoom);
        Button cancelButton=(Button) view1.findViewById(R.id.cancelButton);
        CardView refresh=(CardView) view1.findViewById(R.id.refresh);
        recyclerText=(TextView) view1.findViewById(R.id.recyclerText);

        shimmer=view1.findViewById(R.id.shimmer);
        recyclerView=view1.findViewById(R.id.recyclerView);

        shimmer.startShimmerAnimation();

        appData=new AppData();


        list=new ArrayList<>();

        NativeAd NATIVE_ADS=null;
        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, context)) {
            MobileAds.initialize(context);
            AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        NativeAd N_D=NATIVE_ADS;
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);
                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = view1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                            N_D=nativeAd;
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(recyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryAdapter = new RoomListAdapter(context,list);
        recyclerView.setAdapter(categoryAdapter);


        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }

        displayDataOnRecyclerView(context);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDataOnRecyclerView(context);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{NATIVE_ADS.destroy();}catch (Exception e){}

                try{
                    alertDialog.dismiss();
                }catch (Exception e){

                }
            }
        });


        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{NATIVE_ADS.destroy();}catch (Exception e){}
                JoinWithPasswordDialog joinWithPasswordDialog=new JoinWithPasswordDialog(context,joinButton);
                joinWithPasswordDialog.start();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 createRoom(context,alertDialog,NATIVE_ADS);
            }
        });

    }

    private void displayDataOnRecyclerView(Context context){
        Dialog dialog=null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,context);
        supportAlertDialog.showLoadingDialog();


        table_user.child("TOURNAMENT").child("ROOM").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                recyclerText.setVisibility(View.GONE);

                supportAlertDialog.dismissLoadingDialog();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    try{
                        Room room=dataSnapshot.getValue(Room.class);




                        if(room.isHostActive()){
                            if(room.isActive()==1){
                                if(room.isPrivacy()){
                                    if(room.getNumberOfPlayers()!=0 && room.getNumberOfPlayers()<AppString.TOURNAMENT_MAX_PLAYERS){
                                        list.add(room);
                                    }
                                }
                            }
                        }else{

                            table_user.child("TOURNAMENT").child("ROOM").child(String.valueOf(dataSnapshot.getKey())).removeValue();

                            table_user.child("TOURNAMENT").child("CHAT").child(String.valueOf(dataSnapshot.getKey())).removeValue();
//                            table_user.child("TOURNAMENT").child("PLAYERS").child(dataSnapshot.getKey()).removeValue();
//                            table_user.child("TOURNAMENT").child("RESULT").child(dataSnapshot.getKey()).removeValue();
//                            table_user.child("TOURNAMENT").child("QUESTIONS").child(dataSnapshot.getKey()).removeValue();
//                            table_user.child("TOURNAMENT").child("ANSWERS").child(dataSnapshot.getKey()).removeValue();
                        }

                    }catch (Exception e){
                        try{
                            table_user.child("TOURNAMENT").child("ROOM").child(String.valueOf(dataSnapshot.getKey())).removeValue();
                        }catch (Exception e1){

                        }
                    }




                }
                categoryAdapter.notifyDataSetChanged();

                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.GONE);

                if(list.size()==0){
                    recyclerText.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "No Rooms Available! Please Create one.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        roomCodeInt=roomCodeGenerator.start();


        Dialog dialog = null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,context);
        supportAlertDialog.showLoadingDialog();


        table_user.child("TOURNAMENT").child("ROOM").child(String.valueOf(roomCodeInt)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    Room room1=snapshot.getValue(Room.class);


                    if(room1==null){
                        Room room =new Room(mAuth.getCurrentUser().getUid(),myName,1,myPicURL,String.valueOf(roomCodeInt),1,1,1,true,1,true);



                        ConnectionStatus connectionStatus=new ConnectionStatus();
//        connectionStatus.myStatusSetter();
                        //  table_user.child("TOURNAMENT").child("ROOM").child(String.valueOf(roomCodeInt)).removeValue();
                        table_user.child("TOURNAMENT").child("ROOM").child(String.valueOf(roomCodeInt)).setValue(room).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        try{
                                            LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);

                                            PlayerInfo playerInfo = new PlayerInfo(leaderBoardHolder.getUsername(),leaderBoardHolder.getScore(),leaderBoardHolder.getTotalTime(),leaderBoardHolder.getCorrect(),leaderBoardHolder.getWrong(),leaderBoardHolder.getImageUrl(),leaderBoardHolder.getSumationScore(),true,1);


                                            //        table_user.child("TOURNAMENT").child("PLAYERS").child(String.valueOf(roomCodeInt)).removeValue();
                                            table_user.child("TOURNAMENT").child("PLAYERS").child(String.valueOf(roomCodeInt)).child(mAuth.getCurrentUser().getUid()).setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    supportAlertDialog.dismissLoadingDialog();
                                                    try{
                                                        alertDialog.dismiss();
                                                    }catch (Exception e){

                                                    }

                                                    try{NATIVE_ADS.destroy();}catch (Exception e){}

                                                    connectionStatus.tournamentStatusSetter(String.valueOf(roomCodeInt));
                                                    connectionStatus.tournamentMAINS_STATUS(String.valueOf(roomCodeInt));

                                                    Intent intent=new Intent(context, LobbyActivity.class);
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


                                            table_user.child("TOURNAMENT").child("PLAYERS").child(String.valueOf(roomCodeInt)).child(mAuth.getCurrentUser().getUid()).setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    supportAlertDialog.dismissLoadingDialog();
                                                    try{
                                                        alertDialog.dismiss();
                                                    }catch (Exception e){

                                                    }

                                                    try{NATIVE_ADS.destroy();}catch (Exception e){}

                                                    connectionStatus.tournamentStatusSetter(String.valueOf(roomCodeInt));
                                                    connectionStatus.tournamentMAINS_STATUS(String.valueOf(roomCodeInt));

                                                    Intent intent=new Intent(context, LobbyActivity.class);
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
                    }else{
                        supportAlertDialog.dismissLoadingDialog();
                        createRoom(context,alertDialog,NATIVE_ADS);
                    }




                }catch (Exception e){
                    supportAlertDialog.dismissLoadingDialog();
                    createRoom(context,alertDialog,NATIVE_ADS);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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






    }



}
