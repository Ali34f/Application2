package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeEditConfirmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit_confirm);

        // Done button functionality
        Button btnDone = findViewById(R.id.btnDone3);
        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeEditConfirmActivity.this, EmployeeProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
