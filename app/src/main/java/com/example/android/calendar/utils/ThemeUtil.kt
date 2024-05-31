package com.example.pizzadelivery.ui.utils

import android.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatDelegate

object ThemeUtil {

    fun toggleTheme(activity: Activity) {
        val currentNightMode = activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        val newNightMode = if (isNightMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES

        val view = activity.window.decorView
        val startRadius = 0
        val endRadius = Math.hypot(view.width.toDouble(), view.height.toDouble()).toInt()
        val centerX = view.width
        val centerY = view.height
        val animDuration = 500L

        val anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius.toFloat(), endRadius.toFloat())
        anim.duration = animDuration

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                AppCompatDelegate.setDefaultNightMode(newNightMode)
                activity.recreate()
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        })

        anim.start()
    }

    fun checkTheme(context: Context): Boolean {
        return (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
}
