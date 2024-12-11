package com.example.application2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a list of employees in a RecyclerView.
 */
public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private final Context context;
    private List<Employee> employeeList;

    // Constructor
    public EmployeeAdapter(Context context, List<Employee> employeeList) {
        this.context = context;
        this.employeeList = (employeeList != null) ? employeeList : new ArrayList<>();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);

        // Set data to views
        holder.tvEmployeeName.setText(employee.getName());
        holder.tvEmployeePosition.setText(employee.getPosition());

        // Handle view button click
        holder.btnViewEmployee.setOnClickListener(v -> navigateToViewEmployee(employee.getId()));
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    /**
     * ViewHolder class to hold and bind the views for each employee item.
     */
    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmployeeName, tvEmployeePosition;
        Button btnViewEmployee;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
            tvEmployeePosition = itemView.findViewById(R.id.tvEmployeePosition);
            btnViewEmployee = itemView.findViewById(R.id.btnViewEmployee);
        }
    }

    /**
     * Update the list dynamically (useful for filtering/searching).
     *
     * @param newEmployeeList New list of employees to display.
     */
    public void updateEmployeeList(List<Employee> newEmployeeList) {
        this.employeeList = (newEmployeeList != null) ? newEmployeeList : new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * Helper method to handle navigation to ViewEmployeeActivity.
     *
     * @param employeeId The ID of the employee to view.
     */
    private void navigateToViewEmployee(int employeeId) {
        if (employeeId < 0) {
            Toast.makeText(context, "Invalid employee ID", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent intent = new Intent(context, ViewEmployeeActivity.class);
            intent.putExtra("EMPLOYEE_ID", employeeId);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Error navigating to employee details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
