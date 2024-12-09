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

    // Create Table SQL Query
    private static final String CREATE_TABLE_EMPLOYEES =
            "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_POSITION + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_SALARY + " REAL, " +
                    COLUMN_START_DATE + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_EMPLOYEES);
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error creating database: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        onCreate(db);
    }

    // Add Employee Method
    public long addEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, employee.getName());
            values.put(COLUMN_POSITION, employee.getPosition());
            values.put(COLUMN_EMAIL, employee.getEmail());
            values.put(COLUMN_PHONE, employee.getPhone());
            values.put(COLUMN_SALARY, employee.getSalary());
            values.put(COLUMN_START_DATE, employee.getStartDate());

            result = db.insertOrThrow(TABLE_EMPLOYEES, null, values);
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error adding employee: " + e.getMessage());
        } finally {
            db.close();
        }
        return result;
    }

    // Get All Employees Method
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_EMPLOYEES, null, null, null, null, null, COLUMN_NAME + " ASC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    employeeList.add(cursorToEmployee(cursor));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error fetching employees: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return employeeList;
    }

    // Get Employee by ID Method
    public Employee getEmployeeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Employee employee = null;

        try {
            cursor = db.query(TABLE_EMPLOYEES, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                employee = cursorToEmployee(cursor);
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error fetching employee by ID: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return employee;
    }

    // Search Employees Method
    public List<Employee> searchEmployees(String query) {
        List<Employee> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_EMPLOYEES, null, COLUMN_NAME + " LIKE ?", new String[]{"%" + query + "%"}, null, null, COLUMN_NAME + " ASC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    employeeList.add(cursorToEmployee(cursor));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error searching employees: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return employeeList;
    }

    // Update Employee Method
    public int updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, employee.getName());
            values.put(COLUMN_POSITION, employee.getPosition());
            values.put(COLUMN_EMAIL, employee.getEmail());
            values.put(COLUMN_PHONE, employee.getPhone());
            values.put(COLUMN_SALARY, employee.getSalary());
            values.put(COLUMN_START_DATE, employee.getStartDate());

            rowsAffected = db.update(TABLE_EMPLOYEES, values, COLUMN_ID + "=?", new String[]{String.valueOf(employee.getId())});
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error updating employee: " + e.getMessage());
        } finally {
            db.close();
        }
        return rowsAffected;
    }

    // Delete Employee Method
    public int deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = 0;
        try {
            rowsDeleted = db.delete(TABLE_EMPLOYEES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error deleting employee: " + e.getMessage());
        } finally {
            db.close();
        }
        return rowsDeleted;
    }

    // Helper method to convert cursor to Employee object
    private Employee cursorToEmployee(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
        String position = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSITION));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
        double salary = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SALARY));
        String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE));

        return new Employee(id, name, position, email, phone, salary, startDate);
    }
}
