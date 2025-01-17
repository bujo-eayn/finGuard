package com.example.finguard

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var messageListView: ListView
    private lateinit var bottomNav: BottomNavigationView
    private var allMessages: List<Message> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        messageListView = findViewById(R.id.lvMessages)
        bottomNav = findViewById(R.id.bottom_navigation)

        allMessages = getStoredMessages()
        if (allMessages.isEmpty()) {
            Toast.makeText(this, "No messages found", Toast.LENGTH_SHORT).show()
        } else {
            updateMessageList(allMessages)
        }

        messageListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val encryptedMessage = allMessages[position].content
            android.util.Log.d("HomeActivity", "Encrypted Message: $encryptedMessage") // Debug log
            val intent = Intent(this, MessageDetailActivity::class.java)
            intent.putExtra("message", allMessages[position].content) // Ensure this is not null
            startActivity(intent)
        }


        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_dump -> {
                    val intent = Intent(this, DumpActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun getStoredMessages(): List<Message> {
        val sharedPreferences = getSharedPreferences("FinGuardMessages", MODE_PRIVATE)
        val allEntries = sharedPreferences.all
        val messages = mutableListOf<Message>()

        for ((_, value) in allEntries) {
            if (value is String) {
                val key = "1234567890123456"
                val decryptedMessage = AESUtils.decrypt(key, value) ?: "Error decrypting message"
                messages.add(Message(content = decryptedMessage, time = "10:00 AM"))
            }
        }

        return messages
    }

    private fun updateMessageList(messages: List<Message>) {
        val adapter = MessageAdapter(this, messages)
        messageListView.adapter = adapter
    }
}

data class Message(val content: String, val time: String)
