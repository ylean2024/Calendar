package com.example.android.calendar.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Locale


object EventScheduler {

    fun scheduleEvent(context: Context, eventId: Int, eventName: String, eventDescription: String, eventTime: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, EventBroadcastReceiver::class.java).apply {
            putExtra("event_name", eventName)
            putExtra("event_description", eventDescription)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, eventId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val eventDate = dateFormat.parse(eventTime)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, eventDate.time, pendingIntent)
    }
}