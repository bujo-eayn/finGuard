package com.example.finguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {

    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var biometricLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.btnLogin)
        biometricLoginButton = findViewById(R.id.btnBiometricLogin)

        loginButton.setOnClickListener {
            val enteredPassword = password.text.toString()
            val sharedPreferences = getSharedPreferences("FinGuardPrefs", MODE_PRIVATE)
            val savedPassword = sharedPreferences.getString("password", "")

            if (enteredPassword == savedPassword) {
                navigateToHome()
            } else {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            }
        }

        biometricLoginButton.setOnClickListener {
            authenticateWithBiometrics()
        }
    }

    private fun authenticateWithBiometrics() {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
            BiometricManager.BIOMETRIC_SUCCESS) {

            val executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Biometric Authentication Successful", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Authenticate using your fingerprint")
                .setNegativeButtonText("Cancel")
                .build()

            biometricPrompt.authenticate(promptInfo)
        } else {
            Toast.makeText(this, "Biometric authentication not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
