package com.nbird.multiplayerquiztrivia.LL;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nbird.multiplayerquiztrivia.AppString;
import com.nbird.multiplayerquiztrivia.MAIN.MainActivity;
import com.nbird.multiplayerquiztrivia.Model.questionHolder;
import com.nbird.multiplayerquiztrivia.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LifeLine {

    int lifeLineSum=0;
    int manupulator1=0,manupulator=0;
    int selectNum=0;
    int yo1;
    int yo2;
    int yo3;
    int yo4;



    LinearLayout linearLayoutFiftyFifty,linearLayoutAudience,linearLayoutexpert;
    int position;
    List<questionHolder> list;
    Button option1,option2,option3,option4;
    String myName;
    Context context;

    public LifeLine(LinearLayout linearLayoutFiftyFifty, LinearLayout linearLayoutAudience, LinearLayout linearLayoutexpert, int position, List<questionHolder> list, Button option1, Button option2, Button option3, Button option4, String myName, Context context) {
        this.linearLayoutFiftyFifty = linearLayoutFiftyFifty;
        this.linearLayoutAudience = linearLayoutAudience;
        this.linearLayoutexpert = linearLayoutexpert;
        this.position = position;
        this.list = list;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.myName = myName;
        this.context = context;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void fiftyfiftyLL(){


                    linearLayoutFiftyFifty.setBackgroundResource(R.drawable.usedicon);
                    if (option1.getText().toString().equals(list.get(position).getCorrectAnswer())) {
                        manupulator1 = 1;
                    } else if (option2.getText().toString().equals(list.get(position).getCorrectAnswer())) {
                        manupulator1 = 2;
                    } else if (option3.getText().toString().equals(list.get(position).getCorrectAnswer())) {
                        manupulator1 = 3;
                    } else {
                        manupulator1 = 4;
                    }

                    Random rand3 = new Random();
                    int runStopper = 0;
                    while (runStopper == 0) {

                        selectNum = rand3.nextInt(4) + 1;

                        if (manupulator1 != selectNum) {
                            runStopper = 1;
                        }
                    }

                    switch (selectNum) {
                        case 1: switch (manupulator1) { case 2: option3.setText("");option4.setText("");break; case 3: option2.setText("");option4.setText("");break; case 4: option2.setText("");option3.setText("");break; }break;
                        case 2: switch (manupulator1) { case 1: option3.setText("");option4.setText("");break; case 3: option1.setText("");option4.setText("");break; case 4: option3.setText("");option1.setText("");break; }break;
                        case 3: switch (manupulator1) { case 1: option2.setText("");option4.setText("");break; case 2: option1.setText("");option4.setText("");break; case 4: option2.setText("");option1.setText("");break; }break;
                        case 4: switch (manupulator1) { case 1: option3.setText("");option2.setText("");break; case 2: option1.setText("");option3.setText("");break; case 3: option2.setText("");option1.setText("");break; }break;
                    }





    }

    public void audienceLL(){
        linearLayoutAudience.setBackgroundResource(R.drawable.usedicon);

        if (option1.getText().toString().equals(list.get(position).getCorrectAnswer())) {
            manupulator = 1;
        } else if (option2.getText().toString().equals(list.get(position).getCorrectAnswer())) {
            manupulator = 2;
        } else if (option3.getText().toString().equals(list.get(position).getCorrectAnswer())) {
            manupulator = 3;
        } else {
            manupulator = 4;
        }

        Random rand2 = new Random();


        int setMax = 100 - rand2.nextInt(60);

        switch (manupulator) {
            case 1: yo1 = setMax;yo2 = rand2.nextInt(100 - yo1);yo3 = rand2.nextInt(100 - yo1 - yo2);yo4 = 100 - yo1 - yo2 - yo3;break;
            case 2: yo2 = setMax;yo1 = rand2.nextInt(100 - yo2);yo3 = rand2.nextInt(100 - yo2 - yo1);yo4 = 100 - yo2 - yo1 - yo3;break;
            case 3: yo3 = setMax;yo2 = rand2.nextInt(100 - yo3);yo1 = rand2.nextInt(100 - yo3 - yo2);yo4 = 100 - yo3 - yo2 - yo1;break;
            case 4: yo4 = setMax;yo2 = rand2.nextInt(100 - yo4);yo1 = rand2.nextInt(100 - yo4 - yo2);yo3 = 100 - yo4 - yo2 - yo1;break;
        }



        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        final View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_audience_ll, (ConstraintLayout) linearLayoutAudience.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);
        ((TextView) view1.findViewById(R.id.textTitle)).setText(" MindScapers from across the world have casted their votes above. Choose your option! ");
        ((Button) view1.findViewById(R.id.buttonYes)).setText("OKAY");
        BarChart barChart = ((BarChart) view1.findViewById(R.id.barChart));


        final ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(1, yo1));
        visitors.add(new BarEntry(2, yo2));
        visitors.add(new BarEntry(3, yo3));
        visitors.add(new BarEntry(4, yo4));

//        AdView mAdView = view1.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        BarDataSet barDataSet = new BarDataSet(visitors, "Bar Data");
        barDataSet.setBarBorderColor(R.color.text_head);
        barDataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        barDataSet.setValueTextColor(Color.parseColor("#BFD1FF"));
        barDataSet.setValueTextSize(11f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Audience Poll");
        barChart.animateY(3000);


        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
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
    }

    public void swapTheQuestionLL(){

    }

    public void expertAdviceLL(){
        linearLayoutexpert.setBackgroundResource(R.drawable.usedicon);
        String answerByExpert=list.get(position).getCorrectAnswer();


        AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.AlertDialogTheme);

        final View view1= LayoutInflater.from(context).inflate(R.layout.dialog_expert_advice_ll,(ConstraintLayout) linearLayoutexpert.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);
        TextView titleText=((TextView) view1.findViewById(R.id.textTitle));

        ((TextView) view1.findViewById(R.id.textMessage)).setText(myName+" I feel you should go for  : '"+answerByExpert+"'");
        ((Button) view1.findViewById(R.id.buttonYes)).setText("OKAY");
        ImageView expertImage=((ImageView) view1.findViewById(R.id.imageIcon));

        expertAdviceImageManupulator(expertImage,titleText);


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
    }


    public void expertAdviceImageManupulator(ImageView expertImage, TextView titleText){
        Random rand = new Random();
        int num = rand.nextInt(11)+1;

        switch (num){
            case 1:               //If possible, avatars can match the facial descriptions
                expertImage.setBackgroundResource(R.drawable.expert1female);
                titleText.setText(" Dorjana Sirola: Highest woman scorer in World Quizzing Championship, Croatian linguist and anglicist! is Expert for the day");
                break;            //white complexion,short hair
            case 2:
                expertImage.setBackgroundResource(R.drawable.expert2male);
                titleText.setText("Dr.Neil deGrasse Tyson: Astrophysicist, Planetory scientist, Author and Science communicator! is Expert for the day");
                break;            //Dark complexion
            case 3:
                expertImage.setBackgroundResource(R.drawable.expert3male);
                titleText.setText("Kevin Ashman: Six times World Quizzing Championship winner and Five times British Quizing Champion! is Expert for the day");
                break;            //white complexion
            case 4:
                expertImage.setBackgroundResource(R.drawable.expert4male);
                titleText.setText("Derek O'Brian: Quiz Master, Indian politician and television personality! is Expert for the day");
                break;            //white complexion, spects
            case 5:
                expertImage.setBackgroundResource(R.drawable.expert5male);
                titleText.setText("Pat Gibson: Multiple World champion in quizzing, Software developer and professional Irish quizzer! is Expert for the day");
                break;             // white complexion, spectacles
            case 6:
                expertImage.setBackgroundResource(R.drawable.expert6female);
                titleText.setText("Elsie Kaufmann: Quiz mistress, Ghanaian academic and Biomedical engineer! is Expert for the day.");
                break;          //Dark complexion
            case 7:
                expertImage.setBackgroundResource(R.drawable.expert7male);
                titleText.setText("Olav Bjortomt: Four time World champion and English international quiz star player! is Expert for the day");
                break;          //White complexion
            case 8:
                expertImage.setBackgroundResource(R.drawable.expert8female);
                titleText.setText("Anne Hegerty: English quizzer and famous UK television personality! is Expert for the day");
                break;              //White complexion,short hair,fat face
            case 9:
                expertImage.setBackgroundResource(R.drawable.expert9female);
                titleText.setText("Seema Chari: Quiz mistress, author, anchor and knowledge media professional! is Expert for the day");
                break;          //curly hair
            case 10:
                expertImage.setBackgroundResource(R.drawable.expert10male);
                titleText.setText("Siddhartha Basu: Father of Indian television quizzing, producer-director and quiz show host! is Expert for the day");
                break;          //almost no hair,fair complexion
            case 11:
                expertImage.setBackgroundResource(R.drawable.expert11male);
                titleText.setText("Tom Trogh: Belgian quiz player and European quizzing champion! is Expert for the day");
                break;            //White complexion
            case 12:
                expertImage.setBackgroundResource(R.drawable.expert12male);
                titleText.setText("Ravi Avva: 2020 World Quizzing champion, Singaporean hailing from India and an Engineer! is Expert for the day");
                break;          //Fair complexion,spectacles

        }


    }


    public void LLUsed(String llName){

        AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.AlertDialogTheme);

        final View view1= LayoutInflater.from(context).inflate(R.layout.dialog_anim_text_button,(ConstraintLayout) linearLayoutAudience.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);
        ((TextView) view1.findViewById(R.id.textTitle)).setText("Oops! You Have Used Your "+llName+" Life Line Once.");
        ((Button) view1.findViewById(R.id.buttonYes)).setText("OKAY");


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
    }


}
