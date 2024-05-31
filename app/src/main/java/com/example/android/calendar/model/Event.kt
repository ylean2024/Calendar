package com.example.android.calendar.model

data class Event(
    val id: Int,
    val idUser: Int,
    val name: String,
    val description: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val friends: List<Friend>,
    val showToALl: Boolean
)
