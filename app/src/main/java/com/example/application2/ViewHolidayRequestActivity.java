package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity to view and manage holiday requests.
 */
public class ViewHolidayRequestActivity extends AppCompatActivity {

    private int requestId;
    private DatabaseHelper dbHelper;

    private EditText etReason, etHolidayStartDate, etEndDate, etAdditionalInfo;
    private Button approveButton, rejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_holiday_request);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Retrieve request ID from the intent
        requestId = getIntent().getIntExtra("REQUEST_ID", -1);

        // Initialize UI components
        initializeUI();

        // Load request details
        loadHolidayRequest();

        // Approve button functionality
        approveButton.setOnClickListener(v -> updateRequestStatus(HolidayRequestStatus.APPROVED));

        // Reject button functionality
        rejectButton.setOnClickListener(v -> updateRequestStatus(HolidayRequestStatus.REJECTED));
    }

    /**
     * Initialize UI components.
     */
    private void initializeUI() {
        etReason = findViewById(R.id.etReason);
        etHolidayStartDate = findViewById(R.id.etHolidayStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etAdditionalInfo = findViewById(R.id.etAdditionalComments);
        approveButton = findViewById(R.id.approveButton);
        rejectButton = findViewById(R.id.rejectButton);
    }

    /**
     * Load the holiday request details and display them in the UI.
     */
    private void loadHolidayRequest() {
        HolidayRequest request = dbHelper.getHolidayRequestById(requestId);

        if (request != null) {
            etReason.setText(request.getReason());
            etHolidayStartDate.setText(request.getHolidayStartDate());
            etEndDate.setText(request.getEndDate());
            etAdditionalInfo.setText(request.getAdditionalInfo());
        } else {
            Toast.makeText(this, "Holiday request not found", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the request is not found
        }
    }

    /**
     * Update the status of the holiday request in the database.
     *
     * @param status The new status to set (APPROVED or REJECTED).
     */
    private void updateRequestStatus(HolidayRequestStatus status) {
        int rowsUpdated = dbHelper.updateHolidayRequestStatus(requestId, status.name());
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Request " + status.name() + " successfully", Toast.LENGTH_SHORT).show();
            navigateToStatusActivity(status);
        } else {
            Toast.makeText(this, "Failed to update request status", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navigate to the appropriate activity based on the status.
     *
     * @param status The new status of the holiday request.
     */
    private void navigateToStatusActivity(HolidayRequestStatus status) {
        Intent intent;
        if (status == HolidayRequestStatus.APPROVED) {
            intent = new Intent(this, ApprovedHolidayActivity.class);
        } else {
            intent = new Intent(this, DeniedHolidayActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
