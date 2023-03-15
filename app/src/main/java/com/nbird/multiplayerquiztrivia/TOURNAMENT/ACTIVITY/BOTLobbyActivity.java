package com.nbird.multiplayerquiztrivia.TOURNAMENT.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.Adapter.PlayerDataAdapter;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.BOT.BOTNameAndImage;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.BOTSettingDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.FactsDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.RemovePlayerDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.SettingDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.DIALOG.TroubleShootDialog;
import com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL.Details;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class BOTLobbyActivity extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    DatabaseReference myRef=database.getReference();


    int myPlayerNum=1;

    RecyclerView recyclerview;
    ArrayList<Details> playerDataArrayList;
    PlayerDataAdapter myAdapter;

    AppData appData;


    CardView factButton,cancelButton,settingButton,removePlayerButton,troubleshoot;


    String roomCode,hostNameStr;

    TextView privacyTextView,questionTextView,timeTextView,modeTextView,hostName,roomCodeTextView,numberOfPlayers;

    int numberofQuestion=1,gameMode=1,timeInt=1;
    Boolean privacy=true;


    Button startButton;

    String myName,myPicStr;


    CountDownTimer countDownTimer;

    ArrayList<Integer> listAns;

    NativeAd NATIVE_ADS;
    AdView mAdView;

    int numberOfPlayerInt=1,isRematch;

    String hostImage;

    CountDownTimer timerForNumberOfPlayers,timerForStarter;

    ValueEventListener valueEventListener;

    BOTSettingDialog settingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botlobby);

        recyclerview=(RecyclerView) findViewById(R.id.recyclerview);


        appData=new AppData();


        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, BOTLobbyActivity.this)){
            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        myName=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME,BOTLobbyActivity.this);
        myPicStr=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,BOTLobbyActivity.this);


        privacyTextView=(TextView) findViewById(R.id.privacyTextView);
        questionTextView=(TextView) findViewById(R.id.questionTextView);
        timeTextView=(TextView) findViewById(R.id.timeTextView);
        modeTextView=(TextView) findViewById(R.id.modeTextView);
        hostName=(TextView) findViewById(R.id.hostName);
        numberOfPlayers=(TextView) findViewById(R.id.numberOfPlayers);


        factButton=(CardView) findViewById(R.id.card1fact);
        settingButton=(CardView) findViewById(R.id.cardcancel);
        cancelButton=(CardView) findViewById(R.id.card1cancel);

        removePlayerButton=(CardView) findViewById(R.id.removePlayer);
        troubleshoot=(CardView) findViewById(R.id.troubleshoot);
        startButton=(Button) findViewById(R.id.startButton);



        listAns=new ArrayList<>();


        myPlayerNum=getIntent().getIntExtra("playerNum",1);
        hostNameStr=getIntent().getStringExtra("hostName");
        numberOfPlayerInt=getIntent().getIntExtra("numberOfPlayers",2);
        hostImage=getIntent().getStringExtra("hostImage");
        numberofQuestion=getIntent().getIntExtra("numberOfQuestions",1);
        timeInt=getIntent().getIntExtra("time",1);
        gameMode=getIntent().getIntExtra("gameMode",1);
        isRematch=getIntent().getIntExtra("isRematch",0);



        playerDataArrayList=new ArrayList<>();


        myAdapter=new PlayerDataAdapter(this,playerDataArrayList);
        recyclerview.setLayoutManager(new GridLayoutManager(this,2));
        recyclerview.setAdapter(myAdapter);



        //addingPlayers();




        settingDialog=new BOTSettingDialog(BOTLobbyActivity.this,modeTextView,timeTextView,questionTextView,gameMode,timeInt,numberofQuestion);


        hostName.setText(hostNameStr);

        if(myPlayerNum!=1){
            startButton.setTextSize(8.0f);
            startButton.setText("Waiting for Host to start the game");
            startButton.setEnabled(false);
            startButton.setAlpha(0.7f);

            settingButton.setVisibility(View.GONE);
            removePlayerButton.setVisibility(View.GONE);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                quitLobbyActivity();



            }
        });

        final int[] kl = {0};
        final int[] yo = {0};
        Random random=new Random();
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {





                try{
                    playerDataArrayList.clear();
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                        playerDataArrayList.add(dataSnapshot.getValue(Details.class));

                    }

                    if(playerDataArrayList.size()>10){
                        numberOfPlayers.setText(playerDataArrayList.size()+"/"+playerDataArrayList.size());
                    }else{
                        numberOfPlayers.setText(playerDataArrayList.size()+"/"+AppString.TOURNAMENT_MAX_PLAYERS);
                    }

                    myAdapter.notifyDataSetChanged();



                    if(isRematch==0){
                        if(kl[0] ==0){
                            kl[0]++;
                            if(myPlayerNum==1){
                                myAdder();
                            }else{
                                botDetailsAddition();
                            }

                        }
                    }



                    if(playerDataArrayList.size()==numberOfPlayerInt){
                        if(yo[0]==0){

                            yo[0]++;


                            int kj;
                            if(myPlayerNum==1){
                                kj=180;
                            }else{
                                kj=35;
                            }

                            timerForNumberOfPlayers=new CountDownTimer(1000*kj,1000) {
                                @Override
                                public void onTick(long l) {




                                    if(playerDataArrayList.size()<10){
                                        Boolean num=random.nextBoolean();
                                        if(num){
                                            Boolean isJoin=random.nextBoolean();
                                            if(isJoin){
                                                playerInfoAdder();
                                            }
                                        }
                                    }else{
                                        try{
                                            try{
                                                timerForNumberOfPlayers.cancel();
                                            }catch (Exception e){

                                            }


                                            if(myPlayerNum!=1){
                                             timerStarter();
                                            }


                                        }catch (Exception e){

                                        }

                                    }






                                }

                                @Override
                                public void onFinish() {
                                    timerStarter();
                                }
                            }.start();

                        }

                    }



                }catch (Exception e){


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).addValueEventListener(valueEventListener);





        factButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FactsDialog factsDialog=new FactsDialog(BOTLobbyActivity.this,NATIVE_ADS);
                factsDialog.start(factButton);


            }
        });

        troubleshoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TroubleShootDialog troubleShootDialog=new TroubleShootDialog(BOTLobbyActivity.this,NATIVE_ADS);
                troubleShootDialog.start(troubleshoot);
            }
        });



        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingDialog.start(settingButton,1);
            }
        });



        removePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Details> playerToBeRemoved=new ArrayList<>();


                for(int i=0;i<playerDataArrayList.size();i++){
                    if(!mAuth.getCurrentUser().getUid().equals(playerDataArrayList.get(i).getUid())){
                        playerToBeRemoved.add(playerDataArrayList.get(i));
                    }
                }


                RemovePlayerDialog removePlayerDialog=new RemovePlayerDialog(BOTLobbyActivity.this,roomCode);
                removePlayerDialog.start(removePlayerButton,playerToBeRemoved);

            }
        });



        if(privacy){
            privacyTextView.setText("PUBLIC");
        }else{
            privacyTextView.setText("PRIVATE");
        }

        if(numberofQuestion==1){
            questionTextView.setText("10");
        }else if(numberofQuestion==2){
            questionTextView.setText("15");
        }else{
            questionTextView.setText("20");
        }

        if(timeInt==1){
            timeTextView.setText("3 Mins");
        }else if(timeInt==2){
            timeTextView.setText("4.5 Mins");
        }else{
            timeTextView.setText("6 Mins");
        }

        if(gameMode==1){
            modeTextView.setText("Normal");
        }else if(gameMode==2){
            modeTextView.setText("Picture");
        }else if(gameMode==3){
            modeTextView.setText("Audio");
        }else{
            modeTextView.setText("Video");
        }



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            //    questionUploader();

                startButton.setTextSize(10.0f);

                startButton.setEnabled(false);
                startButton.setAlpha(0.7f);



                settingButton.setVisibility(View.GONE);
                removePlayerButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.INVISIBLE);

                try{
                    timerForNumberOfPlayers.cancel();
                }catch (Exception e){

                }

                timerStarter();


            }
        });






    }

    public void timerStarter(){

        timerForStarter=new CountDownTimer(1000*3,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                startButton.setTextSize(10.0f);

                startButton.setEnabled(false);
                startButton.setAlpha(0.7f);

                countDownTimer=new CountDownTimer(1000*10,1000) {
                    @Override
                    public void onTick(long l) {
                        startButton.setText("Game starts in "+l/1000);
                    }

                    @Override
                    public void onFinish() {
                        intentFunction();
                    }
                }.start();

            }
        }.start();
    }

    public void intentFunction(){

        try{
            table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).removeEventListener(valueEventListener);
        }catch (Exception e){

        }




        Intent intent = null;
        if(settingDialog.gameModeInt==1){
            intent=new Intent(BOTLobbyActivity.this,BOTTournamentNormalActivity.class);
        }else if(settingDialog.gameModeInt==2){
            intent=new Intent(BOTLobbyActivity.this,BOTTournamentPictureActivity.class);
        }else if(settingDialog.gameModeInt==3){
            intent=new Intent(BOTLobbyActivity.this,BOTTournamentAudioActivity.class);
        }

        intent.putExtra("playerNum",myPlayerNum);
        intent.putExtra("hostName",hostNameStr);

        intent.putExtra("numberOfPlayers",playerDataArrayList.size());

        if(settingDialog.numberOfQuestionsInt==1){
            intent.putExtra("numberOfQuestions",11);
        }else if(settingDialog.numberOfQuestionsInt==2){
            intent.putExtra("numberOfQuestions",16);
        }else{
            intent.putExtra("numberOfQuestions",21);
        }


        if(settingDialog.timeInt==1){
            intent.putExtra("time",180);
        }else if(settingDialog.timeInt==2){
            intent.putExtra("time",270);
        }else{
            intent.putExtra("time",360);
        }

        intent.putExtra("gameMode",settingDialog.gameModeInt);
        startActivity(intent);
        finish();
    }


    public void myAdder(){

        Dialog dialog=null;
        SupportAlertDialog supportAlertDialog=new SupportAlertDialog(dialog,BOTLobbyActivity.this);
        supportAlertDialog.showLoadingDialog();



        table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                supportAlertDialog.dismissLoadingDialog();

                try {
                    LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);
                    float acc=((leaderBoardHolder.getCorrect()*100)/(leaderBoardHolder.getWrong()+leaderBoardHolder.getCorrect()));

                    int min=leaderBoardHolder.getTotalTime()/60;
                    int sec=leaderBoardHolder.getTotalTime()%60;

                    String totalTime=min+" min "+sec+" sec";
                    String accStr=acc+"%";
                    String highestScore=String.valueOf(leaderBoardHolder.getScore());

                    Details hostDetails1=new Details(leaderBoardHolder.getImageUrl(),leaderBoardHolder.getUsername(),totalTime,accStr,highestScore,mAuth.getCurrentUser().getUid());

                    table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).child(mAuth.getCurrentUser().getUid()).setValue(hostDetails1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });



                }catch (Exception e){
                    AppData appData=new AppData();

                    String name=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, BOTLobbyActivity.this);
                    String imageURL=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,BOTLobbyActivity.this);
                    LeaderBoardHolder leaderBoardHolder=new LeaderBoardHolder(name,0,0,0,0,imageURL,0);

                    String totalTime="0 min 0 sec";
                    String accStr="0.00 %";
                    String highestScore="0";

                    Details hostDetails1=new Details(leaderBoardHolder.getImageUrl(),leaderBoardHolder.getUsername(),totalTime,accStr,highestScore,mAuth.getCurrentUser().getUid());
                    table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).child(mAuth.getCurrentUser().getUid()).setValue(hostDetails1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });


                }


                addingPlayers();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public void botDetailsAddition(){



        Random r=new Random();
        int score123=r.nextInt(90)+60;
        int timeHolder=r.nextInt(30000)+140;
        int y = 1;

        try{
            y=(timeHolder/120);
        }catch (Exception e){

        }

        int c1 =r.nextInt(8)+1;
        int correct=c1*y;
        int wrongfire=y*(10-c1);
        int total=correct+wrongfire;

        int cc=r.nextInt(80)+15;

        correct=(total*cc)/100;
        wrongfire=total-correct;

        int minutes=timeHolder/60;
        int sec=timeHolder%60;

        String totalTime=minutes+" min "+sec+" sec";


        int sumofq = correct + wrongfire;
        int jocker = (correct *100) / sumofq;


        long sumScore=(score123-30)*y;


        UUID uuid = UUID.randomUUID();
        Details hostDetails=new Details(hostImage,hostNameStr,totalTime,jocker+"%",String.valueOf(score123),String.valueOf(uuid));
        table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(uuid)).setValue(hostDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });





      myAdder();


    }

    public void playerInfoAdder(){
        Random r=new Random();
        int score123=r.nextInt(120)+100;
        int timeHolder=r.nextInt(30000)+140;
        int y = 1;

        try{
            y=(timeHolder/120);
        }catch (Exception e){

        }

        int c1 =r.nextInt(8)+1;
        int correct=c1*y;
        int wrongfire=y*(10-c1);
        int total=correct+wrongfire;

        int cc=r.nextInt(80)+15;

        correct=(total*cc)/100;
        wrongfire=total-correct;


        int minutes=timeHolder/60;
        int sec=timeHolder%60;

        String totalTime=minutes+" min "+sec+" sec";

        int sumofq = correct + wrongfire;
        int jocker = (correct *100) / sumofq;

        BOTNameAndImage botNameAndImage=new BOTNameAndImage();



        if(minutes%2==0){
            UUID uuid = UUID.randomUUID();
            Details hostDetails=new Details(botNameAndImage.getImage(),botNameAndImage.getName(),totalTime,jocker+"%",String.valueOf(score123),String.valueOf(uuid));

            table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(uuid)).setValue(hostDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });



        }else{

            boolean bb=r.nextBoolean();

            if(bb){


                boolean bn=r.nextBoolean();

                if(bn){
                    DownloadTask task = new DownloadTask();
                    task.execute("https://randomuser.me/api/?format=JSON");
                }else{
                    UUID uuid = UUID.randomUUID();
                    Details hostDetails=new Details(botNameAndImage.getImage(),botNameAndImage.getName(),totalTime,jocker+"%",String.valueOf(score123),String.valueOf(uuid));

                    table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(uuid)).setValue(hostDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }


            }else{


                UUID uuid = UUID.randomUUID();
                Details hostDetails=new Details(botNameAndImage.getImage(),botNameAndImage.getName(),totalTime,jocker+"%",String.valueOf(score123),String.valueOf(uuid));

                table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(uuid)).setValue(hostDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }


        }



    }

    public void addingPlayers(){
        for(int i=2;i<numberOfPlayerInt+1;i++){

           playerInfoAdder();

        }


    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {



                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("results");
                JSONArray arr = new JSONArray(weatherInfo);







                String oppoURL=jsonObject.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("picture")
                        .optString("medium");



                Random r=new Random();
                int score123=r.nextInt(80)+80;
                int timeHolder=r.nextInt(30000)+140;
                int y = 1;

                try{
                    y=(timeHolder/120);
                }catch (Exception e){

                }

                int c1 =r.nextInt(8)+1;
                int correct=c1*y;
                int wrongfire=y*(10-c1);
                int total=correct+wrongfire;

                int cc=r.nextInt(80)+15;

                correct=(total*cc)/100;
                wrongfire=total-correct;


                int minutes=timeHolder/60;
                int sec=timeHolder%60;

                String totalTime=minutes+" min "+sec+" sec";

                int sumofq = correct + wrongfire;
                int jocker = (correct *100) / sumofq;

                BOTNameAndImage botNameAndImage=new BOTNameAndImage();

                UUID uuid = UUID.randomUUID();

                Details hostDetails=new Details(oppoURL,botNameAndImage.getName(),totalTime,jocker+"%",String.valueOf(score123),String.valueOf(uuid));

                table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(uuid)).setValue(hostDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });



            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(BOTLobbyActivity.this, "Data Not Available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void quitLobbyActivity(){
        AlertDialog.Builder builderRemove=new AlertDialog.Builder(BOTLobbyActivity.this, R.style.AlertDialogTheme);
        View viewRemove1= LayoutInflater.from(BOTLobbyActivity.this).inflate(R.layout.dialog_model_2,(ConstraintLayout) findViewById(R.id.layoutDialogContainer),false);
        builderRemove.setView(viewRemove1);
        builderRemove.setCancelable(false);


        Button yesButton=(Button) viewRemove1.findViewById(R.id.buttonYes);
        Button noButton=(Button) viewRemove1.findViewById(R.id.buttonNo);

        TextView textTitle=(TextView) viewRemove1.findViewById(R.id.textTitle);

        if(myPlayerNum==1){
            textTitle.setText("You are the host. If you left the room, the whole room will be dissolved.\n\n You really want to quit ?");

        }else{
            textTitle.setText("You really want to quit ?");

        }

        LottieAnimationView anim=(LottieAnimationView)  viewRemove1.findViewById(R.id.imageIcon);
        anim.setAnimation(R.raw.exit_lobby);
        anim.playAnimation();
        anim.loop(true);

        AppData appData=new AppData();
        if(appData.getSharedPreferencesBoolean(AppString.SP_MAIN,AppString.SP_IS_SHOW_ADS, BOTLobbyActivity.this)){
            MobileAds.initialize(BOTLobbyActivity.this);
            AdLoader adLoader = new AdLoader.Builder(BOTLobbyActivity.this, AppString.NATIVE_ID)
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






        final AlertDialog alertDialog=builderRemove.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    table_user.child("BOT_TOURNAMENT").child("INFO").child(mAuth.getCurrentUser().getUid()).removeEventListener(valueEventListener);
                }catch (Exception e){

                }


                Intent intent=new Intent(BOTLobbyActivity.this, MainActivity.class);
                intent.putExtra("notificationOfHost",1);
                BOTLobbyActivity.this.startActivity(intent);
                ((Activity) BOTLobbyActivity.this).finish();



                try{
                    alertDialog.dismiss();
                }catch (Exception e){

                }

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    public void onBackPressed() {
        quitLobbyActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try{
            timerForNumberOfPlayers.cancel();
        }catch (Exception e){

        }

        try{
            timerForStarter.cancel();
        }catch (Exception e){

        }

        try{NATIVE_ADS.destroy();}catch (Exception e){}
        try{mAdView.destroy();}catch (Exception e){}
        Runtime.getRuntime().gc();

    }

}