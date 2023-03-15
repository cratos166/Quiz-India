package com.nbird.multiplayerquiztrivia.BUZZER.BOT.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.BUZZER.ADAPTER.BuzzerResultAdapter;
import com.nbird.multiplayerquiztrivia.BUZZER.BOT.DIALOG.BOTBuzzerJoinCreateTournamentDialog;
import com.nbird.multiplayerquiztrivia.BUZZER.MODEL.BuzzerDataExchangeHolder;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.DataExchangeHolder;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY.BOTLobbyActivity;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.ResultAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.BOTJoinCreateTournamentDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.BasicDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class BOTBuzzerScoreActivity extends AppCompatActivity {
    String hostName;
    int maxQuestions;
    ValueEventListener resultEventListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ArrayList<BuzzerDataExchangeHolder> playerDataArrayList;
    int numberOfActivePlayer, myPlayerNum;

    BuzzerResultAdapter resultAdapter;
    RecyclerView recyclerView;

    LottieAnimationView party_popper;

    Button reMatch, joinOrCreateOtherRoom, quitButton;

    Boolean winnerDeclared = false, isHostActive;
    TextView dis;
    AdView mAdView;
    Boolean isDone=false;
    int numberofQuestion=1,gameMode=1,timeInt=1;
    String myName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botbuzzer_score);


        AppData appData=new AppData();


        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, BOTBuzzerScoreActivity.this);

        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, BOTBuzzerScoreActivity.this)){
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }



        maxQuestions = getIntent().getIntExtra("maxQuestions", 10);
        myPlayerNum = getIntent().getIntExtra("playerNum", 1);
        hostName= getIntent().getStringExtra("hostName");
        numberOfActivePlayer=getIntent().getIntExtra("numberOfActivePlayer",2);

        numberofQuestion=getIntent().getIntExtra("numberOfQuestions",1);
        timeInt=getIntent().getIntExtra("time",1);
        gameMode=getIntent().getIntExtra("gameMode",1);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        party_popper = (LottieAnimationView) findViewById(R.id.party_popper);
        reMatch = (Button) findViewById(R.id.reMatch);
        joinOrCreateOtherRoom = (Button) findViewById(R.id.joinOrCreateOtherRoom);
        quitButton = (Button) findViewById(R.id.quitButton);
        dis=(TextView) findViewById(R.id.dis);

        playerDataArrayList = new ArrayList<>();

        resultAdapter = new BuzzerResultAdapter(BOTBuzzerScoreActivity.this, playerDataArrayList, maxQuestions);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(resultAdapter);


        //   numberOfActivPlayers();
        resultGetter();


        reMatch.setEnabled(false);
        joinOrCreateOtherRoom.setVisibility(View.GONE);





        quitButton.setVisibility(View.GONE);
        reMatch.setVisibility(View.GONE);

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                quitScoreActivityActivity();

            }
        });


        reMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (myPlayerNum == 1) {
                    table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).removeValue();

                    roomEnter();

                } else {

                    Random random=new Random();
                    int ko=random.nextInt(15);
                    if (ko==14) {
                        BasicDialog basicDialog = new BasicDialog(BOTBuzzerScoreActivity.this, reMatch, "Host Started The Game", "Your host has started the game. Please join or create some other room.", "OKAY", R.raw.host_removed);
                        basicDialog.start();
                        joinOrCreateOtherRoom.setVisibility(View.VISIBLE);
                    }else if(ko==13){
                        BasicDialog basicDialog = new BasicDialog(BOTBuzzerScoreActivity.this, reMatch, "Room Dissolved", "Your host left the room because of which the room is dissolved. Please join or create some other room.", "OKAY", R.raw.host_removed);
                        basicDialog.start();
                        joinOrCreateOtherRoom.setVisibility(View.VISIBLE);
                    }else {
                        table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).removeValue();
                        roomEnter();

                    }
                }


            }
        });


        joinOrCreateOtherRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                table_user.child("BOT_TOURNAMENT").child("ANSWERS").child(mAuth.getCurrentUser().getUid()).removeValue();
                try{
                    table_user.child("BOT_TOURNAMENT").child("RESULT").child(mAuth.getCurrentUser().getUid()).removeEventListener(resultEventListener);
                }catch (Exception e){

                }

                BOTBuzzerJoinCreateTournamentDialog botBuzzerJoinCreateTournamentDialog = new BOTBuzzerJoinCreateTournamentDialog();
                botBuzzerJoinCreateTournamentDialog.start(BOTBuzzerScoreActivity.this, joinOrCreateOtherRoom);

            }
        });



    }

    private void roomEnter() {



        Random random=new Random();



        if(playerDataArrayList.size()==4){
            int i=0;
            while(i!=1){
                int kk=random.nextInt(playerDataArrayList.size());

                if(playerDataArrayList.get(kk).getMyNameString().equals(myName)){
                    Log.i("UID","GATE 1");
                }else{
                    if(playerDataArrayList.get(kk).getMyNameString().equals(hostName)){
                        Log.i("UID","GATE 2");
                    }else{
                        i++;
                        int finalI = i;
                        table_user.child("BOT_TOURNAMENT").child("RESULT").child(mAuth.getCurrentUser().getUid()).orderByChild("myNameString").equalTo(playerDataArrayList.get(kk).getMyNameString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try{
                                    String UID = null;
                                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                        UID=dataSnapshot.getKey();
                                    }



                                    Log.i("UID",UID);

                                    table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).child(UID).removeValue();

                                    if(finalI ==1){
                                        intentFun(playerDataArrayList.size()-1);
                                    }


                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        }else{
            intentFun(playerDataArrayList.size() - 2);
        }






    }


    public void intentFun(int i){
        Intent intent=new Intent(BOTBuzzerScoreActivity.this, BOTLobbyBuzzerActivity.class);
        intent.putExtra("playerNum",myPlayerNum);
        intent.putExtra("hostName",hostName);

        if(playerDataArrayList.size()==4){
            intent.putExtra("numberOfPlayers",i);
        }else{
            intent.putExtra("numberOfPlayers",playerDataArrayList.size());
        }


        intent.putExtra("numberOfQuestions",numberofQuestion);



        intent.putExtra("time",timeInt);



        intent.putExtra("gameMode",gameMode);


        intent.putExtra("isRematch",1);


        startActivity(intent);
        finish();
    }





    private void resultGetter() {

        resultEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                playerDataArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    try{
                        BuzzerDataExchangeHolder dataExchangeHolder = dataSnapshot.getValue(BuzzerDataExchangeHolder.class);
                        playerDataArrayList.add(dataExchangeHolder);
                    }catch (Exception e){

                    }



                }

                dis.setText("Number of players completed the quiz : "+playerDataArrayList.size()+"/"+numberOfActivePlayer);


                if (numberOfActivePlayer == playerDataArrayList.size()) {

                    resultComparator();
                    TextView title=(TextView) findViewById(R.id.title);
                    title.setText("All the members completed the quiz.");
                    Collections.reverse(playerDataArrayList);
                    winnerDialog(playerDataArrayList.get(0).getMyPicURL(), playerDataArrayList.get(0).getMyNameString());

                    for (int i = 0; i < playerDataArrayList.size(); i++) {
                        playerDataArrayList.get(i).setPosition(i + 1);
                    }


                    resultAdapter.notifyDataSetChanged();
                } else {


                    resultComparator();
                    Collections.reverse(playerDataArrayList);
                    for (int i = 0; i < playerDataArrayList.size(); i++) {
                        playerDataArrayList.get(i).setPosition(i + 1);
                    }

                    resultAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        table_user.child("BOT_TOURNAMENT").child("RESULT").child(mAuth.getCurrentUser().getUid()).addValueEventListener(resultEventListener);
    }


    private void resultComparator() {

        Collections.sort(playerDataArrayList, new Comparator<BuzzerDataExchangeHolder>() {
            @Override
            public int compare(BuzzerDataExchangeHolder a1, BuzzerDataExchangeHolder a2) {
                return a1.getTotalScore() - a2.getTotalScore();
            }
        });

    }


    private void winnerDialog(String imageURL, String nameStr) {

        winnerDeclared = true;

        reMatch.setEnabled(true);
        reMatch.setVisibility(View.VISIBLE);
        quitButton.setVisibility(View.VISIBLE);
        isDone=true;


        try {
            table_user.child("BOT_TOURNAMENT").child("RESULT").child(mAuth.getCurrentUser().getUid()).removeEventListener(resultEventListener);
        } catch (Exception e) {
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(BOTBuzzerScoreActivity.this, R.style.AlertDialogTheme);

        View view1 = LayoutInflater.from(BOTBuzzerScoreActivity.this).inflate(R.layout.dialog_tournament_winner, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);

        ImageView imageIcon = (ImageView) view1.findViewById(R.id.imageIcon);
        TextView name = (TextView) view1.findViewById(R.id.name);
        TextView textTitle = (TextView) view1.findViewById(R.id.textTitle);

        Button okButton = (Button) view1.findViewById(R.id.okButton);

        MobileAds.initialize(BOTBuzzerScoreActivity.this);
        AdLoader adLoader = new AdLoader.Builder(BOTBuzzerScoreActivity.this, AppString.NATIVE_ID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        ColorDrawable cd = new ColorDrawable(0x393F4E);

                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                        TemplateView template = view1.findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }

        party_popper.setAnimation(R.raw.party_popper);
        party_popper.playAnimation();
        party_popper.loop(false);

        Glide.with(BOTBuzzerScoreActivity.this).load(imageURL).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(imageIcon);

        name.setText(nameStr);
        textTitle.setText(nameStr + " won the round.");


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    alertDialog.dismiss();
                } catch (Exception e) {

                }
            }
        });
    }


    public void quitScoreActivityActivity() {
        AlertDialog.Builder builderRemove = new AlertDialog.Builder(BOTBuzzerScoreActivity.this, R.style.AlertDialogTheme);
        View viewRemove1 = LayoutInflater.from(BOTBuzzerScoreActivity.this).inflate(R.layout.dialog_model_2, (ConstraintLayout) findViewById(R.id.layoutDialogContainer), false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);


        Button yesButton = (Button) viewRemove1.findViewById(R.id.buttonYes);
        Button noButton = (Button) viewRemove1.findViewById(R.id.buttonNo);

        TextView textTitle = (TextView) viewRemove1.findViewById(R.id.textTitle);


        if (myPlayerNum == 1) {
            textTitle.setText("You are the host. If you left the room, the whole room will be dissolved.\n\n You really want to quit ?");
        } else {
            textTitle.setText("You really want to quit ?");
        }


        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, BOTBuzzerScoreActivity.this)){

            MobileAds.initialize(BOTBuzzerScoreActivity.this);
            AdLoader adLoader = new AdLoader.Builder(BOTBuzzerScoreActivity.this, AppString.NATIVE_ID)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            ColorDrawable cd = new ColorDrawable(0x393F4E);

                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            TemplateView template = viewRemove1.findViewById(R.id.my_template);
                            template.setStyles(styles);
                            template.setNativeAd(nativeAd);
                            template.setVisibility(View.VISIBLE);
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());


        }



        LottieAnimationView anim = (LottieAnimationView) viewRemove1.findViewById(R.id.imageIcon);
        anim.setAnimation(R.raw.exit_lobby);
        anim.playAnimation();
        anim.loop(true);


        final AlertDialog alertDialog = builderRemove.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                intentMain();

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private void intentMain() {

        try {
            table_user.child("BOT_TOURNAMENT").child("RESULT").child(mAuth.getCurrentUser().getUid()).removeEventListener(resultEventListener);
        } catch (Exception e) {
        }

        Intent intent = new Intent(BOTBuzzerScoreActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void onBackPressed() {
        if(isDone){
            quitScoreActivityActivity();
        }else{
            Toast.makeText(this, "Please wait until the result has been shown.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        try{mAdView.destroy();}catch (Exception e){}
        Runtime.getRuntime().gc();

    }

}