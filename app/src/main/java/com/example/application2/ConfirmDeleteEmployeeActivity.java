package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmDeleteEmployeeActivity extends AppCompatActivity {

    private int employeeId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirm_employee);

        dbHelper = new DatabaseHelper(this);

        // Get the employee ID from the intent
        employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        // Cancel button to return to the previous activity
        btnCancel.setOnClickListener(v -> finish());

        // Confirm button to delete the employee and navigate to DeleteConfirmActivity
        btnConfirm.setOnClickListener(v -> {
            if (employeeId != -1) {
                int rowsDeleted = dbHelper.deleteEmployee(employeeId);
                if (rowsDeleted > 0) {
                    Toast.makeText(this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmDeleteEmployeeActivity.this, DeleteConfirmActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Error deleting employee", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid employee ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
