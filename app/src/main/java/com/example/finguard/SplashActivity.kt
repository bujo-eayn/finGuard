package com.example.finguard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // Check registration and login status
            val sharedPreferences = getSharedPreferences("FinGuardPrefs", MODE_PRIVATE)
            val isRegistered = sharedPreferences.contains("fullName") && sharedPreferences.contains("password")
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            val nextActivity = when {
                !isRegistered -> RegisterActivity::class.java
                isLoggedIn -> HomeActivity::class.java
                else -> LoginActivity::class.java
            }

            // Navigate to the next activity
            startActivity(Intent(this, nextActivity))
            finish()
        }, 2000) // 2-second delay for splash screen
    }
}
