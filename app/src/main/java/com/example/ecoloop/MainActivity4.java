package com.example.ecoloop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity4 extends AppCompatActivity {

    private Button btnAddActivity;
    private Button btnAIAdvisor;
    private Button btnCircular;
    private Button btnBeforeBuy;
    private Button btnRecycling;
    private Button btnChallenges;
    private Button btnAcceptRecommendation;

    private ImageButton btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main4);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {

        btnAddActivity = findViewById(R.id.btnAddActivity);
        btnAIAdvisor = findViewById(R.id.btnAIAdvisor);
        btnCircular = findViewById(R.id.btnCircular);
        btnBeforeBuy = findViewById(R.id.btnBeforeBuy);
        btnRecycling = findViewById(R.id.btnRecycling);
        btnChallenges = findViewById(R.id.btnChallenges);
        btnAcceptRecommendation = findViewById(R.id.btnAcceptRecommendation);

        btnProfile = findViewById(R.id.btnProfile);
    }

    private void setupClickListeners() {

        btnAddActivity.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity4.this, MainActivity4.class));
        });

        btnAIAdvisor.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity4.this, MainActivity4.class));
        });

        btnCircular.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity4.this, MainActivity4.class));
        });

        btnBeforeBuy.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity4.this, MainActivity4.class));
        });

        btnRecycling.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity4.this, MainActivity4.class));
        });

        btnChallenges.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity4.this, MainActivity4.class));
        });

        btnAcceptRecommendation.setOnClickListener(v -> {
            Toast.makeText(
                    MainActivity4.this,
                    "Recommendation accepted!",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity4.this, MainActivity.class));
        });
    }
}