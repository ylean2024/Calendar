package com.example.pizzadelivery.ui.utils

import android.content.Context

object SearchHistoryManager {
    private const val PREF_NAME = "search_history"
    private const val KEY_SEARCH_HISTORY = "search_history"
    private const val MAX_HISTORY_SIZE = 10

    fun addToHistory(context: Context, item: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val history = getHistory(context).toMutableList()
        history.add(0, item)
        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.size - 1)
        }
        prefs.edit().putStringSet(KEY_SEARCH_HISTORY, history.toSet()).apply()
    }

    fun getHistory(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_SEARCH_HISTORY, emptySet())?.toList() ?: emptyList()
    }

    fun clearHistory(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_SEARCH_HISTORY).apply()
    }
}
