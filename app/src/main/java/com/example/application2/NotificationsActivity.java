package com.example.application2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application2.model.AdminNotification;

import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Back Button functionality
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Close activity and go back

        // Find the RecyclerView and loading TextView
        TextView tempTextView = findViewById(R.id.tempTextView);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotifications);

        // Fetch admin notifications from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<AdminNotification> notifications = databaseHelper.getUnreadAdminNotifications();

        // Check if there are notifications
        if (!notifications.isEmpty()) {
            tempTextView.setVisibility(View.GONE); // Hide the loading message

            // Set up RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            NotificationAdapter adapter = new NotificationAdapter(this, notifications);
            recyclerView.setAdapter(adapter);
        } else {
            tempTextView.setVisibility(View.VISIBLE); // Show the no-notifications message
            tempTextView.setText("No new notifications."); // Customize the message
        }
    }
}
