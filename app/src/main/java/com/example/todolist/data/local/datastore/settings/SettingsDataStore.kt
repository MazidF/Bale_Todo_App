package com.example.todolist.data.local.datastore.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.todolist.utils.changeTheme
import com.example.todolist.utils.sharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SettingsDataStore::class.java.simpleName
)

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    dispatcher: CoroutineDispatcher,
) {
    private val dataStore = context.dataStore

    val preferences = dataStore.data.map { preference ->
        val theme = Theme.getTheme(preference[SettingsKey.THEME_KEY])

        SettingsInfo(
            theme = theme,
        )
    }.catch {
        // TODO: ??
    }.flowOn(dispatcher)

    suspend fun updateTheme(theme: Theme) {
        dataStore.edit { preference ->
            preference[SettingsKey.THEME_KEY] = theme.name
        }
        context.sharedPreferences().apply {
            changeTheme(theme.mode)
        }
    }

    private object SettingsKey {
        val THEME_KEY = stringPreferencesKey("themeKey")
    }
}
