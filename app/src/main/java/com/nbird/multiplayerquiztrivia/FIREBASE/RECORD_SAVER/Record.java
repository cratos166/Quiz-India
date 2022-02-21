package com.nbird.multiplayerquiztrivia.FIREBASE.RECORD_SAVER;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.multiplayerquiztrivia.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Record {

    int score,correctAns,category;
    Context context;
    CardView view;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("NEW_APP");
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    int timeTakenInt;
    String myNameString,myPicURL;

    public Record(int score, int correctAns, int category,Context context,CardView view,int timeTakenInt,String myNameString,String myPicURL) {
        this.score = score;
        this.correctAns=correctAns;
        this.category=category;
        this.context=context;
        this.view=view;
        this.timeTakenInt=timeTakenInt;
        this.myNameString=myNameString;
        this.myPicURL=myPicURL;
    }

    public Record(int score, int correctAns) {
        this.score = score;
        this.correctAns=correctAns;
    }

    public void LeaderBoardUploader(LeaderBoardHolder leaderBoardHolder){
        table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).setValue(leaderBoardHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void setLeaderBoard(){

        table_user.child("LeaderBoard").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    LeaderBoardHolder leaderBoardHolder=snapshot.getValue(LeaderBoardHolder.class);

                    if(score>leaderBoardHolder.getScore()){
                        leaderBoardHolder.setScore(score);
                    }
                    leaderBoardHolder.setScore(leaderBoardHolder.getScore()+timeTakenInt);
                    leaderBoardHolder.setCorrect(leaderBoardHolder.getScore()+correctAns);
                    leaderBoardHolder.setWrong(leaderBoardHolder.getWrong()+(10-correctAns));
                    leaderBoardHolder.setSumationScore(leaderBoardHolder.getSumationScore()+score);
                    LeaderBoardUploader(leaderBoardHolder);
                }catch (Exception e){
                    LeaderBoardHolder leaderBoardHolder=new LeaderBoardHolder(myNameString,score,timeTakenInt,correctAns,10-correctAns,myPicURL,score);
                    LeaderBoardUploader(leaderBoardHolder);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void getLeaderBoard(){



        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        final View view1= LayoutInflater.from(context).inflate(R.layout.one_vs_one_leaderboard,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);


        RecyclerView recyclerView=(RecyclerView) view1.findViewById(R.id.recyclerview);

        ShimmerFrameLayout mShimmerViewContainer=(ShimmerFrameLayout) view1.findViewById(R.id.shimmer_view_container);;

        mShimmerViewContainer.startShimmerAnimation();



        final AlertDialog alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        ArrayList<LeaderBoardHolder> list=new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(recyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerViewLeaderBoardAdapter categoryAdapter = new RecyclerViewLeaderBoardAdapter(list);
        recyclerView.setAdapter(categoryAdapter);


        table_user.child("LeaderBoard").orderByChild("score").limitToLast(50).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    list.add(dataSnapshot1.getValue(LeaderBoardHolder.class));
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }

                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void startLineGraph(){
        String key = table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("LineGraph").push().getKey();
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("LineGraph").child(key).setValue(score).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void startBarGroup(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        final String dayOfTheWeek = sdf.format(d);

        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("BarGroup").child(dayOfTheWeek).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    BarGroupHolder barGroupHolder=snapshot.getValue(BarGroupHolder.class);
                    int cA=barGroupHolder.getCorrect()+correctAns;
                    int wA=barGroupHolder.getWrong()+(10-correctAns);
                    BarGroupHolder b1=new BarGroupHolder(cA,wA);
                    table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("BarGroup").child(dayOfTheWeek).setValue(b1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                }catch (Exception e){
                    BarGroupHolder barGroupHolder=new BarGroupHolder(correctAns,10-correctAns);
                    table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("BarGroup").child(dayOfTheWeek).setValue(barGroupHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void startPieChart(){
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("PieChart").child(String.valueOf(category)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    int y=snapshot.getValue(Integer.class);
                    y=y+1;
                    table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("PieChart").child(String.valueOf(category)).setValue(y).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }catch (Exception e){
                    table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("PieChart").child(String.valueOf(category)).setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getLineGraph(LineChart lineChart){

        List<Entry> entryList = new ArrayList<>();
        table_user.child("User").child(mAuth.getCurrentUser().getUid()).child("MY_DATA").child("LineGraph").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num=0;
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    int score = dataSnapshot1.getValue(Integer.class);
                    entryList.add(new Entry(num,score));
                    num++;

                }

                LineDataSet lineDataSet = new LineDataSet(entryList,"Quiz Score");
                lineDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                lineDataSet.setFillAlpha(110);
                lineDataSet.setCircleColor(R.color.text_head);
                LineData lineData = new LineData(lineDataSet);
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





}
