package com.example.dryunchik.news.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.dryunchik.news.R;

public class NewsPreference {

    public static boolean getPreferenceTop(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForTop = context.getString(R.string.pref_choose_top_key);
        String defaultTop = context.getString(R.string.pref_choose_top_label_american);
        String preferredTop = sharedPreferences.getString(keyForTop,defaultTop);
        String ressians = context.getString(R.string.pref_choose_top_value_russians);

        boolean userPrefersAmerican;
        if (ressians.equals(preferredTop)){
            userPrefersAmerican = true;
        }else {
            userPrefersAmerican = false;
        }
      return userPrefersAmerican;
    }

    public static String setPreference(Context context){

        String choose;
        if (getPreferenceTop(context)){
            choose = "ru";
        }else {
            choose = "us";
        }
        return choose;
    }
}
