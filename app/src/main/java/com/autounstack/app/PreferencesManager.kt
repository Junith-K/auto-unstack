package com.autounstack.app

import android.content.Context

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("auto_unstack_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ENABLED = "service_enabled"
        private const val DEFAULT_ENABLED = true
    }

    fun isServiceEnabled(): Boolean {
        return prefs.getBoolean(KEY_ENABLED, DEFAULT_ENABLED)
    }

    fun setServiceEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ENABLED, enabled).apply()
    }
}
