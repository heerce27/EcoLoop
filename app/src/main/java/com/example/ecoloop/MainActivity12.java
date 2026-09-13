 package com.example.ecoloop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity12 extends AppCompatActivity {

    EditText editAdminPassword;
    Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main12);

        editAdminPassword = findViewById(R.id.editAdminPassword);
        btnVerify = findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(v -> {

            String password =
                    editAdminPassword.getText()
                            .toString()
                            .trim();

            if (password.equals("admin12345")) {

                Toast.makeText(
                        MainActivity12.this,
                        "Admin Login Successful",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        MainActivity12.this,
                        MainActivity11.class
                );

                startActivity(intent);

            } else {

                Toast.makeText(
                        MainActivity12.this,
                        "Wrong Admin Password",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}

