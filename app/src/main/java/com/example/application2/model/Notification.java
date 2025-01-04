package com.example.application2.model;

/**
 * Model class representing a notification.
 */
public class Notification {
    private int id;           // Unique identifier for the notification
    private String title;     // Title of the notification
    private String message;   // Message content of the notification

    /**
     * Constructor for Notification.
     *
     * @param id      The unique identifier for the notification.
     * @param title   The title of the notification.
     * @param message The message content of the notification.
     */
    public Notification(int id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    // Getters for accessing the notification properties

    /**
     * Get the notification ID.
     *
     * @return The ID of the notification.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the notification title.
     *
     * @return The title of the notification.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the notification message.
     *
     * @return The message of the notification.
     */
    public String getMessage() {
        return message;
    }

    // Optional: Add a toString method for debugging purposes
    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
