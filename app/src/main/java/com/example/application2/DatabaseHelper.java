package com.example.application2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.application2.model.Employee;
import com.example.application2.model.Notification;
import com.example.application2.model.AdminNotification;


import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper class for managing the employee database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database constants
    private static final String DATABASE_NAME = "employees.db";
    private static final int DATABASE_VERSION = 11;

    // Table Name and Columns
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POSITION = "position";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_SALARY = "salary";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_LEAVES = "leaves";

    // Table Name and Columns for Admins
    private static final String TABLE_ADMINS = "admins";
    private static final String COLUMN_ADMIN_ID = "id";
    private static final String COLUMN_ADMIN_USERNAME = "username";
    private static final String COLUMN_ADMIN_PASSWORD = "password";

    // Columns for Notifications Table
    private static final String TABLE_NOTIFICATIONS = "notifications";
    private static final String COLUMN_NOTIFICATION_ID = "notification_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_RECIPIENT = "recipient";
    private static final String COLUMN_READ_STATUS = "read_status";

    // Holiday Requests Table Name and Columns
    private static final String TABLE_HOLIDAY_REQUESTS = "holiday_requests";
    private static final String COLUMN_REQUEST_ID = "request_id";
    private static final String COLUMN_EMPLOYEE_NAME = "employee_name";
    private static final String COLUMN_REASON = "reason";
    private static final String COLUMN_HOLIDAY_START_DATE = "holiday_start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_ADDITIONAL_INFO = "additional_info";
    private static final String COLUMN_STATUS = "status";

    // SQL statements to create tables
    private static final String CREATE_TABLE_EMPLOYEES =
            "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_POSITION + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_SALARY + " REAL, " +
                    COLUMN_START_DATE + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_LEAVES + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_HOLIDAY_REQUESTS =
            "CREATE TABLE " + TABLE_HOLIDAY_REQUESTS + " (" +
                    COLUMN_REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMPLOYEE_NAME + " TEXT NOT NULL, " +
                    COLUMN_HOLIDAY_START_DATE + " TEXT NOT NULL, " +
                    COLUMN_END_DATE + " TEXT NOT NULL, " +
                    COLUMN_ADDITIONAL_INFO + " TEXT, " +
                    COLUMN_REASON + " TEXT NOT NULL, " +
                    COLUMN_STATUS + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_ADMINS =
            "CREATE TABLE " + TABLE_ADMINS + " (" +
                    COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ADMIN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_ADMIN_PASSWORD + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_NOTIFICATIONS =
            "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                    COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_MESSAGE + " TEXT NOT NULL, " +
                    COLUMN_RECIPIENT + " TEXT NOT NULL, " +
                    COLUMN_READ_STATUS + " TEXT DEFAULT 'unread')";

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_EMPLOYEES);
            db.execSQL(CREATE_TABLE_ADMINS);
            db.execSQL(CREATE_TABLE_HOLIDAY_REQUESTS);
            db.execSQL(CREATE_TABLE_NOTIFICATIONS);
            ensureDefaultAdminCredentials(db);
            Log.d(TAG, "Database created successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating database: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMINS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOLIDAY_REQUESTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
            onCreate(db);
            Log.d(TAG, "Database upgraded successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
        }
    }

    private void ensureDefaultAdminCredentials(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_ADMINS, null, COLUMN_ADMIN_USERNAME + " = ?", new String[]{"admin"}, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                ContentValues adminValues = new ContentValues();
                adminValues.put(COLUMN_ADMIN_USERNAME, "admin");
                adminValues.put(COLUMN_ADMIN_PASSWORD, "admin123");
                db.insert(TABLE_ADMINS, null, adminValues);
                Log.d(TAG, "Default admin credentials added.");
            } else {
                Log.d(TAG, "Default admin credentials already exist.");
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error ensuring default admin credentials: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    // ========================= Notification Management =========================

    public void storeNotification(String title, String message, String recipient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_MESSAGE, message);
        values.put(COLUMN_RECIPIENT, recipient);
        db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
    }

    public List<Notification> getUnreadNotifications(String recipient) {
        List<Notification> notifications = new ArrayList<>();
        if (recipient == null || recipient.isEmpty()) {
            Log.e(TAG, "Invalid recipient for notifications");
            return notifications;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_NOTIFICATIONS + " WHERE " + COLUMN_RECIPIENT + " = ? AND " +
                            COLUMN_READ_STATUS + " = 'unread'", new String[]{recipient});

            while (cursor.moveToNext()) {
                notifications.add(new Notification(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE))
                ));
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving unread notifications: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return notifications;
    }

    public void markNotificationAsRead(int notificationId) {
        if (notificationId <= 0) {
            Log.w(TAG, "Invalid notification ID: " + notificationId);
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_READ_STATUS, "read");
        db.update(TABLE_NOTIFICATIONS, values, COLUMN_NOTIFICATION_ID + " = ?", new String[]{String.valueOf(notificationId)});
        db.close();
    }

    public void markAllNotificationsAsRead(String recipient) {
        if (recipient == null || recipient.isEmpty()) {
            Log.w(TAG, "Invalid recipient for marking notifications as read");
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_READ_STATUS, "read");
            int rowsUpdated = db.update(TABLE_NOTIFICATIONS, values, COLUMN_RECIPIENT + " = ?", new String[]{recipient});
            Log.d(TAG, "Notifications marked as read for recipient: " + recipient + " (" + rowsUpdated + " updated)");
        } catch (SQLException e) {
            Log.e(TAG, "Error marking notifications as read: " + e.getMessage());
        } finally {
            db.close();
        }

    }

    /**
     * Retrieve all unread notifications for the admin.
     *
     * @return A list of unread admin notifications.
     */
    public List<AdminNotification> getUnreadAdminNotifications() {
        List<AdminNotification> notifications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_NOTIFICATIONS +
                            " WHERE " + COLUMN_RECIPIENT + " = 'admin' AND " + COLUMN_READ_STATUS + " = 'unread'", null);

            while (cursor != null && cursor.moveToNext()) {
                notifications.add(new AdminNotification(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE)),
                        false, // Default to unread since the query filters only unread notifications
                        System.currentTimeMillis() // Replace with a timestamp if stored in the DB
                ));
            }
            Log.d(TAG, "Unread admin notifications fetched successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error fetching unread admin notifications: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return notifications;
    }




