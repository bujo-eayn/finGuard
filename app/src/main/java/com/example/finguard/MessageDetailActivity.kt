package com.example.finguard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MessageDetailActivity : AppCompatActivity() {

    private lateinit var messageDetailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)

        messageDetailTextView = findViewById(R.id.tvMessageDetail)

        val message = intent.getStringExtra("message")
        // Decrypt the message here before showing
        messageDetailTextView.text = message
    }
}
