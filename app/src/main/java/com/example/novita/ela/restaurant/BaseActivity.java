package com.example.novita.ela.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class BaseActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    String TAG = "respon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            Log.d(TAG, "onResume: first time");
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.putExtra("status", "first");
            startActivity(intent);
            prefs.edit().putBoolean("firstrun", false).commit();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

}
