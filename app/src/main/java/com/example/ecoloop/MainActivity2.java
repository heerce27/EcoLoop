package com.example.ecoloop;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MainActivity2 extends AppCompatActivity {

    // =========================================================
    // LOGIN VIEWS
    // =========================================================

    private EditText loginName;
    private EditText loginPassword;

    private Button btnLogin;
    private android.widget.TextView btnGoRegister;


    // =========================================================
    // REGISTER VIEWS
    // =========================================================

    private EditText registerName;
    private EditText registerMobile;
    private EditText registerEmail;
    private EditText registerPassword;

    private Button btnRegister;
    private android.widget.TextView btnGoLogin;


    // =========================================================
    // FORMS
    // =========================================================

    private LinearLayout loginForm;
    private LinearLayout registerForm;


    // =========================================================
    // DATABASE
    // =========================================================

    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        initializeViews();

        createDatabase();

        setupListeners();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        // Login
        loginName = findViewById(R.id.loginName);
        loginPassword = findViewById(R.id.loginPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);


        // Register
        registerName = findViewById(R.id.registerName);
        registerMobile = findViewById(R.id.registerMobile);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnGoLogin = findViewById(R.id.btnGoLogin);


        // Forms
        loginForm = findViewById(R.id.loginForm);
        registerForm = findViewById(R.id.registerForm);
    }


    // =========================================================
    // CREATE DATABASE
    // =========================================================

    private void createDatabase() {

        database = openOrCreateDatabase(
                "EcoLoopDB",
                Context.MODE_PRIVATE,
                null
        );

        database.execSQL(
                "CREATE TABLE IF NOT EXISTS users (" +

                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +

                        "name TEXT UNIQUE," +

                        "mobile TEXT," +

                        "email TEXT," +

                        "password TEXT," +

                        "carbonFootprint REAL DEFAULT 0," +

                        "earnedPoints INTEGER DEFAULT 0" +

                        ")"
        );
    }


    // =========================================================
    // LISTENERS
    // =========================================================

    private void setupListeners() {

        // Go to Register
        btnGoRegister.setOnClickListener(v -> {

            flipToRegister();

        });


        // Go back to Login
        btnGoLogin.setOnClickListener(v -> {

            flipToLogin();

        });


        // Register
        btnRegister.setOnClickListener(v -> {

            registerUser();

        });


        // Login
        btnLogin.setOnClickListener(v -> {

            loginUser();

        });
    }


    // =========================================================
    // REGISTER USER
    // =========================================================

    private void registerUser() {

        String name = registerName.getText()
                .toString()
                .trim();

        String mobile = registerMobile.getText()
                .toString()
                .trim();

        String email = registerEmail.getText()
                .toString()
                .trim();

        String password = registerPassword.getText()
                .toString()
                .trim();


        // =====================================================
        // VALIDATION
        // =====================================================

        if (TextUtils.isEmpty(name)) {

            registerName.setError("Enter your name");
            registerName.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(mobile)) {

            registerMobile.setError("Enter mobile number");
            registerMobile.requestFocus();

            return;
        }


        if (mobile.length() != 10) {

            registerMobile.setError(
                    "Mobile number must be 10 digits"
            );

            registerMobile.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(email)) {

            registerEmail.setError("Enter email");
            registerEmail.requestFocus();

            return;
        }


        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            registerEmail.setError(
                    "Enter a valid email"
            );

            registerEmail.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(password)) {

            registerPassword.setError(
                    "Enter password"
            );

            registerPassword.requestFocus();

            return;
        }


        if (password.length() < 6) {

            registerPassword.setError(
                    "Password must contain at least 6 characters"
            );

            registerPassword.requestFocus();

            return;
        }


        // =====================================================
        // CHECK EXISTING USER
        // =====================================================

        Cursor cursor = database.rawQuery(
                "SELECT id FROM users WHERE name = ?",
                new String[]{name}
        );


        if (cursor.moveToFirst()) {

            cursor.close();

            Toast.makeText(
                    this,
                    "Name already registered!",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        cursor.close();


        // =====================================================
        // HASH PASSWORD
        // =====================================================

        String hashedPassword = hashPassword(password);


        // =====================================================
        // INSERT USER
        // =====================================================

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("mobile", mobile);
        values.put("email", email);
        values.put("password", hashedPassword);

        // Initial values
        values.put("carbonFootprint", 0.0);
        values.put("earnedPoints", 0);


        long userId = database.insert(
                "users",
                null,
                values
        );


        if (userId == -1) {

            Toast.makeText(
                    this,
                    "Registration failed!",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =====================================================
        // REGISTRATION SUCCESS
        // =====================================================

        Toast.makeText(
                this,
                "Account created successfully!",
                Toast.LENGTH_SHORT
        ).show();


        // Clear register fields
        registerName.setText("");
        registerMobile.setText("");
        registerEmail.setText("");
        registerPassword.setText("");


        // Put name into login field
        loginName.setText(name);


        // Go back to login
        flipToLogin();
    }


    // =========================================================
    // LOGIN USER
    // =========================================================

    private void loginUser() {

        String name = loginName.getText()
                .toString()
                .trim();

        String password = loginPassword.getText()
                .toString()
                .trim();


        // =====================================================
        // VALIDATION
        // =====================================================

        if (TextUtils.isEmpty(name)) {

            loginName.setError("Enter your name");
            loginName.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(password)) {

            loginPassword.setError(
                    "Enter your password"
            );

            loginPassword.requestFocus();

            return;
        }


        // =====================================================
        // HASH ENTERED PASSWORD
        // =====================================================

        String hashedPassword =
                hashPassword(password);


        // =====================================================
        // CHECK DATABASE
        // =====================================================

        Cursor cursor = database.rawQuery(

                "SELECT id, name, carbonFootprint, earnedPoints " +
                        "FROM users " +
                        "WHERE name = ? AND password = ?",

                new String[]{
                        name,
                        hashedPassword
                }
        );


        // =====================================================
        // LOGIN SUCCESS
        // =====================================================

        if (cursor.moveToFirst()) {

            int userId = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );

            String userName = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
            );

            double carbonFootprint = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(
                            "carbonFootprint"
                    )
            );

            int earnedPoints = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            "earnedPoints"
                    )
            );


            cursor.close();


            // =================================================
            // SAVE LOGIN SESSION
            // =================================================

            getSharedPreferences(
                    "EcoLoopPrefs",
                    MODE_PRIVATE
            )
                    .edit()
                    .putBoolean("loggedIn", true)
                    .putInt("USER_ID", userId)
                    .putString("USER_NAME", userName)
                    .putFloat(
                            "CARBON_FOOTPRINT",
                            (float) carbonFootprint
                    )
                    .putInt(
                            "EARNED_POINTS",
                            earnedPoints
                    )
                    .apply();


            // =================================================
            // OPEN DASHBOARD
            // =================================================

            Intent intent = new Intent(
                    MainActivity2.this,
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

            intent.putExtra(
                    "CARBON_FOOTPRINT",
                    carbonFootprint
            );

            intent.putExtra(
                    "EARNED_POINTS",
                    earnedPoints
            );


            startActivity(intent);

            finish();

        } else {

            cursor.close();

            Toast.makeText(
                    this,
                    "Invalid name or password!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =========================================================
    // FLIP TO REGISTER
    // =========================================================

    private void flipToRegister() {

        Animation flipOut =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.flip_out
                );

        Animation flipIn =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.flip_in
                );


        loginForm.startAnimation(flipOut);

        flipOut.setAnimationListener(
                new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(
                            Animation animation) {
                    }


                    @Override
                    public void onAnimationEnd(
                            Animation animation) {

                        loginForm.setVisibility(
                                android.view.View.GONE
                        );

                        registerForm.setVisibility(
                                android.view.View.VISIBLE
                        );

                        registerForm.startAnimation(
                                flipIn
                        );
                    }


                    @Override
                    public void onAnimationRepeat(
                            Animation animation) {
                    }
                }
        );
    }


    // =========================================================
    // FLIP TO LOGIN
    // =========================================================

    private void flipToLogin() {

        Animation flipOut =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.flip_out
                );

        Animation flipIn =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.flip_in
                );


        registerForm.startAnimation(flipOut);


        flipOut.setAnimationListener(
                new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(
                            Animation animation) {
                    }


                    @Override
                    public void onAnimationEnd(
                            Animation animation) {

                        registerForm.setVisibility(
                                android.view.View.GONE
                        );

                        loginForm.setVisibility(
                                android.view.View.VISIBLE
                        );

                        loginForm.startAnimation(
                                flipIn
                        );
                    }


                    @Override
                    public void onAnimationRepeat(
                            Animation animation) {
                    }
                }
        );
    }


    // =========================================================
    // SHA-256 PASSWORD HASH
    // =========================================================

    private String hashPassword(String password) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");


            byte[] hash =
                    digest.digest(
                            password.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );


            StringBuilder hexString =
                    new StringBuilder();


            for (byte b : hash) {

                String hex =
                        Integer.toHexString(
                                0xff & b
                        );

                if (hex.length() == 1) {

                    hexString.append('0');
                }

                hexString.append(hex);
            }


            return hexString.toString();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }


    // =========================================================
    // CLOSE DATABASE
    // =========================================================

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (database != null &&
                database.isOpen()) {

            database.close();
        }
    }
}