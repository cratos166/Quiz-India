package com.nbird.multiplayerquiztrivia.SharePreferene;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AppData {

    public boolean getSharedPreferencesBollean(String stringA,String stringB, Context context){
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

    public void setSharedPreferencesString(String stringA,String stringB, Context context,String value){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(stringA, 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(stringB, value);
        editor.commit();
    }

    public String getSharedPreferencesString(String stringA,String stringB, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(stringA, 0);

        String value = sharedPreferences.getString(stringB,"");
        return value;
    }

}
