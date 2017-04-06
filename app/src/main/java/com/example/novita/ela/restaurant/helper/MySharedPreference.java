package com.example.novita.ela.restaurant.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by elaa on 02/04/17.
 */

public class MySharedPreference extends Application {

    private final String myPref = "myPrefs";
    private SharedPreferences sp;
    private Context _context;
    public SharedPreferences.Editor editor;

    public MySharedPreference(Context context) {
        this._context = context;
        this.sp = _context.getSharedPreferences(myPref, MODE_PRIVATE);
        this.editor = sp.edit();
    }

    public String getToken() {
        return sp.getString("token", null);
    }
}
