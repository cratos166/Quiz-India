package com.nbird.multiplayerquiztrivia.NAVIGATION.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.Dialog.SupportAlertDialog;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.BarGroupHolder;
import com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER.LeaderBoardHolder;
import com.nbird.multiplayerquiztrivia.NAVIGATION.DIALOG.ChangeNameAndImageDialog;
import com.nbird.multiplayerquiztrivia.R;
import com.nbird.multiplayerquiztrivia.SharePreferene.AppData;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    CardView changeNameCardView;
    ImageView profileImage;
    TextView name;

    String usernameStr;
    String imageStr;
    AppData appData;

    TextView correctVsWrong,totalTimeTaken,totalQuizPlayed,accuracy;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("NEW_APP");
    DatabaseReference myRef = database.getReference();

    SupportAlertDialog supportAlertDialog;

    int lu=0,mu=0;
    CountDownTimer countDownTimer,countDownTimer1;
    ArrayList correctBar,wrongBar;
    BarDataSet barDataSet1,barDataSet2;
    BarChart barChart;
    String[] days = new String[]{"Sunday", "Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday"};
    int v=0,x=0;
    ArrayList<PieEntry> visitors1;
    PieChart pieChart;
    LineData lineData;
    List<Entry> entryList = new ArrayList<>();
    LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Dialog dialog=null;
        supportAlertDialog=new SupportAlertDialog(dialog,MyProfileActivity.this);
        supportAlertDialog.showLoadingDialog();

        changeNameCardView=(CardView) findViewById(R.id.changeNameCardView);
        profileImage=(ImageView) findViewById(R.id.profileImage);
        name=(TextView) findViewById(R.id.name);

        correctVsWrong=(TextView) findViewById(R.id.correctVsWrong);
        totalTimeTaken=(TextView) findViewById(R.id.totalTimeTaken);
        totalQuizPlayed=(TextView) findViewById(R.id.totalQuizPlayed);
        accuracy=(TextView) findViewById(R.id.accuracy);
        barChart = findViewById(R.id.idBarChart);

        pieChart=(PieChart) findViewById(R.id.pieChart);
        lineChart = findViewById(R.id.lineChart);
        mAuth = FirebaseAuth.getInstance();

        correctBar=new ArrayList();
        wrongBar=new ArrayList();
        visitors1=new ArrayList<>();

        appData = new AppData();
        usernameStr=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_NAME, MyProfileActivity.this);
        imageStr=appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MyProfileActivity.this);

        name.setText(usernameStr);
        Glide.with(MyProfileActivity.this).load(appData.getSharedPreferencesString(AppString.SP_MAIN,AppString.SP_MY_PIC,MyProfileActivity.this)).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(profileImage);


        changeNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChangeNameAndImageDialog changeNameAndImageDialog=new ChangeNameAndImageDialog(imageStr,usernameStr,MyProfileActivity.this,changeNameCardView,name,profileImage);
                changeNameAndImageDialog.start();

            }
        });
        textDataSetter();
        groupBarData();
