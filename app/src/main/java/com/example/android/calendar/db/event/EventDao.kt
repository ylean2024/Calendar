package com.example.android.calendar.db.event

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE userId = :userId")
    suspend fun getEventsByUserId(userId: Int): List<EventDb>

    @Query("SELECT * FROM events WHERE date = :date AND userId = :userId")
    suspend fun getEventsByDateAndUserId(date: String, userId: Int): List<EventDb>

    @Query("SELECT * FROM events WHERE showToAll = 1 AND date = :date")
    suspend fun getEventsBySeeAll(date: String): List<EventDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventDb)

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: Int)

    @Query("UPDATE events SET showToAll = :showToAll WHERE userId = :userId")
    suspend fun updateShowToAllForUserEvents(userId: Int, showToAll: Boolean)

}