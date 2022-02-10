package com.nbird.multiplayerquiztrivia.Dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.common.SignInButton;
import com.nbird.multiplayerquiztrivia.R;

public class DialogSingleOrMulti {

    public void start(Context context,View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        View view1= LayoutInflater.from(context).inflate(R.layout.dialog_select_mode,(ConstraintLayout) view.findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);









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
