package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ApprovedHolidayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_holiday);

        Button btnDone = findViewById(R.id.btnDone2);
        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(ApprovedHolidayActivity.this, HolidayRequestActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
