package com.example.application2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper class for managing the employee database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database constants
    private static final String DATABASE_NAME = "employees.db";
    private static final int DATABASE_VERSION = 9;

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



    // Holiday Requests Table Name and Columns
    private static final String TABLE_HOLIDAY_REQUESTS = "holiday_requests";
    private static final String COLUMN_REQUEST_ID = "request_id";
    private static final String COLUMN_EMPLOYEE_NAME = "employee_name";
    private static final String COLUMN_REASON = "reason";
    private static final String COLUMN_HOLIDAY_START_DATE = "holiday_start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_ADDITIONAL_INFO = "additional_info";
    private static final String COLUMN_STATUS = "status";


    // SQL statement to create the employees table
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


    // SQL statement to create the holiday requests table
    private static final String CREATE_TABLE_HOLIDAY_REQUESTS =
            "CREATE TABLE " + TABLE_HOLIDAY_REQUESTS + " (" +
                    COLUMN_REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMPLOYEE_NAME + " TEXT NOT NULL, " +
                    COLUMN_HOLIDAY_START_DATE + " TEXT NOT NULL, " +
                    COLUMN_END_DATE + " TEXT NOT NULL, " +
                    COLUMN_ADDITIONAL_INFO + " TEXT, " +
                    COLUMN_REASON + " TEXT NOT NULL, " +
                    COLUMN_STATUS + " TEXT NOT NULL)";



    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_EMPLOYEES);
            db.execSQL(CREATE_TABLE_HOLIDAY_REQUESTS);
            Log.d(TAG, "Database created successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating database: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOLIDAY_REQUESTS);
            onCreate(db);
            Log.d(TAG, "Database upgraded successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
        }
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

    // ========================= Holiday Request Methods =========================

    /**
     * Add a new holiday request to the database.
     */
    public long addHolidayRequest(HolidayRequest holidayRequest) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMPLOYEE_NAME, holidayRequest.getEmployeeName());
            values.put(COLUMN_HOLIDAY_START_DATE, holidayRequest.getHolidayStartDate());
            values.put(COLUMN_END_DATE, holidayRequest.getEndDate());
            values.put(COLUMN_REASON, holidayRequest.getReason());
            values.put(COLUMN_ADDITIONAL_INFO, holidayRequest.getAdditionalInfo());
            values.put(COLUMN_STATUS, holidayRequest.getStatus().name());

            long result = db.insertOrThrow(TABLE_HOLIDAY_REQUESTS, null, values);
            Log.d(TAG, "Holiday request added successfully for employee: " + holidayRequest.getEmployeeName());
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
                        HolidayRequestStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)))
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
     * Retrieve a specific holiday request by ID.
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
                        HolidayRequestStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)))
                );
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error fetching holiday request by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Update the status of a holiday request.
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
}