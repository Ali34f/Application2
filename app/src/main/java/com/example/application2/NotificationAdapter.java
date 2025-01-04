package com.example.application2;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application2.model.AdminNotification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context context;
    private final List<AdminNotification> notificationList;

    // Constructor
    public NotificationAdapter(Context context, List<AdminNotification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminNotification notification = notificationList.get(position);

        // Set title, message, and timestamp
        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());
        holder.timeTextView.setText(
                DateFormat.format("hh:mm a", notification.getTimestamp()) // Format timestamp
        );

        // Show or hide the unread indicator
        if (notification.isRead()) {
            holder.unreadIndicator.setVisibility(View.GONE); // Hide if read
        } else {
            holder.unreadIndicator.setVisibility(View.VISIBLE); // Show if unread
        }

        // Mark notification as read when clicked
        holder.itemView.setOnClickListener(v -> {
            if (!notification.isRead()) {
                notification.setRead(true); // Update notification to read
                notifyItemChanged(position); // Refresh the specific item
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // ViewHolder class
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, messageTextView, timeTextView;
        View unreadIndicator; // Unread indicator view

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notificationTitle);
            messageTextView = itemView.findViewById(R.id.notificationMessage);
            timeTextView = itemView.findViewById(R.id.notificationTime);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator); // Initialize unread indicator
        }
    }
}
