package com.example.application2.model;

/**
 * Model class representing an admin notification.
 */
public class AdminNotification {
    private String title;     // Title of the notification
    private String message;   // Message content of the notification
    private boolean isRead;   // Whether the notification has been read
    private long timestamp;   // Time the notification was created (milliseconds)

    /**
     * Constructor for AdminNotification.
     *
     * @param title     The title of the notification.
     * @param message   The message content of the notification.
     * @param isRead    Whether the notification has been read.
     * @param timestamp The time the notification was created.
     */
    public AdminNotification(String title, String message, boolean isRead, long timestamp) {
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AdminNotification{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                ", timestamp=" + timestamp +
                '}';
    }
}