//        barChart();
        pieCaller();
        linearChart();
    }


    public void linearChart(){

        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("LineGraph").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    int historyData = dataSnapshot1.getValue(Integer.class);

                    entryList.add(new Entry(x, historyData));
                    x++;

                }




                LineDataSet lineDataSet = new LineDataSet(entryList,"Quiz Score");
                lineDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                lineDataSet.setFillAlpha(110);
                lineData = new LineData(lineDataSet);
                lineChart.getDescription().setEnabled(false);
                lineChart.setData(lineData);
                lineChart.setVisibleXRangeMaximum(10);
                lineChart.moveViewToX(lineChart.getXChartMax());
                lineChart.invalidate();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pieCaller(){
        pieDataManu(1,"Historical Monuments");
        pieDataManu(2,"Flora And Fauna");
        pieDataManu(3,"Geography");
        pieDataManu(4,"Astronomy And Space");
        pieDataManu(5,"Sports");
        pieDataManu(6,"Some Superlatives");
        pieDataManu(7,"Country,Capitals And Currencies");
        pieDataManu(8,"Famous Personalities");
        pieDataManu(9,"Science");
        pieDataManu(10,"Important Dates And Events");
        pieDataManu(11,"Religion And Mythology");
        pieDataManu(12,"History");
        pieDataManu(13,"Film And Entertainment");
        pieDataManu(14,"Inventions And Discoveries");
        pieDataManu(15,"First In Different Fields");
        pieDataManu(16,"Festival,Art And Culture");
        pieDataManu(17,"Polity And Constitution");
        pieDataManu(18,"Literature");
        pieDataManu(19,"Health And Disease");
        pieDataManu(20,"Miscellaneous");


        countDownTimer1=new CountDownTimer(1000*60*60,1000) {
            @Override
            public void onTick(long l) {
                if(mu>=20){
                    pieChart();
                    countDownTimer1.cancel();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }

    public void pieDataManu(int i, final String category){
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("PieChart").child(String.valueOf(i)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    int i=snapshot.getValue(Integer.class);

                    visitors1.add(new PieEntry(i,category));
                }catch (Exception e){

                }
                mu++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void pieChart(){


        PieDataSet pieDataSet=new PieDataSet(visitors1,"Category Wise Total Number Of Times Played");
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setValueLineColor(R.color.black);
        pieDataSet.setFormSize(2);



        PieData pieData=new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.invalidate();         //Imporatant line showing pie chart
        pieChart.setEntryLabelTextSize(5);
        pieChart.setEntryLabelColor(R.color.black);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Times Played");
        pieChart.animate();
    }




    private void textDataSetter(){

        table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);

                    correctVsWrong.setText(leaderBoardHolder.getCorrect()+"/"+leaderBoardHolder.getWrong());
                    String timeStr=leaderBoardHolder.getTotalTime()/60+" Min "+leaderBoardHolder.getTotalTime()%60+" Sec ";
                    totalTimeTaken.setText(timeStr);
                    String quizPlayed=String.valueOf((leaderBoardHolder.getCorrect()+leaderBoardHolder.getWrong())/10);
                    totalQuizPlayed.setText(quizPlayed);
                    try{
                        int accuStr=(leaderBoardHolder.getCorrect()*100)/(leaderBoardHolder.getCorrect()+leaderBoardHolder.getWrong());
                        accuracy.setText(String.valueOf(accuStr)+"%");
                    }catch (Exception e){
                        accuracy.setText("0%");
                    }

                }catch (Exception e){
                    correctVsWrong.setText("0/0");
                    totalTimeTaken.setText("0 Min 0 Sec");
                    totalQuizPlayed.setText("0");
                    accuracy.setText("0%");
                }
                supportAlertDialog.dismissLoadingDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void groupBarData(){

        BarGroupDataGetter("Sunday",1);
        BarGroupDataGetter("Monday",2);
        BarGroupDataGetter("Tuesday",3);
        BarGroupDataGetter("Wednesday",4);
        BarGroupDataGetter("Thursday",5);
        BarGroupDataGetter("Friday",6);
        BarGroupDataGetter("Saturday",7);

            countDownTimer=new CountDownTimer(1000*60*60,1000) {
                @Override
                public void onTick(long l) {
                    if(lu==7){
                        barGroupChart();
                        countDownTimer.cancel();
                    }
                }

                @Override
                public void onFinish() {

                }
            }.start();


    }

    public void barGroupChart(){

        barDataSet1 = new BarDataSet(correctBar, "Correct");
        barDataSet1.setColor(getApplicationContext().getResources().getColor(R.color.black));
        barDataSet2 = new BarDataSet(wrongBar, "Wrong");
        barDataSet2.setColor(Color.BLUE);



        BarData data = new BarData(barDataSet1, barDataSet2);
        barDataSet1.setColors(ColorTemplate.LIBERTY_COLORS);
        barDataSet2.setColors(ColorTemplate.LIBERTY_COLORS);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextSize(5f);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(7);
        float barSpace = 0.1f;
        float groupSpace = 0.5f;
        data.setBarWidth(0.15f);
        data.setValueTextSize(5f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.animate();
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();
    }


  private void BarGroupDataGetter(String day,int i){
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("BarGroup").child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    BarGroupHolder barGroupHolder =new BarGroupHolder();
                    barGroupHolder=snapshot.getValue(BarGroupHolder.class);
                    int c=barGroupHolder.getCorrect();
                    int w=barGroupHolder.getWrong();
                    correctBar.add(new BarEntry(i, c));
                    wrongBar.add(new BarEntry(i, w));
                    lu++;


                }catch (Exception e){
                    correctBar.add(new BarEntry(i, 0));
                    wrongBar.add(new BarEntry(i, 0));
                    lu++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
  }

}