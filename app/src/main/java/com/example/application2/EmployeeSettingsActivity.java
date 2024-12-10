package com.example.application2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;

public class EmployeeSettingsActivity extends AppCompatActivity {

    private ImageView backArrow;
    private SwitchCompat switchDarkMode, switchEmailNotifications, switchPushNotifications, switchTwoFactor;
    private Spinner spinnerLanguage;
    private Button btnSaveChanges, btnChangePassword;

    private SharedPreferences sharedPreferences;
    private boolean isDarkModeEnabled, isEmailEnabled, isPushEnabled, isTwoFactorEnabled;
    private String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply dark mode and language before setting content view
        applySavedPreferences();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_settings);

        // Initialize views
        backArrow = findViewById(R.id.backArrow);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchEmailNotifications = findViewById(R.id.switchEmailNotifications);
        switchPushNotifications = findViewById(R.id.switchPushNotifications);
        switchTwoFactor = findViewById(R.id.switchTwoFactor);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        // Load preferences
        sharedPreferences = getSharedPreferences("EmployeeSettings", MODE_PRIVATE);
        loadPreferences();

        // Back button functionality
        backArrow.setOnClickListener(v -> navigateToDashboard());

        // Dark mode toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> isDarkModeEnabled = isChecked);

        // Save changes button functionality
        btnSaveChanges.setOnClickListener(v -> saveChanges());

        // Change password button functionality
        btnChangePassword.setOnClickListener(v -> navigateToChangePassword());
    }

    private void applySavedPreferences() {
        sharedPreferences = getSharedPreferences("EmployeeSettings", MODE_PRIVATE);

        // Apply dark mode preference
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkModeEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        // Apply saved language preference
        String language = sharedPreferences.getString("language", "English");
        applyLanguage(language);
    }

    private void loadPreferences() {
        isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false);
        isEmailEnabled = sharedPreferences.getBoolean("emailNotifications", true);
        isPushEnabled = sharedPreferences.getBoolean("pushNotifications", true);
        isTwoFactorEnabled = sharedPreferences.getBoolean("twoFactor", false);
        selectedLanguage = sharedPreferences.getString("language", "English");

        switchDarkMode.setChecked(isDarkModeEnabled);
        switchEmailNotifications.setChecked(isEmailEnabled);
        switchPushNotifications.setChecked(isPushEnabled);
        switchTwoFactor.setChecked(isTwoFactorEnabled);

        setupLanguageSpinner();
    }

    private void setupLanguageSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        // Set the current selected language
        int position = adapter.getPosition(selectedLanguage);
        spinnerLanguage.setSelection(position);
    }

    private void saveChanges() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("darkMode", switchDarkMode.isChecked());
        editor.putBoolean("emailNotifications", switchEmailNotifications.isChecked());
        editor.putBoolean("pushNotifications", switchPushNotifications.isChecked());
        editor.putBoolean("twoFactor", switchTwoFactor.isChecked());
        editor.putString("language", spinnerLanguage.getSelectedItem().toString());

        editor.apply();

        // Apply dark mode immediately
        applyDarkMode(switchDarkMode.isChecked());

        // Apply language change
        String language = spinnerLanguage.getSelectedItem().toString();
        applyLanguage(language);

        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();

        // Restart the app to apply changes globally
        restartApp();
    }

    private void applyDarkMode(boolean isEnabled) {
        AppCompatDelegate.setDefaultNightMode(
                isEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    private void applyLanguage(String language) {
        Locale locale;
        switch (language) {
            case "French":
                locale = Locale.FRENCH;
                break;
            case "Spanish":
                locale = new Locale("es");
                break;
            case "German":
                locale = Locale.GERMAN;
                break;
            case "Chinese":
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            default:
                locale = Locale.ENGLISH;
                break;
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(EmployeeSettingsActivity.this, EmployeeDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToChangePassword() {
        Intent intent = new Intent(EmployeeSettingsActivity.this, EmployeeRestActivity.class);
        startActivity(intent);
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), EmployeeDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
