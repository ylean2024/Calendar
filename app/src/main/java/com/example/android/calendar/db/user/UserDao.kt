package com.example.android.calendar.db.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserDb

    @Query("SELECT * FROM user WHERE id != :userId")
    suspend fun getAllUsers(userId: Int): List<UserDb>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserDb)

    @Query("UPDATE user SET showToAllFriends = :showToAllFriends WHERE id = :userId")
    suspend fun updateShowToAllFriends(userId: Int, showToAllFriends: Boolean)

}