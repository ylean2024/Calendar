package com.example.android.calendar.db

import android.content.Context
import com.example.android.calendar.db.event.EventDb
import com.example.android.calendar.db.friend.FriendDb
import com.example.android.calendar.db.user.UserDb
import com.example.android.calendar.model.Friend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository private constructor(context: Context) {
    private val appDatabase = AppDatabase.getInstance(context)

    private val eventDao = appDatabase.eventDao()
    private val userDao = appDatabase.userDao()
    private val friendDao = appDatabase.friendDao()

    suspend fun getEventsByDateAndUserId(userId: Int, date: String): List<EventDb> {
        return eventDao.getEventsByDateAndUserId(date, userId)
    }

    suspend fun insertEvent(event: EventDb) {
        withContext(Dispatchers.IO) {
            eventDao.insertEvent(event)
        }
    }

    suspend fun insertUser(user: UserDb) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserDb? {
        return userDao.getUserByEmail(email)
    }
    suspend fun getAllUsers(userId: Int): List<UserDb>{
        return userDao.getAllUsers(userId)
    }

    suspend fun addFriend(friend: FriendDb) {
        friendDao.insertFriend(friend)
    }

    suspend fun getFriendsForUser(userId: Int): List<FriendDb> {
        return friendDao.getFriendsForUser(userId)
    }

    suspend fun getEventsBySeeAll(date: String): List<EventDb>{
        return eventDao.getEventsBySeeAll(date)
    }
    suspend fun updateShowToAllForUserEvents(userId: Int, showToAll: Boolean){
        return eventDao.updateShowToAllForUserEvents(userId, showToAll)
    }

    suspend fun updateUserShowToAllFriends(userId: Int, showToAllFriends: Boolean) {
        userDao.updateShowToAllFriends(userId, showToAllFriends)
    }

    suspend fun deleteEventById(eventId: Int) {
        eventDao.deleteEventById(eventId)
    }

    companion object {
        private var INSTANCE: Repository? = null

        fun getInstance(context: Context): Repository {
            return INSTANCE ?: synchronized(this) {
                val instance = Repository(context)
                INSTANCE = instance
                instance
            }
        }

        fun get(): Repository{
            return INSTANCE ?:
            throw  java.lang.IllegalStateException("Repository must be initialized")
        }
    }
}