package com.example.finguard

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MessageDetailActivity : AppCompatActivity() {

    private lateinit var messageDetailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)

        messageDetailTextView = findViewById(R.id.tvMessageDetail)

        val encryptedMessage = intent.getStringExtra("message")
        if (encryptedMessage.isNullOrEmpty()) {
            Toast.makeText(this, "No message to decrypt", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val key = "1234567890123456" // Replace with your encryption key
        val decryptedMessage = AESUtils.decrypt(key, encryptedMessage)

        if (decryptedMessage.isNullOrEmpty() || decryptedMessage == "Error decrypting message") {
            Log.e("MessageDetailActivity", "Failed to decrypt message: $encryptedMessage")
            Toast.makeText(this, "Failed to decrypt the message", Toast.LENGTH_SHORT).show()
        } else {
            messageDetailTextView.text = decryptedMessage
        }
    }
}
