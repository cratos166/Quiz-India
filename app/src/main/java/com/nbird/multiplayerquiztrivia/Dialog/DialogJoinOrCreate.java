package com.nbird.multiplayerquiztrivia.Dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.nbird.multiplayerquiztrivia.R;

public class DialogJoinOrCreate {

    public void start(Context context, View view, int quizMode){
        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.quit_asker_layout,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);


        LottieAnimationView imageIcon=(LottieAnimationView) view1.findViewById(R.id.imageIcon);
        TextView textTitle=(TextView) view1.findViewById(R.id.textTitle);
        Button joinButton=(Button) view1 .findViewById(R.id.buttonYes);
        Button createButton=(Button) view1.findViewById(R.id.buttonNo);


        imageIcon.setAnimation(R.raw.select_option);
        imageIcon.playAnimation();

        textTitle.setText("CREATE your own room, share the code and play with the team! Enjoy with friends and ace the quiz! \nAlready have a code? Click JOIN now!");

        joinButton.setText("JOIN");
        createButton.setText("CREATE");




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
               DialogJoinVS dialogJoinVS=new DialogJoinVS();
               dialogJoinVS.start(context,view,quizMode);
               alertDialog.dismiss();
           }
       });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogWaiterVS dialogWaiterVS=new DialogWaiterVS();
                dialogWaiterVS.start(context,view,quizMode);
                alertDialog.dismiss();
            }
        });





    }

}