// ========================= Login and Reset Management =========================


    /**
     * Validate admin login credentials.
     *
     * @param username The admin username.
     * @param password The admin password.
     * @return True if the credentials are valid, false otherwise.
     */
    public boolean validateAdminLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_ADMINS,
                    new String[]{COLUMN_ADMIN_ID}, // Only need ID for validation
                    COLUMN_ADMIN_USERNAME + " = ? AND " + COLUMN_ADMIN_PASSWORD + " = ?",
                    new String[]{username, password},
                    null, null, null);

            return cursor != null && cursor.moveToFirst();
        } catch (SQLException e) {
            Log.e(TAG, "Error validating admin login: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }




    /**
     * Update the password for an employee based on their email.
     *
     * @param email       The email of the employee.
     * @param newPassword The new password to set.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updatePasswordByEmail(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isUpdated = false;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PASSWORD, newPassword);

            int rowsAffected = db.update(TABLE_EMPLOYEES, values, COLUMN_EMAIL + " = ?", new String[]{email});
            isUpdated = rowsAffected > 0;

            if (isUpdated) {
                Log.d("DatabaseHelper", "Password updated successfully for email: " + email);
            } else {
                Log.d("DatabaseHelper", "Email not found: " + email);
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error updating password: " + e.getMessage());
        } finally {
            db.close();
        }

        return isUpdated;
    }


    /**
     * Update an employee's profile details (for employee use only).
     *
     * @param employee The updated Employee object with new details.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateEmployeeProfile(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isUpdated = false;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, employee.getName());
            values.put(COLUMN_POSITION, employee.getPosition());
            values.put(COLUMN_PHONE, employee.getPhone());
            values.put(COLUMN_EMAIL, employee.getEmail());
            values.put(COLUMN_PASSWORD, employee.getPassword());
            values.put(COLUMN_LEAVES, employee.getLeaves());

            int rowsAffected = db.update(TABLE_EMPLOYEES, values, COLUMN_EMAIL + " = ?", new String[]{employee.getEmail()});
            isUpdated = rowsAffected > 0;
            Log.d(TAG, "Employee profile updated successfully for email: " + employee.getEmail());
        } catch (SQLException e) {
            Log.e(TAG, "Error updating employee profile: " + e.getMessage());
        } finally {
            db.close();
        }

        return isUpdated;
    }

    // ========================= CRUD operation in Database Management =========================

    /**
     * Search for employees by name or email.
     *
     * @param query The search query string.
     * @return A list of employees matching the search criteria.
     */
    public List<Employee> searchEmployees(String query) {
        List<Employee> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_EMPLOYEES,
                    null,
                    COLUMN_NAME + " LIKE ? OR " + COLUMN_EMAIL + " LIKE ?",
                    new String[]{"%" + query + "%", "%" + query + "%"},
                    null, null, COLUMN_NAME + " ASC");

            while (cursor != null && cursor.moveToNext()) {
                employeeList.add(cursorToEmployee(cursor));
            }
            Log.d(TAG, "Employees matching query fetched successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error searching employees: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return employeeList;
    }

    /**
     * Add a new employee to the database.
     *
     * @param employee The Employee object to add.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long addEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = createContentValues(employee);
            result = db.insertOrThrow(TABLE_EMPLOYEES, null, values);
            Log.d(TAG, "Employee added successfully: " + employee.getName());
        } catch (SQLException e) {
            Log.e(TAG, "Error adding employee: " + e.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    /**
     * Retrieve all employees from the database.
     *
     * @return A list of all employees.
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_EMPLOYEES, null, null, null, null, null, COLUMN_NAME + " ASC");
            while (cursor != null && cursor.moveToNext()) {
                employeeList.add(cursorToEmployee(cursor));
            }
            Log.d(TAG, "All employees fetched successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error fetching all employees: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return employeeList;
    }

    /**
     * Update an existing employee's details.
     *
     * @param employee The updated Employee object.
     * @return The number of rows affected.
     */
    public int updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsUpdated = 0;

        try {
            ContentValues values = createContentValues(employee);
            rowsUpdated = db.update(TABLE_EMPLOYEES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(employee.getId())});
            Log.d(TAG, "Employee with ID " + employee.getId() + " updated successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error updating employee: " + e.getMessage());
        } finally {
            db.close();
        }

        return rowsUpdated;
    }


    /**
     * Retrieve an employee by their ID.
     *
     * @param id The ID of the employee.
     * @return The Employee object, or null if not found.
     */
    public Employee getEmployeeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Employee employee = null;

        try {
            cursor = db.query(TABLE_EMPLOYEES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                employee = cursorToEmployee(cursor);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error fetching employee by ID: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return employee;
    }

    /**
     * Retrieve an employee by their email.
     *
     * @param email The email of the employee.
     * @return The Employee object, or null if not found.
     */
    public Employee getEmployeeByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Employee employee = null;

        try {
            cursor = db.query(TABLE_EMPLOYEES, null, COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                employee = cursorToEmployee(cursor);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error fetching employee by email: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return employee;
    }


    /**
     * Delete an employee by their ID.
     *
     * @param id The ID of the employee to delete.
     * @return The number of rows affected.
     */
    public int deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = 0;

        try {
            rowsDeleted = db.delete(TABLE_EMPLOYEES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            Log.d(TAG, "Employee with ID " + id + " deleted successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting employee: " + e.getMessage());
        } finally {
            db.close();
        }

        return rowsDeleted;
    }


    /**
     * Convert a database cursor to an Employee object.
     */
    private Employee cursorToEmployee(Cursor cursor) {
        return new Employee(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSITION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SALARY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEAVES))
        );
    }

    /**
     * Create ContentValues from an Employee object.
     */
    private ContentValues createContentValues(Employee employee) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, employee.getName());
        values.put(COLUMN_POSITION, employee.getPosition());
        values.put(COLUMN_EMAIL, employee.getEmail());
        values.put(COLUMN_PHONE, employee.getPhone());
        values.put(COLUMN_SALARY, employee.getSalary());
        values.put(COLUMN_START_DATE, employee.getStartDate());
        values.put(COLUMN_PASSWORD, employee.getPassword());
        values.put(COLUMN_LEAVES, employee.getLeaves());
        return values;
    }


    // ========================= Holiday Request Management =========================

    /**
     * Add a new holiday request to the database.
     *
     * @param holidayRequest The HolidayRequest object containing the request details.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long addHolidayRequest(HolidayRequest holidayRequest) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMPLOYEE_NAME, holidayRequest.getEmployeeName());
            values.put(COLUMN_HOLIDAY_START_DATE, holidayRequest.getHolidayStartDate());
            values.put(COLUMN_END_DATE, holidayRequest.getEndDate());
            values.put(COLUMN_REASON, holidayRequest.getReason());
            values.put(COLUMN_ADDITIONAL_INFO, holidayRequest.getAdditionalInfo());
            values.put(COLUMN_STATUS, HolidayRequestStatus.PENDING.name()); // Use enum's case

            long result = db.insertOrThrow(TABLE_HOLIDAY_REQUESTS, null, values);

            if (result != -1) {
                // Notify the admin
                storeNotification(
                        "New Holiday Request",
                        "A new holiday request has been submitted by " + holidayRequest.getEmployeeName() + ".",
                        "admin"
                );


                Log.d(TAG, "Holiday request added successfully for employee: " + holidayRequest.getEmployeeName());
            }

            return result;
        } catch (SQLException e) {
            Log.e(TAG, "Error adding holiday request: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Retrieve all holiday requests from the database.
     *
     * @return A list of all holiday requests.
     */
    public List<HolidayRequest> getAllHolidayRequests() {
        List<HolidayRequest> holidayRequests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_HOLIDAY_REQUESTS, null, null, null, null, null, COLUMN_REQUEST_ID + " ASC");

            while (cursor != null && cursor.moveToNext()) {
                holidayRequests.add(new HolidayRequest(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOLIDAY_START_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REASON)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDITIONAL_INFO)),
                        parseHolidayRequestStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)))
                ));
            }
            Log.d(TAG, "All holiday requests fetched successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error fetching holiday requests: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return holidayRequests;
    }

    /**
     * Retrieve a specific holiday request by its ID.
     *
     * @param id The ID of the holiday request.
     * @return The HolidayRequest object, or null if not found.
     */
    public HolidayRequest getHolidayRequestById(int id) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(TABLE_HOLIDAY_REQUESTS, null, COLUMN_REQUEST_ID + " = ?",
                     new String[]{String.valueOf(id)}, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                return new HolidayRequest(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUEST_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOLIDAY_START_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REASON)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDITIONAL_INFO)),
                        parseHolidayRequestStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)))
                );
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error fetching holiday request by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Update the status of a holiday request.
     *
     * @param requestId The ID of the holiday request.
     * @param newStatus The new status to set (e.g., "APPROVED" or "REJECTED").
     * @return The number of rows updated.
     */
    public int updateHolidayRequestStatus(int requestId, String newStatus) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_STATUS, newStatus);

            int rowsUpdated = db.update(TABLE_HOLIDAY_REQUESTS, values, COLUMN_REQUEST_ID + " = ?", new String[]{String.valueOf(requestId)});
            Log.d(TAG, "Holiday request with ID " + requestId + " updated to status: " + newStatus);
            return rowsUpdated;
        } catch (SQLException e) {
            Log.e(TAG, "Error updating holiday request status: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Helper method to parse a status value from the database into a HolidayRequestStatus enum.
     *
     * @param statusValue The status value as a string.
     * @return The corresponding HolidayRequestStatus enum, or PENDING if invalid.
     */
    private HolidayRequestStatus parseHolidayRequestStatus(String statusValue) {
        try {
            return HolidayRequestStatus.valueOf(statusValue.toUpperCase()); // Normalize to uppercase
        } catch (IllegalArgumentException | NullPointerException e) {
            // Log invalid status and default to PENDING
            Log.e(TAG, "Invalid status value: " + statusValue + ". Defaulting to PENDING.");
            return HolidayRequestStatus.PENDING;
        }
    }

    // ========================= Employee Login Management =========================


    /**
     * Validates employee login credentials.
     *
     * @param email    The email provided by the employee.
     * @param password The plain-text password provided by the employee.
     * @return True if credentials are valid, false otherwise.
     */
    public boolean validateEmployeeLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_EMPLOYEES,
                    new String[]{COLUMN_PASSWORD}, // Retrieve only the password column
                    COLUMN_EMAIL + " = ?",         // WHERE clause
                    new String[]{email},           // WHERE arguments
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                return storedPassword != null && storedPassword.equals(password);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error validating employee login: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return false;
    }

}