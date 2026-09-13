 package com.example.ecoloop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity4 extends AppCompatActivity {

    private Button btnAddActivity;
    private Button btnLogout;
    private Button btnAIAdvisor;
    private Button btnCircular;
    private Button btnBeforeBuy;
    private Button btnRecycling;
    private Button btnChallenges;

    private ImageButton btnProfile;

    private TextView txtUserName;
    private TextView txtUserId;
    private TextView txtCarbonFootprint;
    private TextView txtEarnedPoints;

    private int userId;
    private String userName;
    private double carbonFootprint;
    private int earnedPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main4);

        getUserData();

        initializeViews();

        displayUserData();

        setupClickListeners();
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> logoutUser());
    }


    // =========================================================
    // GET USER DATA
    // =========================================================
    private void logoutUser() {

        // Login session/preferences clear
        getSharedPreferences("EcoLoopPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Login screen par wapas
        Intent intent = new Intent(MainActivity4.this, MainActivity.class);

        // Dashboard ko back stack se remove kar do
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }
    private void getUserData() {

        Intent intent = getIntent();

        userId = intent.getIntExtra(
                "USER_ID",
                -1
        );

        userName = intent.getStringExtra(
                "USER_NAME"
        );

        carbonFootprint = intent.getDoubleExtra(
                "CARBON_FOOTPRINT",
                0.0
        );

        earnedPoints = intent.getIntExtra(
                "EARNED_POINTS",
                0
        );


        // SharedPreferences fallback

        if (carbonFootprint == 0.0 &&
                earnedPoints == 0) {

            float savedCarbon =
                    getSharedPreferences(
                            "EcoLoopPrefs",
                            MODE_PRIVATE
                    ).getFloat(
                            "CARBON_FOOTPRINT",
                            0.0f
                    );

            int savedPoints =
                    getSharedPreferences(
                            "EcoLoopPrefs",
                            MODE_PRIVATE
                    ).getInt(
                            "EARNED_POINTS",
                            0
                    );

            carbonFootprint = savedCarbon;
            earnedPoints = savedPoints;
        }


        if (userName == null ||
                userName.isEmpty()) {

            userName = "User";
        }
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        btnAddActivity =
                findViewById(R.id.btnAddActivity);

        btnAIAdvisor =
                findViewById(R.id.btnAIAdvisor);

        btnCircular =
                findViewById(R.id.btnCircular);

        btnBeforeBuy =
                findViewById(R.id.btnBeforeBuy);

        btnRecycling =
                findViewById(R.id.btnRecycling);

        btnChallenges =
                findViewById(R.id.btnChallenges);

        btnProfile =
                findViewById(R.id.btnProfile);


        txtUserName =
                findViewById(R.id.txtUserName);

        txtUserId =
                findViewById(R.id.txtUserId);

        txtCarbonFootprint =
                findViewById(R.id.txtCarbonFootprint);

        txtEarnedPoints =
                findViewById(R.id.txtEarnedPoints);
    }


    // =========================================================
    // DISPLAY DATA
    // =========================================================

    private void displayUserData() {

        txtUserName.setText(
                userName
        );

        txtUserId.setText(
                "ID: " + userId
        );

        txtCarbonFootprint.setText(
                String.format(
                        "%.2f kg CO₂",
                        carbonFootprint
                )
        );

        txtEarnedPoints.setText(
                earnedPoints + " Points"
        );
    }


    // =========================================================
    // BUTTON LISTENERS
    // =========================================================

    private void setupClickListeners() {


        // =====================================================
        // ADD ACTIVITY - MAIN ACTIVITY 5
        // =====================================================

        btnAddActivity.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity4.this,
                            MainActivity5.class
                    );

            sendUserData(intent);

            startActivity(intent);
        });


        // =====================================================
        // AI ADVISOR - MAIN ACTIVITY 6
        // =====================================================

        btnAIAdvisor.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity4.this,
                            MainActivity6.class
                    );

            sendUserData(intent);

            startActivity(intent);
        });


        // =====================================================
        // CIRCULAR ACTIONS - MAIN ACTIVITY 7
        // =====================================================

        btnCircular.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity4.this,
                            MainActivity7.class
                    );

            sendUserData(intent);

            startActivity(intent);
        });


        // =====================================================
        // BEFORE YOU BUY - MAIN ACTIVITY 8
        // =====================================================

        btnBeforeBuy.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity4.this,
                    MainActivity8.class
            );


            startActivity(intent);
        });


        // =====================================================
        // RECYCLING MAP - MAIN ACTIVITY 9
        // =====================================================

        btnRecycling.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity4.this,
                            MainActivity9.class
                    );

            sendUserData(intent);

            startActivity(intent);
        });


        // =====================================================
        // ECO CHALLENGES - MAIN ACTIVITY 10
        // =====================================================

        btnChallenges.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity4.this,
                            MainActivity10.class
                    );

            sendUserData(intent);

            startActivity(intent);
        });


        // =====================================================
        // PROFILE
        // =====================================================

        btnProfile.setOnClickListener(v -> {

            Toast.makeText(
                    MainActivity4.this,
                    "Profile coming soon!",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }


    // =========================================================
    // SEND USER DATA
    // =========================================================

    private void sendUserData(Intent intent) {

        intent.putExtra(
                "USER_ID",
                userId
        );

        intent.putExtra(
                "USER_NAME",
                userName
        );

        intent.putExtra(
                "CARBON_FOOTPRINT",
                carbonFootprint
        );

        intent.putExtra(
                "EARNED_POINTS",
                earnedPoints
        );
    }
}

