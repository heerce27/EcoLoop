package com.example.ecoloop;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity2 extends AppCompatActivity {

    // --------------------------------------------------
    // LOGIN FIELDS
    // --------------------------------------------------

    private EditText loginName;
    private EditText loginPassword;
    private Button btnLogin;
    private TextView btnGoRegister;

    // --------------------------------------------------
    // REGISTER FIELDS
    // --------------------------------------------------

    private EditText registerName;
    private EditText registerMobile;
    private EditText registerEmail;
    private EditText registerPassword;

    private Button btnRegister;
    private TextView btnGoLogin;

    // --------------------------------------------------
    // FORM CONTAINERS
    // --------------------------------------------------

    private View loginForm;
    private View registerForm;

    // --------------------------------------------------
    // DATABASE
    // --------------------------------------------------

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        initializeViews();
        createDatabase();
        setupListeners();
    }

    // ==================================================
    // INITIALIZE VIEWS
    // ==================================================

    private void initializeViews() {

        // ---------------- LOGIN ----------------

        loginName = findViewById(R.id.loginName);

        loginPassword = findViewById(R.id.loginPassword);

        btnLogin = findViewById(R.id.btnLogin);

        btnGoRegister = findViewById(R.id.btnGoRegister);


        // ---------------- REGISTER ----------------

        registerName = findViewById(R.id.registerName);

        registerMobile = findViewById(R.id.registerMobile);

        registerEmail = findViewById(R.id.registerEmail);

        registerPassword = findViewById(R.id.registerPassword);

        btnRegister = findViewById(R.id.btnRegister);

        btnGoLogin = findViewById(R.id.btnGoLogin);


        // ---------------- FORMS ----------------

        loginForm = findViewById(R.id.loginForm);

        registerForm = findViewById(R.id.registerForm);
    }

    // ==================================================
    // CREATE DATABASE
    // ==================================================

    private void createDatabase() {

        database = openOrCreateDatabase(
                "EcoLoopDB",
                Context.MODE_PRIVATE,
                null
        );

        /*
         * users table
         *
         * id       -> Automatically generated unique ID
         * name     -> User name
         * mobile   -> Mobile number
         * email    -> Email address
         * password -> Hashed password
         */

        database.execSQL(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT UNIQUE," +
                        "mobile TEXT," +
                        "email TEXT," +
                        "password TEXT)"
        );
    }

    // ==================================================
    // BUTTON LISTENERS
    // ==================================================

    private void setupListeners() {

        // ---------------- GO TO REGISTER ----------------

        btnGoRegister.setOnClickListener(v -> {
            flipToRegister();
        });


        // ---------------- GO TO LOGIN ----------------

        btnGoLogin.setOnClickListener(v -> {
            flipToLogin();
        });


        // ---------------- REGISTER ----------------

        btnRegister.setOnClickListener(v -> {
            registerUser();
        });


        // ---------------- LOGIN ----------------

        btnLogin.setOnClickListener(v -> {
            loginUser();
        });
    }

    // ==================================================
    // REGISTER USER
    // ==================================================

    private void registerUser() {

        String name =
                registerName.getText().toString().trim();

        String mobile =
                registerMobile.getText().toString().trim();

        String email =
                registerEmail.getText().toString().trim();

        String password =
                registerPassword.getText().toString();


        // --------------------------------------------------
        // NAME VALIDATION
        // --------------------------------------------------

        if (TextUtils.isEmpty(name)) {

            registerName.setError("Enter your name");

            registerName.requestFocus();

            return;
        }


        // --------------------------------------------------
        // MOBILE VALIDATION
        // --------------------------------------------------

        if (TextUtils.isEmpty(mobile)) {

            registerMobile.setError(
                    "Enter mobile number"
            );

            registerMobile.requestFocus();

            return;
        }


        if (mobile.length() != 10) {

            registerMobile.setError(
                    "Enter valid 10 digit mobile number"
            );

            registerMobile.requestFocus();

            return;
        }


        // --------------------------------------------------
        // EMAIL VALIDATION
        // --------------------------------------------------

        if (TextUtils.isEmpty(email)) {

            registerEmail.setError(
                    "Enter email address"
            );

            registerEmail.requestFocus();

            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            registerEmail.setError(
                    "Enter valid email address"
            );

            registerEmail.requestFocus();

            return;
        }


        // --------------------------------------------------
        // PASSWORD VALIDATION
        // --------------------------------------------------

        if (TextUtils.isEmpty(password)) {

            registerPassword.setError(
                    "Enter password"
            );

            registerPassword.requestFocus();

            return;
        }


        if (password.length() < 6) {

            registerPassword.setError(
                    "Password must be at least 6 characters"
            );

            registerPassword.requestFocus();

            return;
        }


        // --------------------------------------------------
        // CHECK EXISTING USER
        // --------------------------------------------------

        Cursor cursor = database.rawQuery(
                "SELECT * FROM users WHERE name = ?",
                new String[]{name}
        );


        if (cursor.moveToFirst()) {

            cursor.close();

            Toast.makeText(
                    this,
                    "User already registered!",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        cursor.close();


        // --------------------------------------------------
        // HASH PASSWORD
        // --------------------------------------------------

        String hashedPassword =
                hashPassword(password);


        // --------------------------------------------------
        // INSERT USER
        // --------------------------------------------------

        database.execSQL(
                "INSERT INTO users " +
                        "(name, mobile, email, password) " +
                        "VALUES (?, ?, ?, ?)",

                new Object[]{
                        name,
                        mobile,
                        email,
                        hashedPassword
                }
        );


        // --------------------------------------------------
        // SUCCESS MESSAGE
        // --------------------------------------------------

        Toast.makeText(
                this,
                "Registration successful!",
                Toast.LENGTH_SHORT
        ).show();


        // --------------------------------------------------
        // CLEAR REGISTER FIELDS
        // --------------------------------------------------

        registerName.setText("");

        registerMobile.setText("");

        registerEmail.setText("");

        registerPassword.setText("");


        // --------------------------------------------------
        // GO BACK TO LOGIN
        // --------------------------------------------------

        flipToLogin();


        // Put registered name into login

        loginName.setText(name);

        loginPassword.requestFocus();
    }

    // ==================================================
    // LOGIN USER
    // ==================================================

    private void loginUser() {

        String name =
                loginName.getText().toString().trim();

        String password =
                loginPassword.getText().toString();


        // --------------------------------------------------
        // NAME VALIDATION
        // --------------------------------------------------

        if (TextUtils.isEmpty(name)) {

            loginName.setError(
                    "Enter your name"
            );

            loginName.requestFocus();

            return;
        }


        // --------------------------------------------------
        // PASSWORD VALIDATION
        // --------------------------------------------------

        if (TextUtils.isEmpty(password)) {

            loginPassword.setError(
                    "Enter password"
            );

            loginPassword.requestFocus();

            return;
        }


        // --------------------------------------------------
        // HASH PASSWORD
        // --------------------------------------------------

        String hashedPassword =
                hashPassword(password);


        // --------------------------------------------------
        // CHECK USER
        // --------------------------------------------------

        Cursor cursor = database.rawQuery(
                "SELECT * FROM users " +
                        "WHERE name = ? AND password = ?",

                new String[]{
                        name,
                        hashedPassword
                }
        );


        // --------------------------------------------------
        // LOGIN SUCCESS
        // --------------------------------------------------

        if (cursor.moveToFirst()) {

            cursor.close();

            Toast.makeText(
                    this,
                    "Login successful! 🌱",
                    Toast.LENGTH_SHORT
            ).show();


            Intent intent = new Intent(
                    MainActivity2.this,
                    MainActivity4.class
            );

            startActivity(intent);

            finish();
        }


        // --------------------------------------------------
        // LOGIN FAILED
        // --------------------------------------------------

        else {

            cursor.close();

            Toast.makeText(
                    this,
                    "Invalid name or password!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // ==================================================
    // FLIP TO REGISTER
    // ==================================================

    private void flipToRegister() {

        Animation outAnimation =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.flip_out
                );

        Animation inAnimation =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.flip_in
                );


        loginForm.startAnimation(outAnimation);

        loginForm.setVisibility(View.GONE);

        registerForm.setVisibility(View.VISIBLE);

        registerForm.startAnimation(inAnimation);
    }

    // ==================================================
    // FLIP TO LOGIN
    // ==================================================

    private void flipToLogin() {

        Animation outAnimation =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.flip_out
                );

        Animation inAnimation =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.flip_in
                );


        registerForm.startAnimation(outAnimation);

        registerForm.setVisibility(View.GONE);

        loginForm.setVisibility(View.VISIBLE);

        loginForm.startAnimation(inAnimation);
    }

    // ==================================================
    // PASSWORD HASH
    // ==================================================

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

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }

    // ==================================================
    // CLOSE DATABASE
    // ==================================================

    @Override
    protected void onDestroy() {

        if (database != null) {
            database.close();
        }

        super.onDestroy();

    }

}

