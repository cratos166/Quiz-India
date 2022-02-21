package com.nbird.multiplayerquiztrivia.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.nbird.multiplayerquiztrivia.QUIZ.NormalSingleQuiz;
import com.nbird.multiplayerquiztrivia.R;

public class DialogCategory {
    Context context;
    View view;

    CardView historicalMonuments,floraAndFauna,geography,astronmyAndSpace,sports,someSuperlatives,countriesCapitalsAndCurriencies,famousPersonalities;
    CardView science,importantDatesAndEvents,religionAndMythology,history,filmAndEntertainment,inventionAndDiscoveries,firstInDifferentFields;
    CardView festivalArtAndCulture,polityAndConstitution,literature,healthAndDiseases,miscellaneous;

    AlertDialog alertDialog;

    public DialogCategory(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public void start(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_category,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);


        historicalMonuments=(CardView) view1.findViewById(R.id.cardviewHistoricalMonuments);
        floraAndFauna=(CardView) view1.findViewById(R.id.cardviewFloraAndFauna);
        geography=(CardView) view1.findViewById(R.id.cardviewGeography);
        astronmyAndSpace=(CardView) view1.findViewById(R.id.cardviewAstronomyAndSpace);
        sports=(CardView) view1.findViewById(R.id.cardviewSports);
        someSuperlatives=(CardView) view1.findViewById(R.id.cardviewSomeSuperlatives);
        countriesCapitalsAndCurriencies=(CardView) view1.findViewById(R.id.cardviewConuntryCapitalAndCurrencies);
        famousPersonalities=(CardView) view1.findViewById(R.id.cardviewFamousPersonalities);
        science=(CardView) view1.findViewById(R.id.cardviewHealthAndDiseases);
        importantDatesAndEvents=(CardView) view1.findViewById(R.id.cardviewScience);
        religionAndMythology=(CardView) view1.findViewById(R.id.cardviewReligionAndMythology);
        history=(CardView) view1.findViewById(R.id.cardviewHistory);
        filmAndEntertainment=(CardView) view1.findViewById(R.id.cardviewFilmAndEntertainment);
        inventionAndDiscoveries=(CardView) view1.findViewById(R.id.cardviewInventionsAndDiscoveries);
        firstInDifferentFields=(CardView) view1.findViewById(R.id.cardviewFirstInDifferentFields);
        festivalArtAndCulture=(CardView) view1.findViewById(R.id.cardviewFestivalArtAndCulture);
        polityAndConstitution=(CardView) view1.findViewById(R.id.cardviewPolityAndConstitution);
        literature=(CardView) view1.findViewById(R.id.cardviewLiterature);
        healthAndDiseases=(CardView) view1.findViewById(R.id.cardviewHealthDiseases);
        miscellaneous=(CardView) view1.findViewById(R.id.cardviewMiscellaneous);

        ImageView cancel=(ImageView) view1.findViewById(R.id.cancel);




        alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        cardViewOnClick(historicalMonuments,1);cardViewOnClick(floraAndFauna,2);
        cardViewOnClick(geography,3);cardViewOnClick(astronmyAndSpace,4);
        cardViewOnClick(sports,5);cardViewOnClick(someSuperlatives,6);
        cardViewOnClick(countriesCapitalsAndCurriencies,7);cardViewOnClick(famousPersonalities,8);
        cardViewOnClick(science,9);cardViewOnClick(importantDatesAndEvents,10);
        cardViewOnClick(religionAndMythology,11);cardViewOnClick(history,12);
        cardViewOnClick(filmAndEntertainment,13);cardViewOnClick(inventionAndDiscoveries,14);
        cardViewOnClick(firstInDifferentFields,15);cardViewOnClick(festivalArtAndCulture,16);
        cardViewOnClick(polityAndConstitution,17);cardViewOnClick(literature,18);
        cardViewOnClick(healthAndDiseases,19);cardViewOnClick(miscellaneous,20);


    }


    public void cardViewOnClick(CardView cardView,int value) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NormalSingleQuiz.class);
                intent.putExtra("category", value);
                alertDialog.dismiss();
                view.getContext().startActivity(intent);
                ((Activity)view.getContext()).finish();
            }
        });
    }

}
