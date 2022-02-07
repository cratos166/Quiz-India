package com.nbird.multiplayerquiztrivia.SharePreferene;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AppData {

    public boolean getSharedPreferences(String stringA,String stringB, Context context){
         SharedPreferences sharedPreferences = context.getSharedPreferences(stringA, 0);
         SharedPreferences.Editor editor = sharedPreferences.edit();

        Boolean value = sharedPreferences.getBoolean(stringB, false);

        if(value){
            return true;
        }else{
            editor.putBoolean(stringB, true);
            editor.apply();
            return false;
        }
    }
}
