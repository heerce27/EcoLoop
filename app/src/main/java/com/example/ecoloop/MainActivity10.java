package com.example.ecoloop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity10 extends AppCompatActivity {

    // =====================================================
    // VIEWS
    // =====================================================

    private TextView txtProgress;
    private TextView txtStatus;

    private TextView txtChallengeTitle;
    private TextView txtChallengeDescription;
    private TextView txtChallengeCategory;
    private TextView txtChallengeDuration;
    private TextView txtChallengeReward;

    private Button btnChallenge1;
    private Button btnChallenge2;
    private Button btnChallenge3;
    private Button btnChallenge4;
    private Button btnChallenge5;

    private Button btnComplete;

    private LinearLayout detailsCard;

    // =====================================================
    // DATA
    // =====================================================

    private int selectedChallenge = 0;
    private int completedCount = 0;

    private SharedPreferences preferences;

    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main10);

        initializeViews();

        setupPreferences();

        loadProgress();

        setupChallengeButtons();

        updateProgress();

        detailsCard.setVisibility(View.GONE);
    }

    // =====================================================
    // INITIALIZE VIEWS
    // =====================================================

    private void initializeViews() {

        txtProgress = findViewById(
                R.id.txtProgress
        );

        txtStatus = findViewById(
                R.id.txtStatus
        );

        txtChallengeTitle = findViewById(
                R.id.txtChallengeTitle
        );

        txtChallengeDescription = findViewById(
                R.id.txtChallengeDescription
        );

        txtChallengeCategory = findViewById(
                R.id.txtChallengeCategory
        );

        txtChallengeDuration = findViewById(
                R.id.txtChallengeDuration
        );

        txtChallengeReward = findViewById(
                R.id.txtChallengeReward
        );

        btnChallenge1 = findViewById(
                R.id.btnChallenge1
        );

        btnChallenge2 = findViewById(
                R.id.btnChallenge2
        );

        btnChallenge3 = findViewById(
                R.id.btnChallenge3
        );

        btnChallenge4 = findViewById(
                R.id.btnChallenge4
        );

        btnChallenge5 = findViewById(
                R.id.btnChallenge5
        );

        btnComplete = findViewById(
                R.id.btnComplete
        );

        detailsCard = findViewById(
                R.id.detailsCard
        );
    }

    // =====================================================
    // SHARED PREFERENCES
    // =====================================================

    private void setupPreferences() {

        preferences = getSharedPreferences(
                "EcoLoopPrefs",
                MODE_PRIVATE
        );
    }

    // =====================================================
    // LOAD PROGRESS
    // =====================================================

    private void loadProgress() {

        completedCount =
                preferences.getInt(
                        "COMPLETED_ECO_CHALLENGES",
                        0
                );
    }

    // =====================================================
    // CHALLENGE BUTTONS
    // =====================================================

    private void setupChallengeButtons() {

        btnChallenge1.setOnClickListener(v -> {

            selectChallenge(1);

        });

        btnChallenge2.setOnClickListener(v -> {

            selectChallenge(2);

        });

        btnChallenge3.setOnClickListener(v -> {

            selectChallenge(3);

        });

        btnChallenge4.setOnClickListener(v -> {

            selectChallenge(4);

        });

        btnChallenge5.setOnClickListener(v -> {

            selectChallenge(5);

        });

        btnComplete.setOnClickListener(v -> {

            completeSelectedChallenge();

        });
    }

    // =====================================================
    // SELECT CHALLENGE
    // =====================================================

    private void selectChallenge(int number) {

        selectedChallenge = number;

        detailsCard.setVisibility(
                View.VISIBLE
        );

        switch (number) {

            case 1:

                txtChallengeTitle.setText(
                        "🚶 Walk Instead of Driving"
                );

                txtChallengeDescription.setText(
                        "Choose walking instead of using a "
                                + "car or other motorized transport "
                                + "for one short trip today. "
                                + "Walking for nearby destinations "
                                + "can reduce unnecessary fuel use."
                );

                txtChallengeCategory.setText(
                        "🌍 Sustainable Transport"
                );

                txtChallengeDuration.setText(
                        "⏱ Duration: 1 Day"
                );

                txtChallengeReward.setText(
                        "🏆 Reward: 20 Eco Points"
                );

                break;


            case 2:

                txtChallengeTitle.setText(
                        "🥤 Use a Reusable Bottle"
                );

                txtChallengeDescription.setText(
                        "Carry a reusable water bottle with you "
                                + "throughout the day. Try to avoid "
                                + "using disposable plastic bottles "
                                + "when a reusable option is available."
                );

                txtChallengeCategory.setText(
                        "♻ Plastic Reduction"
                );

                txtChallengeDuration.setText(
                        "⏱ Duration: 1 Day"
                );

                txtChallengeReward.setText(
                        "🏆 Reward: 20 Eco Points"
                );

                break;


            case 3:

                txtChallengeTitle.setText(
                        "💡 Save Electricity"
                );

                txtChallengeDescription.setText(
                        "Switch off lights, fans and electronic "
                                + "devices when they are not needed. "
                                + "Make it a habit to check rooms "
                                + "before leaving them."
                );

                txtChallengeCategory.setText(
                        "⚡ Energy Conservation"
                );

                txtChallengeDuration.setText(
                        "⏱ Duration: 1 Day"
                );

                txtChallengeReward.setText(
                        "🏆 Reward: 25 Eco Points"
                );

                break;


            case 4:

                txtChallengeTitle.setText(
                        "♻ Separate Your Waste"
                );

                txtChallengeDescription.setText(
                        "Separate recyclable materials such as "
                                + "paper, plastic, metal and glass "
                                + "from general waste. Keep different "
                                + "types of waste organized for proper "
                                + "recycling."
                );

                txtChallengeCategory.setText(
                        "♻ Recycling"
                );

                txtChallengeDuration.setText(
                        "⏱ Duration: 3 Days"
                );

                txtChallengeReward.setText(
                        "🏆 Reward: 30 Eco Points"
                );

                break;


            case 5:

                txtChallengeTitle.setText(
                        "🛍 Avoid Unnecessary Purchases"
                );

                txtChallengeDescription.setText(
                        "Before buying something, pause and ask "
                                + "yourself whether you really need it. "
                                + "Avoid unnecessary purchases for "
                                + "one week and focus on using what "
                                + "you already have."
                );

                txtChallengeCategory.setText(
                        "🌱 Responsible Consumption"
                );

                txtChallengeDuration.setText(
                        "⏱ Duration: 7 Days"
                );

                txtChallengeReward.setText(
                        "🏆 Reward: 40 Eco Points"
                );

                break;
        }

        updateCompleteButton();
    }

    // =====================================================
    // COMPLETE CHALLENGE
    // =====================================================

    private void completeSelectedChallenge() {

        if (selectedChallenge == 0) {

            Toast.makeText(
                    this,
                    "Please select a challenge first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String completedKey =
                "ECO_CHALLENGE_"
                        + selectedChallenge
                        + "_DONE";

        boolean alreadyDone =
                preferences.getBoolean(
                        completedKey,
                        false
                );

        if (alreadyDone) {

            Toast.makeText(
                    this,
                    "This challenge is already completed!",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Save individual challenge
        preferences.edit()
                .putBoolean(
                        completedKey,
                        true
                )
                .apply();

        // Increase progress
        completedCount++;

        // Save progress
        preferences.edit()
                .putInt(
                        "COMPLETED_ECO_CHALLENGES",
                        completedCount
                )
                .apply();

        updateProgress();

        updateCompleteButton();

        Toast.makeText(
                this,
                "🎉 Challenge completed!",
                Toast.LENGTH_LONG
        ).show();
    }

    // =====================================================
    // UPDATE PROGRESS
    // =====================================================

    private void updateProgress() {

        txtProgress.setText(
                completedCount + " / 5"
        );

        if (completedCount == 0) {

            txtStatus.setText(
                    "Start your first eco challenge 🌱"
            );

        } else if (completedCount < 5) {

            txtStatus.setText(
                    "Great job! Keep going 🌿"
            );

        } else {

            txtStatus.setText(
                    "🎉 All challenges completed!"
            );
        }
    }

    // =====================================================
    // UPDATE COMPLETE BUTTON
    // =====================================================

    private void updateCompleteButton() {

        if (selectedChallenge == 0) {

            return;
        }

        String completedKey =
                "ECO_CHALLENGE_"
                        + selectedChallenge
                        + "_DONE";

        boolean alreadyDone =
                preferences.getBoolean(
                        completedKey,
                        false
                );

        if (alreadyDone) {

            btnComplete.setText(
                    "✓ Completed"
            );

            btnComplete.setEnabled(
                    false
            );

        } else {

            btnComplete.setText(
                    "Complete This Challenge"
            );

            btnComplete.setEnabled(
                    true
            );
        }
    }

    // =====================================================
    // SYSTEM BACK
    // =====================================================

    @Override
    public void onBackPressed() {

        Intent intent =
                new Intent(
                        MainActivity10.this,
                        MainActivity4.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);

        finish();
    }
}