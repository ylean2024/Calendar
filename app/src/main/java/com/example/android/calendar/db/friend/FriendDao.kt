package com.example.android.calendar.db.friend

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.calendar.model.Friend

@Dao
interface FriendDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFriend(friend: FriendDb)

    @Query("SELECT * FROM friends WHERE userId1 = :userId OR userId2 = :userId")
    suspend fun getFriendsForUser(userId: Int): List<FriendDb>
}