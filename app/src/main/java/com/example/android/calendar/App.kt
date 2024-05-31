package com.example.android.calendar

import android.app.Application
import com.example.android.calendar.db.Repository

class App : Application()
{
    override fun onCreate() {
        super.onCreate()
        Repository.getInstance(this)
    }
}