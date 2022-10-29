package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.BOT.AvatarLink;
import com.nbird.multiplayerquiztrivia.BOT.BotDetails;
import com.nbird.multiplayerquiztrivia.BOT.VsBOTAudioQuiz;
import com.nbird.multiplayerquiztrivia.BOT.VsBOTNormalQuiz;
import com.nbird.multiplayerquiztrivia.BOT.VsBOTPictureQuiz;
import com.nbird.multiplayerquiztrivia.FACTS.mainMenuFactsHolder;
import com.nbird.multiplayerquiztrivia.FACTS.slideAdapterMainMenuHorizontalSlide;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.FIREBASE.VS.RoomCodeGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.BatchGenerator;
import com.nbird.multiplayerquiztrivia.GENERATORS.LevelGenerators;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DialogBotWaiterVS {


    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    ShimmerFrameLayout shimmerOppo,shimerFact;
    slideAdapterMainMenuHorizontalSlide sliderAdapter;
    DatabaseReference myRef = database.getReference();

    ViewPager slideViewPager;
    LinearLayout dotLayout;
    List<mainMenuFactsHolder> list;
    int num=0;
    TextView[] mDots;
    ActionBarDrawerToggle mToggle;

    Context context;
    private int currentPage;

    AppData appData;
    ImageView oppoImage;
    TextView oppoName,upperHeading;
    TextView highestScore;
    TextView totalTime;
    TextView oppoRatio;
    TextView oppoAccu;
    LinearLayout mainlinearLayout,roomCodeLinear;
    CardView oppoBatchCardView;
    ImageView oppoBatch;
    TextView oppoLevelText;
    Button cancelButton;

    DownloadTask task;
    CountDownTimer countDownTimerIntent,countDownTimerMain;

    int incrementer=0;
    int roomCodeInt;

    LeaderBoardHolder leaderBoardHolder;
    String oppoURL="";


    public void start (Context context, View view, int quizMode){
        this.context=context;

        appData = new AppData();

        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_waiter_onevsone,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);

        shimmerOppo=view1.findViewById(R.id.shimmerOppo);
        shimerFact=view1.findViewById(R.id.shimmerFact);

        shimerFact.startShimmerAnimation();
        shimmerOppo.startShimmerAnimation();

        slideViewPager=(ViewPager) view1.findViewById(R.id.slideViewPager);
        dotLayout=(LinearLayout) view1.findViewById(R.id.dotLayout);

        TextView myName=(TextView) view1.findViewById(R.id.myName);
        ImageView myImage=(ImageView) view1.findViewById(R.id.myImage);
        ImageView myBatch=(ImageView) view1.findViewById(R.id.myBatch);
        TextView myLevelText=(TextView) view1.findViewById(R.id.myLevelText);

        upperHeading=(TextView) view1.findViewById(R.id.upperHeading);

        upperHeading.setText("Finding opponent from around the world! Please wait for 5-20 seconds.");

        oppoImage=(ImageView) view1.findViewById(R.id.oppoImage);
        oppoName=(TextView) view1.findViewById(R.id.oppoName);

        highestScore=(TextView) view1.findViewById(R.id.highestScore);
        totalTime=(TextView) view1.findViewById(R.id.totalTime);
        oppoRatio=(TextView) view1.findViewById(R.id.oppoRatio);
        oppoAccu=(TextView) view1.findViewById(R.id.oppoAccu);

        mainlinearLayout=(LinearLayout) view1.findViewById(R.id.mainlinearLayout);
        roomCodeLinear=(LinearLayout) view1.findViewById(R.id.roomCodeLinear);

        oppoBatchCardView=(CardView) view1.findViewById(R.id.oppoBatchCardView);
        oppoBatch=(ImageView) view1.findViewById(R.id.oppoBatch);
        oppoLevelText=(TextView) view1.findViewById(R.id.oppoLevelText);

        cancelButton=(Button) view1.findViewById(R.id.cancelButton);

        CardView share=(CardView) view1.findViewById(R.id.share);


        roomCodeLinear.setVisibility(View.GONE);


        final AlertDialog alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

        String myNameStr=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plane");
                String shareBody=myNameStr+" Has Created A Room To Play With You.\n"+"Here's Your Room Code : "+roomCodeInt+".";
                String sharesub="Multiplayer Quiz Trivia";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                context.startActivity(Intent.createChooser(shareIntent,"Room Code"));
            }
        });



        myName.setText(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, context));
        Glide.with(context).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,context)).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(myImage);


        myBatchSetter(myBatch,myLevelText);

        for(int i=1;i<=3;i++){
            dataForHorizontalSlide(context);
        }


        Random random = new Random();
        int randomTimer=random.nextInt(10)+3;

        countDownTimerIntent=new CountDownTimer(randomTimer*1000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                shimmerOppo.stopShimmerAnimation();
                shimmerOppo.setVisibility(View.GONE);

                BotDetails botDetails=new BotDetails(oppoName,oppoImage,highestScore,totalTime,oppoRatio,oppoAccu,oppoBatchCardView,oppoBatch,oppoLevelText,mainlinearLayout,context);
                botDetails.start();

                cancelButton.setTextSize(10f);
                cancelButton.setEnabled(false);

                getImage();

                countDownTimerMain=new CountDownTimer(10*1000,1000) {
                    @Override
                    public void onTick(long l) {
                        cancelButton.setText("Game Starts In " + l / 1000);
                    }

                    @Override
                    public void onFinish() {

                        if(quizMode==1){
                            try{
                                alertDialog.dismiss();
                            }catch (Exception e){

                            }
                            Intent intent=new Intent(context, VsBOTPictureQuiz.class);
                            intent.putExtra("oppoName",oppoName.getText().toString());
                            intent.putExtra("oppoImageURL",oppoURL);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }else if(quizMode==2){
                            try{
                                alertDialog.dismiss();
                            }catch (Exception e){

                            }
                            Intent intent=new Intent(context,VsBOTNormalQuiz.class);
                            intent.putExtra("oppoName",oppoName.getText().toString());
                            intent.putExtra("oppoImageURL",oppoURL);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }else if(quizMode==3){
                            try{
                                alertDialog.dismiss();
                            }catch (Exception e){

                            }
                            Intent intent=new Intent(context, VsBOTAudioQuiz.class);
                            intent.putExtra("oppoName",oppoName.getText().toString());
                            intent.putExtra("oppoImageURL",oppoURL);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }else{

                        }



                    }
                }.start();

            }
        }.start();




    }


    public void getImage(){
        Random r= new Random();
        int  tom=r.nextInt(25);
        AvatarLink avatarLink=new AvatarLink();

        int tt=r.nextInt(20)+1;



        if(tt<9){


            oppoURL= avatarLink.getArr().get(tom);
            Glide.with(context)
                    .load(oppoURL)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(oppoImage);

        }else if(tt==13){


            oppoURL= "https://i.pravatar.cc/100";
            Glide.with(context)
                    .load(oppoURL)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(oppoImage);

        }else{
            task = new DownloadTask();
            task.execute("https://randomuser.me/api/?format=JSON");
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


                Log.i("arr",String.valueOf(arr));





                 oppoURL=jsonObject.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("picture")
                        .optString("medium");



                Glide.with(context)
                        .load(oppoURL)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                        .into(oppoImage);




            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(context, "Data Not Available", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void myBatchSetter(ImageView myBatch, TextView myLevelText){
        table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).child("sumationScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    int summationScore=snapshot.getValue(Integer.class);
                    BatchGenerator batchGenerator=new BatchGenerator(summationScore,myBatch);
                    batchGenerator.start();
                    LevelGenerators levelGenerators=new LevelGenerators(summationScore,myLevelText);
                    levelGenerators.start();
                }catch (Exception e){
                    BatchGenerator batchGenerator=new BatchGenerator(0,myBatch);
                    batchGenerator.start();
                    LevelGenerators levelGenerators=new LevelGenerators(0,myLevelText);
                    levelGenerators.start();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void dataForHorizontalSlide(Context context){

        // create instance of Random class
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        int setRandomNumber;
        final int categoryRandomNumber = rand.nextInt(7)+1;
        if(categoryRandomNumber<=5||categoryRandomNumber==7){
            setRandomNumber = rand.nextInt(49)+1;
        }else{
            setRandomNumber = rand.nextInt(199)+1;
        }

        list=new ArrayList<>();

        myRef.child("Facts").child(String.valueOf(categoryRandomNumber)).child(String.valueOf(setRandomNumber)).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange( DataSnapshot snapshot) {

                list.add(snapshot.getValue(mainMenuFactsHolder.class));
                num++;


                if(num==3){
                    shimerFact.stopShimmerAnimation();
                    shimerFact.setVisibility(View.GONE);
                    AdapterManupulation(context);
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context,"Facts Data Can't be Loaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void AdapterManupulation(Context context){
        sliderAdapter=new slideAdapterMainMenuHorizontalSlide(context,list);
        slideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(viewListner);
        sliderAdapter.notifyDataSetChanged();
    }



    public void addDotsIndicator(int position){
        mDots=new TextView[3];
        dotLayout.removeAllViews();
        for(int i=0;i<mDots.length;i++){
            mDots[i]=new TextView(context);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(Color.parseColor("#555B68"));
            dotLayout.addView(mDots[i]);

        }
        if(mDots.length>0){
            mDots[position].setTextColor(Color.parseColor("#BFD1FF"));
        }
    }
    ViewPager.OnPageChangeListener viewListner=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage=position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };




}
