package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RestConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_confirmation);

        // Initialize Done button
        Button btnDone = findViewById(R.id.btnDone);

        // Done button functionality
        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(RestConfirmActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
