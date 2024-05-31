package com.example.android.calendar.utils

import com.example.android.calendar.db.user.UserDb
import com.example.android.calendar.model.Friend

object FriendConverter {

    fun fromUserDb(userDb: UserDb): Friend {
        return Friend(
            id = userDb.id,
            email = userDb.email
        )
    }

    fun fromUserDbList(userDbList: List<UserDb>): List<Friend> {
        return userDbList.map { fromUserDb(it) }
    }
}