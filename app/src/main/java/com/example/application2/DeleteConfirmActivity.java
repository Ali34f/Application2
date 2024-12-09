package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DeleteConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirmation);

        Button btnDone = findViewById(R.id.btnDone3);

        // Done Button Functionality
        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(DeleteConfirmActivity.this, EmployeeManagementActivity.class);
            // Clear the back stack to avoid returning to the delete confirmation screen
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
