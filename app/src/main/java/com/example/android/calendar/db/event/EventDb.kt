package com.example.android.calendar.db.event

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.android.calendar.db.user.UserDb
import java.util.Date


@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = UserDb::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Optional: specify what happens when a user is deleted
        )
    ]
)
data class EventDb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val time: String,
    val name: String,
    val description: String,
    val date: String,
    val showToAll: Boolean
)