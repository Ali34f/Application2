package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeRestConfirmActivity extends AppCompatActivity {

    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_rest_confirm);

        // Initialize UI elements
        doneButton = findViewById(R.id.btnDone2);

        // Set done button click listener
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to EmployeeLoginActivity
                Intent intent = new Intent(EmployeeRestConfirmActivity.this, EmployeeLoginActivity.class);
                startActivity(intent);
                finish(); // Close this activity to prevent navigating back
            }
        });
    }
}
