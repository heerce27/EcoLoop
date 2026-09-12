package com.example.ecoloop; // Apne actual package name se match karein

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

// Splash Screen Activity ka class definition
public class sa extends AppCompatActivity {

    // Splash screen visible rehne ka time (millisecond me, 2500ms = 2.5 seconds)
    private static final int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Is activity ka UI layout set karein (make sure R.layout.activity_sa file available ho)
        setContentView(R.layout.activity_sa);

        // Nirdharit samay ke baad execute hone wala handler
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Splash screen ke baad redirected activity (is example me MainActivity hai)
                Intent intent = new Intent(sa.this, MainActivity.class);
                startActivity(intent);

                // Is initial activity ko close karein taki user wapas splash screen par na aa sake
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}