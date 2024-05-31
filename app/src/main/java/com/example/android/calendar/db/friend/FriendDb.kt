package com.example.android.calendar.db.friend

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.android.calendar.db.user.UserDb

@Entity(
    tableName = "friends",
    foreignKeys = [
        ForeignKey(
            entity = UserDb::class,
            parentColumns = ["id"],
            childColumns = ["userId1"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserDb::class,
            parentColumns = ["id"],
            childColumns = ["userId2"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FriendDb(
    @PrimaryKey val userId1: Int,
     val userId2: Int
)