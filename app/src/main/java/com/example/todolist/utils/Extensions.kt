package com.example.todolist.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.todolist.data.local.datastore.settings.Theme

fun Context.sharedPreferences(): SharedPreferences {
    return getSharedPreferences(packageName, Context.MODE_PRIVATE)
}

fun SharedPreferences.changeTheme(themeMode: Int) {
    edit {
        putInt(THEME_PREFERENCES_KEY, themeMode)
    }
}

fun SharedPreferences.getThemeMode(): Int {
    return getInt(THEME_PREFERENCES_KEY, Theme.defaultMode())
}

fun currentThemeMode(): Int {
    return AppCompatDelegate.getDefaultNightMode()
}

fun changeTheme(themeMode: Int) {
    if (currentThemeMode() != themeMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }
}

fun applyTheme(context: Context) {
    val mode = context.sharedPreferences().getThemeMode()
    changeTheme(mode)
}