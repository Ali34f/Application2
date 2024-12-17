package com.example.application2;

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
        etReason = findViewById(R.id.etReason);
        etHolidayStartDate = findViewById(R.id.etHolidayStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etAdditionalInfo = findViewById(R.id.etAdditionalComments);
        approveButton = findViewById(R.id.approveButton);
        rejectButton = findViewById(R.id.rejectButton);

        // Load request details
        loadHolidayRequest();

        // Approve button functionality
        approveButton.setOnClickListener(v -> updateRequestStatus("Approved"));

        // Reject button functionality
        rejectButton.setOnClickListener(v -> updateRequestStatus("Rejected"));
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
     * @param status The new status to set (e.g., "Approved" or "Rejected").
     */
    private void updateRequestStatus(String status) {
        int rowsUpdated = dbHelper.updateHolidayRequestStatus(requestId, status);
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Request " + status + " successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity after successful update
        } else {
            Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
        }
    }
}
