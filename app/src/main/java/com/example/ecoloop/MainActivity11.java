package com.example.ecoloop;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity11 extends AppCompatActivity {

    private SQLiteDatabase database;

    private TextView txtTotalUsers;
    private TextView txtTotalActivities;
    private TextView txtTotalPoints;
    private TextView txtTotalCarbon;

    private EditText searchUser;

    private LinearLayout usersContainer;
    private LinearLayout activitiesContainer;

    private Button btnRefresh;
    private Button btnAdminLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main11);

        initializeViews();

        setupDatabase();

        btnRefresh.setOnClickListener(v -> loadDashboard());

        btnAdminLogout.setOnClickListener(v -> logoutAdmin());

        searchUser.setOnEditorActionListener((v, actionId, event) -> {
            loadUsers(
                    searchUser.getText()
                            .toString()
                            .trim()
            );
            return false;
        });

        loadDashboard();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        txtTotalUsers = findViewById(R.id.txtTotalUsers);
        txtTotalActivities = findViewById(R.id.txtTotalActivities);
        txtTotalPoints = findViewById(R.id.txtTotalPoints);
        txtTotalCarbon = findViewById(R.id.txtTotalCarbon);

        searchUser = findViewById(R.id.searchUser);

        usersContainer = findViewById(R.id.usersContainer);
        activitiesContainer = findViewById(R.id.activitiesContainer);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnAdminLogout = findViewById(R.id.btnAdminLogout);
    }


    // =========================================================
    // DATABASE SETUP
    // =========================================================

    private void setupDatabase() {

        database = openOrCreateDatabase(
                "EcoLoopDB",
                MODE_PRIVATE,
                null
        );


        // Create users table if it does not exist
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "email TEXT," +
                        "mobile TEXT," +
                        "password TEXT," +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );


        // Add missing columns to old users table
        addColumnIfMissing(
                "users",
                "email",
                "TEXT"
        );

        addColumnIfMissing(
                "users",
                "mobile",
                "TEXT"
        );

        addColumnIfMissing(
                "users",
                "password",
                "TEXT"
        );

        addColumnIfMissing(
                "users",
                "created_at",
                "DATETIME"
        );


        // Create activities table
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS activities (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "user_id INTEGER," +
                        "user_name TEXT," +
                        "activity_type TEXT," +
                        "carbon_footprint REAL," +
                        "points INTEGER," +
                        "proof_path TEXT," +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );


        // Add missing activity columns
        addColumnIfMissing(
                "activities",
                "user_id",
                "INTEGER"
        );

        addColumnIfMissing(
                "activities",
                "user_name",
                "TEXT"
        );

        addColumnIfMissing(
                "activities",
                "activity_type",
                "TEXT"
        );

        addColumnIfMissing(
                "activities",
                "carbon_footprint",
                "REAL"
        );

        addColumnIfMissing(
                "activities",
                "points",
                "INTEGER"
        );

        addColumnIfMissing(
                "activities",
                "proof_path",
                "TEXT"
        );

        addColumnIfMissing(
                "activities",
                "created_at",
                "DATETIME"
        );
    }


    // =========================================================
    // CHECK AND ADD COLUMN
    // =========================================================

    private void addColumnIfMissing(
            String tableName,
            String columnName,
            String columnType
    ) {

        Cursor cursor = database.rawQuery(
                "PRAGMA table_info(" + tableName + ")",
                null
        );

        boolean exists = false;

        while (cursor.moveToNext()) {

            String existingColumn =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            if (existingColumn.equalsIgnoreCase(columnName)) {
                exists = true;
                break;
            }
        }

        cursor.close();


        if (!exists) {

            database.execSQL(
                    "ALTER TABLE " +
                            tableName +
                            " ADD COLUMN " +
                            columnName +
                            " " +
                            columnType
            );
        }
    }


    // =========================================================
    // LOAD DASHBOARD
    // =========================================================

    private void loadDashboard() {

        loadStatistics();

        loadUsers(
                searchUser.getText()
                        .toString()
                        .trim()
        );

        loadActivities();
    }


    // =========================================================
    // STATISTICS
    // =========================================================

    private void loadStatistics() {

        Cursor cursor;


        // TOTAL USERS
        cursor = database.rawQuery(
                "SELECT COUNT(*) FROM users",
                null
        );

        if (cursor.moveToFirst()) {

            txtTotalUsers.setText(
                    String.valueOf(cursor.getInt(0))
            );
        }

        cursor.close();


        // TOTAL ACTIVITIES
        cursor = database.rawQuery(
                "SELECT COUNT(*) FROM activities",
                null
        );

        if (cursor.moveToFirst()) {

            txtTotalActivities.setText(
                    String.valueOf(cursor.getInt(0))
            );
        }

        cursor.close();


        // TOTAL POINTS
        cursor = database.rawQuery(
                "SELECT COALESCE(SUM(points),0) FROM activities",
                null
        );

        if (cursor.moveToFirst()) {

            txtTotalPoints.setText(
                    String.valueOf(cursor.getInt(0))
            );
        }

        cursor.close();


        // TOTAL CARBON
        cursor = database.rawQuery(
                "SELECT COALESCE(SUM(carbon_footprint),0) " +
                        "FROM activities",
                null
        );

        if (cursor.moveToFirst()) {

            double carbon =
                    cursor.getDouble(0);

            txtTotalCarbon.setText(
                    String.format(
                            "%.2f kg",
                            carbon
                    )
            );
        }

        cursor.close();
    }


    // =========================================================
    // LOAD USERS
    // =========================================================

    private void loadUsers(String keyword) {

        usersContainer.removeAllViews();

        Cursor cursor;


        if (keyword.isEmpty()) {

            cursor = database.rawQuery(
                    "SELECT id,name,email,mobile,password,created_at " +
                            "FROM users ORDER BY id DESC",
                    null
            );

        } else {

            cursor = database.rawQuery(
                    "SELECT id,name,email,mobile,password,created_at " +
                            "FROM users " +
                            "WHERE name LIKE ? " +
                            "ORDER BY id DESC",
                    new String[]{
                            "%" + keyword + "%"
                    }
            );
        }


        if (cursor.getCount() == 0) {

            usersContainer.addView(
                    createInfoText(
                            "No registered users found."
                    )
            );

            cursor.close();

            return;
        }


        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);

            String name = cursor.getString(1);

            String email = cursor.getString(2);

            String mobile = cursor.getString(3);

            String password = cursor.getString(4);

            String createdAt = cursor.getString(5);


            if (email == null) {
                email = "Not available";
            }

            if (mobile == null) {
                mobile = "Not available";
            }

            if (password == null) {
                password = "Not available";
            }

            if (createdAt == null) {
                createdAt = "Not available";
            }


            addUserCard(
                    id,
                    name,
                    email,
                    mobile,
                    password,
                    createdAt
            );
        }

        cursor.close();
    }


    // =========================================================
    // USER CARD
    // =========================================================

    private void addUserCard(
            int id,
            String name,
            String email,
            String mobile,
            String password,
            String createdAt
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                20,
                20,
                20,
                20
        );

        card.setBackgroundColor(
                Color.WHITE
        );


        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                18
        );

        card.setLayoutParams(cardParams);


        card.addView(
                createTitleText(
                        "👤 " + name
                )
        );

        card.addView(
                createNormalText(
                        "User ID: " + id
                )
        );

        card.addView(
                createNormalText(
                        "Email: " + email
                )
        );

        card.addView(
                createNormalText(
                        "Mobile: " + mobile
                )
        );

        card.addView(
                createNormalText(
                        "Password: " + password
                )
        );

        card.addView(
                createNormalText(
                        "Registered: " + createdAt
                )
        );


        addUserStats(
                card,
                id
        );


        Button removeButton =
                new Button(this);

        removeButton.setText(
                "REMOVE USER"
        );

        removeButton.setTextColor(
                Color.WHITE
        );

        removeButton.setBackgroundColor(
                Color.rgb(
                        211,
                        47,
                        47
                )
        );


        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55
                );

        buttonParams.setMargins(
                0,
                15,
                0,
                0
        );

        removeButton.setLayoutParams(
                buttonParams
        );


        removeButton.setOnClickListener(v ->
                confirmDeleteUser(
                        id,
                        name
                )
        );


        card.addView(removeButton);

        usersContainer.addView(card);
    }


    // =========================================================
    // USER STATISTICS
    // =========================================================

    private void addUserStats(
            LinearLayout card,
            int userId
    ) {

        Cursor cursor =
                database.rawQuery(
                        "SELECT COUNT(*)," +
                                "COALESCE(SUM(points),0)," +
                                "COALESCE(SUM(carbon_footprint),0) " +
                                "FROM activities WHERE user_id=?",
                        new String[]{
                                String.valueOf(userId)
                        }
                );


        if (cursor.moveToFirst()) {

            int activities =
                    cursor.getInt(0);

            int points =
                    cursor.getInt(1);

            double carbon =
                    cursor.getDouble(2);


            TextView stats =
                    createNormalText(
                            "Activities: " +
                                    activities +
                                    " | Points: " +
                                    points +
                                    " | Carbon: " +
                                    String.format(
                                            "%.2f kg",
                                            carbon
                                    )
                    );


            stats.setTextColor(
                    Color.rgb(
                            46,
                            125,
                            50
                    )
            );


            card.addView(stats);
        }

        cursor.close();
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    private void confirmDeleteUser(
            int userId,
            String userName
    ) {

        new AlertDialog.Builder(this)

                .setTitle("Remove User?")

                .setMessage(
                        "Are you sure you want to remove " +
                                userName +
                                "?"
                )

                .setNegativeButton(
                        "CANCEL",
                        null
                )

                .setPositiveButton(
                        "REMOVE",
                        (dialog, which) -> {

                            database.delete(
                                    "activities",
                                    "user_id=?",
                                    new String[]{
                                            String.valueOf(userId)
                                    }
                            );


                            database.delete(
                                    "users",
                                    "id=?",
                                    new String[]{
                                            String.valueOf(userId)
                                    }
                            );


                            loadDashboard();
                        }
                )

                .show();
    }


    // =========================================================
    // LOAD ACTIVITIES
    // =========================================================

    private void loadActivities() {

        activitiesContainer.removeAllViews();


        Cursor cursor =
                database.rawQuery(
                        "SELECT id,user_name,activity_type," +
                                "carbon_footprint,points,created_at " +
                                "FROM activities " +
                                "ORDER BY id DESC",
                        null
                );


        if (cursor.getCount() == 0) {

            activitiesContainer.addView(
                    createInfoText(
                            "No activities recorded yet."
                    )
            );

            cursor.close();

            return;
        }


        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);

            String user = cursor.getString(1);

            String activity = cursor.getString(2);

            double carbon = cursor.getDouble(3);

            int points = cursor.getInt(4);

            String date = cursor.getString(5);


            if (user == null) {
                user = "Unknown";
            }

            if (activity == null) {
                activity = "Unknown Activity";
            }


            addActivityCard(
                    id,
                    user,
                    activity,
                    carbon,
                    points,
                    date
            );
        }


        cursor.close();
    }


    // =========================================================
    // ACTIVITY CARD
    // =========================================================

    private void addActivityCard(
            int id,
            String user,
            String activity,
            double carbon,
            int points,
            String date
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                20,
                20,
                20,
                20
        );

        card.setBackgroundColor(
                Color.WHITE
        );


        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                18
        );

        card.setLayoutParams(params);


        card.addView(
                createTitleText(
                        "🌱 " + activity
                )
        );


        card.addView(
                createNormalText(
                        "Activity ID: " + id
                )
        );


        card.addView(
                createNormalText(
                        "User: " + user
                )
        );


        card.addView(
                createNormalText(
                        "Carbon Footprint: " +
                                String.format(
                                        "%.2f kg",
                                        carbon
                                )
                )
        );


        card.addView(
                createNormalText(
                        "Points Earned: " +
                                points
                )
        );


        card.addView(
                createNormalText(
                        "Recorded: " +
                                date
                )
        );


        activitiesContainer.addView(card);
    }


    // =========================================================
    // TEXT HELPERS
    // =========================================================

    private TextView createTitleText(
            String text
    ) {

        TextView view =
                new TextView(this);

        view.setText(text);

        view.setTextSize(18);

        view.setTextColor(
                Color.rgb(
                        27,
                        94,
                        32
                )
        );

        view.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        view.setPadding(
                0,
                0,
                0,
                8
        );

        return view;
    }


    private TextView createNormalText(
            String text
    ) {

        TextView view =
                new TextView(this);

        view.setText(text);

        view.setTextSize(14);

        view.setTextColor(
                Color.DKGRAY
        );

        view.setPadding(
                0,
                4,
                0,
                4
        );

        return view;
    }


    private TextView createInfoText(
            String text
    ) {

        TextView view =
                createNormalText(text);

        view.setGravity(
                Gravity.CENTER
        );

        view.setPadding(
                10,
                30,
                10,
                30
        );

        return view;
    }


    // =========================================================
    // ADMIN LOGOUT
    // =========================================================

    private void logoutAdmin() {

        Intent intent =
                new Intent(
                        MainActivity11.this,
                        MainActivity.class
                );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }


    // =========================================================
    // CLOSE DATABASE
    // =========================================================

    @Override
    protected void onDestroy() {

        if (database != null &&
                database.isOpen()) {

            database.close();
        }

        super.onDestroy();
    }
}
