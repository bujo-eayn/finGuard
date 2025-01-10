package com.example.finguard

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var messageListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        messageListView = findViewById(R.id.lvMessages)

        // Fetch messages from local storage (dummy example here)
        val messages = listOf("Encrypted message 1", "Encrypted message 2")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        messageListView.adapter = adapter

        messageListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, MessageDetailActivity::class.java)
            intent.putExtra("message", messages[position])
            startActivity(intent)
        }
    }
}
