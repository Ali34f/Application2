package com.example.application2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HolidayRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HolidayRequestAdapter adapter;
    private List<HolidayRequest> holidayRequests;
    private DatabaseHelper dbHelper;
    private ImageView backButton;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_requests);

        // Initialize views
        initializeUI();

        // Load holiday requests
        loadHolidayRequests();

        // Back button functionality
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Initialize UI components.
     */
    private void initializeUI() {
        recyclerView = findViewById(R.id.recyclerViewHolidayRequests);
        backButton = findViewById(R.id.backButton);
        emptyView = findViewById(R.id.emptyView);
        dbHelper = new DatabaseHelper(this);

        // Set RecyclerView properties
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Load holiday requests from the database and set up RecyclerView.
     */
    private void loadHolidayRequests() {
        holidayRequests = dbHelper.getAllHolidayRequests();

        if (holidayRequests != null && !holidayRequests.isEmpty()) {
            adapter = new HolidayRequestAdapter(this, holidayRequests);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            // Show empty view if no holiday requests are available
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No holiday requests available.");
            Toast.makeText(this, "No holiday requests found", Toast.LENGTH_SHORT).show();
        }
    }
}
