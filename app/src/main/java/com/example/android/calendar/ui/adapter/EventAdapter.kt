package com.example.android.calendar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.calendar.R
import com.example.android.calendar.model.Event

class EventAdapter(
    private val events: List<Event>,
    private val onDeleteEvent: (Event) -> Unit) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventTime: TextView = itemView.findViewById(R.id.eventTime)
        val eventDescription: TextView = itemView.findViewById(R.id.eventDescription)
        val deleteEvent: ImageView = itemView.findViewById(R.id.delete_note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = "Название - " + event.name
        holder.eventTime.text = "Время - " + event.startTime + " - " + event.endTime
        holder.eventDescription.text = "Описание - " + event.description
        holder.deleteEvent.setOnClickListener {
            onDeleteEvent(event)
        }
    }

    override fun getItemCount() = events.size
}
