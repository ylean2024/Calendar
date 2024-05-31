package com.example.android.calendar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.calendar.R
import com.example.android.calendar.model.Friend

class FriendAdapter(
    private val friends: List<Friend>,
    private val onAddButtonClick: (Friend) -> Unit
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend, onAddButtonClick)
    }

    override fun getItemCount(): Int = friends.size

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val friendNameTextView: TextView = itemView.findViewById(R.id.friendName)
        private val addButton: Button = itemView.findViewById(R.id.button)

        fun bind(friend: Friend, onAddButtonClick: (Friend) -> Unit) {
            friendNameTextView.text = "Почта: ${friend.email}"
            addButton.setOnClickListener {
                onAddButtonClick(friend)
            }
        }
    }
}