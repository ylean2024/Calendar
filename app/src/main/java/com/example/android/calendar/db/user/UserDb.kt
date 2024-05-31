package com.example.android.calendar.db.user

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user",
    indices = [Index(value = ["email"], unique = true)])
data class UserDb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    var showToAllFriends: Boolean
)