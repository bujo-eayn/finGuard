package com.example.finguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameField: EditText
    private lateinit var phoneNumberField: EditText
    private lateinit var fingerprintField: EditText
    private lateinit var expiryDurationField: EditText
    private lateinit var saveButton: Button
    private lateinit var setFingerprintButton: Button
    private lateinit var logoutButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize UI components
        nameField = findViewById(R.id.etName)
        phoneNumberField = findViewById(R.id.etPhoneNumber)
        fingerprintField = findViewById(R.id.etFingerprint)
        expiryDurationField = findViewById(R.id.etExpiryDuration)
        saveButton = findViewById(R.id.btnSave)
        setFingerprintButton = findViewById(R.id.btnSetFingerprint)
        logoutButton = findViewById(R.id.btnLogout)
        deleteButton = findViewById(R.id.btnDelete)

        // Fetch stored data
        val sharedPreferences = getSharedPreferences("FinGuardPrefs", MODE_PRIVATE)
        val name = sharedPreferences.getString("fullName", "N/A")
        val phoneNumber = sharedPreferences.getString("phoneNumber", "N/A")
        val fingerprint = if (sharedPreferences.getBoolean("fingerprintSet", false)) {
            "Enabled"
        } else {
            "Not Set"
        }
        val expiryDuration = sharedPreferences.getInt("expiryDuration", 0)

        // Display stored data
        nameField.setText(name)
        phoneNumberField.setText(phoneNumber)
        fingerprintField.setText(fingerprint)
        expiryDurationField.setText(if (expiryDuration > 0) expiryDuration.toString() else "")

        // Save button logic (save expiry duration)
        saveButton.setOnClickListener {
            val expiry = expiryDurationField.text.toString()
            if (expiry.isNotEmpty()) {
                val editor = sharedPreferences.edit()
                editor.putInt("expiryDuration", expiry.toInt())
                editor.apply()

                Toast.makeText(this, "Expiry Duration saved", Toast.LENGTH_SHORT).show()

                // Navigate to home (example HomeActivity)
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter a valid expiry duration", Toast.LENGTH_SHORT).show()
            }
        }

        // Set fingerprint logic (add fingerprint)
        setFingerprintButton.setOnClickListener {
            Toast.makeText(this, "Fingerprint setup logic goes here", Toast.LENGTH_SHORT).show()

            // Example fingerprint logic: Mark fingerprint as set
            val editor = sharedPreferences.edit()
            editor.putBoolean("fingerprintSet", true)
            editor.apply()

            fingerprintField.setText("Enabled")
        }

        // Logout logic
        logoutButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false) // Reset login status
            editor.apply()

            // Navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Delete all stored information
        deleteButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear() // Clear all data
            editor.apply()

            Toast.makeText(this, "All information deleted", Toast.LENGTH_SHORT).show()

            // Refresh the activity to reflect cleared data
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
