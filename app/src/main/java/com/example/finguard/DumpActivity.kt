package com.example.finguard

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DumpActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dump)

        listView = findViewById(R.id.lvDumpMessages)

        // Fetch stored information
        val storedData = getStoredInformation()

        if (storedData.isEmpty()) {
            Toast.makeText(this, "No information found in storage.", Toast.LENGTH_SHORT).show()
        } else {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, storedData)
            listView.adapter = adapter
        }
    }

    private fun getStoredInformation(): List<String> {
        val sharedPreferences = getSharedPreferences("FinGuardMessages", MODE_PRIVATE)
        val allEntries = sharedPreferences.all
        val storedData = mutableListOf<String>()

        for ((key, value) in allEntries) {
            if (value is String) {
                // Add the key and the encrypted value directly
                storedData.add("Key: $key\nEncrypted Message: $value")
            } else {
                // Handle non-string values (if any)
                storedData.add("Key: $key\nValue: $value")
            }
        }

        return storedData
    }
}
