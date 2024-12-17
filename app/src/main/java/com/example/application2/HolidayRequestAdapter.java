package com.example.application2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for displaying a list of holiday requests in a RecyclerView.
 */
public class HolidayRequestAdapter extends RecyclerView.Adapter<HolidayRequestAdapter.ViewHolder> {

    private final List<HolidayRequest> holidayRequests;
    private final Context context;

    /**
     * Constructor to initialize the adapter with context and holiday requests list.
     *
     * @param context          The context of the calling activity.
     * @param holidayRequests  The list of holiday requests to display.
     */
    public HolidayRequestAdapter(Context context, List<HolidayRequest> holidayRequests) {
        this.context = context;
        this.holidayRequests = holidayRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_holiday_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HolidayRequest request = holidayRequests.get(position);

        // Bind data to the views
        holder.employeeName.setText(request.getEmployeeName());
        holder.requestDate.setText(String.format("Request: %s - %s", request.getHolidayStartDate(), request.getEndDate()));
        holder.status.setText(String.format("Status: %s", request.getStatus()));

        // Set click listener for the view button
        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewHolidayRequestActivity.class);
            intent.putExtra("REQUEST_ID", request.getRequestId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return holidayRequests != null ? holidayRequests.size() : 0;
    }

    /**
     * ViewHolder class to hold and bind views for each holiday request item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName;
        TextView requestDate;
        TextView status;
        Button viewButton;

        /**
         * Constructor to initialize views from the item layout.
         *
         * @param itemView The item view for each holiday request.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.requestName);
            requestDate = itemView.findViewById(R.id.requestDate);
            status = itemView.findViewById(R.id.requestStatus);
            viewButton = itemView.findViewById(R.id.viewButton);
        }
    }
}
