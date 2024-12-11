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
    private static final int DATABASE_VERSION = 1;

    // Table Name and Columns
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POSITION = "position";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_SALARY = "salary";
    private static final String COLUMN_START_DATE = "start_date";

    // SQL statement to create the employees table
    private static final String CREATE_TABLE_EMPLOYEES =
            "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_POSITION + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_SALARY + " REAL, " +
                    COLUMN_START_DATE + " TEXT)";

    // Log Tag
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_EMPLOYEES);
            Log.d(TAG, "Database created successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating database: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
            onCreate(db);
            Log.d(TAG, "Database upgraded successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
        }
    }

    /**
     * Add a new employee to the database.
     *
     * @param employee The employee object to add.
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
     * @return A list of employees.
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

            Log.d(TAG, "Fetched " + employeeList.size() + " employees.");
        } catch (SQLException e) {
            Log.e(TAG, "Error fetching employees: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return employeeList;
    }

    /**
     * Retrieve an employee by their ID.
     *
     * @param id The employee's ID.
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
                Log.d(TAG, "Employee with ID " + id + " fetched successfully.");
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
     * Search employees by name.
     *
     * @param query The search query.
     * @return A list of employees matching the search criteria.
     */
    public List<Employee> searchEmployees(String query) {
        List<Employee> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_EMPLOYEES, null, COLUMN_NAME + " LIKE ?", new String[]{"%" + query + "%"}, null, null, COLUMN_NAME + " ASC");

            while (cursor != null && cursor.moveToNext()) {
                employeeList.add(cursorToEmployee(cursor));
            }

            Log.d(TAG, "Search returned " + employeeList.size() + " employees for query: " + query);
        } catch (SQLException e) {
            Log.e(TAG, "Error searching employees: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return employeeList;
    }

    /**
     * Update an existing employee.
     *
     * @param employee The employee object with updated data.
     * @return The number of rows affected.
     */
    public int updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = 0;

        try {
            ContentValues values = createContentValues(employee);
            rowsAffected = db.update(TABLE_EMPLOYEES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(employee.getId())});
            Log.d(TAG, "Employee with ID " + employee.getId() + " updated successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error updating employee: " + e.getMessage());
        } finally {
            db.close();
        }

        return rowsAffected;
    }

    /**
     * Delete an employee by their ID.
     *
     * @param id The employee's ID.
     * @return The number of rows deleted.
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
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE))
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
        return values;
    }
}
