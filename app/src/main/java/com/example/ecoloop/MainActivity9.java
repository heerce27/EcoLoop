package com.example.ecoloop;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity9 extends AppCompatActivity {

    private Spinner wasteSpinner;

    private Button findButton;
    private Button mapsButton;
    private Button directionButton;

    private TextView locationText;
    private TextView centerName;
    private TextView centerType;
    private TextView distanceText;
    private TextView savingText;
    private TextView pointsText;

    private View resultBox;

    private String selectedWaste = "";

    private static final int LOCATION_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main9);

        initializeViews();
        setupWasteSpinner();
        setupButtons();

        resultBox.setVisibility(View.GONE);
    }

    // =========================================================
    // INITIALIZE
    // =========================================================

    private void initializeViews() {

        wasteSpinner = findViewById(R.id.wasteSpinner);

        findButton = findViewById(R.id.findButton);
        mapsButton = findViewById(R.id.mapsButton);
        directionButton = findViewById(R.id.directionButton);

        locationText = findViewById(R.id.locationText);

        centerName = findViewById(R.id.centerName);
        centerType = findViewById(R.id.centerType);

        distanceText = findViewById(R.id.distanceText);
        savingText = findViewById(R.id.savingText);
        pointsText = findViewById(R.id.pointsText);

        resultBox = findViewById(R.id.resultBox);
    }

    // =========================================================
    // WASTE SPINNER
    // =========================================================

    private void setupWasteSpinner() {

        String[] wasteList = {

                "Choose waste category",

                "E-Waste",

                "Plastic",

                "Paper",

                "Glass",

                "Metal",

                "Clothes",

                "Batteries",

                "Electronics",

                "Furniture",

                "General Recyclable Waste"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        wasteList
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        wasteSpinner.setAdapter(adapter);
    }

    // =========================================================
    // BUTTONS
    // =========================================================

    private void setupButtons() {

        findButton.setOnClickListener(v -> {

            checkLocationAndFind();

        });

        mapsButton.setOnClickListener(v -> {

            openNearbyMaps();

        });

        directionButton.setOnClickListener(v -> {

            openDirections();

        });
    }

    // =========================================================
    // CHECK LOCATION
    // =========================================================

    private void checkLocationAndFind() {

        selectedWaste =
                wasteSpinner
                        .getSelectedItem()
                        .toString();

        if (selectedWaste.equals(
                "Choose waste category"
        )) {

            Toast.makeText(
                    this,
                    "Please choose a waste category",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean fineLocation =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocation =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        if (!fineLocation && !coarseLocation) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_REQUEST
            );

            return;
        }

        showResult();
    }

    // =========================================================
    // RESULT
    // =========================================================

    private void showResult() {

        String name;
        String type;

        double distance;
        double saving;

        int points;

        if (selectedWaste.equals("E-Waste")
                || selectedWaste.equals("Electronics")) {

            name = "Eco E-Waste Collection Hub";
            type = "Electronic Waste Recycling";

            distance = 2.4;
            saving = 4.5;
            points = 40;

        } else if (selectedWaste.equals("Plastic")) {

            name = "Green Plastic Recovery Center";
            type = "Plastic Recycling";

            distance = 1.8;
            saving = 2.5;
            points = 25;

        } else if (selectedWaste.equals("Paper")) {

            name = "Eco Paper Collection Point";
            type = "Paper Recycling";

            distance = 1.5;
            saving = 1.8;
            points = 20;

        } else if (selectedWaste.equals("Glass")) {

            name = "Clean Glass Recycling Point";
            type = "Glass Recycling";

            distance = 3.1;
            saving = 2.2;
            points = 25;

        } else if (selectedWaste.equals("Metal")) {

            name = "Green Metal Recovery Center";
            type = "Metal Recycling";

            distance = 2.7;
            saving = 3.0;
            points = 30;

        } else if (selectedWaste.equals("Clothes")) {

            name = "Eco Textile Donation Center";
            type = "Clothes Reuse & Recycling";

            distance = 2.0;
            saving = 3.2;
            points = 30;

        } else if (selectedWaste.equals("Batteries")) {

            name = "Safe Battery Collection Point";
            type = "Battery Recycling";

            distance = 2.2;
            saving = 2.8;
            points = 35;

        } else if (selectedWaste.equals("Furniture")) {

            name = "Furniture Reuse Center";
            type = "Furniture Donation & Reuse";

            distance = 4.0;
            saving = 5.0;
            points = 45;

        } else {

            name = "EcoLoop Recycling Collection Hub";
            type = "General Recycling Facility";

            distance = 2.5;
            saving = 2.0;
            points = 20;
        }

        // Display location status

        locationText.setText(
                "● Location permission available"
        );

        // Display center

        centerName.setText(
                name
        );

        centerType.setText(
                "Facility: " + type
        );

        distanceText.setText(
                String.format(
                        "📍 %.1f km approximately",
                        distance
                )
        );

        savingText.setText(
                String.format(
                        "🌱 %.1f kg CO₂e saved",
                        saving
                )
        );

        pointsText.setText(
                "+" + points + " Eco Points"
        );

        resultBox.setVisibility(
                View.VISIBLE
        );

        saveData(
                name,
                saving,
                points
        );

        Toast.makeText(
                this,
                "Recycling option found!",
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================================================
    // SAVE DATA
    // =========================================================

    private void saveData(
            String center,
            double saving,
            int points
    ) {

        SharedPreferences preferences =
                getSharedPreferences(
                        "EcoLoopPrefs",
                        MODE_PRIVATE
                );

        preferences.edit()

                .putString(
                        "LAST_RECYCLING_CENTER",
                        center
                )

                .putString(
                        "LAST_RECYCLING_WASTE",
                        selectedWaste
                )

                .putFloat(
                        "RECYCLING_CARBON_SAVING",
                        (float) saving
                )

                .putInt(
                        "RECYCLING_POINTS",
                        points
                )

                .apply();
    }

    // =========================================================
    // GOOGLE MAPS SEARCH
    // =========================================================

    private void openNearbyMaps() {

        String waste =
                wasteSpinner
                        .getSelectedItem()
                        .toString();

        if (waste.equals(
                "Choose waste category"
        )) {

            Toast.makeText(
                    this,
                    "Please choose waste category first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String query =
                Uri.encode(
                        "recycling center near me "
                                + waste
                );

        Uri uri =
                Uri.parse(
                        "geo:0,0?q="
                                + query
                );

        Intent mapsIntent =
                new Intent(
                        Intent.ACTION_VIEW,
                        uri
                );

        mapsIntent.setPackage(
                "com.google.android.apps.maps"
        );

        try {

            startActivity(
                    mapsIntent
            );

        } catch (Exception e) {

            Intent browserIntent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                    "https://www.google.com/maps/search/?api=1&query="
                                            + query
                            )
                    );

            startActivity(
                    browserIntent
            );
        }
    }

    // =========================================================
    // DIRECTIONS
    // =========================================================

    private void openDirections() {

        if (resultBox.getVisibility()
                != View.VISIBLE) {

            Toast.makeText(
                    this,
                    "Find a recycling center first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String destination =
                centerName
                        .getText()
                        .toString();

        String query =
                Uri.encode(
                        destination
                );

        Uri navigationUri =
                Uri.parse(
                        "google.navigation:q="
                                + query
                );

        Intent navigationIntent =
                new Intent(
                        Intent.ACTION_VIEW,
                        navigationUri
                );

        navigationIntent.setPackage(
                "com.google.android.apps.maps"
        );

        try {

            startActivity(
                    navigationIntent
            );

        } catch (Exception e) {

            Intent browserIntent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                    "https://www.google.com/maps/search/?api=1&query="
                                            + query
                            )
                    );

            startActivity(
                    browserIntent
            );
        }
    }

    // =========================================================
    // PERMISSION RESULT
    // =========================================================

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] results
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                results
        );

        if (requestCode == LOCATION_REQUEST) {

            boolean granted = false;

            for (int result : results) {

                if (result ==
                        PackageManager.PERMISSION_GRANTED) {

                    granted = true;
                    break;
                }
            }

            if (granted) {

                Toast.makeText(
                        this,
                        "Location permission granted",
                        Toast.LENGTH_SHORT
                ).show();

                showResult();

            } else {

                locationText.setText(
                        "● Location permission not granted"
                );

                Toast.makeText(
                        this,
                        "Location permission is needed",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}