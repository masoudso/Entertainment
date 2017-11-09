package com.androidapplication.entertainmentmedia.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ahmed on 11/7/2017.
 */

public class Preference {

    SharedPreferences sharedPreferences;

    //The Constructor
    public Preference(Activity activity)
    {
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void setSearch(String search)
    {
        sharedPreferences.edit().putString("search", search).commit();
    }

    public String getSearch(){
        return sharedPreferences.getString("search", "batman");
    }

}


