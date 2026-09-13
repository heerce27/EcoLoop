package com.example.ecoloop;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity8 extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText edtQuantity;
    private Button btnCheck;
    private Button btnBack;
    private TextView txtRecommendation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        edtQuantity = findViewById(R.id.edtQuantity);
        btnCheck = findViewById(R.id.btnCheck);
        btnBack = findViewById(R.id.btnBack);
        txtRecommendation = findViewById(R.id.txtRecommendation);

        setupSpinner();

        btnBack.setOnClickListener(v -> finish());

        btnCheck.setOnClickListener(v -> {

            String category =
                    spinnerCategory.getSelectedItem().toString();

            String quantity =
                    edtQuantity.getText().toString().trim();

            if (category.equals("Select Category")) {

                Toast.makeText(
                        MainActivity8.this,
                        "Please select a category",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (quantity.isEmpty()) {

                edtQuantity.setError("Please enter quantity");
                edtQuantity.requestFocus();

                return;
            }

            String recommendation =
                    getRecommendation(category);

            txtRecommendation.setText(recommendation);
        });
    }

    private void setupSpinner() {

        String[] categories = {
                "Select Category",
                "Clothing",
                "Electronics",
                "Food",
                "Furniture",
                "Plastic Products",
                "Daily Use Products"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(adapter);
    }

    private String getRecommendation(String category) {

        switch (category) {

            case "Clothing":
                return "🌱 Eco Recommendation\n\n"
                        + "Before buying clothing, consider whether "
                        + "you really need it. Choose durable clothes "
                        + "that can be used for a long time. Prefer "
                        + "sustainable or recycled materials when possible. "
                        + "Avoid unnecessary purchases caused by short-term "
                        + "fashion trends. Reusing and donating clothes can "
                        + "also help reduce textile waste.";

            case "Electronics":
                return "🔋 Eco Recommendation\n\n"
                        + "Before buying electronics, check their energy "
                        + "efficiency and expected lifespan. Prefer products "
                        + "that consume less electricity and can be repaired. "
                        + "Also check whether spare parts are available. "
                        + "When the product reaches the end of its useful "
                        + "life, dispose of it through proper e-waste "
                        + "recycling channels.";

            case "Food":
                return "🥗 Eco Recommendation\n\n"
                        + "Buy only the amount of food you actually need "
                        + "to reduce food waste. Prefer local and seasonal "
                        + "food when possible. Avoid unnecessary packaging "
                        + "and choose reusable bags or containers. Proper "
                        + "storage can also help food last longer.";

            case "Furniture":
                return "🪑 Eco Recommendation\n\n"
                        + "Choose furniture that is strong, durable and "
                        + "repairable. Consider responsibly sourced "
                        + "materials and avoid replacing furniture "
                        + "unnecessarily. Second-hand or refurbished "
                        + "furniture can also be a sustainable choice.";

            case "Plastic Products":
                return "♻ Eco Recommendation\n\n"
                        + "Before buying a plastic product, check whether "
                        + "a reusable or plastic-free alternative is "
                        + "available. Avoid single-use plastic whenever "
                        + "possible. Prefer products that can be reused "
                        + "for a long time and properly recycled after use.";

            case "Daily Use Products":
                return "🌍 Eco Recommendation\n\n"
                        + "Think about whether you really need the product. "
                        + "Choose durable, reusable and repairable products "
                        + "instead of low-quality items that need frequent "
                        + "replacement. Also check the amount of packaging "
                        + "before making your purchase.";

            default:
                return "Please select a valid category.";
        }
    }
}