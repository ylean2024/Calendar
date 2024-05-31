package com.example.android.calendar.utils

import com.example.android.calendar.db.event.EventDb
import com.example.android.calendar.model.Event
import java.text.SimpleDateFormat
import java.util.Locale

object EventConverter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun modelEventToDbEvent(modelEvent: Event, userId: Int): EventDb {
        val time: String = "${modelEvent.startTime} - ${modelEvent.endTime}"
        return EventDb(
            id = modelEvent.id,
            name = modelEvent.name,
            userId = userId,
            description = modelEvent.description,
            date = modelEvent.date,
            time = time,
            showToAll = modelEvent.showToALl
        )
    }

    fun dbEventToModelEvent(dbEventList: List<EventDb>): List<Event> {
        return dbEventList.map {
            val times = it.time.split(" - ")
            val startTime = times[0]
            val endTime = if (times.size > 1) times[1] else ""
            Event(
                id = it.id,
                idUser = it.userId,
                name = it.name,
                description = it.description,
                date = it.date,
                startTime = startTime,
                endTime = endTime,
                friends = emptyList(),
                showToALl = it.showToAll
            )
        }
    }
}