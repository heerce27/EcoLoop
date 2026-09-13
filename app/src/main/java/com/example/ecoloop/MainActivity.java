package com.example.ecoloop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =====================================================
        // CHECK LOGIN STATUS
        // =====================================================

        boolean loggedIn = getSharedPreferences(
                "EcoLoopPrefs",
                MODE_PRIVATE
        ).getBoolean("loggedIn", false);


        // =====================================================
        // USER ALREADY LOGGED IN
        // =====================================================

        if (loggedIn) {

            int userId = getSharedPreferences(
                    "EcoLoopPrefs",
                    MODE_PRIVATE
            ).getInt("USER_ID", -1);


            String userName = getSharedPreferences(
                    "EcoLoopPrefs",
                    MODE_PRIVATE
            ).getString("USER_NAME", "User");


            Intent intent = new Intent(
                    MainActivity.this,
                    MainActivity4.class
            );


            intent.putExtra(
                    "USER_ID",
                    userId
            );


            intent.putExtra(
                    "USER_NAME",
                    userName
            );


            startActivity(intent);

            finish();

            return;
        }


        // =====================================================
        // NORMAL HOME PAGE
        // =====================================================

        setContentView(R.layout.activity_main);


        // =====================================================
        // LOGIN BUTTON
        // =====================================================

        Button btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    MainActivity2.class
            );

            startActivity(intent);
        });
    }
}