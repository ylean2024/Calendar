package com.example.android.calendar.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.calendar.db.event.EventDao
import com.example.android.calendar.db.event.EventDb
import com.example.android.calendar.db.friend.FriendDao
import com.example.android.calendar.db.friend.FriendDb
import com.example.android.calendar.db.user.UserDao
import com.example.android.calendar.db.user.UserDb

@Database(entities = [EventDb::class, UserDb::class, FriendDb::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun friendDao(): FriendDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}