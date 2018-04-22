package com.example.hp.jsonparsing;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String fontpath="fonts/Quikhand.ttf";
        TextView txt=findViewById(R.id.Quikhand_ttf);
        Typeface tf=Typeface.createFromAsset(getAssets(),fontpath);
        txt.setTypeface(tf);
        int SPLASH_TIMEOUT = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(),FirstActivty.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
