package com.example.finguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var fullName: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fullName = findViewById(R.id.etFullName)
        phoneNumber = findViewById(R.id.etPhoneNumber)
        password = findViewById(R.id.etPassword)
        confirmPassword = findViewById(R.id.etConfirmPassword)
        registerButton = findViewById(R.id.btnRegister)
        loginButton = findViewById(R.id.btnLogin)
        tvTitle = findViewById(R.id.tvTitle)

        // Check if user is already registered (Auto-login scenario)
        val sharedPreferences = getSharedPreferences("FinGuardPrefs", MODE_PRIVATE)
        val savedPhone = sharedPreferences.getString("phoneNumber", null)

        if (savedPhone != null) {
            // User is already registered, proceed to the Home screen or Profile
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerButton.setOnClickListener {
            val name = fullName.text.toString()
            val phone = phoneNumber.text.toString()
            val pass = password.text.toString()
            val confirmPass = confirmPassword.text.toString()

            if (name.isEmpty() || phone.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (pass != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Save data to SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("fullName", name)
                editor.putString("phoneNumber", phone)
                editor.putString("password", pass)
                editor.apply()

                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                // Navigate to Profile or Home screen
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Login button click listener (for users who are already registered)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}