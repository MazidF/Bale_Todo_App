package com.example.todolist.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todolist.data.local.datastore.settings.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

fun setTheme(themeMode: Int) {
    if (currentThemeMode() != themeMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }
}

fun applyTheme(context: Context) {
    val mode = context.sharedPreferences().getThemeMode()
    setTheme(mode)
}

fun Fragment.launch(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state, block)
    }
}

fun View.inflater(): LayoutInflater {
    return LayoutInflater.from(context)
}

fun Context.orientation(): Int {
    return resources.configuration.orientation
}

fun RadioButton.set(value: Boolean) {
    isSelected = value
    isChecked = value
}

fun RadioButton.setup(default: Boolean = false) {
    set(default)
    setOnClickListener {
        set(isSelected.not())
    }
}

fun RadioButton.setup(default: Boolean = false, cb: (Boolean) -> Unit) {
    setup(default)
    setOnCheckedChangeListener { _, checked ->
        cb(checked)
    }
}

fun TextView.strikeThroughText(has: Boolean) {
    paintFlags = if (has) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and -Paint.STRIKE_THRU_TEXT_FLAG
    }

}
