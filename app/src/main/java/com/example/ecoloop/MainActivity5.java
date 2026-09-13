package com.example.ecoloop;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity5 extends AppCompatActivity {

    private Spinner spinnerActivity;
    private LinearLayout dynamicForm;
    private Button btnSubmit;
    private ImageButton btnBack;

    private SQLiteDatabase database;

    private int userId = -1;
    private String userName = "User";

    private String proofPath = null;
    private ImageView proofPreview;

    private final ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            proofPath = savePhotoToInternalStorage(uri);

                            if (proofPath != null && proofPreview != null) {
                                proofPreview.setImageURI(Uri.fromFile(new File(proofPath)));
                                proofPreview.setVisibility(View.VISIBLE);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main5);

        getUserData();
        setupDatabase();
        initializeViews();
        setupActivitySpinner();

        btnBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> calculateAndSaveActivity());
    }

    // ---------------------------------------------------------
    // GET USER DATA
    // ---------------------------------------------------------

    private void getUserData() {

        Intent intent = getIntent();

        if (intent != null) {

            userId = intent.getIntExtra("USER_ID", -1);

            String name = intent.getStringExtra("USER_NAME");

            if (name != null && !name.trim().isEmpty()) {
                userName = name;
            }
        }

        if (userId == -1) {
            Toast.makeText(
                    this,
                    "User information not found",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // ---------------------------------------------------------
    // DATABASE
    // ---------------------------------------------------------

    private void setupDatabase() {

        database = openOrCreateDatabase(
                "EcoLoopDB",
                MODE_PRIVATE,
                null
        );

        database.execSQL(
                "CREATE TABLE IF NOT EXISTS activities (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "user_id INTEGER, " +
                        "user_name TEXT, " +
                        "activity_type TEXT, " +
                        "carbon_footprint REAL, " +
                        "points INTEGER, " +
                        "proof_path TEXT, " +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );

        try {

            database.execSQL(
                    "ALTER TABLE activities ADD COLUMN proof_path TEXT"
            );

        } catch (Exception ignored) {
        }
    }

    // ---------------------------------------------------------
    // INITIALIZE VIEWS
    // ---------------------------------------------------------

    private void initializeViews() {

        spinnerActivity = findViewById(R.id.spinnerActivity);

        dynamicForm = findViewById(R.id.formContainer);

        btnSubmit = findViewById(R.id.btnSubmitActivity);

        btnBack = findViewById(R.id.btnBack);

        dynamicForm.setPadding(
                0,
                5,
                0,
                10
        );
    }

    // ---------------------------------------------------------
    // ACTIVITY SPINNER
    // ---------------------------------------------------------

    private void setupActivitySpinner() {

        String[] activities = {
                "Select Activity",
                "Transportation",
                "Electricity Usage",
                "Waste Recycling",
                "Purchases"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        activities
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerActivity.setAdapter(adapter);

        spinnerActivity.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        showActivityForm(position);
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent
                    ) {
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // SHOW DYNAMIC FORM
    // ---------------------------------------------------------

    private void showActivityForm(int position) {

        dynamicForm.removeAllViews();

        proofPath = null;
        proofPreview = null;

        if (position == 0) {
            return;
        }

        // -----------------------------------------------------
        // TRANSPORTATION
        // -----------------------------------------------------

        if (position == 1) {

            TextView vehicleLabel =
                    createLabel("Vehicle Type");

            dynamicForm.addView(vehicleLabel);

            Spinner vehicleSpinner =
                    createSpinner(
                            new String[]{
                                    "Car",
                                    "Bike",
                                    "Bus",
                                    "Train"
                            }
                    );

            dynamicForm.addView(vehicleSpinner);

            addSpace(25);

            TextView distanceLabel =
                    createLabel("Distance Travelled (km)");

            dynamicForm.addView(distanceLabel);

            EditText distanceInput =
                    createEditText("Enter distance in km");

            dynamicForm.addView(distanceInput);

            addSpace(25);

            TextView fuelLabel =
                    createLabel("Fuel Type");

            dynamicForm.addView(fuelLabel);

            Spinner fuelSpinner =
                    createSpinner(
                            new String[]{
                                    "Petrol",
                                    "Diesel",
                                    "Electric",
                                    "Not Applicable"
                            }
                    );

            dynamicForm.addView(fuelSpinner);

            addSpace(30);

            addPhotoProofSection();

            // Store fields inside tag
            dynamicForm.setTag(
                    new Object[]{
                            vehicleSpinner,
                            distanceInput,
                            fuelSpinner
                    }
            );
        }

        // -----------------------------------------------------
        // ELECTRICITY
        // -----------------------------------------------------

        else if (position == 2) {

            TextView electricityLabel =
                    createLabel("Electricity Usage (kWh)");

            dynamicForm.addView(electricityLabel);

            EditText electricityInput =
                    createEditText("Enter electricity usage");

            dynamicForm.addView(electricityInput);

            addSpace(30);

            addPhotoProofSection();

            dynamicForm.setTag(electricityInput);
        }

        // -----------------------------------------------------
        // WASTE RECYCLING
        // -----------------------------------------------------

        else if (position == 3) {

            TextView wasteTypeLabel =
                    createLabel("Waste Type");

            dynamicForm.addView(wasteTypeLabel);

            Spinner wasteTypeSpinner =
                    createSpinner(
                            new String[]{
                                    "Plastic",
                                    "Paper",
                                    "Glass",
                                    "Metal",
                                    "Organic Waste"
                            }
                    );

            dynamicForm.addView(wasteTypeSpinner);

            addSpace(25);

            TextView quantityLabel =
                    createLabel("Quantity (kg)");

            dynamicForm.addView(quantityLabel);

            EditText quantityInput =
                    createEditText("Enter quantity in kg");

            dynamicForm.addView(quantityInput);

            addSpace(25);

            TextView recycledLabel =
                    createLabel("Was it Recycled?");

            dynamicForm.addView(recycledLabel);

            Spinner recycledSpinner =
                    createSpinner(
                            new String[]{
                                    "Yes",
                                    "No"
                            }
                    );

            dynamicForm.addView(recycledSpinner);

            addSpace(30);

            addPhotoProofSection();

            dynamicForm.setTag(
                    new Object[]{
                            wasteTypeSpinner,
                            quantityInput,
                            recycledSpinner
                    }
            );
        }

        // -----------------------------------------------------
        // PURCHASES
        // -----------------------------------------------------

        else if (position == 4) {

            TextView categoryLabel =
                    createLabel("Purchase Category");

            dynamicForm.addView(categoryLabel);

            Spinner categorySpinner =
                    createSpinner(
                            new String[]{
                                    "Clothing",
                                    "Electronics",
                                    "Food",
                                    "Furniture",
                                    "Other"
                            }
                    );

            dynamicForm.addView(categorySpinner);

            addSpace(25);

            TextView amountLabel =
                    createLabel("Purchase Amount (₹)");

            dynamicForm.addView(amountLabel);

            EditText amountInput =
                    createEditText("Enter purchase amount");

            dynamicForm.addView(amountInput);

            addSpace(30);

            addPhotoProofSection();

            dynamicForm.setTag(
                    new Object[]{
                            categorySpinner,
                            amountInput
                    }
            );
        }
    }

    // ---------------------------------------------------------
    // ADD PHOTO PROOF
    // ---------------------------------------------------------

    private void addPhotoProofSection() {

        TextView proofLabel =
                createLabel("Photo Proof");

        dynamicForm.addView(proofLabel);

        Button btnPhoto =
                new Button(this);

        btnPhoto.setText("📷  Upload Photo Proof");

        btnPhoto.setTextSize(25);

        btnPhoto.setTextColor(Color.WHITE);

        btnPhoto.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        Color.rgb(46, 125, 50)
                )
        );

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        190
                );

        buttonParams.setMargins(
                0,
                25,
                0,
                25
        );

        btnPhoto.setLayoutParams(buttonParams);

        dynamicForm.addView(btnPhoto);

        proofPreview =
                new ImageView(this);

        proofPreview.setScaleType(
                ImageView.ScaleType.CENTER_CROP
        );

        LinearLayout.LayoutParams imageParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        180
                );

        imageParams.setMargins(
                0,
                5,
                0,
                10
        );

        proofPreview.setLayoutParams(imageParams);

        proofPreview.setVisibility(View.GONE);

        dynamicForm.addView(proofPreview);

        btnPhoto.setOnClickListener(
                v -> galleryLauncher.launch("image/*")
        );
    }

    // ---------------------------------------------------------
    // CALCULATE AND SAVE
    // ---------------------------------------------------------

    private void calculateAndSaveActivity() {

        int position =
                spinnerActivity.getSelectedItemPosition();

        if (position == 0) {

            Toast.makeText(
                    this,
                    "Please select an activity",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (proofPath == null) {

            Toast.makeText(
                    this,
                    "Please upload photo proof",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double carbon = 0;
        int points = 0;

        // -----------------------------------------------------
        // TRANSPORTATION
        // -----------------------------------------------------

        if (position == 1) {

            Object[] fields =
                    (Object[]) dynamicForm.getTag();

            Spinner vehicleSpinner =
                    (Spinner) fields[0];

            EditText distanceInput =
                    (EditText) fields[1];

            String vehicle =
                    vehicleSpinner
                            .getSelectedItem()
                            .toString();

            String distanceText =
                    distanceInput
                            .getText()
                            .toString()
                            .trim();

            if (distanceText.isEmpty()) {

                distanceInput.setError(
                        "Enter distance"
                );

                return;
            }

            double distance;

            try {

                distance =
                        Double.parseDouble(distanceText);

            } catch (Exception e) {

                distanceInput.setError(
                        "Enter valid distance"
                );

                return;
            }

            double factor;

            if (vehicle.equals("Car")) {

                factor = 0.21;

            } else if (vehicle.equals("Bike")) {

                factor = 0.10;

            } else if (vehicle.equals("Bus")) {

                factor = 0.08;

            } else {

                factor = 0.04;
            }

            carbon =
                    distance * factor;

            points = 10;
        }

        // -----------------------------------------------------
        // ELECTRICITY
        // -----------------------------------------------------

        else if (position == 2) {

            EditText electricityInput =
                    (EditText) dynamicForm.getTag();

            String electricityText =
                    electricityInput
                            .getText()
                            .toString()
                            .trim();

            if (electricityText.isEmpty()) {

                electricityInput.setError(
                        "Enter electricity usage"
                );

                return;
            }

            double electricity;

            try {

                electricity =
                        Double.parseDouble(
                                electricityText
                        );

            } catch (Exception e) {

                electricityInput.setError(
                        "Enter valid value"
                );

                return;
            }

            carbon =
                    electricity * 0.70;

            points = 10;
        }

        // -----------------------------------------------------
        // WASTE
        // -----------------------------------------------------

        else if (position == 3) {

            Object[] fields =
                    (Object[]) dynamicForm.getTag();

            Spinner wasteTypeSpinner =
                    (Spinner) fields[0];

            EditText quantityInput =
                    (EditText) fields[1];

            Spinner recycledSpinner =
                    (Spinner) fields[2];

            String wasteType =
                    wasteTypeSpinner
                            .getSelectedItem()
                            .toString();

            String quantityText =
                    quantityInput
                            .getText()
                            .toString()
                            .trim();

            if (quantityText.isEmpty()) {

                quantityInput.setError(
                        "Enter quantity"
                );

                return;
            }

            double quantity;

            try {

                quantity =
                        Double.parseDouble(
                                quantityText
                        );

            } catch (Exception e) {

                quantityInput.setError(
                        "Enter valid quantity"
                );

                return;
            }

            double factor;

            if (wasteType.equals("Plastic")) {

                factor = 2.5;

            } else if (wasteType.equals("Paper")) {

                factor = 1.3;

            } else if (wasteType.equals("Glass")) {

                factor = 0.9;

            } else if (wasteType.equals("Metal")) {

                factor = 1.8;

            } else {

                factor = 0.5;
            }

            carbon =
                    quantity * factor;

            String recycled =
                    recycledSpinner
                            .getSelectedItem()
                            .toString();

            if (recycled.equals("Yes")) {

                carbon =
                        carbon * 0.30;
            }

            points = 20;
        }

        // -----------------------------------------------------
        // PURCHASES
        // -----------------------------------------------------

        else if (position == 4) {

            Object[] fields =
                    (Object[]) dynamicForm.getTag();

            Spinner categorySpinner =
                    (Spinner) fields[0];

            EditText amountInput =
                    (EditText) fields[1];

            String category =
                    categorySpinner
                            .getSelectedItem()
                            .toString();

            String amountText =
                    amountInput
                            .getText()
                            .toString()
                            .trim();

            if (amountText.isEmpty()) {

                amountInput.setError(
                        "Enter purchase amount"
                );

                return;
            }

            double amount;

            try {

                amount =
                        Double.parseDouble(
                                amountText
                        );

            } catch (Exception e) {

                amountInput.setError(
                        "Enter valid amount"
                );

                return;
            }

            double factor;

            if (category.equals("Clothing")) {

                factor = 0.0005;

            } else if (category.equals("Electronics")) {

                factor = 0.0004;

            } else if (category.equals("Food")) {

                factor = 0.0003;

            } else if (category.equals("Furniture")) {

                factor = 0.0004;

            } else {

                factor = 0.0003;
            }

            carbon =
                    amount * factor;

            points = 5;
        }

        saveActivity(
                getActivityName(position),
                carbon,
                points
        );
    }

    // ---------------------------------------------------------
    // SAVE ACTIVITY
    // ---------------------------------------------------------

    private void saveActivity(
            String activityType,
            double carbon,
            int points
    ) {

        ContentValues values =
                new ContentValues();

        values.put(
                "user_id",
                userId
        );

        values.put(
                "user_name",
                userName
        );

        values.put(
                "activity_type",
                activityType
        );

        values.put(
                "carbon_footprint",
                carbon
        );

        values.put(
                "points",
                points
        );

        values.put(
                "proof_path",
                proofPath
        );

        long result =
                database.insert(
                        "activities",
                        null,
                        values
                );

        if (result == -1) {

            Toast.makeText(
                    this,
                    "Failed to save activity",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double totalCarbon =
                getTotalCarbon();

        int totalPoints =
                getTotalPoints();

        getSharedPreferences(
                "EcoLoopPrefs",
                MODE_PRIVATE
        )
                .edit()
                .putFloat(
                        "CARBON_FOOTPRINT",
                        (float) totalCarbon
                )
                .putInt(
                        "EARNED_POINTS",
                        totalPoints
                )
                .apply();

        Toast.makeText(
                this,
                "Activity saved successfully!",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent =
                new Intent(
                        MainActivity5.this,
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

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);

        finish();
    }

    // ---------------------------------------------------------
    // GET TOTAL CARBON
    // ---------------------------------------------------------

    private double getTotalCarbon() {

        double total = 0;

        Cursor cursor =
                database.rawQuery(
                        "SELECT SUM(carbon_footprint) " +
                                "FROM activities " +
                                "WHERE user_id = ?",
                        new String[]{
                                String.valueOf(userId)
                        }
                );

        if (cursor.moveToFirst()) {

            if (!cursor.isNull(0)) {

                total =
                        cursor.getDouble(0);
            }
        }

        cursor.close();

        return total;
    }

    // ---------------------------------------------------------
    // GET TOTAL POINTS
    // ---------------------------------------------------------

    private int getTotalPoints() {

        int total = 0;

        Cursor cursor =
                database.rawQuery(
                        "SELECT SUM(points) " +
                                "FROM activities " +
                                "WHERE user_id = ?",
                        new String[]{
                                String.valueOf(userId)
                        }
                );

        if (cursor.moveToFirst()) {

            if (!cursor.isNull(0)) {

                total =
                        cursor.getInt(0);
            }
        }

        cursor.close();

        return total;
    }

    // ---------------------------------------------------------
    // ACTIVITY NAME
    // ---------------------------------------------------------

    private String getActivityName(int position) {

        if (position == 1) {
            return "Transportation";
        }

        if (position == 2) {
            return "Electricity Usage";
        }

        if (position == 3) {
            return "Waste Recycling";
        }

        if (position == 4) {
            return "Purchases";
        }

        return "Unknown";
    }

    // ---------------------------------------------------------
    // CREATE LABEL
    // ---------------------------------------------------------

    private TextView createLabel(String text) {

        TextView label =
                new TextView(this);

        label.setText(text);

        label.setTextSize(16);

        label.setTextColor(
                Color.rgb(
                        27,
                        94,
                        32
                )
        );

        label.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                10,
                0,
                10
        );

        label.setLayoutParams(params);

        label.setPadding(
                0,
                10,
                0,
                10
        );

        return label;
    }

    // ---------------------------------------------------------
    // CREATE EDIT TEXT
    // ---------------------------------------------------------

    private EditText createEditText(String hint) {

        EditText editText =
                new EditText(this);

        editText.setHint(hint);

        editText.setTextColor(
                Color.BLACK
        );

        editText.setHintTextColor(
                Color.rgb(
                        119,
                        119,
                        119
                )
        );

        editText.setTextSize(16);

        editText.setSingleLine(true);

        editText.setPadding(
                16,
                15,
                16,
                15
        );

        editText.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
75
                );

        params.setMargins(
                0,
                15,
                0,
                15
        );

        editText.setLayoutParams(params);

        return editText;
    }

    // ---------------------------------------------------------
    // CREATE SPINNER
    // ---------------------------------------------------------

    private Spinner createSpinner(String[] items) {

        Spinner spinner =
                new Spinner(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        items
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);

        spinner.setPadding(
                10,
                0,
                10,
                0
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        58
                );

        params.setMargins(
                0,
                5,
                0,
                5
        );

        spinner.setLayoutParams(params);

        return spinner;
    }

    // ---------------------------------------------------------
    // EXPLICIT SPACE
    // ---------------------------------------------------------

    private void addSpace(int height) {

        Space space =
                new Space(this);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        height
                );

        space.setLayoutParams(params);

        dynamicForm.addView(space);
    }

    // ---------------------------------------------------------
    // SAVE PHOTO
    // ---------------------------------------------------------

    private String savePhotoToInternalStorage(Uri uri) {

        try {

            File directory =
                    new File(
                            getFilesDir(),
                            "activity_proofs"
                    );

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName =
                    "proof_" +
                            System.currentTimeMillis() +
                            ".jpg";

            File file =
                    new File(
                            directory,
                            fileName
                    );

            InputStream inputStream =
                    getContentResolver()
                            .openInputStream(uri);

            FileOutputStream outputStream =
                    new FileOutputStream(file);

            byte[] buffer =
                    new byte[1024];

            int length;

            while (
                    (length =
                            inputStream.read(buffer)) > 0
            ) {

                outputStream.write(
                        buffer,
                        0,
                        length
                );
            }

            inputStream.close();

            outputStream.close();

            return file.getAbsolutePath();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to save photo",
                    Toast.LENGTH_SHORT
            ).show();

            return null;
        }
    }

    // ---------------------------------------------------------
    // DESTROY
    // ---------------------------------------------------------

    @Override
    protected void onDestroy() {

        if (database != null &&
                database.isOpen()) {

            database.close();
        }

        super.onDestroy();
    }
}