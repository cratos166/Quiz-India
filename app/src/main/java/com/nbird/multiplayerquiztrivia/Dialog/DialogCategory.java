package com.nbird.multiplayerquiztrivia.Dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.nbird.multiplayerquiztrivia.R;

public class DialogCategory {
    Context context;
    View view;

    public DialogCategory(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public void start(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_category,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);



        final AlertDialog alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

    }

}
