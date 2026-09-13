package com.example.ecoloop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity6 extends AppCompatActivity {

    private TextView txtWelcome;
    private TextView txtCarbon;
    private TextView txtRecommendation;
    private TextView txtSaving;

    private Spinner spinnerActivity;
    private Spinner spinnerFrequency;

    private Button btnGetAdvice;
    private Button btnAcceptAction;

    private LinearLayout adviceCard;

    private int userId;
    private String userName;
    private double carbonFootprint;
    private int earnedPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main6);

        getUserData();

        initializeViews();

        displayUserData();

        setupSpinners();

        setupListeners();
    }


    // =========================================================
    // GET USER DATA
    // =========================================================

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


        // SharedPreferences backup

        SharedPreferences prefs =
                getSharedPreferences(
                        "EcoLoopPrefs",
                        MODE_PRIVATE
                );


        if (carbonFootprint == 0.0) {

            carbonFootprint =
                    prefs.getFloat(
                            "CARBON_FOOTPRINT",
                            0.0f
                    );
        }


        if (earnedPoints == 0) {

            earnedPoints =
                    prefs.getInt(
                            "EARNED_POINTS",
                            0
                    );
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

        txtWelcome =
                findViewById(R.id.txtWelcome);

        txtCarbon =
                findViewById(R.id.txtCarbon);

        txtRecommendation =
                findViewById(
                        R.id.txtRecommendation
                );

        txtSaving =
                findViewById(
                        R.id.txtSaving
                );


        spinnerActivity =
                findViewById(
                        R.id.spinnerActivity
                );

        spinnerFrequency =
                findViewById(
                        R.id.spinnerFrequency
                );


        btnGetAdvice =
                findViewById(
                        R.id.btnGetAdvice
                );

        btnAcceptAction =
                findViewById(
                        R.id.btnAcceptAction
                );


        adviceCard =
                findViewById(
                        R.id.adviceCard
                );
    }


    // =========================================================
    // DISPLAY USER DATA
    // =========================================================

    private void displayUserData() {

        txtWelcome.setText(
                "Hello " + userName + "! 🌱"
        );


        txtCarbon.setText(
                String.format(
                        "%.2f kg CO₂",
                        carbonFootprint
                )
        );
    }


    // =========================================================
    // SPINNERS
    // =========================================================

    private void setupSpinners() {

        String[] activities = {

                "Select activity",

                "🚗 Transport",

                "💡 Electricity",

                "🛍 Shopping",

                "🍔 Food",

                "♻ Waste & Recycling",

                "👕 Clothing",

                "📱 Electronics"

        };


        String[] frequencies = {

                "Select frequency",

                "Daily",

                "Several times a week",

                "Weekly",

                "Monthly",

                "Occasionally"

        };


        ArrayAdapter<String> activityAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        activities
                );


        ArrayAdapter<String> frequencyAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        frequencies
                );


        spinnerActivity.setAdapter(
                activityAdapter
        );

        spinnerFrequency.setAdapter(
                frequencyAdapter
        );
    }


    // =========================================================
    // BUTTON LISTENERS
    // =========================================================

    private void setupListeners() {

        btnGetAdvice.setOnClickListener(v -> {

            generateAIRecommendation();

        });


        btnAcceptAction.setOnClickListener(v -> {

            acceptRecommendation();

        });
    }


    // =========================================================
    // AI RECOMMENDATION ENGINE
    // =========================================================

    private void generateAIRecommendation() {

        String activity =
                spinnerActivity
                        .getSelectedItem()
                        .toString();


        String frequency =
                spinnerFrequency
                        .getSelectedItem()
                        .toString();


        if (activity.equals("Select activity")) {

            Toast.makeText(
                    this,
                    "Please select an activity.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        if (frequency.equals("Select frequency")) {

            Toast.makeText(
                    this,
                    "Please select frequency.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        String recommendation;

        double saving;


        // =====================================================
        // TRANSPORT
        // =====================================================

        if (activity.contains("Transport")) {

            if (frequency.equals("Daily")) {

                recommendation =
                        "Try replacing short car or bike trips "
                                + "with walking, cycling or public transport. "
                                + "For longer trips, consider carpooling.";

                saving = 1.8;

            } else {

                recommendation =
                        "For your regular trips, consider public "
                                + "transport or carpooling whenever possible. "
                                + "Walking for short distances can also help.";

                saving = 1.2;
            }
        }


        // =====================================================
        // ELECTRICITY
        // =====================================================

        else if (activity.contains("Electricity")) {

            if (frequency.equals("Daily")) {

                recommendation =
                        "Switch off lights, fans and appliances "
                                + "when they are not being used. Use LED bulbs "
                                + "and energy-efficient appliances.";

                saving = 1.5;

            } else {

                recommendation =
                        "Reduce unnecessary electricity usage and "
                                + "avoid keeping electronic devices on standby.";

                saving = 0.8;
            }
        }


        // =====================================================
        // SHOPPING
        // =====================================================

        else if (activity.contains("Shopping")) {

            recommendation =
                    "Before buying something new, check whether "
                            + "you can repair, reuse or buy a second-hand "
                            + "alternative. Prefer durable products.";

            saving = 1.0;
        }


        // =====================================================
        // FOOD
        // =====================================================

        else if (activity.contains("Food")) {

            recommendation =
                    "Reduce food waste by planning meals and storing "
                            + "food properly. Try adding more local and "
                            + "plant-based food to your diet.";

            saving = 1.3;
        }


        // =====================================================
        // WASTE
        // =====================================================

        else if (activity.contains("Waste")) {

            recommendation =
                    "Separate dry and wet waste. Recycle paper, "
                            + "plastic, metal and e-waste through authorized "
                            + "recycling facilities.";

            saving = 0.9;
        }


        // =====================================================
        // CLOTHING
        // =====================================================

        else if (activity.contains("Clothing")) {

            recommendation =
                    "Repair or reuse clothes before buying new ones. "
                            + "Donate unwanted clothes or choose second-hand "
                            + "options.";

            saving = 1.1;
        }


        // =====================================================
        // ELECTRONICS
        // =====================================================

        else {

            recommendation =
                    "Avoid replacing electronics unnecessarily. "
                            + "Repair devices, upgrade components when possible "
                            + "and recycle old electronics responsibly.";

            saving = 1.4;
        }


        // =====================================================
        // DISPLAY RESULT
        // =====================================================

        txtRecommendation.setText(
                recommendation
        );


        txtSaving.setText(
                "🌱 Estimated reduction: "
                        + String.format(
                        "%.2f kg CO₂",
                        saving
                )
                        + " per relevant period"
        );


        adviceCard.setVisibility(
                View.VISIBLE
        );


        // Save latest recommendation

        getSharedPreferences(
                "EcoLoopPrefs",
                MODE_PRIVATE
        )
                .edit()
                .putString(
                        "LAST_RECOMMENDATION",
                        recommendation
                )
                .putFloat(
                        "ESTIMATED_SAVING",
                        (float) saving
                )
                .apply();
    }


    // =========================================================
    // ACCEPT RECOMMENDATION
    // =========================================================

    private void acceptRecommendation() {

        earnedPoints += 0;


        getSharedPreferences(
                "EcoLoopPrefs",
                MODE_PRIVATE
        )
                .edit()
                .putInt(
                        "EARNED_POINTS",
                        earnedPoints
                )
                .apply();


        Toast.makeText(
                this,
                "🎉 Eco Action accepted! +20 points",
                Toast.LENGTH_LONG
        ).show();


        btnAcceptAction.setText(
                "✅ Action Accepted"
        );


        btnAcceptAction.setEnabled(
                false
        );
    }
}