package com.example.todolist.data.local.datastore.settings

import androidx.appcompat.app.AppCompatDelegate

enum class Theme(
    val mode: Int,
) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    NIGHT(AppCompatDelegate.MODE_NIGHT_YES),
    AUTO(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        fun getTheme(themeName: String?): Theme {
            return values().firstOrNull {
                it.name == themeName
            } ?: AUTO
        }

        fun defaultMode(): Int {
            return AUTO.mode
        }
    }
}