package com.example.finguard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.ImageView

class MessageAdapter(private val context: Context, private val messages: List<Message>) : BaseAdapter() {

    override fun getCount(): Int = messages.size

    override fun getItem(position: Int): Message = messages[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.message_list_item, parent, false)

        val profileIcon = view.findViewById<ImageView>(R.id.ivProfileIcon)
        val messagePreview = view.findViewById<TextView>(R.id.tvMessagePreview)
        val timeReceived = view.findViewById<TextView>(R.id.tvTimeReceived)

        val message = getItem(position)

        profileIcon.setImageResource(R.drawable.ic_default_profile) // Replace with real profile icon if available
        messagePreview.text = message.content
        timeReceived.text = message.time

        return view
    }
}
