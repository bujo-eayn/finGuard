package com.example.finguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var expiryDuration: EditText
    private lateinit var setFingerprintButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        expiryDuration = findViewById(R.id.etExpiryDuration)
        setFingerprintButton = findViewById(R.id.btnSetFingerprint)

        setFingerprintButton.setOnClickListener {
            val expiry = expiryDuration.text.toString()

            if (expiry.isEmpty()) {
                Toast.makeText(this, "Please set expiry duration", Toast.LENGTH_SHORT).show()
            } else {
                // Save expiry duration to SharedPreferences
                val sharedPreferences = getSharedPreferences("FinGuardPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("expiryDuration", expiry.toInt())
                editor.apply()

                Toast.makeText(this, "Fingerprint and expiry set", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
