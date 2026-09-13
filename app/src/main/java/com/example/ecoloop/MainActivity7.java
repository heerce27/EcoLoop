package com.example.ecoloop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity7 extends AppCompatActivity {

    // =========================================================
    // VIEWS
    // =========================================================

    private ImageButton btnBack;

    private TextView txtWelcome;
    private TextView txtActionTitle;
    private TextView txtActionDescription;
    private TextView txtCarbonSaving;
    private TextView txtPoints;

    private Spinner spinnerProduct;
    private Spinner spinnerAction;

    private Button btnAnalyze;
    private Button btnCompleteAction;

    private LinearLayout resultCard;


    // =========================================================
    // USER DATA
    // =========================================================

    private int userId;
    private String userName;

    private double carbonFootprint;

    private int earnedPoints;


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_main7
        );


        // Get user information
        getUserData();


        // Connect XML views
        initializeViews();


        // Display user information
        displayUserData();


        // Setup dropdowns
        setupSpinners();


        // Setup buttons
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


        carbonFootprint =
                intent.getDoubleExtra(
                        "CARBON_FOOTPRINT",
                        0.0
                );


        earnedPoints =
                intent.getIntExtra(
                        "EARNED_POINTS",
                        0
                );


        // =====================================================
        // SHARED PREFERENCES
        // =====================================================

        SharedPreferences prefs =
                getSharedPreferences(
                        "EcoLoopPrefs",
                        MODE_PRIVATE
                );


        // If carbon is not passed from Dashboard

        if (carbonFootprint == 0.0) {

            carbonFootprint =
                    prefs.getFloat(
                            "CARBON_FOOTPRINT",
                            0.0f
                    );
        }


        // If points are not passed from Dashboard

        if (earnedPoints == 0) {

            earnedPoints =
                    prefs.getInt(
                            "EARNED_POINTS",
                            0
                    );
        }


        // Default username

        if (userName == null ||
                userName.isEmpty()) {

            userName = "User";
        }
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        // Back button

        btnBack =
                findViewById(
                        R.id.btnBack
                );


        // TextViews

        txtWelcome =
                findViewById(
                        R.id.txtWelcome
                );


        txtActionTitle =
                findViewById(
                        R.id.txtActionTitle
                );


        txtActionDescription =
                findViewById(
                        R.id.txtActionDescription
                );


        txtCarbonSaving =
                findViewById(
                        R.id.txtCarbonSaving
                );


        txtPoints =
                findViewById(
                        R.id.txtPoints
                );


        // Spinners

        spinnerProduct =
                findViewById(
                        R.id.spinnerProduct
                );


        spinnerAction =
                findViewById(
                        R.id.spinnerAction
                );


        // Buttons

        btnAnalyze =
                findViewById(
                        R.id.btnAnalyze
                );


        btnCompleteAction =
                findViewById(
                        R.id.btnCompleteAction
                );


        // Result card

        resultCard =
                findViewById(
                        R.id.resultCard
                );
    }


    // =========================================================
    // DISPLAY USER DATA
    // =========================================================

    private void displayUserData() {

        txtWelcome.setText(
                "Hello " + userName + "! 🌱"
        );
    }


    // =========================================================
    // SETUP SPINNERS
    // =========================================================

    private void setupSpinners() {


        // =====================================================
        // PRODUCT LIST
        // =====================================================

        String[] products = {

                "Select product",

                "👕 Clothes",

                "📱 Electronics",

                "🪑 Furniture",

                "📚 Books",

                "🔌 Electrical Items",

                "🛍 Plastic Items",

                "🏠 Household Items"
        };


        // =====================================================
        // ACTION LIST
        // =====================================================

        String[] actions = {

                "Select action",

                "🔧 Repair",

                "♻ Reuse",

                "🎁 Donate",

                "🎨 Upcycle",

                "♻ Recycle",

                "🔄 Buy Second-Hand"
        };


        // =====================================================
        // PRODUCT ADAPTER
        // =====================================================

        ArrayAdapter<String> productAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        products
                );


        // =====================================================
        // ACTION ADAPTER
        // =====================================================

        ArrayAdapter<String> actionAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        actions
                );


        spinnerProduct.setAdapter(
                productAdapter
        );


        spinnerAction.setAdapter(
                actionAdapter
        );
    }


    // =========================================================
    // BUTTON LISTENERS
    // =========================================================

    private void setupListeners() {


        // =====================================================
        // BACK TO DASHBOARD
        // =====================================================

        btnBack.setOnClickListener(v -> {

            goBackToDashboard();

        });


        // =====================================================
        // GET RECOMMENDATION
        // =====================================================

        btnAnalyze.setOnClickListener(v -> {

            generateCircularRecommendation();

        });


        // =====================================================
        // COMPLETE ACTION
        // =====================================================

        btnCompleteAction.setOnClickListener(v -> {

            completeCircularAction();

        });
    }


    // =========================================================
    // BACK TO MAINACTIVITY4
    // =========================================================

    private void goBackToDashboard() {


        Intent intent =
                new Intent(
                        MainActivity7.this,
                        MainActivity4.class
                );


        // Pass user data back

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


        // =====================================================
        // CLEAR CURRENT ACTIVITY
        // =====================================================

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );


        startActivity(intent);


        // Close MainActivity7

        finish();
    }


    // =========================================================
    // GENERATE CIRCULAR RECOMMENDATION
    // =========================================================

    private void generateCircularRecommendation() {


        // Get selected product

        String product =
                spinnerProduct
                        .getSelectedItem()
                        .toString();


        // Get selected action

        String action =
                spinnerAction
                        .getSelectedItem()
                        .toString();


        // =====================================================
        // VALIDATE PRODUCT
        // =====================================================

        if (product.equals(
                "Select product"
        )) {

            Toast.makeText(
                    this,
                    "Please select a product.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =====================================================
        // VALIDATE ACTION
        // =====================================================

        if (action.equals(
                "Select action"
        )) {

            Toast.makeText(
                    this,
                    "Please select a circular action.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =====================================================
        // VARIABLES
        // =====================================================

        String title;

        String description;

        double saving;

        int points;


        // =====================================================
        // REPAIR
        // =====================================================

        if (action.contains(
                "Repair"
        )) {


            title =
                    "🔧 Repair Before Replacing";


            description =
                    "Instead of throwing away your "
                            + product
                            + ", try repairing it first. "
                            + "A local repair service can extend "
                            + "the product's useful life and reduce "
                            + "the need for a new product.";


            saving = 2.0;

            points = 30;
        }


        // =====================================================
        // REUSE
        // =====================================================

        else if (action.contains(
                "Reuse"
        )) {


            title =
                    "♻ Reuse the Product";


            description =
                    "Find another useful purpose for your "
                            + product
                            + ". Reusing an existing product "
                            + "reduces waste and avoids unnecessary "
                            + "new purchases.";


            saving = 1.5;

            points = 25;
        }


        // =====================================================
        // DONATE
        // =====================================================

        else if (action.contains(
                "Donate"
        )) {


            title =
                    "🎁 Donate Instead of Throwing Away";


            description =
                    "If your "
                            + product
                            + " is still usable, donate it to "
                            + "someone who needs it. This keeps "
                            + "the product in use and reduces waste.";


            saving = 1.8;

            points = 35;
        }


        // =====================================================
        // UPCYCLE
        // =====================================================

        else if (action.contains(
                "Upcycle"
        )) {


            title =
                    "🎨 Upcycle the Product";


            description =
                    "Turn your old "
                            + product
                            + " into something useful or creative. "
                            + "Upcycling gives waste materials a "
                            + "new purpose.";


            saving = 1.7;

            points = 30;
        }


        // =====================================================
        // RECYCLE
        // =====================================================

        else if (action.contains(
                "Recycle"
        )) {


            title =
                    "♻ Recycle Responsibly";


            description =
                    "Separate your "
                            + product
                            + " and send it to an authorized "
                            + "recycling facility. Never mix "
                            + "electronic or recyclable waste "
                            + "with general garbage.";


            saving = 1.2;

            points = 25;
        }


        // =====================================================
        // SECOND-HAND
        // =====================================================

        else {


            title =
                    "🔄 Choose Second-Hand";


            description =
                    "Before purchasing a new "
                            + product
                            + ", check second-hand or refurbished "
                            + "options. This can reduce resource "
                            + "consumption and product waste.";


            saving = 2.2;

            points = 40;
        }


        // =====================================================
        // DISPLAY ACTION TITLE
        // =====================================================

        txtActionTitle.setText(
                title
        );


        // =====================================================
        // DISPLAY DESCRIPTION
        // =====================================================

        txtActionDescription.setText(
                description
        );


        // =====================================================
        // DISPLAY CO2 SAVING
        // =====================================================

        txtCarbonSaving.setText(
                "🌱 Estimated CO₂ saving: "
                        + String.format(
                        "%.2f kg CO₂",
                        saving
                )
        );


        // =====================================================
        // DISPLAY POINTS
        // =====================================================

        txtPoints.setText(
                "🏆 Reward: +"
                        + points
                        + " Eco Points"
        );


        // =====================================================
        // SHOW RESULT CARD
        // =====================================================

        resultCard.setVisibility(
                View.VISIBLE
        );


        // =====================================================
        // SAVE RECOMMENDATION
        // =====================================================

        SharedPreferences prefs =
                getSharedPreferences(
                        "EcoLoopPrefs",
                        MODE_PRIVATE
                );


        prefs.edit()

                .putString(
                        "LAST_CIRCULAR_ACTION",
                        title
                )

                .putFloat(
                        "CIRCULAR_CARBON_SAVING",
                        (float) saving
                )

                .putInt(
                        "CIRCULAR_POINTS",
                        points
                )

                .putString(
                        "LAST_PRODUCT",
                        product
                )

                .apply();
    }


    // =========================================================
    // COMPLETE CIRCULAR ACTION
    // =========================================================

    private void completeCircularAction() {


        String product =
                spinnerProduct
                        .getSelectedItem()
                        .toString();


        String action =
                spinnerAction
                        .getSelectedItem()
                        .toString();


        // =====================================================
        // VALIDATION
        // =====================================================

        if (product.equals(
                "Select product"
        ) ||
                action.equals(
                        "Select action"
                )) {


            Toast.makeText(
                    this,
                    "Please select product and action first.",
                    Toast.LENGTH_SHORT
            ).show();


            return;
        }


        // =====================================================
        // CALCULATE POINTS
        // =====================================================

        int points;


        if (action.contains(
                "Repair"
        )) {

            points = 30;

        } else if (action.contains(
                "Reuse"
        )) {

            points = 25;

        } else if (action.contains(
                "Donate"
        )) {

            points = 35;

        } else if (action.contains(
                "Upcycle"
        )) {

            points = 30;

        } else if (action.contains(
                "Recycle"
        )) {

            points = 25;

        } else {

            points = 40;
        }


        // =====================================================
        // ADD POINTS
        // =====================================================

        earnedPoints =
                earnedPoints + points;


        // =====================================================
        // SAVE POINTS
        // =====================================================

        SharedPreferences prefs =
                getSharedPreferences(
                        "EcoLoopPrefs",
                        MODE_PRIVATE
                );


        prefs.edit()

                .putInt(
                        "EARNED_POINTS",
                        earnedPoints
                )

                .putString(
                        "LAST_PRODUCT",
                        product
                )

                .putString(
                        "LAST_CIRCULAR_ACTION",
                        action
                )

                .apply();


        // =====================================================
        // SUCCESS MESSAGE
        // =====================================================

        Toast.makeText(
                this,
                "🎉 Circular Action completed!\n+"
                        + points
                        + " Eco Points",
                Toast.LENGTH_LONG
        ).show();


        // =====================================================
        // DISABLE BUTTON
        // =====================================================

        btnCompleteAction.setText(
                "✅ Action Completed"
        );


        btnCompleteAction.setEnabled(
                false
        );
    }


    // =========================================================
    // ANDROID SYSTEM BACK BUTTON
    // =========================================================

    @Override
    public void onBackPressed() {

        goBackToDashboard();
    }
}