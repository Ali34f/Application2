package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for confirming the deletion of an employee.
 */
public class ConfirmDeleteEmployeeActivity extends AppCompatActivity {

    private int employeeId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirm_employee);

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Get the employee ID from the intent
        employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);

        // Initialize buttons
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        // Set button click listeners
        setButtonListeners(btnCancel, btnConfirm);
    }

    /**
     * Set button click listeners for cancel and confirm actions.
     *
     * @param btnCancel  The cancel button.
     * @param btnConfirm The confirm button.
     */
    private void setButtonListeners(Button btnCancel, Button btnConfirm) {
        // Cancel button to return to the previous activity
        btnCancel.setOnClickListener(v -> finish());

        // Confirm button to delete the employee and navigate to DeleteConfirmActivity
        btnConfirm.setOnClickListener(v -> confirmDeletion());
    }

    /**
     * Confirm the deletion of the employee.
     */
    private void confirmDeletion() {
        if (employeeId == -1) {
            showToast("Invalid employee ID");
            return;
        }

        int rowsDeleted = dbHelper.deleteEmployee(employeeId);

        if (rowsDeleted > 0) {
            showToast("Employee deleted successfully");
            navigateToDeleteConfirmActivity();
        } else {
            showToast("Error deleting employee");
        }
    }

    /**
     * Navigate to the DeleteConfirmActivity after successful deletion.
     */
    private void navigateToDeleteConfirmActivity() {
        Intent intent = new Intent(this, DeleteConfirmActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Utility method to show toast messages.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
